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
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        int slotTotal = inventorySlots.size();

        if(slot != null && !slot.getHasStack())
        {
            ItemStack ogStack = slot.getStack();
            stack = ogStack.copy();
            boolean result = false;

            if(index <= 4)
                result = mergeItemStack(ogStack,5, slotTotal, false);
            else
                result = mergeItemStack(ogStack, 0, 1, false);

            if(!result)
                return ItemStack.EMPTY;

            if(!ogStack.isEmpty())
                slot.onSlotChanged();
        }

        return stack;
    }

    public void assemble() {assembling = true;}

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.isUsableByPlayer(playerIn);
    }
}
