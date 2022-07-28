package com.cibernet.minestuckuniverse.entity.ai;

import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIHopeHurtTarget extends EntityAITarget
{
	EntityHopeGolem golem;
	EntityLivingBase attacker;
	private int timestamp;

	public EntityAIHopeHurtTarget(EntityHopeGolem golem)
	{
		super(golem, false);
		this.golem = golem;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if (!this.golem.hasOwner())
		{
			return false;
		}
		else
		{
			EntityLivingBase entitylivingbase = this.golem.getOwner();

			if (entitylivingbase == null)
			{
				return false;
			}
			else
			{
				this.attacker = entitylivingbase.getLastAttackedEntity();
				int i = entitylivingbase.getLastAttackedEntityTime();
				return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.golem.shouldAttackEntity(this.attacker, entitylivingbase);
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.attacker);
		EntityLivingBase entitylivingbase = this.golem.getOwner();

		if (entitylivingbase != null)
		{
			this.timestamp = entitylivingbase.getLastAttackedEntityTime();
		}

		super.startExecuting();
	}
}