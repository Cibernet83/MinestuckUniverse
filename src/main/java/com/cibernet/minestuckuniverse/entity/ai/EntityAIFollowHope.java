package com.cibernet.minestuckuniverse.entity.ai;

import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowHope extends EntityAIBase
{
	private final EntityHopeGolem golem;
	private EntityLivingBase owner;
	World world;
	private final double followSpeed;
	private final PathNavigate petPathfinder;
	private int timeToRecalcPath;
	float maxDist;
	float minDist;
	private float oldWaterCost;

	public EntityAIFollowHope(EntityHopeGolem golem, double followSpeedIn, float minDistIn, float maxDistIn)
	{
		this.golem = golem;
		this.world = golem.world;
		this.followSpeed = followSpeedIn;
		this.petPathfinder = golem.getNavigator();
		this.minDist = minDistIn;
		this.maxDist = maxDistIn;
		this.setMutexBits(3);

		if (!(golem.getNavigator() instanceof PathNavigateGround) && !(golem.getNavigator() instanceof PathNavigateFlying))
		{
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.golem.getOwner();

		if (entitylivingbase == null)
		{
			return false;
		}
		else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
		{
			return false;
		}
		else if (this.golem.getDistanceSq(entitylivingbase) < (double)(this.minDist * this.minDist))
		{
			return false;
		}
		else
		{
			this.owner = entitylivingbase;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return !this.petPathfinder.noPath() && this.golem.getDistanceSq(this.owner) > (double)(this.maxDist * this.maxDist);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.golem.getPathPriority(PathNodeType.WATER);
		this.golem.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		this.owner = null;
		this.petPathfinder.clearPath();
		this.golem.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		this.golem.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.golem.getVerticalFaceSpeed());

		if (--this.timeToRecalcPath <= 0)
		{
			this.timeToRecalcPath = 10;

			if (!this.petPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed))
			{
				if (!this.golem.getLeashed() && !this.golem.isRiding())
				{
					if (this.golem.getDistanceSq(this.owner) >= 144.0D)
					{
						int i = MathHelper.floor(this.owner.posX) - 2;
						int j = MathHelper.floor(this.owner.posZ) - 2;
						int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

						for (int l = 0; l <= 4; ++l)
						{
							for (int i1 = 0; i1 <= 4; ++i1)
							{
								if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1))
								{
									this.golem.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.golem.rotationYaw, this.golem.rotationPitch);
									this.petPathfinder.clearPath();
									return;
								}
							}
						}
					}
				}
			}
		}

	}

	protected boolean isTeleportFriendlyBlock(int x, int p_192381_2_, int y, int p_192381_4_, int p_192381_5_)
	{
		BlockPos blockpos = new BlockPos(x + p_192381_4_, y - 1, p_192381_2_ + p_192381_5_);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.golem) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
	}
}
