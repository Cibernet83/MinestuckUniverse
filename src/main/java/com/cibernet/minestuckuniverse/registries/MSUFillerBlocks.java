package com.cibernet.minestuckuniverse.registries;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUFillerBlocks
{
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		
	}
	
	private static Block registerBlock(IForgeRegistry<Block> registry, Block block, boolean... hasItem)
	{
		block.setCreativeTab(TabMinestuckUniverse.fillerItems);
		registry.register(block);
		MSUModelManager.blocks.add(block);
		
		if(hasItem.length > 0)
			MinestuckUniverseItems.itemBlocks.add(block);
		
		return block;
	}
}
