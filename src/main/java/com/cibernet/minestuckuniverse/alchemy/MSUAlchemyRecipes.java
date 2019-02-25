package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.alchemy.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;

import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.*;
import static com.mraof.minestuck.alchemy.GristType.*;
import static com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist.*;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;

public class MSUAlchemyRecipes
{
    public static void registerRecipes()
    {
        registerVanilla();
        registerMSU();

        if(MinestuckUniverse.isThaumLoaded) registerThaumcraft();

        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();
    }

    public static void registerVanilla()
    {

    }

    public static void registerMSU()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(magicBlock), new GristSet(new GristType[] {Vis}, new int[] {1}));

        //Alchemy
    }
    public static void registerThaumcraft()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.amber), new GristSet(Amber, 8));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.thaumonomicon), new GristSet(new GristType[] {Vis, Iodine, Chalk}, new int[] {24, 5, 2}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.salisMundus), new GristSet(Vis, 12));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.crystalEssence), new GristSet(new GristType[] {Vis, Quartz}, new int[] {8, 1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.phial, 1, 1), new GristSet(new GristType[] {Build, Shale, Vis}, new int[] {1, 1, 8}));


        GristRegistry.addGristConversion(new ItemStack(ItemsTC.nuggets, 1, 0), new GristSet(new GristType[] {Rust}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.nuggets, 1, 5), new GristSet(new GristType[] {Mercury}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.nuggets, 1, 10), new GristSet(new GristType[] {Vis, Rust}, new int[] {2, 1}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.ingots, 1, 0), new GristSet(new GristType[] {Vis, Rust}, new int[] {18, 9}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.ingots, 1, 2), new GristSet(new GristType[] {Vis, Gold, Rust}, new int[] {5, 4, 9}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.voidSeed), new GristSet(new GristType[] {Vis, Shale, Tar, Uranium}, new int[] {45, 21, 32, 4}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.vishroom), new GristSet(new GristType[] {Vis, Iodine}, new int[] {5, 4}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.cinderpearl), new GristSet(new GristType[] {Tar, Uranium, Iodine}, new int[] {8, 2, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.shimmerleaf), new GristSet(new GristType[] {Mercury, Iodine}, new int[] {8, 2}));

        GristRegistry.addGristConversion("crystal_aer", new GristSet(new GristType[] {Vis, Build, Marble}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion("crystal_ignis", new GristSet(new GristType[] {Vis, Build, Amber}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion("crystal_aqua", new GristSet(new GristType[] {Vis, Build, Tar}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion("crystal_terra", new GristSet(new GristType[] {Vis, Build, Uranium}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion("crystal_ordo", new GristSet(new GristType[] {Vis, Build, Quartz}, new int[] {9, 8, 13}));
        GristRegistry.addGristConversion("crystal_perditio", new GristSet(new GristType[] {Vis, Build, Quartz}, new int[] {13, 8, 9}));
        GristRegistry.addGristConversion("crystal_vitium", new GristSet(new GristType[] {Vis, Build}, new int[] {28, 2}));


        //Alchemy
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(ItemsTC.quicksilver), MODE_AND, new ItemStack(BlocksTC.shimmerleaf));
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.BLAZE_POWDER), MODE_AND, new ItemStack(BlocksTC.cinderpearl));
        CombinationRegistry.addCombination(new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(ItemsTC.crystalEssence), MODE_AND, new ItemStack(BlocksTC.vishroom));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(Blocks.SAPLING, 1, 5), MODE_OR, true, true, new ItemStack(BlocksTC.saplingGreatwood));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING), new ItemStack(ItemsTC.quicksilver), MODE_OR, new ItemStack(BlocksTC.saplingSilverwood));


    }

    public static void registerArsenalFallback()
    {
        if(MinestuckUniverse.isBotaniaLoaded)
        {

        }
    }

    public static void addGristConversion(String modid, String itemid, GristSet grist)
    {
        Item i = Item.REGISTRY.getObject(new ResourceLocation(modid, itemid));
        GristRegistry.addGristConversion(new ItemStack(i), grist);
    }

}
