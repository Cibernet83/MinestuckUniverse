package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.event.AlchemyCombinationEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class IDBasedAlchemyHandler
{
	private static int MAX_ID;

	@SubscribeEvent
	public static void onAlchemyCombo(AlchemyCombinationEvent event)
	{
		if(!MSUConfig.IDAlchemy)
			return;

		if(event.getResultItem().isEmpty() && !event.getItemB().isEmpty() && !event.getItemB().isEmpty())
		{
			int idA = getItemAlchemyID(event.getItemA().getItem());
			int idB = getItemAlchemyID(event.getItemB().getItem());

			if(idA < 0 || idB < 0)
				return;

			Item result = null;

			if(event.getMode() == CombinationRegistry.Mode.MODE_AND)
			{
				result = getItemFromAlchemyID(idA & idB);
			}
			else if(event.getMode() == CombinationRegistry.Mode.MODE_OR)
			{
				result = getItemFromAlchemyID(idA | idB);
			}

			if(result != null)
				event.setResultItem(new ItemStack(result));
		}
	}

	public static int getItemAlchemyID(Item item)
	{
		if(item == Item.getItemFromBlock(MinestuckBlocks.genericObject))
			return 0;
		if(item == Item.getItemFromBlock(MinestuckUniverseBlocks.uniqueObject))
			return MAX_ID;
		if(item == Item.getItemFromBlock(MinestuckUniverseBlocks.artifact))
			return -1;
		return Item.REGISTRY.getIDForObject(item);
	}

	public static Item getItemFromAlchemyID(int id)
	{
		if(id == 0)
			return Item.getItemFromBlock(MinestuckBlocks.genericObject);
		if(id > MAX_ID)
			return Item.getItemFromBlock(MinestuckUniverseBlocks.uniqueObject);

		Item result = Item.REGISTRY.getObjectById(id);

		if(result == Item.getItemFromBlock(MinestuckUniverseBlocks.uniqueObject))
			result = null;
		if(result == Item.getItemFromBlock(MinestuckUniverseBlocks.artifact))
			result = null;

		return result == null ? Item.getItemFromBlock(MinestuckUniverseBlocks.artifact) : result;
	}

	@Deprecated //DO NOT USE, only for CommonProxy.postInit
	public static void calculateMaxID()
	{
		Iterator<Item> iter = Item.REGISTRY.iterator();
		while(iter.hasNext())
		{
			int itemID = Item.REGISTRY.getIDForObject(iter.next());
			if(itemID > MAX_ID)
				MAX_ID = itemID;
		}
	}

	public static int getMaxId()
	{
		return MAX_ID;
	}
}
