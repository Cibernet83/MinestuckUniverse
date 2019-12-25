package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockGrist;
import com.cibernet.minestuckuniverse.blocks.BlockWoolTransportalizer;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.ItemMinestuckCandy;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
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
import javax.swing.text.ComponentView;

import java.util.ArrayList;

import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.*;
import static com.mraof.minestuck.alchemy.GristType.*;
import static com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist.*;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

public class MSUAlchemyRecipes
{
    private static GristType wood = Build;
    private static GristType iron = Rust;
    
    private static ArrayList<Block> gristBlocks = new ArrayList<Block>() {{add(gristBlockAmber); add(gristBlockAmethyst); add(gristBlockArtifact); add(gristBlockCaulk); add(gristBlockChalk); add(gristBlockCobalt); add(gristBlockBuild); add(gristBlockDiamond); add(gristBlockGarnet); add(gristBlockGold); add(gristBlockIodine); add(gristBlockMarble);
        add(gristBlockMercury); add(gristBlockQuartz); add(gristBlockRuby); add(gristBlockRust); add(gristBlockSulfur); add(gristBlockShale); add(gristBlockTar); add(gristBlockUranium); add(gristBlockZillium);}};
    
    private static ArrayList<BlockWoolTransportalizer> sleevedTPs = new ArrayList<BlockWoolTransportalizer>() {{add(cyanWoolTransportalizer); add(whiteWoolTransportalizer); add(orangeWoolTransportalizer); add(magentaWoolTransportalizer); add(lightBlueWoolTransportalizer); add(yellowWoolTransportalizer); add(limeWoolTransportalizer); add(pinkWoolTransportalizer);
    add(grayWoolTransportalizer); add(silverWoolTransportalizer); add(purpleWoolTransportalizer); add(blueWoolTransportalizer); add(brownWoolTransportalizer); add(greenWoolTransportalizer); add(redWoolTransportalizer); add(blackWoolTransportalizer);}};
    
    public static void registerRecipes()
    {
        registerVanilla();
        registerMSU();

        if(MinestuckUniverse.isThaumLoaded) registerThaumcraft();

        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();
        
        registerGristBlockRecipes();
        registerSleevedTransportalizerRecipes();
    }

    public static void registerVanilla()
    {
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.scarletZillyhoo) , new GristSet(new GristType[] {Build, Zillium, Ruby, Quartz, Diamond}, new int[] {1200, 800, 600, 30, 15}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.zillyhooHammer) , new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.zillywairCutlass) , new GristSet(Zillium, 1000));
    
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.clawHammer), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(MinestuckItems.zillyhooHammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.unbreakableKatana), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(MinestuckItems.zillywairCutlass));
    }

    public static void registerMSU()
    {
        GristSet magicBlockCost = new GristSet(Build, 2);
         int magicGristTotal = (
                (MinestuckUniverse.isThaumLoaded ? 1 : 0)
               +(MinestuckUniverse.isBotaniaLoaded ? 1 : 0)

        );
        
        int magicGristSpread = 0;
        if(magicGristTotal > 0)
            magicGristSpread = 16 / magicGristTotal;

        if(MinestuckUniverse.isThaumLoaded) magicBlockCost.addGrist(Vis, magicGristSpread);
        if(MinestuckUniverse.isBotaniaLoaded) magicBlockCost.addGrist(Mana, magicGristSpread);

        //Grist Conversions
        
        GristRegistry.addGristConversion(new ItemStack(diverHelmet), new GristSet(new GristType[] {Rust}, new int[] {64}));
        
        GristRegistry.addGristConversion(new ItemStack(magicBlock), magicBlockCost);
        GristRegistry.addGristConversion(new ItemStack(sbahjBedrock), new GristSet(new GristType[] {Artifact}, new int[] {2000}));
        GristRegistry.addGristConversion(new ItemStack(zillyStone), new GristSet(new GristType[] {Zillium, Build}, new int[] {1, 100}));
        GristRegistry.addGristConversion(new ItemStack(smoothIron), new GristSet(new GristType[] {Build, Rust, Quartz}, new int[] {10, 8, 4}));
        
        GristRegistry.addGristConversion(new ItemStack(machineChasis), new GristSet(new GristType[] {Build, Rust, Uranium, Diamond}, new int[] {750, 98, 55, 4}));
        GristRegistry.addGristConversion(new ItemStack(gristHopper), new GristSet(new GristType[] {Build, Rust, Uranium}, new int[] {250, 55, 10}));
        GristRegistry.addGristConversion(new ItemStack(autoWidget), new GristSet(new GristType[] {Build, Rust, Uranium, Garnet, Zillium}, new int[] {550, 34, 24, 35, 1}));
        GristRegistry.addGristConversion(new ItemStack(autoCaptcha), new GristSet(new GristType[] {Build, Rust, Uranium, Cobalt, Ruby, Quartz}, new int[] {140, 36, 22, 16, 12, 1}));
        GristRegistry.addGristConversion(new ItemStack(porkhollowAtm), new GristSet(new GristType[] {Build, Rust, Uranium, Iodine, Diamond}, new int[] {180, 35, 16, 18, 4}));
        
        GristRegistry.addGristConversion(new ItemStack(rubyRedTransportalizer), new GristSet(new GristType[] {Build, Ruby, Rust, Uranium}, new int[] {450, 100, 36, 24}));
        GristRegistry.addGristConversion(new ItemStack(goldenTransportalizer), new GristSet(new GristType[] {Build, Gold, Shale, Uranium}, new int[] {350, 36, 30, 20}));
        GristRegistry.addGristConversion(new ItemStack(paradoxTransportalizer), new GristSet(new GristType[] {Build, Rust, Uranium, Diamond, Zillium}, new int[] {750, 120, 256, 74, 25}));

        GristRegistry.addGristConversion(new ItemStack(moonstone), new GristSet(new GristType[] {Build, Cobalt, Amethyst, Uranium}, new int[] {5, 4, 3, 2}));
        GristRegistry.addGristConversion(new ItemStack(zillystoneShard), new GristSet(new GristType[] {Zillium}, new int[] {1}));
    
        GristRegistry.addGristConversion(new ItemStack(spaceSalt), new GristSet(new GristType[] {Uranium, Tar, Zillium}, new int[] {10, 32, 1}));
    
    
        GristRegistry.addGristConversion(new ItemStack(trueUnbreakableKatana) , new GristSet(Zillium, 1000));
        
        CombinationRegistry.addCombination(new ItemStack(zillystoneShard), new ItemStack(Items.SUGAR), MODE_OR, Zillium.getCandyItem());
        
        GristRegistry.addGristConversion(new ItemStack(murica), new GristSet(Artifact, -1));
        GristRegistry.addGristConversion(new ItemStack(muricaSouth), new GristSet(new GristType[] {Artifact, Garnet}, new int[] {-10, 1}));
        
        //Alchemy
        
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_HELMET), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, false, false, new ItemStack(diverHelmet));
        
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.rawCruxite), new ItemStack(Blocks.END_STONE), MODE_OR, new ItemStack(moonstone));
        CombinationRegistry.addCombination(new ItemStack(Blocks.BEDROCK), new ItemStack(MinestuckItems.sbahjPoster), MODE_AND, new ItemStack(sbahjBedrock));
        CombinationRegistry.addCombination(new ItemStack(sbahjBedrock), Zillium.getCandyItem(), MODE_OR, false, true, new ItemStack(zillyStone));
        CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.QUARTZ_BLOCK, 1, 0), MODE_OR, false, true, new ItemStack(smoothIron));
        
        CombinationRegistry.addCombination(new ItemStack(moonstone), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, new ItemStack(machineChasis));
        
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 2), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(rubyRedTransportalizer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 5), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(goldenTransportalizer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 6), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(paradoxTransportalizer));

        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 6), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, true, false, new ItemStack(spaceSalt));
        
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.unbreakableKatana), new ItemStack(Blocks.BEDROCK), MODE_AND, false, false, new ItemStack(trueUnbreakableKatana));
        
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.sbahjPoster), new ItemStack(Items.COMPASS), MODE_OR, new ItemStack(murica));
        CombinationRegistry.addCombination(new ItemStack(murica), new ItemStack(Blocks.WOOL, 1, EnumDyeColor.RED.getMetadata()), MODE_AND, false, true, new ItemStack(muricaSouth));
        
        if(MinestuckUniverse.isBotaniaLoaded)
            gristBlocks.add(gristBlockMana);
    }
    
    public static void registerSleevedTransportalizerRecipes()
    {
        for(BlockWoolTransportalizer block : sleevedTPs)
        {
            ItemStack dye = new ItemStack(Items.DYE, 1, block.color.getDyeDamage());
            ItemStack wool = new ItemStack(Blocks.WOOL, 1, block.color.getMetadata());
            ItemStack stack = new ItemStack(block);
            GristRegistry.addGristConversion(stack, GristRegistry.getGristConversion(wool).addGrist(GristRegistry.getGristConversion(new ItemStack(MinestuckBlocks.transportalizer))));
            if(!block.equals(whiteWoolTransportalizer))
                CombinationRegistry.addCombination(new ItemStack(whiteWoolTransportalizer), dye, MODE_OR, stack);
            CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.transportalizer), wool, MODE_AND, stack);
        }
    }
    
    public static void registerGristBlockRecipes()
    {
        for(Block block : gristBlocks)
        {
            GristRegistry.addGristConversion(new ItemStack(block), new GristSet(new GristType[] {((BlockGrist)block).type}, new int[] {((BlockGrist)block).value}));
            CombinationRegistry.addCombination(new ItemStack(block), new ItemStack(Items.SUGAR),MODE_OR, ((BlockGrist) block).type.getCandyItem());
            CombinationRegistry.addCombination(((BlockGrist) block).type.getCandyItem(), new ItemStack(Blocks.STONE), MODE_AND, new ItemStack(block));
        }
    }
    
    public static void registerThaumcraft()
    {
        gristBlocks.add(gristBlockVis);
        
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

        //Alchemy
        CombinationRegistry.addCombination(new ItemStack(ItemsTC.crystalEssence), new ItemStack(Items.SUGAR), MODE_OR, Vis.getCandyItem());
        
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(ItemsTC.quicksilver), MODE_AND, new ItemStack(BlocksTC.shimmerleaf));
        CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.BLAZE_POWDER), MODE_AND, new ItemStack(BlocksTC.cinderpearl));
        CombinationRegistry.addCombination(new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(ItemsTC.crystalEssence), MODE_AND, new ItemStack(BlocksTC.vishroom));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(Blocks.SAPLING, 1, 5), MODE_OR, true, true, new ItemStack(BlocksTC.saplingGreatwood));
        CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(ItemsTC.quicksilver), MODE_OR, true, false,  new ItemStack(BlocksTC.saplingSilverwood));
        CombinationRegistry.addCombination(new ItemStack(BlocksTC.leafGreatwood, 1, 0), new ItemStack(BlocksTC.logGreatwood, 1, 5), MODE_AND, new ItemStack(BlocksTC.saplingGreatwood));
        CombinationRegistry.addCombination(new ItemStack(BlocksTC.leafSilverwood, 1, 0), new ItemStack(BlocksTC.leafSilverwood, 1, 5), MODE_AND, new ItemStack(BlocksTC.saplingSilverwood));

        //Crucible Recipes
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("minestuckuniverse","magicBlock"), new CrucibleRecipe("UNLOCKALCHEMY@1", new ItemStack(magicBlock), new ItemStack(MinestuckBlocks.genericObject), (new AspectList()).merge(Aspect.MAGIC, 10).merge(Aspect.ALCHEMY, 10)));
        //ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("minestuckuniverse", "salisSpatius"), new CrucibleRecipe("SBURBOMANCY@0", new ItemStack(spaceSalt), new ItemStack(ItemsTC.salisMundus), new AspectList().merge(Aspect.EXCHANGE, 50).merge(Aspect.AURA, 20)));

        //Infusion
        //ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("minestuckuniverse","gristDecomposer"), new InfusionRecipe("SBURBOMANCY@1", new ItemStack(gristDecomposer), 1, (new AspectList()).add(Aspect.ALCHEMY, 30).add(Aspect.MECHANISM, 40).add(Aspect.MAGIC, 30), new ItemStack(thaumChasis), new Object[]{new ItemStack(MinestuckBlocks.sburbMachine, 1, 3), new ItemStack(MinestuckItems.energyCore), ConfigItems.ENTROPY_CRYSTAL, ConfigItems.ORDER_CRYSTAL}));

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
