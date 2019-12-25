package com.cibernet.minestuckuniverse.gui.container;

import com.cibernet.minestuckuniverse.gui.slots.SlotEmptyCaptcha;
import com.cibernet.minestuckuniverse.tileentity.TileEntityAutoCaptcha;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.inventory.SlotOutput;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAutoCaptcha extends Container
{
	private final TileEntityAutoCaptcha tileEntity;
	private int timer;
	
	public ContainerAutoCaptcha(InventoryPlayer player, TileEntityAutoCaptcha tileEntity)
	{
		super();
		this.tileEntity = tileEntity;
		
		addSlotToContainer(new Slot(tileEntity, 0, 56, 22));
		addSlotToContainer(new SlotEmptyCaptcha(tileEntity, 1, 56, 48));
		addSlotToContainer(new SlotOutput(tileEntity, 2, 116, 35));
		
		for(int yy = 0; yy < 3; yy++)
			for(int xx = 0; xx < 9; xx++)
				addSlotToContainer(new Slot(player, xx + yy*9 + 9, 8 + xx*18, 84 + yy*18 ));
		
		for(int xx = 0; xx < 9; xx++)
			addSlotToContainer(new Slot(player, xx, 8 + xx*18, 142));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tileEntity.isUsableByPlayer(playerIn);
	}
	
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		this.tileEntity.setField(id, data);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < listeners.size(); i++)
		{
			IContainerListener listener = listeners.get(i);
			if(timer != tileEntity.getField(0)) listener.sendWindowProperty(this, 0, tileEntity.getField(0));
		}
		
		timer = tileEntity.getField(0);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		ItemStack captchaStack = slot.inventory.getStackInSlot(1);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 2)
			{
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 1 && index != 0)
			{
				
				if (itemstack1.getItem() == MinestuckItems.captchaCard && !AlchemyRecipes.hasDecodedItem(itemstack1) && (captchaStack.isEmpty() || itemstack1.isItemEqual(captchaStack)))
				{
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (true)
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 3 && index < 30)
				{
					if (!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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
			
			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
}
