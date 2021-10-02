package com.cibernet.minestuckuniverse.strife;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;

public class StrifeSpecibus
{
	protected final LinkedList<ItemStack> items = new LinkedList<>();
	protected KindAbstratus kindAbstratus;
	protected String customName = "";

	public StrifeSpecibus(KindAbstratus abstratus)
	{
		this.kindAbstratus = abstratus;
	}

	public static StrifeSpecibus empty()
	{
		return new StrifeSpecibus((KindAbstratus) null);
	}

	public StrifeSpecibus(NBTTagCompound nbt)
	{
		readFromNBT(nbt);
	}

	public StrifeSpecibus readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("KindAbstratus"))
			kindAbstratus = KindAbstratus.REGISTRY.getValue(new ResourceLocation(nbt.getString("KindAbstratus")));

		if(isAssigned())
		{
			NBTTagList inv = nbt.getTagList("Contents", 10);

			for(int i = 0; i < inv.tagCount(); i++)
			{
				ItemStack stack = new ItemStack(inv.getCompoundTagAt(i));
				if(!stack.isEmpty())
					items.add(stack);
			}
		}

		if(nbt.hasKey("CustomName"))
			setCustomName(nbt.getString("CustomName"));

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(isAssigned())
		{
			nbt.setString("KindAbstratus", kindAbstratus.getRegistryName().toString());

			NBTTagList inv = new NBTTagList();

			for(ItemStack stack : items)
				inv.appendTag(stack.writeToNBT(new NBTTagCompound()));

			nbt.setTag("Contents", inv);
		}

		if(hasCustomName())
			nbt.setString("CustomName", getCustomName());
		return nbt;
	}

	public boolean putItemStack(ItemStack stack)
	{
		return putItemStack(stack, -1);
	}

	public boolean putItemStack(ItemStack stack, int slot)
	{
		if (stack.isEmpty() || kindAbstratus == null || !kindAbstratus.isStackCompatible(stack))
			return false;

		boolean prevAssigned = stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned");
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setBoolean("StrifeAssigned", true);
		if(!prevAssigned)
		{
			if(slot == -1)
				items.add(stack);
			else items.add(slot, stack);
		}
		return true;
	}

	public boolean unassign(ItemStack stack)
	{
		if(!items.contains(stack))
			return false;
		return unassign(items.indexOf(stack));
	}

	public boolean unassign(int index)
	{
		if(index < 0 || index >= items.size())
			return false;
		items.remove(index);
		return true;
	}

	public ItemStack retrieveStack(ItemStack stack)
	{
		if(!items.contains(stack))
			return ItemStack.EMPTY;
		return retrieveStack(items.indexOf(stack));
	}

	public ItemStack retrieveStack(int index)
	{
		if(index < 0 || index >= items.size())
			return ItemStack.EMPTY;
		return items.get(index).copy();
	}

	public void switchKindAbstratus(KindAbstratus abstratus, EntityPlayer player)
	{
		if(abstratus == kindAbstratus)
			return;
		kindAbstratus = abstratus;

		for(ItemStack stack : new ArrayList<>(getContents()))
			if(!abstratus.isStackCompatible(stack))
			{
				getContents().remove(getContents().indexOf(stack));
				if(player != null)
					CaptchaDeckHandler.launchAnyItem(player, stack);
			}
	}

	public LinkedList<ItemStack> getContents() {
		return items;
	}

	public KindAbstratus getKindAbstratus() {
		return kindAbstratus;
	}

	public boolean isAssigned()
	{
		return kindAbstratus != null;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName.trim();
	}

	public boolean hasCustomName()
	{
		return customName != null && !customName.isEmpty();
	}

	@Override
	public String toString() {
		return kindAbstratus + " " + items;
	}

	public String getDisplayName()
	{
		if(hasCustomName())
			return customName;
		return kindAbstratus == null ? "" : kindAbstratus.getLocalizedName();
	}
	public String getDisplayNameForCard()
	{
		String name = (hasCustomName() ? customName : kindAbstratus == null ? "" : kindAbstratus.getDisplayName()).toLowerCase();

		if(name.length() > 12)
			name = name.substring(0, 9) + "...";

		return name;
	}
}
