package com.cibernet.minestuckuniverse.gui.slots;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEmptyCaptcha extends Slot
{
	public SlotEmptyCaptcha(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	public boolean isItemValid(ItemStack itemStack)
	{
		return itemStack.getItem() == MinestuckItems.captchaCard && !itemStack.hasTagCompound();
	}
}
