package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

public class PropertyFire extends WeaponProperty implements IEnchantableProperty
{
	boolean onCrit;
	float chance;
	int fireSeconds;

	public PropertyFire(int fireSeconds, float chance, boolean onCrit)
	{
		this.fireSeconds = fireSeconds;
		this.chance = chance;
		this.onCrit = onCrit;
	}


	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
			target.setFire(fireSeconds);
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		if(onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
			target.setFire(fireSeconds);
		return super.onCrit(stack, player, target, damageModifier);
	}

	@Override
	public Boolean canEnchantWith(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.FIRE_ASPECT ? false : null;
	}
}
