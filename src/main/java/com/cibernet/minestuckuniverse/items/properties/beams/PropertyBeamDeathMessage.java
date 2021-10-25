package com.cibernet.minestuckuniverse.items.properties.beams;

import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.item.ItemStack;

public class PropertyBeamDeathMessage extends WeaponProperty implements IPropertyBeam
{
	String name;

	public PropertyBeamDeathMessage(String name)
	{
		this.name = name;
	}

	@Override
	public String beamDamageName(Beam beam, ItemStack stack, String name)
	{
		return name;
	}
}
