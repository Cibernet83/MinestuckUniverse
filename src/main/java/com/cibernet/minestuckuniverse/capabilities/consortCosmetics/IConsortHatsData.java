package com.cibernet.minestuckuniverse.capabilities.consortCosmetics;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IConsortHatsData extends IMSUCapabilityBase<EntityLivingBase>
{
	void setHeadStack(ItemStack stack);
	ItemStack getHeadStack();

	void setPickupDelay(int i);
	int getPickupDelay();
	int shrinkPickupDelay();
}
