package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.MemoryGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;

public class MemoryModus extends BaseModus
{
	public NonNullList<MemoryEntry> entries = NonNullList.create();
	@SideOnly(Side.CLIENT)
	protected NonNullList<MemoryEntry> clientEntries;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int size)
	{
		super.initModus(prev, size);
		
		while(this.list.size() <= this.size)
			this.list.add(ItemStack.EMPTY);

		shuffleEntries();
	}
	
	@Override
	protected boolean getSort()
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new MemoryGuiHandler(this);
		return gui;
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(!super.putItemStack(stack))
			return false;
		
		NonNullList<ItemStack> listA = new NonNullList<ItemStack>(){{addAll(list);}};
		Collections.shuffle(listA);
		//list = listA;
		shuffleEntries();
		return true;
	}
	
	@Override
	public boolean increaseSize()
	{
		if(super.increaseSize())
		{
			list.add(ItemStack.EMPTY);
			shuffleEntries();
			return true;
		}
		return false;
	}
	
	private void shuffleEntries()
	{
		entries.clear();

		for(int i = 0; i < list.size(); i++)
		{
			entries.add(new MemoryEntry(list.get(i), i));
			entries.add(new MemoryEntry(list.get(i), i));
		}

		Collections.shuffle(entries);

	}

	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		ItemStack result = super.getItem(id, asCard);

		if(asCard || id == CaptchaDeckHandler.EMPTY_CARD)
			shuffleEntries();
		else if(id != -1)
		{
			for (MemoryEntry entry : entries)
				if (entry.index == id)
					entry.stack = ItemStack.EMPTY;
		}

		return result;
	}

	public NonNullList<MemoryEntry> getDisplayEntries()
	{
		if (this.side.isServer())
		{
			NonNullList<MemoryEntry> entries = NonNullList.create();
			this.fillEntries(entries);
			return entries;
		} else
		{
			if (this.changed)
				this.fillEntries(this.clientEntries);
			return this.clientEntries;
		}
	}
	
	private void fillEntries(NonNullList<MemoryEntry> items)
	{
		items.clear();
		Iterator<MemoryEntry> iter = this.entries.iterator();
		
		for(int i = 0; i < this.size*2; ++i) {
			if (iter.hasNext()) {
				items.add(iter.next());
			} else {
				items.add(new MemoryEntry(ItemStack.EMPTY, i));
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.entries = NonNullList.create();

		for(int i = 0; i < this.size*2 && nbt.hasKey("entry" + i); ++i) {
			this.entries.add(new MemoryEntry(nbt.getCompoundTag("entry" + i)));
		}

		if (this.side.isClient()) {
			this.clientEntries = NonNullList.create();
			this.changed = true;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		Iterator<MemoryEntry> iter = this.entries.iterator();

		for(int i = 0; i < this.entries.size(); ++i) {
			MemoryEntry entry = iter.next();
			nbt.setTag("entry" + i, entry.writeToNBT(new NBTTagCompound()));
		}
		return super.writeToNBT(nbt);
	}

	public class MemoryEntry
	{
		final int index;
		ItemStack stack;

		public MemoryEntry(ItemStack stack, int index)
		{
			this.index = index;
			this.stack = stack;
		}

		public MemoryEntry(NBTTagCompound nbt)
		{
			stack = new ItemStack(nbt);
			index = nbt.getInteger("CardIndex");
		}

		public int getIndex()
		{
			return index;
		}

		public ItemStack getStack()
		{
			return stack;
		}

		@Override
		public String toString() {
			return getStack().toString() + " : " + index;
		}

		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			nbt.setInteger("CardIndex", index);
			stack.writeToNBT(nbt);
			return nbt;
		}
	}
}
