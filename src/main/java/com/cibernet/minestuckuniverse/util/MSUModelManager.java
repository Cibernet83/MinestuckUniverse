package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.items.ItemBeamBlade;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.weapon.ItemDualWeapon;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;
import static com.mraof.minestuck.item.MinestuckItems.*;

public class MSUModelManager
{
    public static List<Item> items = new ArrayList<>();
    public static List<Block> blocks = new ArrayList<>();

    public static List<Pair<Item, CustomItemMeshDefinition>> customItemModels = new ArrayList<>();

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
        /*
        for(Pair<Item, CustomItemMeshDefinition> pair : customItemModels)
        {
            ModelLoader.registerItemVariants(pair.getKey(), pair.getValue().getResourceLocations());
            ModelLoader.setCustomMeshDefinition(pair.getKey(), pair.getValue());
        }*/

        //ModelLoader.registerItemVariants(batteryBeamBlade, new ModelResourceLocation[]{new ModelResourceLocation("minestuck:catclaws_sheathed"), new ModelResourceLocation("minestuck:catclaws_drawn")});
        //ModelLoader.setCustomMeshDefinition(batteryBeamBlade, new DualWeaponDefinition(batteryBeamBlade));


        //register(batteryBeamBlade);

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

    private static void register(Item item, ItemMeshDefinition mesh)
    {
        ModelLoader.setCustomMeshDefinition(item, mesh);
    }

    private static void register(Block block)
    {
        register(Item.getItemFromBlock(block));
    }

    private static void register(Block block, int meta, String modelResource)
    {
        register(Item.getItemFromBlock(block), meta, modelResource);
    }

    public static interface CustomItemMeshDefinition extends ItemMeshDefinition {
        public ResourceLocation[] getResourceLocations();
    }

    public static class BeamBladeDefinition implements CustomItemMeshDefinition {
        private Item item;

        public BeamBladeDefinition(Item item /*, String onLocation, String offLocation*/) {

            this.item = item;

            System.out.println(item);
            System.out.println(getResourceLocations());
        }

        public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation(item.getRegistryName().toString() + (((ItemBeamBlade)this.item).isDrawn(stack) ? "_drawn ": ""), "inventory");
        }

        @Override
        public ResourceLocation[] getResourceLocations() {
            return new ResourceLocation[] {item.getRegistryName(), new ResourceLocation(item.getRegistryName().toString() + "_drawn")};
        }
    }

    private static class DualWeaponDefinition implements ItemMeshDefinition {
        private Item item;

        public DualWeaponDefinition(Item item) {
            this.item = item;
        }

        public ModelResourceLocation getModelLocation(ItemStack stack) {
            return ((ItemDualWeapon)this.item).IsDrawn(stack) ? new ModelResourceLocation("minestuck:" + ((ItemDualWeapon)this.item).Prefex + "_drawn", "inventory") : new ModelResourceLocation("minestuck:" + ((ItemDualWeapon)this.item).Prefex + "", "inventory");
        }
    }
}
