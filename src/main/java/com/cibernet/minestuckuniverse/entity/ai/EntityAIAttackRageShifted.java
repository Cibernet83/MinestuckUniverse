package com.cibernet.minestuckuniverse.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRageShifted extends EntityAIAttackMelee
{
	public EntityAIAttackRageShifted(EntityCreature creature, double speedIn, boolean useLongMemory)
	{
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase target, double range)
	{
		double d0 = this.getAttackReachSqr(target);

		if (range <= d0 && this.attackTick <= 0)
		{
			this.attackTick = (int) (20 * Math.sqrt(attacker.width * attacker.height));
			this.attacker.swingArm(EnumHand.MAIN_HAND);

			if(!this.attacker.attackEntityAsMob(target))
			{
				float volume = attacker.width * attacker.height;

				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), attacker.getHealth());
			}
		}
	}
}
