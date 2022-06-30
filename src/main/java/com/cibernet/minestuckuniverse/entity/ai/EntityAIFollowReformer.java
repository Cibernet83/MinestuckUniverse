package com.cibernet.minestuckuniverse.entity.ai;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIFollowReformer extends EntityAIBase
{
	/** The entity using this AI that is tempted by the player. */
	private final EntityCreature temptedEntity;
	private final double speed;
	/** X position of player tempting this mob */
	private double targetX;
	/** Y position of player tempting this mob */
	private double targetY;
	/** Z position of player tempting this mob */
	private double targetZ;
	/** Tempting player's pitch */
	private double pitch;
	/** Tempting player's yaw */
	private double yaw;

	private EntityPlayer temptingPlayer;
	private boolean isRunning;

	public EntityAIFollowReformer(EntityCreature temptedEntityIn, double speedIn)
	{
		this.temptedEntity = temptedEntityIn;
		this.speed = speedIn;
		this.setMutexBits(3);

		if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround))
		{
			throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		this.temptingPlayer = this.temptedEntity.world.getClosestPlayerToEntity(this.temptedEntity, 10.0D);

		if (this.temptingPlayer == null)
			return false;
		else return this.isTempting(this.temptingPlayer);
	}

	protected boolean isTempting(EntityPlayer player)
	{
		return player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.BLOOD_REFORMERS_REACH);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		if (this.temptedEntity.getDistanceSq(this.temptingPlayer) < 36.0D)
		{
			if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
			{
				return false;
			}

			if (Math.abs((double)this.temptingPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs((double)this.temptingPlayer.rotationYaw - this.yaw) > 5.0D)
			{
				return false;
			}
		}
		else
		{
			this.targetX = this.temptingPlayer.posX;
			this.targetY = this.temptingPlayer.posY;
			this.targetZ = this.temptingPlayer.posZ;
		}

		this.pitch = (double)this.temptingPlayer.rotationPitch;
		this.yaw = (double)this.temptingPlayer.rotationYaw;

		return this.shouldExecute();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.targetX = this.temptingPlayer.posX;
		this.targetY = this.temptingPlayer.posY;
		this.targetZ = this.temptingPlayer.posZ;
		this.isRunning = true;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		this.temptingPlayer = null;
		this.temptedEntity.getNavigator().clearPath();
		this.isRunning = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, (float)(this.temptedEntity.getHorizontalFaceSpeed() + 20), (float)this.temptedEntity.getVerticalFaceSpeed());

		if (this.temptedEntity.getDistanceSq(this.temptingPlayer) < 6.25D)
		{
			this.temptedEntity.getNavigator().clearPath();
		}
		else
		{
			this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
		}
	}

	/**
	 * @see #isRunning
	 */
	public boolean isRunning()
	{
		return this.isRunning;
	}
}
