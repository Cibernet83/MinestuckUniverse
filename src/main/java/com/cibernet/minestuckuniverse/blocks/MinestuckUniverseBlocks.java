package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckUniverseBlocks
{

    //Base
    public static Block magicBlock = new MSUBlockBase(Material.CLAY, "magic_block", "magicBlock");

    //Thaumcraft
    public static Block thaumChasis = new MSUBlockBase(Material.IRON, "thaumic_machine_frame", "thaumChasis");
    public static Block gristDecomposer = new BlockGristDecomposer();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(magicBlock);

        if(MinestuckUniverse.isThaumLoaded)
        {
            registry.register(thaumChasis);
            registry.register(gristDecomposer);
        }
    }


}
