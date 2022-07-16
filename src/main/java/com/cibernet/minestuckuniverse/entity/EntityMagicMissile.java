package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityMagicMissile extends Entity implements IProjectile
{
	private static final Predicate<Entity> TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, p_apply_1_ -> p_apply_1_.canBeCollidedWith());

	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.VARINT);
	public final ArrayList<PotionEffect> effects = new ArrayList<>();

	public float damage = 2;
	private int ticksInAir = 0;
	public Entity shootingEntity;


	public EntityMagicMissile(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntityMagicMissile(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityMagicMissile(World worldIn, EntityLivingBase shooter)
	{
		this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
		this.shootingEntity = shooter;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(world.isRemote) 
			for (int k = 0; k < 8; ++k)
				MSUParticles.spawnPowerParticle(world, this.posX + this.motionX * (double)k / 8.0D + (rand.nextFloat()*.25-0.125), this.posY + this.motionY * (double)k / 8.0D + (rand.nextFloat()*.25-0.125), this.posZ + this.motionZ * (double)k / 8.0D + (rand.nextFloat()*.25-0.125),
						-this.motionX, -this.motionY, -this.motionZ,
						3, getColor());

		++this.ticksInAir;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null)
		{
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);
		
		if (entity != null)
		{
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;

			if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
			{
				raytraceresult = null;
			}
		}

		if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
		{
			this.onHit(raytraceresult);
		}

		this.setEntityBoundingBox(getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ));
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
	}

	public void onHit(RayTraceResult raytraceresult)
	{
		if(world.isRemote || ticksExisted <= 1 || raytraceresult.typeOfHit == RayTraceResult.Type.MISS)
			return;
		if(raytraceresult.entityHit != null)
		{
			if(raytraceresult.entityHit.equals(shootingEntity))
				return;
			if(damage > 0)
				raytraceresult.entityHit.attackEntityFrom(new EntityDamageSourceIndirect(MinestuckUniverse.MODID+"magic", this, shootingEntity), damage);
			else if(damage < 0 && raytraceresult.entityHit instanceof EntityLivingBase)
				((EntityLivingBase) raytraceresult.entityHit).heal(-damage);
			if(raytraceresult.entityHit instanceof EntityLivingBase)
				for(PotionEffect effect : effects)
					((EntityLivingBase) raytraceresult.entityHit).addPotionEffect(effect);
		}

		setDead();
	}


	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end)
	{
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), TARGETS);
		double d0 = 0.0D;
		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity1 = list.get(i);
			if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
			{
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null)
				{
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D)
					{
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		return entity;
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(COLOR, 0x4bec13);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("Color"))
			setColor(compound.getInteger("Color"));

		if(compound.hasKey("Damage"))
			damage = compound.getFloat("Damage");

		if(compound.hasUniqueId("Owner"))
			shootingEntity = world.getPlayerEntityByUUID(compound.getUniqueId("Owner"));

		if (compound.hasKey("Effects", 9))
		{
			NBTTagList nbttaglist = compound.getTagList("Effects", 10);
			this.effects.clear();

			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));

				if (potioneffect != null)
					effects.add(potioneffect);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Color", getColor());
		compound.setFloat("Damage", damage);

		if(shootingEntity != null)
			compound.setUniqueId("Owner", shootingEntity.getUniqueID());

		if (!this.effects.isEmpty())
		{
			NBTTagList nbttaglist = new NBTTagList();

			for (PotionEffect potioneffect : this.effects)
				nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));

			compound.setTag("Effects", nbttaglist);
		}
	}

	public int getColor()
	{
		return dataManager.get(COLOR);
	}

	public void setColor(int color)
	{
		dataManager.set(COLOR, color);
	}

	/**
	 * Sets throwable heading based on an entity that's throwing it
	 */
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
	{
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
		this.motionX += entityThrower.motionX;
		this.motionZ += entityThrower.motionZ;

		if (!entityThrower.onGround)
		{
			this.motionY += entityThrower.motionY;
		}
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
	 */
	public void shoot(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double)f;
		y = y / (double)f;
		z = z / (double)f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		x = x * (double)velocity;
		y = y * (double)velocity;
		z = z * (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	public boolean isInvisible() {
		return true;
	}
}
