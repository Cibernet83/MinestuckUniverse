package com.cibernet.minestuckuniverse.capabilities.beam;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Beam
{

	private static final Predicate<Entity> BEAM_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, p_apply_1_ -> p_apply_1_.canBeCollidedWith());

	public World world;
	public ItemStack sourceStack = ItemStack.EMPTY;
	public Entity source;
	public UUID sourceUuid;
	private final UUID uuid;
	private boolean isDead = false;

	public float damage = 10;
	int duration = 10;
	int decayTime = duration;
	protected float length = 0;
	protected boolean releaseBeam = false;
	protected boolean anchorToSource = false;
	protected int damageCooldown = 0;

	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;

	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double prevMotionX;
	public double prevMotionY;
	public double prevMotionZ;

	public Beam(World world, UUID uuid)
	{
		this.world = world;
		this.uuid = uuid;
	}

	public Beam(EntityLivingBase source, ItemStack stack)
	{
		this(source.world, UUID.randomUUID());

		setPositionToEntity(source);
		sourceStack = stack;
		this.source = source;
		anchorToSource = true;
	}

	public void setPositionToEntity(Entity source)
	{
		posX = source.posX;
		posY = source.posY+source.getEyeHeight()*0.8;
		posZ = source.posZ;

		motionX = -MathHelper.sin(source.rotationYaw * 0.017453292F) * MathHelper.cos(source.rotationPitch * 0.017453292F) + source.motionX;
		motionY = -MathHelper.sin(source.rotationPitch * 0.017453292F);
		motionZ = MathHelper.cos(source.rotationYaw * 0.017453292F) * MathHelper.cos(source.rotationPitch * 0.017453292F) + source.motionZ;
	}

	public void onUpdate()
	{
		if(source == null && sourceUuid != null)
		{
			for (Entity entity : world.getEntities(Entity.class, (entity) -> entity.getUniqueID().equals(sourceUuid)))
				source = entity;
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		prevMotionX = motionX;
		prevMotionY = motionY;
		prevMotionZ = motionZ;

		damageCooldown = Math.max(0, damageCooldown-1);

		if(length > 512 || (decayTime < duration && !releaseBeam) || (anchorToSource && !sourceStack.isEmpty() && source instanceof EntityLivingBase && !ItemStack.areItemsEqualIgnoreDurability(((EntityLivingBase) source).getActiveItemStack(), sourceStack)))
			releaseBeam = true;

		if(source != null && anchorToSource)
			setPositionToEntity(source);

		if(releaseBeam)
		{
			decayTime--;
			if(decayTime < 0)
				setDead();
		}
		else
		{
			Vec3d posVec = new Vec3d(posX, posY, posZ);
			Vec3d nextPosVec = new Vec3d(motionX, motionY, motionZ).normalize().scale(length).add(posVec);

			Entity target = findEntityOnPath(posVec, nextPosVec);

			if(target != null)
			{
				length = (float) Math.sqrt((posX-target.posX)*(posX-target.posX) + (posY-target.posY)*(posY-target.posY) + (posZ-target.posZ)*(posZ-target.posZ));
				nextPosVec = new Vec3d(motionX, motionY, motionZ).normalize().scale(length).add(posVec);
			}
			else
			{
				RayTraceResult rayTrace = world.rayTraceBlocks(posVec, nextPosVec, false, true, false);
				if(rayTrace != null)
				{
					Vec3d hitVec = rayTrace.hitVec;
					length = (float) Math.sqrt((posX-hitVec.x)*(posX-hitVec.x) + (posY-hitVec.y)*(posY-hitVec.y) + (posZ-hitVec.z)*(posZ-hitVec.z));
					nextPosVec = new Vec3d(motionX, motionY, motionZ).normalize().scale(length).add(posVec);
				}
			}

			posVec = nextPosVec;
			nextPosVec = nextPosVec.add(new Vec3d(motionX, motionY, motionZ));
			target = findEntityOnPath(posVec, nextPosVec);
			if(target != null)
			{
				if(damageCooldown <= 0 && damage != 0)
				{
					if(damage < 0)
					{
						if(target instanceof EntityLivingBase)
							((EntityLivingBase) target).heal(damage);
					} else target.attackEntityFrom(new EntityDamageSource(MinestuckUniverse.MODID+".beam", source).setMagicDamage(), damage);

					damageCooldown = 30;
				}
			}
			else
			{
				RayTraceResult blockRayTrace = world.rayTraceBlocks(posVec, nextPosVec, false, true, false);
				if(blockRayTrace == null)
				{
					length += MathHelper.sqrt(motionX*motionX + motionY*motionY + motionZ*motionZ);
				}
				else
				{
					//world.setBlockState(blockRayTrace.getBlockPos(), MinestuckBlocks.genericObject.getDefaultState());
				}
			}

		}
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead()
	{
		isDead = true;
	}

	public static void fireBeam(Beam beam)
	{
		beam.world.getCapability(MSUCapabilities.BEAM_DATA, null).addBeam(beam);
		if(!beam.world.isRemote)
			for(EntityPlayer player : beam.world.playerEntities)
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_BEAMS, beam.world), player);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		posX = nbt.getDouble("PosX");
		posY = nbt.getDouble("PosZ");
		posZ = nbt.getDouble("PosZ");

		motionX = nbt.getDouble("MotionX");
		motionY = nbt.getDouble("MotionY");
		motionZ = nbt.getDouble("MotionZ");

		decayTime = nbt.getInteger("DecayTime");
		duration = nbt.getInteger("Duration");
		damageCooldown = nbt.getInteger("DamageCooldown");
		damage = nbt.getFloat("Damage");
		length = nbt.getFloat("Length");
		anchorToSource = nbt.getBoolean("Anchored");
		releaseBeam = nbt.getBoolean("BeamReleased");

		sourceStack = new ItemStack(nbt.getCompoundTag("Item"));

		sourceUuid = nbt.getUniqueId("Source");
	}
	
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setUniqueId("UUID", uuid);

		nbt.setDouble("PosX", posX);
		nbt.setDouble("PosY", posY);
		nbt.setDouble("PosZ", posZ);

		nbt.setDouble("MotionX", motionX);
		nbt.setDouble("MotionY", motionY);
		nbt.setDouble("MotionZ", motionZ);

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		prevMotionX = motionX;
		prevMotionY = motionY;
		prevMotionZ = motionZ;

		nbt.setInteger("DecayTime", decayTime);
		nbt.setInteger("Duration", duration);
		nbt.setInteger("DamageCooldown", damageCooldown);
		nbt.setFloat("Damage", damage);
		nbt.setFloat("Length", length);
		nbt.setBoolean("Anchored", anchorToSource);
		nbt.setBoolean("BeamReleased", releaseBeam);

		nbt.setTag("Item", sourceStack.writeToNBT(new NBTTagCompound()));
		if(source != null)
			nbt.setUniqueId("Source", source.getUniqueID());


		return nbt;
	}

	public void releaseBeam()
	{
		releaseBeam = true;
	}

	public boolean isBeamReleased()
	{
		return releaseBeam;
	}

	public UUID getUniqueID()
	{
		return uuid;
	}

	public void setPositionAndMotion(double posX, double posY, double posZ, double motionX, double motionY, double motionZ)
	{
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public Vec3d getStartPoint(float partialTicks)
	{
		return new Vec3d(lerp(prevPosX, posX, partialTicks), lerp(prevPosY, posY, partialTicks), lerp(prevPosZ, posZ, partialTicks));
	}

	public Vec3d getEndPoint(float partialTicks)
	{
		double x = lerp(prevMotionX, motionX, partialTicks), y = lerp(prevMotionY, motionY, partialTicks), z = lerp(prevMotionZ, motionZ, partialTicks);

		return new Vec3d(x, y, z).normalize().scale(length).addVector(lerp(prevPosX, posX, partialTicks)+x, lerp(prevPosY, posY, partialTicks)+y, lerp(prevPosZ, posZ, partialTicks)+z);
	}

	//TODO move to math helper class
	public static double lerp(double a, double b, double amount)
	{
		return a + (b-a)*amount;
	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end)
	{
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this.source, new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), BEAM_TARGETS);
		double d0 = 0.0D;

		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity1 = list.get(i);

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

		return entity;
	}

	public float getLength() {
		return length;
	}

	public float getAlpha()
	{
		return (float)decayTime/(float)duration;
	}
}
