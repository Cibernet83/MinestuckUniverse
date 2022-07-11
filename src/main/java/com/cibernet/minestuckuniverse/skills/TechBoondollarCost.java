package com.cibernet.minestuckuniverse.skills;

import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TechBoondollarCost extends Abilitech
{
	public long cost;
	public final ArrayList<ItemStack> requiredStacks = new ArrayList<>();
	public boolean needsReqStacks = true;

	public TechBoondollarCost(String name, long cost)
	{
		super(name);
		//TODO: set this back to cost

		this.cost = 1;
	}
	
	public TechBoondollarCost(String name, long cost, EnumTechType... techTypes)
	{
		super(name, techTypes);
		
		this.cost = 1;
	}

	@Override
	public String getUnlockRequirements()
	{
		if(!requiredStacks.isEmpty())
		{
			String entries = "";
			for(int i = 1; i < requiredStacks.size()- (cost == 0 ? 1 : 0); i++)
				entries += I18n.format("tech.unlock.list.entry", requiredStacks.get(i).getCount(), requiredStacks.get(i).getDisplayName());
			String last =  cost == 0 ? I18n.format("tech.unlock.list.solo", requiredStacks.get(requiredStacks.size()-1).getCount(), requiredStacks.get(requiredStacks.size()-1).getDisplayName()) :
					I18n.format("tech.unlock.boondollar", cost);

			return I18n.format("tech.unlock.list", requiredStacks.get(0).getCount(), requiredStacks.get(0).getDisplayName(), entries, last);
		}
		return new TextComponentTranslation(cost == 0 ? "tech.unlock.free" : "tech.unlock.boondollar", cost).getFormattedText();
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		boolean hasItems = true;
		for(ItemStack stack : requiredStacks)
			hasItems = hasItems && Badge.findItem(player, stack, false, !stack.hasTagCompound());

		return hasItems && ((world.isRemote ? MinestuckPlayerData.boondollars : MinestuckPlayerData.getData(player).boondollars) >= cost);
	}

	@Override
	public void onUnlock(World world, EntityPlayer player)
	{
		if(player.world.isRemote)
			MinestuckPlayerData.boondollars -= cost;
		else MinestuckPlayerData.addBoondollars(player, -cost);

		for(ItemStack stack : requiredStacks)
			Badge.findItem(player, stack, true, !stack.hasTagCompound());
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		if(super.canAppearOnList(world, player) && (!needsReqStacks || requiredStacks.isEmpty()))
			return true;

		boolean hasItems = true;
		for(ItemStack stack : requiredStacks)
		{
			ItemStack s = stack.copy();
			s.setCount(1);
			hasItems = hasItems && Badge.findItem(player, s, false, !stack.hasTagCompound());
		}

		return hasItems;
	}
}
