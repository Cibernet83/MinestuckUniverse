package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.IEnchantableProperty;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class PropertyFlamingArrow extends WeaponProperty implements IPropertyArrow, IEnchantableProperty
{
	float drawTime;
	int fireTime;

	public PropertyFlamingArrow(int fireTime, float drawTime)
	{
		this.fireTime = fireTime;
		this.drawTime = drawTime;
	}

	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		if(arrow.isBurning())
			result.entityHit.setFire(fireTime);
		return true;
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow, float chargeTime)
	{
		if(chargeTime >= drawTime)
			arrow.setFire(600);
		return arrow;
	}

	@Override
	public Boolean canEnchantWith(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.FLAME ? false : null;
	}
}
