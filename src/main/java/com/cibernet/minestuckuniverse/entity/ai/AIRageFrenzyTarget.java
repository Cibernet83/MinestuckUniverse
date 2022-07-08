package com.cibernet.minestuckuniverse.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;

import javax.annotation.Nullable;

public class AIRageFrenzyTarget extends EntityAINearestAttackableTarget<EntityCreature>
{

	public AIRageFrenzyTarget(EntityCreature creature)
	{
		super(creature, EntityCreature.class, true);
	}

	@Override
	protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
	{
		if(target instanceof EntityCreature)
			for(EntityAITasks.EntityAITaskEntry taskEntry : ((EntityCreature) target).targetTasks.taskEntries)
				if(taskEntry.action instanceof AIRageFrenzyTarget)
					return true;
		return false;
	}
}
