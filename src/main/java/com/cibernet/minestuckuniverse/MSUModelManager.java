package com.cibernet.minestuckuniverse;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

public class MSUModelManager
{
    @SubscribeEvent
    public static void handleModelRegistry(ModelRegistryEvent event)
    {
        ItemModels();
        ItemBlockModels();
    }

    private static void ItemModels()
    {
        register(spaceSalt);
        register(moonstone);
        register(moonstoneChisel);
        register(zillystoneShard);
    }

    private static void ItemBlockModels()
    {
        register(magicBlock);
        register(sbahjBedrock);
        register(zillyStone);

        if(MinestuckUniverse.isThaumLoaded)
        {
            register(thaumChasis);
            register(gristDecomposer);
        }
    }

    private static void register(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Item.REGISTRY.getNameForObject(item), "inventory"));
    }

    private static void register(Item item, int meta, String modelResource)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("minestuckuniverse:"+modelResource, "inventory"));
    }

    private static void register(Block block)
    {
        register(Item.getItemFromBlock(block));
    }

    private static void register(Block block, int meta, String modelResource)
    {
        register(Item.getItemFromBlock(block), meta, modelResource);
    }
}
