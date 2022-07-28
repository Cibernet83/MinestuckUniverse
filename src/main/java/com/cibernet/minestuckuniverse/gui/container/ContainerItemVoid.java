package com.cibernet.minestuckuniverse.gui.container;

import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemVoid extends Container
{
	private final int numRows;
	private final IInventory inventory;

	public ContainerItemVoid(EntityPlayer player)
	{
		inventory = GameData.getItemVoid();
		this.numRows = inventory.getSizeInventory() / 9;
		inventory.openInventory(player);

		for (int j = 0; j < this.numRows; ++j)
		{
			for (int k = 0; k < 9; ++k)
				this.addSlotToContainer(new VoidSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
		}

		for (int l = 0; l < 3; ++l)

		{
			for (int j1 = 0; j1 < 9; ++j1)
				this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 94 + l * 18));
		}

		for (int i1 = 0; i1 < 9; ++i1)
			this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 152));
	}


	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.numRows * 9)
			{
				if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public static class VoidSlot extends Slot
	{

		public VoidSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
