package com.cibernet.minestuckuniverse.strife;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;

public class StrifeSpecibus
{
	protected final LinkedList<PairedStack> items = new LinkedList<>();
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
				NBTTagCompound stackNbt = inv.getCompoundTagAt(i);
				if(stackNbt.hasKey("MainStack") || stackNbt.hasKey("OffStack"))
					items.add(new PairedStack(new ItemStack(stackNbt.getCompoundTag("MainStack")), new ItemStack(stackNbt.getCompoundTag("OffStack"))));
				else
				{
					ItemStack stack = new ItemStack(stackNbt);
					if(!stack.isEmpty())
						items.add(new PairedStack(stack, ItemStack.EMPTY));
				}
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

			for(PairedStack pair : items)
			{
				if(pair.offStack.isEmpty() || pair.mainStack.isEmpty())
					inv.appendTag((pair.mainStack.isEmpty() ? pair.offStack : pair.mainStack).writeToNBT(new NBTTagCompound()));
				else
				{
					NBTTagCompound pairNBT = new NBTTagCompound();
					pairNBT.setTag("MainStack", pair.mainStack.writeToNBT(new NBTTagCompound()));
					pairNBT.setTag("OffStack", pair.offStack.writeToNBT(new NBTTagCompound()));
					inv.appendTag(pairNBT);
				}
			}

			nbt.setTag("Contents", inv);
		}

		if(hasCustomName())
			nbt.setString("CustomName", getCustomName());
		return nbt;
	}

	public boolean putItemStack(ItemStack stack, ItemStack offStack)
	{
		return putItemStack(stack, offStack, -1);
	}

	public boolean putItemStack(ItemStack stack, ItemStack offStack, int slot)
	{
		if (stack.isEmpty() || kindAbstratus == null || !kindAbstratus.isStackCompatible(stack) || (!offStack.isEmpty() && !kindAbstratus.isStackCompatible(offStack)))
			return false;

		boolean prevAssigned = stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned");
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setBoolean("StrifeAssigned", true);
		if(!prevAssigned)
		{
			if(slot == -1)
				items.add(new PairedStack(stack, offStack));
			else items.add(slot, new PairedStack(stack, offStack));
		}
		return true;
	}

	public boolean unassign(int index)
	{
		if(index < 0 || index >= items.size())
			return false;
		items.remove(index);
		return true;
	}

	public PairedStack retrieveStack(int index)
	{
		if(index < 0 || index >= items.size())
			return PairedStack.EMPTY;
		return items.get(index).copy();
	}

	public void switchKindAbstratus(KindAbstratus abstratus, EntityPlayer player)
	{
		if(abstratus == kindAbstratus)
			return;
		kindAbstratus = abstratus;

		for(PairedStack stack : new ArrayList<>(getContents()))
			if(!abstratus.isStackCompatible(stack.mainStack))
			{
				getContents().remove(getContents().indexOf(stack));
				if(player != null)
				{
					CaptchaDeckHandler.launchAnyItem(player, stack.mainStack);
					CaptchaDeckHandler.launchAnyItem(player, stack.offStack);
				}
			}
	}

	public LinkedList<PairedStack> getContents() {
		return items;
	}

	public void updatePairEntry(int index, ItemStack stack, EnumHand side)
	{
		PairedStack pair = getContents().get(index);
		if(side == EnumHand.MAIN_HAND)
			pair.mainStack = stack;
		else pair.offStack = stack;
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

	public static class PairedStack
	{
		public ItemStack mainStack;
		public ItemStack offStack;

		public static final PairedStack EMPTY = new PairedStack(ItemStack.EMPTY, ItemStack.EMPTY);

		public PairedStack(ItemStack mainStack, ItemStack offStack)
		{
			this.mainStack = mainStack;
			this.offStack = offStack;
		}

		public ItemStack getSingleStack()
		{
			return mainStack.isEmpty() ? offStack : mainStack;
		}

		public boolean hasPair()
		{
			return !mainStack.isEmpty() && !offStack.isEmpty();
		}

		public ItemStack getSingleOppositeStack()
		{
			return !mainStack.isEmpty() ? offStack : mainStack;
		}

		public PairedStack copy()
		{
			return new PairedStack(mainStack.copy(), offStack.copy());
		}

		public String getDisplayName() {
			return !hasPair() ? getSingleStack().getDisplayName() : (mainStack.getDisplayName() + " | " + offStack.getDisplayName());
		}
	}
}
