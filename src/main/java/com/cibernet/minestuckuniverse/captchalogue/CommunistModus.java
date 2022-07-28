package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.CommunistGuiHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class CommunistModus extends Modus
{
	protected static int size;
	public static NonNullList<ItemStack> list;
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int prevSize)
	{
		if(list == null)
		{
			this.list = NonNullList.create();
		}
		if (prev != null)
		{
			Iterator var3 = prev.iterator();
			
			while(var3.hasNext())
			{
				ItemStack stack = (ItemStack) var3.next();
				if(!stack.isEmpty())
				{
					this.list.add(stack);
				}
			}
		}
		
		int remainingCards = Math.max(0, prevSize+size - MinestuckConfig.modusMaxSize);
		size = MinestuckConfig.modusMaxSize > 0 ? Math.min(MinestuckConfig.modusMaxSize, size+prevSize) : size+prevSize;
		
		if(MinestuckConfig.modusMaxSize > 0)
			for(int i = 0; i < remainingCards; i++)
				CaptchaDeckHandler.launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, 1));
			
		sendUpdate();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		
		if (this.side.isClient()) {
			this.items = NonNullList.create();
			this.changed = true;
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.REQUEST_COM_UPDATE));
			getGuiHandler().updateContent();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		return nbtTagCompound;
	}
	
	public static void readFromNBTGlobal(NBTTagCompound nbt) {
		size = nbt.getInteger("size");
		list = NonNullList.create();
		
		for(int i = 0; i < size && nbt.hasKey("item" + i); ++i) {
			list.add(new ItemStack(nbt.getCompoundTag("item" + i)));
		}
		
		/*
		if (this.side.isClient()) {
			this.items = NonNullList.create();
			this.changed = true;
		}
		*/
	}
	
	public static NBTTagCompound writeToNBTGlobal(NBTTagCompound nbt) {
		nbt.setInteger("size", size);
		if(list == null)
			list = NonNullList.create();
		Iterator<ItemStack> iter = list.iterator();
		
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = iter.next();
			nbt.setTag("item" + i, stack.writeToNBT(new NBTTagCompound()));
		}
		
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if((size <= list.size() && !list.get(list.size()-1).isEmpty()) || stack.isEmpty())
			return false;
		
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).isEmpty())
			{
				list.set(i, stack);
				return sendUpdate();
			}
		list.add(stack);
		
		return sendUpdate();
	}
	
	public boolean sendUpdate()
	{
		if(!player.world.isRemote)
			for(EntityPlayer p : player.world.getPlayers(EntityPlayer.class, (e) -> MinestuckPlayerData.getData(e).modus instanceof CommunistModus))
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.COM_UPDATE, writeToNBTGlobal(new NBTTagCompound())), p);
		return true;
	}
	
	public static boolean sendUpdate(EntityPlayer player)
	{
		if(!player.world.isRemote)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.COM_UPDATE, writeToNBTGlobal(new NBTTagCompound())), player);
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		return NonNullList.create();
	}
	
	public NonNullList<ItemStack> getGlobalItems()
	{
		if (this.side.isServer())
		{
			NonNullList<ItemStack> items = NonNullList.create();
			this.fillList(items);
			return items;
		} else
		{
			if (this.changed)
				this.fillList(this.items);
			return this.items;
		}
	}
	
	protected void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		if(list == null)
			list = NonNullList.create();
		Iterator<ItemStack> iter = list.iterator();
		
		for(int i = 0; i < this.size; ++i) {
			if (iter.hasNext()) {
				items.add(iter.next());
			} else {
				items.add(ItemStack.EMPTY);
			}
		}
		
	}
	
	public boolean increaseSize()
	{
		if (MinestuckConfig.modusMaxSize > 0 && size >= MinestuckConfig.modusMaxSize)
			return false;
		++this.size;
		sendUpdate();
		return true;
	}
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				sendUpdate();
				return new ItemStack(MinestuckItems.captchaCard);
			} else return ItemStack.EMPTY;
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack item : list)
				CaptchaDeckHandler.launchAnyItem(player, item);
			list.clear();
			sendUpdate();
			return ItemStack.EMPTY;
		}
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
		
		ItemStack item = this.list.set(id, ItemStack.EMPTY);
		
		if(asCard)
		{
			list.remove(id);
			size--;
			item = AlchemyRecipes.createCard(item, false);
		}
		sendUpdate();
		return item;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return 0;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
		{
			gui = new CommunistGuiHandler(this);
			gui.updateContent();
		}
		return gui;
	}
}
