package com.cibernet.minestuckuniverse.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckUniverseBlocks
{

    public static Block magicBlock = new MSUBlockBase(Material.CLAY, "magic_block", "magicBlock");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(magicBlock);
    }


}
