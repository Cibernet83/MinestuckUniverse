package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;
import static com.mraof.minestuck.item.MinestuckItems.candy;

public class MSUModelManager
{
    public static List<Item> items = new ArrayList<>();
    public static List<Block> blocks = new ArrayList<>();

    @SubscribeEvent
    public static void handleModelRegistry(ModelRegistryEvent event)
    {
        ItemModels();
        ItemBlockModels();
    }

    private static void ItemModels()
    {
        
        register(unbreakableKatana, 0, "unbreakable_katana");
        
        for(Item item : items)
            register(item);
        
        //Grist Candy
        if(MinestuckUniverse.isThaumLoaded)
            register(candy, GristType.REGISTRY.getID(MinestuckUniverseGrist.Vis) + 1, "vis_nerds");
        if(MinestuckUniverse.isBotaniaLoaded)
            register(candy, GristType.REGISTRY.getID(MinestuckUniverseGrist.Mana) + 1, "mana_gummy_drop");
    }

    private static void ItemBlockModels()
    {
        for(Block block : blocks)
            register(block);
    }

    private static void register(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Item.REGISTRY.getNameForObject(item), "inventory"));
    }

    private static void register(Item item, int meta, String modelResource)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(MinestuckUniverse.MODID+":"+modelResource, "inventory"));
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
