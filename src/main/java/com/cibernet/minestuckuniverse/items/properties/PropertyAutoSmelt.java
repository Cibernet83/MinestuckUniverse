package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ListIterator;

public class PropertyAutoSmelt extends WeaponProperty
{
	@SubscribeEvent
	public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
	{
		if(event.getHarvester() == null)
			return;

		ItemStack tool = event.getHarvester().getHeldItemMainhand();
		if(tool.getItem() instanceof IPropertyWeapon && tool.canHarvestBlock(event.getState()) && ((IPropertyWeapon) tool.getItem()).hasProperty(PropertyAutoSmelt.class, tool))
		{
			ListIterator<ItemStack> iter = event.getDrops().listIterator();
			while (iter.hasNext())
			{
				ItemStack drop = iter.next();
				ItemStack smeltStack = FurnaceRecipes.instance().getSmeltingResult(drop).copy();
				if(!smeltStack.isEmpty())
				{
					smeltStack.setCount(drop.getCount());
					iter.set(smeltStack);

					float xp = FurnaceRecipes.instance().getSmeltingExperience(smeltStack);
					if(xp < 1 && Math.random() < xp)
						xp += 1;
					if(xp >= 1)
						event.getState().getBlock().dropXpOnBlockBreak(event.getWorld(), event.getPos(), (int) xp);
				}
			}
		}
	}
}
