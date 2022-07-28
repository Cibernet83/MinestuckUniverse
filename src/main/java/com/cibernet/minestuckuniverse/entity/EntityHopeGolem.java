package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIFollowHope;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIHopeHurtByTarget;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIHopeHurtTarget;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope.TechHopeyShit;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityHopeGolem extends EntityIronGolem
{
	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityHopeGolem.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	protected static final DataParameter<Integer> HOPE_TICKS = EntityDataManager.createKey(EntityHopeGolem.class, DataSerializers.VARINT);
	protected static final DataParameter<Boolean> ANGRY = EntityDataManager.createKey(EntityHopeGolem.class, DataSerializers.BOOLEAN);

	public static final int MAX_HOPE_TICKS = 7200;
	public static final int MAX_EFFECTIVE_TICKS = 6000;
	
	public int attackTimerr = 0;

	public EntityHopeGolem(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
		this.dataManager.register(HOPE_TICKS, MAX_EFFECTIVE_TICKS);
		this.dataManager.register(ANGRY, false);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(6, new EntityAIFollowHope(this, 1.0D, 10.0F, 2.0F));
		this.targetTasks.addTask(1, new EntityAIHopeHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIHopeHurtTarget(this));

		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true,
				(Predicate<EntityLiving>) p_apply_1_ -> p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper)));
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public void onAddedToWorld()
	{
		super.onAddedToWorld();
		getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HOPE, 20);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (this.attackTimerr > 0)
            --this.attackTimerr;

		if(!world.isRemote)
		{
			int hopeDecay = 1;

			if(hasOwner())
			{
				EntityLivingBase owner = getOwner();
				hopeDecay = owner.isDead ? MAX_EFFECTIVE_TICKS : Math.max(10-(int) (owner.getHealth()/owner.getMaxHealth()*10), 1);
			}

			List<EntityHopeGolem> golemAllies = world.getEntitiesWithinAABB(EntityHopeGolem.class, getEntityBoundingBox().grow(128), t -> t != this && t.getOwner() == getOwner());

			hopeDecay *= golemAllies.size()*2+1;

			setHopeTicks(getHopeTicks()-hopeDecay);
			if(getHopeTicks() < 0)
			{
				getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.HOPE, 20);
				setDead();
			}

			if (this.getAttackTarget() == null && this.isAngry())
				this.setAngry(false);
		}
	}
	
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
    	super.handleStatusUpdate(id);
        if (id == 4)
            this.attackTimerr = 10;
    }
	
	@SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimerr;
    }

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, Math.min(amount, 10 * (1 - (getHopeTicks()/MAX_EFFECTIVE_TICKS))));
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
		this.attackTimerr = 10;
		this.world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom((getHopeTicks() >= MAX_EFFECTIVE_TICKS * .8) ? new TechHopeyShit.HopeDamageSource(this) : DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));

        if (flag)
        {
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

	public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);

		if (entitylivingbaseIn == null)
			this.setAngry(false);
		else this.setAngry(true);
	}

	public int getHopeTicks() { return dataManager.get(HOPE_TICKS); }
	public void setHopeTicks(int v) { dataManager.set(HOPE_TICKS, Math.min(MAX_HOPE_TICKS, v)); }

	public boolean isAngry() { return dataManager.get(ANGRY); }
	public void setAngry(boolean v) { dataManager.set(ANGRY, v); }

	@Nullable
	public UUID getOwnerId()
	{
		return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
	}

	public void setOwnerId(@Nullable UUID p_184754_1_)
	{
		this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
	}

	public void setCreatedBy(EntityPlayer player)
	{
		this.setOwnerId(player.getUniqueID());
	}

	@Nullable
	public EntityLivingBase getOwner()
	{
		try
		{
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
		}
		catch (IllegalArgumentException var2)
		{
			return null;
		}
	}

	public boolean isOwner(EntityLivingBase entityIn)
	{
		return entityIn == this.getOwner();
	}


	public boolean hasOwner()
	{
		return getOwner() != null;
	}

	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
	{
		return target != owner && !(target instanceof EntityCreeper);
	}

	public boolean isOnSameTeam(Entity entityIn)
	{
		if (this.hasOwner())
		{
			EntityLivingBase entitylivingbase = this.getOwner();

			if (entityIn == entitylivingbase)
			{
				return true;
			}

			if (entitylivingbase != null)
			{
				return entitylivingbase.isOnSameTeam(entityIn);
			}
		}

		return super.isOnSameTeam(entityIn);
	}


	public void onDeath(DamageSource cause)
	{
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP)
		{
			this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
		}

		super.onDeath(cause);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setInteger("HopeTicks", getHopeTicks());
		compound.setBoolean("Angry", isAngry());
		if(hasOwner())
			compound.setString("OwnerUUID", this.getOwnerId().toString());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if(compound.hasKey("HopeTicks"))
			setHopeTicks(compound.getInteger("HopeTicks"));
		if(compound.hasKey("Angry"))
			setAngry(compound.getBoolean("Angry"));

		String s;
		if (compound.hasKey("OwnerUUID", 8))
			s = compound.getString("OwnerUUID");
		else
		{
			String s1 = compound.getString("Owner");
			s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
		}

		if (!s.isEmpty())
		{
			try
			{
				this.setOwnerId(UUID.fromString(s));
			}
			catch (Throwable var4)
			{

			}
		}

	}
}
