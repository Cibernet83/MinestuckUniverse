package com.cibernet.minestuckuniverse.tileentity;

import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.item.ItemCaptchaCard;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class TileEntityAutoCaptcha extends TileEntity implements ITickable, ISidedInventory
{
	protected NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	public int timer;
	public boolean powered = false;
	private String customName;
	public static final int totalTime = 50;
	
	private static final int[] SLOTS_TOP = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
	private static final int[] SLOTS_SIDES = new int[] {1};
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
			return SLOTS_BOTTOM;
		else
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == 2;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 3;
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.get(0).isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
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
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return (index != 2) && (index == 0 || (index == 1 && stack.getItem() == MinestuckItems.captchaCard && !stack.hasTagCompound()));
	}
	
	@Override
	public int getField(int id)
	{
		return timer;
	}
	
	@Override
	public void setField(int id, int value)
	{
		timer = value;
	}
	
	@Override
	public int getFieldCount()
	{
		return 1;
	}
	
	@Override
	public void clear() {
		inventory.clear();
	}
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : I18n.translateToLocal("tile.autoCaptcha.name");
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
	public void update()
	{
		powered = world.isBlockPowered(getPos());
		
		if(canProcess() && !powered)
		{
				timer--;
			if(timer <= 0)
				processContents();
		}
		else timer = totalTime;
	}
	
	public boolean canProcess()
	{
		ItemStack in = inventory.get(0);
		ItemStack out = AlchemyRecipes.createCard(in, false);
		return (MSUUtils.compareCards(out, inventory.get(2), false) || inventory.get(2).isEmpty()) && !inventory.get(1).isEmpty() && !in.isEmpty();
	}
	
	public void processContents()
	{
		timer = totalTime;
		ItemStackHelper.getAndSplit(this.inventory, 1, 1);
		ItemStack in = ItemStackHelper.getAndSplit(this.inventory, 0, 64);
		ItemStack out = AlchemyRecipes.createCard(in, false);
		
		if(MSUUtils.compareCards(out, inventory.get(2), false))
			inventory.get(2).grow(1);
		else inventory.set(2, out);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		timer = compound.getInteger("timer");
		
		if (compound.hasKey("CustomName", 8))
			this.customName = compound.getString("CustomName");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("timer", (short)this.timer);
		ItemStackHelper.saveAllItems(compound, this.inventory);
		
		if (this.hasCustomName())
			compound.setString("CustomName", this.customName);
		
		return super.writeToNBT(compound);
	}
	
	
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tagCompound = this.getUpdateTag();
		return new SPacketUpdateTileEntity(this.pos, 2, tagCompound);
	}
	
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
}
