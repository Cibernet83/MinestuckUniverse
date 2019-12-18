package com.cibernet.minestuckuniverse.gui.container;

import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerMachineChasis extends Container
{
    private final TileEntityMachineChasis tileEntity;

    private boolean assembling;

    public ContainerMachineChasis(InventoryPlayer player, TileEntityMachineChasis tileEntity)
    {
        super();
        this.tileEntity = tileEntity;

        addSlotToContainer(new Slot(tileEntity, 0, 80, 38));
        addSlotToContainer(new Slot(tileEntity, 1, 80, 13));
        addSlotToContainer(new Slot(tileEntity, 2, 103, 38));
        addSlotToContainer(new Slot(tileEntity, 3, 80, 63));
        addSlotToContainer(new Slot(tileEntity, 4, 57, 38));

        for(int yy = 0; yy < 3; yy++)
            for(int xx = 0; xx < 9; xx++)
                addSlotToContainer(new Slot(player, xx + yy*9 + 9, 8 + xx*18, 84 + yy*18 ));

        for(int xx = 0; xx < 9; xx++)
            addSlotToContainer(new Slot(player, xx, 8 + xx*18, 142));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for(int i = 0; i < listeners.size(); i++)
        {
            IContainerListener listener = listeners.get(i);
            if(assembling != (tileEntity.getField(0) == 1)) listener.sendWindowProperty(this, 0, tileEntity.getField(0));
        }

        assembling = tileEntity.getField(0) == 1;
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
    
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
        
            if (index < 5)
            {
                if (!this.mergeItemStack(itemstack1, 5, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 5, false))
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

    public void assemble() {assembling = true;}

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.isUsableByPlayer(playerIn);
    }
}
