package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class PropertySweep extends WeaponProperty implements IEnchantableProperty
{
	float rangeMod;

	public PropertySweep(float rangeMod)
	{
		this.rangeMod = rangeMod;
	}

	public PropertySweep()
	{
		this(1);
	}


	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.onEntityHit(stack, target, player);

		if(!(player instanceof EntityPlayer))
			return;

		float attackStrength = CommonEventHandler.getCooledAttackStrength((EntityPlayer) player);

		boolean isCrit = attackStrength > 0.9F && !player.isSprinting() && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder()
				&& !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && target instanceof EntityLivingBase;

		if (attackStrength > 0.9F && !isCrit && !player.isSprinting() && player.onGround && (double)(player.distanceWalkedModified - player.prevDistanceWalkedModified) < (double)player.getAIMoveSpeed())
		{
			float dmg = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * (0.2F + attackStrength * attackStrength * 0.8F);

			for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().grow(1.0D*rangeMod, 0.25D, 1.0D*rangeMod)))
			{
				if (entitylivingbase != player && entitylivingbase != target && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D*rangeMod)
				{
					entitylivingbase.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
					entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), dmg);
				}
			}

			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
			((EntityPlayer)player).spawnSweepParticles();
		}
	}

	@Override
	public Boolean canEnchantWith(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.SWEEPING ? true : null;
	}
}
