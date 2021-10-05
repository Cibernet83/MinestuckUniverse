package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.bowkind.PropertyHookshot;
import net.minecraft.entity.item.EntityItem;

public class PropertyPrjctleItemPull extends WeaponProperty implements IPropertyThrowable
{
	float radius;
	float strength;

	public PropertyPrjctleItemPull(float radius, float strength)
	{
		this.radius = radius;
		this.strength = strength;
	}

	@Override
	public void onProjectileUpdate(EntityMSUThrowable projectile)
	{
		for(EntityItem target : projectile.world.getEntitiesWithinAABB(EntityItem.class, projectile.getEntityBoundingBox().grow(radius)))
			PropertyHookshot.moveTowards(target, projectile, strength*2f);
	}
}
