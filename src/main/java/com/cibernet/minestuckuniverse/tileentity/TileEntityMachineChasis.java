package com.cibernet.minestuckuniverse.tileentity;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityMachineChasis extends TileEntity implements IInventory, ITickable
{

    private NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    private String customName;

    public boolean assembling = false;

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : inventory)
            if(!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack stackInSlot = inventory.get(index);
        inventory.set(index, stack);

        if(stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inventory);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        ItemStackHelper.saveAllItems(compound, inventory);
        return super.writeToNBT(compound);
    }



    public ItemStack[] invToArray()
    {
        return new ItemStack[]
        {
            inventory.get(0),
            inventory.get(1),
            inventory.get(2),
            inventory.get(3),
            inventory.get(4),
        };

    }

    public boolean canAssemble()
    {
        return MachineChasisRecipes.recipeExists(invToArray());
    }

    public void assemble()
    {
        if(canAssemble() /*&& !world.isRemote*/)
        {
            IBlockState output = MachineChasisRecipes.getOutput(invToArray()).getDefaultState();
            clear();
            world.destroyBlock(pos, false);
            world.setBlockState(pos, output);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public String getGuiID() {return MinestuckUniverse.MODID + ":machine_chasis";}

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return inventory.get(index).isEmpty();
    }

    @Override
    public int getField(int id)
    {
        return assembling ? 1 : 0;
    }

    @Override
    public void setField(int id, int value)
    {
        assembling = value == 1;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : I18n.translateToLocal("container.machineChassis");
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String name) {this.customName = name;}

    @Nullable
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(this.getName());
    }

    @Override
    public void update() {
        if(assembling)
            assemble();
    }
}
