package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class PropertyShieldHeal extends WeaponProperty implements IPropertyShield
{
	float healPctg;
	boolean onParry;

	public PropertyShieldHeal(float healPctg, boolean onParry)
	{
		this.onParry = onParry;
		this.healPctg = healPctg;
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{
		if(!onParry && blocked)
			player.heal(damage*healPctg);
	}

	@Override
	public boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		if(onParry)
			player.heal(damage*healPctg);
		return true;
	}
}
