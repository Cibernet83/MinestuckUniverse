package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PropertyVisualParry extends WeaponProperty implements IPropertyShield
{
	@Override
	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player) {
		return stack.getItem() instanceof MSUShieldBase && ((MSUShieldBase) stack.getItem()).isParrying(stack);
	}
}
