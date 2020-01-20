package com.cibernet.minestuckuniverse.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;

public class EntityAIAttackShifted extends EntityAIAttackMelee
{
	public EntityAIAttackShifted(EntityLiving creature, double speedIn, boolean useLongMemory)
	{
		super((EntityCreature) creature, speedIn, useLongMemory);
	}
	
	@Override
	protected void checkAndPerformAttack(EntityLivingBase target, double p_190102_2_)
	{
		double d0 = this.getAttackReachSqr(target);
		
		if (p_190102_2_ <= d0 && this.attackTick <= 0)
		{
			this.attackTick = 20;
			this.attacker.swingArm(EnumHand.MAIN_HAND);
			
			if(!this.attacker.attackEntityAsMob(target))
				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 3.0f);
		}
	}
}
