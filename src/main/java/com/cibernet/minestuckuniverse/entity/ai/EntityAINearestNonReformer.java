package com.cibernet.minestuckuniverse.entity.ai;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.google.common.base.Predicate;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nullable;

public class EntityAINearestNonReformer extends EntityAINearestAttackableTargetWithHeight
{
	public EntityAINearestNonReformer(EntityCreature owner, Class<? extends Entity> target, float par3, int par4, boolean par5, boolean par6, Predicate par7IEntitySelector) {
		super(owner, target, par3, par4, par5, par6, par7IEntitySelector);
	}

	@Override
	protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
	{
		return super.isSuitableTarget(target, includeInvincibles) && (target == null || !target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isReforming());
	}
}
