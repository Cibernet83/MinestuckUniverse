package com.cibernet.minestuckuniverse.skills;

import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechBoondollarCost extends Abilitech
{
	public long cost;

	public TechBoondollarCost(String name, long cost)
	{
		super(name);
		//TODO: set this back to cost
		this.cost = 1;
	}

	@Override
	public String getUnlockRequirements()
	{
		return new TextComponentTranslation("tech.unlock.boondollar", cost).getFormattedText();
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		return (MinestuckPlayerData.getData(player).boondollars >= cost);
	}

	@Override
	public void onUnlock(World world, EntityPlayer player)
	{
		MinestuckPlayerData.addBoondollars(player, -cost);
	}
}
