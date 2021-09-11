package com.cibernet.minestuckuniverse.items.weapons;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IBeamStats
{
	float getBeamRadius(ItemStack stack);
	int getBeamHurtTime(ItemStack stack);

	void setBeamTexture(String name);
	void setCustomBeamTexture();
	ResourceLocation getBeamTexture();
}
