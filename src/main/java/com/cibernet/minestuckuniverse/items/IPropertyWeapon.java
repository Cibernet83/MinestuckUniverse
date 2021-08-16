package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.item.Item;

import java.util.List;

public interface IPropertyWeapon<T extends Item>
{

	List<WeaponProperty> getProperties();

	public default T addProperties(WeaponProperty... properties)
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
	public default boolean hasProperty(Class<WeaponProperty> propertyClass)
	{
		return getProperty(propertyClass) != null;
	}

	default WeaponProperty getProperty(Class<WeaponProperty> propertyClass)
	{
		for(WeaponProperty p : getProperties())
			if(propertyClass.isInstance(p))
				return p;
		return null;
	}
}
