package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MSUModelManager;
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

    public static Block sbahjBedrock = new MSUBlockBase(Material.CAKE, "sbahj_bedrock", "sbahjBedrock").setResistance(0F).setHardness(-1F);
    public static Block zillyStone = new MSUBlockBase(Material.ROCK, "zillystone", "zillystone").setResistance(999F).setHardness(5.5F);

    //Thaumcraft
    public static Block thaumChasis = new MSUBlockBase(Material.IRON, "thaumic_machine_frame", "thaumChasis");
    public static Block gristDecomposer = new BlockGristDecomposer();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registerBlock(registry, magicBlock);
        registerBlock(registry, sbahjBedrock);
        registerBlock(registry, zillyStone);

        if(MinestuckUniverse.isThaumLoaded)
        {
            registerBlock(registry, thaumChasis);
            registerBlock(registry, gristDecomposer);
        }
    }
    
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block)
    {
        registry.register(block);
        MSUModelManager.blocks.add(block);
        return block;
    }

}
