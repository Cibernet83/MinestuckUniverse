package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusMod;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;

import javax.swing.*;

import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.*;
import static com.mraof.minestuck.alchemy.GristType.*;
import static com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist.*;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

public class MSUAlchemyRecipes
{
    private static GristType wood = Build;
    private static GristType iron = Rust;

    public static void registerRecipes()
    {
        registerVanilla();
        registerMSU();

        if(MinestuckUniverse.isThaumLoaded) registerThaumcraft();

        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();
        else
        {

        }
    }

    public static void registerVanilla()
    {

    }

    public static void registerMSU()
    {
        GristSet magicBlockCost = new GristSet(Build, 2);
        int magicGristSpread = 16 / (
                (MinestuckUniverse.isThaumLoaded ? 1 : 0)
               +(MinestuckUniverse.isBotaniaLoaded ? 1 : 0)

        );

        if(MinestuckUniverse.isThaumLoaded) magicBlockCost.addGrist(Vis, magicGristSpread);
        if(MinestuckUniverse.isBotaniaLoaded) magicBlockCost.addGrist(Mana, magicGristSpread);

        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(magicBlock), new GristSet(new GristType[] {Vis}, new int[] {1}));

        //Alchemy
    }
    public static void registerThaumcraft()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.amber), new GristSet(Amber, 8));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.quicksilver), new GristSet(Mercury, 9));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.thaumonomicon), new GristSet(new GristType[] {Vis, Iodine, Chalk}, new int[] {24, 5, 2}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.salisMundus), new GristSet(Vis, 12));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.crystalEssence), new GristSet(new GristType[] {Vis, Quartz}, new int[] {8, 1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.phial, 1, 1), new GristSet(new GristType[] {Build, Shale, Vis}, new int[] {1, 1, 8}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.nuggets, 1, 0), new GristSet(new GristType[] {iron}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.nuggets, 1, 10), new GristSet(new GristType[] {Vis, Rust, Gold}, new int[] {2, 2, 1}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.ingots, 1, 0), new GristSet(new GristType[] {Vis, iron}, new int[] {18, 9}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.ingots, 1, 2), new GristSet(new GristType[] {Vis, Gold, iron}, new int[] {5, 4, 9}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.clusters, 1, 0), new GristSet(new GristType[] {Build, iron}, new int[] {4, 16*MinestuckConfig.oreMultiplier}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.clusters, 1, 1), new GristSet(new GristType[] {Build, Gold}, new int[] {4, 16*MinestuckConfig.oreMultiplier}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.clusters, 1, 6), new GristSet(new GristType[] {Build, Mercury}, new int[] {4, 16*MinestuckConfig.oreMultiplier}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.clusters, 1, 7), new GristSet(new GristType[] {Build, Quartz, Marble}, new int[] {2, 16*MinestuckConfig.oreMultiplier, 2*MinestuckConfig.oreMultiplier}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.voidSeed), new GristSet(new GristType[] {Vis, Shale, Tar, Uranium}, new int[] {45, 21, 32, 4}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.brain), new GristSet(new GristType[] {Iodine, Vis, Shale}, new int[] {1, 5, 8}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.tallow), new GristSet(new GristType[] {Iodine, Vis, Rust}, new int[] {1, 2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.fabric), new GristSet(new GristType[] {Chalk, Vis}, new int[] {14, 1}));

        GristRegistry.addGristConversion(new ItemStack(ItemsTC.chunks), new GristSet(new GristType[] {Iodine, Tar}, new int[] {2, 1}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.oreAmber), new GristSet(new GristType[] {Build, Amber}, new int[] {4, 8 * MinestuckConfig.oreMultiplier}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.oreCinnabar), new GristSet(new GristType[] {Build, Mercury}, new int[] {4, 9 * MinestuckConfig.oreMultiplier}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.vishroom), new GristSet(new GristType[] {Vis, Iodine}, new int[] {5, 4}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.cinderpearl), new GristSet(new GristType[] {Tar, Uranium, Iodine}, new int[] {8, 2, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.shimmerleaf), new GristSet(new GristType[] {Mercury, Iodine}, new int[] {8, 2}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalAir), new GristSet(new GristType[] {Vis, Build, Marble}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalFire), new GristSet(new GristType[] {Vis, Build, Amber}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalWater), new GristSet(new GristType[] {Vis, Build, Tar}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalEarth), new GristSet(new GristType[] {Vis, Build, Uranium}, new int[] {20, 8, 2}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalOrder), new GristSet(new GristType[] {Vis, Build, Quartz}, new int[] {8, 8, 14}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalEntropy), new GristSet(new GristType[] {Vis, Build, Quartz}, new int[] {13, 8, 9}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.crystalTaint), new GristSet(new GristType[] {Vis, Build}, new int[] {28, 2}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.saplingSilverwood), new GristSet(new GristType[] {Vis, Build, Mercury}, new int[] {16, 16, 8}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.saplingGreatwood), new GristSet(new GristType[] {Vis, Build, Iodine}, new int[] {8, 16, 16}));

        GristRegistry.addGristConversion(new ItemStack(BlocksTC.logSilverwood), new GristSet(new GristType[] {wood, Vis, Mercury}, new int[] {8, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.logGreatwood), new GristSet(new GristType[] {wood, Vis}, new int[] {12, 4}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.leafSilverwood), new GristSet(new GristType[] {Build, Mercury}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(BlocksTC.leafGreatwood), new GristSet(new GristType[] {Build, Vis}, new int[] {1, 1}));

        //Aspect Conversion
        ThaumcraftApi.registerObjectTag(new ItemStack(MinestuckItems.rawUranium), (new AspectList()).add(Aspect.METAL, 10).add(Aspect.DEATH, 5).add(Aspect.ENERGY, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(MinestuckBlocks.oreUranium), (new AspectList()).add(Aspect.METAL, 10).add(Aspect.DEATH, 5).add(Aspect.ENERGY, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(MinestuckItems.rawCruxite), (new AspectList()).add(Aspect.METAL, 5).add(Aspect.ALCHEMY, 15).add(Aspect.MECHANISM, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(MinestuckBlocks.oreCruxite), (new AspectList()).add(Aspect.METAL, 5).add(Aspect.ALCHEMY, 15).add(Aspect.MECHANISM, 4));


        //Alchemy
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(ItemsTC.quicksilver), MODE_AND, new ItemStack(BlocksTC.shimmerleaf));
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.BLAZE_POWDER), MODE_AND, new ItemStack(BlocksTC.cinderpearl));
        CombinationRegistry.addCombination(new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(ItemsTC.crystalEssence), MODE_AND, new ItemStack(BlocksTC.vishroom));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(Blocks.SAPLING, 1, 5), MODE_OR, true, true, new ItemStack(BlocksTC.saplingGreatwood));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(ItemsTC.quicksilver), MODE_OR, true, false,  new ItemStack(BlocksTC.saplingSilverwood));
        CombinationRegistry.addCombination(new ItemStack(BlocksTC.leafGreatwood, 1, 0), new ItemStack(BlocksTC.logGreatwood, 1, 5), MODE_AND, new ItemStack(BlocksTC.saplingGreatwood));
        CombinationRegistry.addCombination(new ItemStack(BlocksTC.leafSilverwood, 1, 0), new ItemStack(BlocksTC.leafSilverwood, 1, 5), MODE_AND, new ItemStack(BlocksTC.saplingSilverwood));

        CombinationRegistry.addCombination(new ItemStack(magicBlock), new ItemStack(BlocksTC.metalAlchemical), MODE_AND, new ItemStack(thaumChasis));

        //Crucible Recipes
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("minestuckuniverse","magicBlock"), new CrucibleRecipe("SBURBOMANCY@0", new ItemStack(magicBlock), new ItemStack(MinestuckBlocks.genericObject), (new AspectList()).merge(Aspect.MAGIC, 10).merge(Aspect.ALCHEMY, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("minestuckuniverse", "salisSpatius"), new CrucibleRecipe("SBURBOMANCY@0", new ItemStack(spaceSalt), new ItemStack(ItemsTC.salisMundus), new AspectList().merge(Aspect.EXCHANGE, 50).merge(Aspect.AURA, 20)));

        //Infusion
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("minestuckuniverse","gristDecomposer"), new InfusionRecipe("SBURBOMANCY@1", new ItemStack(gristDecomposer), 1, (new AspectList()).add(Aspect.ALCHEMY, 30).add(Aspect.MECHANISM, 40).add(Aspect.MAGIC, 30), new ItemStack(thaumChasis), new Object[]{new ItemStack(MinestuckBlocks.sburbMachine, 1, 3), new ItemStack(MinestuckItems.energyCore), ConfigItems.ENTROPY_CRYSTAL, ConfigItems.ORDER_CRYSTAL}));



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
