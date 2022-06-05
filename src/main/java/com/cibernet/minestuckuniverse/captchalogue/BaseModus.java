package com.cibernet.minestuckuniverse.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Iterator;

public abstract class BaseModus extends Modus
{
	protected int size;
	protected NonNullList<ItemStack> list;
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		this.list = NonNullList.create();
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
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		this.size = nbt.getInteger("size");
		this.list = NonNullList.create();
		
		for(int i = 0; i < this.size && nbt.hasKey("item" + i); ++i) {
			this.list.add(new ItemStack(nbt.getCompoundTag("item" + i)));
		}
		
		if (this.side.isClient()) {
			this.items = NonNullList.create();
			this.changed = true;
		}
		
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("size", this.size);
		Iterator<ItemStack> iter = this.list.iterator();
		
		for(int i = 0; i < this.list.size(); ++i) {
			ItemStack stack = iter.next();
			nbt.setTag("item" + i, stack.writeToNBT(new NBTTagCompound()));
		}
		
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		
		if(stack.isEmpty())
			return false;
		
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).isEmpty())
			{
				list.set(i, stack);
				return true;
			}
		
		if(size <= list.size())
			return false;
		
		list.add(stack);
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
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
		Iterator<ItemStack> iter = this.list.iterator();
		
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
		if (MinestuckConfig.modusMaxSize > 0 && this.size >= MinestuckConfig.modusMaxSize)
			return false;
		++this.size;
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
			return ItemStack.EMPTY;
		}
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
		
		ItemStack item = getSort() || asCard ? list.remove(id) : this.list.set(id, ItemStack.EMPTY);
		
		if(asCard)
		{
			size--;
			item = AlchemyRecipes.createCard(item, false);
		}
		return item;
	}
	
	protected abstract boolean getSort();
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
}
