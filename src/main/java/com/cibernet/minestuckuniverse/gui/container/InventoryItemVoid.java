package com.cibernet.minestuckuniverse.gui.container;

import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItemVoid extends InventoryBasic
{
	private static final int slotsCount = 27;

	public InventoryItemVoid()
	{
		super("container.itemVoid", false, slotsCount);
		addItem(new ItemStack(MinestuckBlocks.genericObject));
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack result = super.removeStackFromSlot(index);
		addItem(new ItemStack(MinestuckBlocks.genericObject));
		return result;
	}


	@Override
	public ItemStack addItem(ItemStack stack)
	{
		ItemStack itemstack = stack.copy();

		for (int i = 0; i < this.slotsCount; ++i)
		{
			ItemStack itemstack1 = this.getStackInSlot(i);

			if (itemstack1.isEmpty())
			{
				this.setInventorySlotContents(i, itemstack);
				this.markDirty();
				return ItemStack.EMPTY;
			}

			if (ItemStack.areItemsEqual(itemstack1, itemstack))
			{
				int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
				int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

				if (k > 0)
				{
					itemstack1.grow(k);
					itemstack.shrink(k);

					if (itemstack.isEmpty())
					{
						this.markDirty();
						return ItemStack.EMPTY;
					}
				}
			}
		}

		for (int i = 0; i < this.slotsCount-1; ++i)
			this.setInventorySlotContents(i, getStackInSlot(i+1));
		this.setInventorySlotContents(slotsCount-1, itemstack);
		this.markDirty();

		return ItemStack.EMPTY;
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	public void loadInventoryFromNBT(NBTTagList p_70486_1_)
	{
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			this.setInventorySlotContents(i, ItemStack.EMPTY);
		}

		for (int k = 0; k < p_70486_1_.tagCount(); ++k)
		{
			NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.getSizeInventory())
			{
				this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
			}
		}
	}

	public NBTTagList saveInventoryToNBT()
	{
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.getStackInSlot(i);

			if (!itemstack.isEmpty())
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				itemstack.writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}

}
