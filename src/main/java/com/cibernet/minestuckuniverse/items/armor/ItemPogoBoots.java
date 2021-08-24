package com.cibernet.minestuckuniverse.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ItemPogoBoots extends MSUArmorBase
{
	boolean isSolar = false;
	public final float power;

	public ItemPogoBoots(float power, ArmorMaterial materialIn, int renderIndexIn, String unlocName, String registryName)
	{
		super(materialIn, renderIndexIn, EntityEquipmentSlot.FEET, unlocName, registryName);
		this.power = power;
	}

	public ItemPogoBoots(float power, int maxUses, ArmorMaterial materialIn, int renderIndexIn, String unlocName, String registryName)
	{
		super(maxUses, materialIn, renderIndexIn, EntityEquipmentSlot.FEET, unlocName, registryName);
		this.power = power;
	}



	public ItemPogoBoots setSolar()
	{
		this.isSolar = true;
		return this;
	}

	public boolean isSolar()
	{
		return isSolar;
	}
}
