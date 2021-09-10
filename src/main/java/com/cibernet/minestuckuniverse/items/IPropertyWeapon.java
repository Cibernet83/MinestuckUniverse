package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IPropertyWeapon<T extends Item>
{

	List<WeaponProperty> getProperties();

	default List<WeaponProperty> getProperties(ItemStack stack)
	{
		return getProperties();
	}

	default T addProperties(WeaponProperty... properties)
	{
		List<WeaponProperty> propertiesList = getProperties();
		for(WeaponProperty p : properties)
		{
			for (WeaponProperty p1 : propertiesList)
				if(!p.compatibleWith(p1))
					throw new IllegalArgumentException("Property " + p1 + " is not compatible with " + p);

			propertiesList.add(p);
		}
		return (T) this;
	}
	default boolean hasProperty(Class<? extends WeaponProperty> propertyClass, ItemStack stack)
	{
		return getProperty(propertyClass, stack) != null;
	}

	default WeaponProperty getProperty(Class<? extends WeaponProperty> propertyClass, ItemStack stack)
	{
		for(WeaponProperty p : getProperties(stack))
			if(propertyClass.isInstance(p))
				return p;
		return null;
	}
}
