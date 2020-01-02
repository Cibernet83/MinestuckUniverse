package com.cibernet.minestuckuniverse.tileentity;

import com.cibernet.minestuckuniverse.blocks.BlockAutoWidget;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;

public class TileEntityAutoWidget extends TileEntity implements ITickable, ISidedInventory
{
	protected NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	public int timer;
	public boolean isActive = false;
	
	@Override
	public void update()
	{
		if(!isEmpty())
		{
			if(timer <= 0)
				processContents();
			else timer--;
		}
		else
		{
			if(timer <= 0)
			{
				timer = 36;
			}
		}
		
		boolean item = this.getStackInSlot(0).getCount() != 0;
		if (item != this.isActive)
		{
			this.isActive = item;
			BlockAutoWidget.updateItem(isActive, world, getPos());
		}
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
	
	public GristSet getGristWidgetResult()
	{
		ItemStack item = AlchemyRecipes.getDecodedItem((ItemStack)this.inventory.get(0), true);
		GristSet gristSet = GristRegistry.getGristConversion(item);
		if ((this.inventory.get(0)).getItem() == MinestuckItems.captchaCard && !AlchemyRecipes.isPunchedCard(this.inventory.get(0)) && item.getItem() != MinestuckItems.captchaCard && gristSet != null) {
			if (item.getCount() != 1) {
				gristSet.scaleGrist((float)item.getCount());
			}
			
			if (item.isItemDamaged()) {
				float multiplier = 1.0F - (float)item.getItem().getDamage(item) / (float)item.getMaxDamage();
				Iterator var4 = gristSet.getArray().iterator();
				
				while(var4.hasNext()) {
					GristAmount amount = (GristAmount)var4.next();
					gristSet.setGrist(amount.getType(), (int)((float)amount.getAmount() * multiplier));
				}
			}
			
			return gristSet;
		} else {
			return null;
		}
	}
	
	
	
	public void processContents()
	{
		GristAmount gristAmount;
		GristSet set = getGristWidgetResult();
		if(set != null)
		{
			Iterator var2 = set.getMap().entrySet().iterator();
			while(var2.hasNext() && !world.isRemote)
			{
				Map.Entry<GristType, Integer> entry = (Map.Entry) var2.next();
				for(int grist = entry.getValue(); grist != 0; grist -= gristAmount.getAmount())
				{
					gristAmount = new GristAmount(entry.getKey(), grist <= 3 ? grist : this.world.rand.nextInt(grist) + 1);
					EntityGrist entity = new EntityGrist(this.world, (double) this.pos.getX() + 0.5D, (double) (this.pos.getY() + 1), (double) this.pos.getZ() + 0.5D, gristAmount);
					entity.motionX /= 2.0D;
					entity.motionY /= 2.0D;
					entity.motionZ /= 2.0D;
					this.world.spawnEntity(entity);
				}
			}
		}
		ItemStackHelper.getAndSplit(this.inventory, 0, 1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		ItemStackHelper.loadAllItems(compound,inventory);
		timer = compound.getInteger("time");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		ItemStackHelper.saveAllItems(compound, inventory);
		compound.setInteger("time", timer);
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
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
		return 1;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (stack.getItem() == MinestuckItems.captchaCard && isEmpty() && stack.hasTagCompound())
			return !stack.getTagCompound().getBoolean("punched") && stack.getTagCompound().getInteger("contentSize") > 0 && GristRegistry.getGristConversion(AlchemyRecipes.getDecodedItem(stack)) != null;
	 	else
	 		return false;
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
	public void clear()
	{
		inventory.clear();
	}
	
	@Override
	public String getName()
	{
		return I18n.translateToLocal(MinestuckUniverseBlocks.autoWidget.getUnlocalizedName());
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {0};
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn) && !isActive && direction != world.getBlockState(pos).getValue(BlockAutoWidget.FACING) && direction != EnumFacing.UP;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return false;
	}
}
