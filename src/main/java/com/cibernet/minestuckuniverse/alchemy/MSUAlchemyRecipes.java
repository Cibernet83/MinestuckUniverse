package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockGrist;
import com.cibernet.minestuckuniverse.blocks.BlockWoolTransportalizer;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.modSupport.BotaniaSupport;
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
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.swing.*;
import javax.swing.text.ComponentView;

import java.util.ArrayList;
import java.util.TreeMap;

import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.*;
import static com.mraof.minestuck.alchemy.GristType.*;
import static com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist.*;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

public class MSUAlchemyRecipes
{
    private static GristType wood = MinestuckUniverse.isArsenalLoaded ? GristType.getTypeFromString("minestuckarsenal:wood")  : Build;
    private static GristType iron = MinestuckUniverse.isArsenalLoaded ? GristType.getTypeFromString("minestuckarsenal:iron")  : Rust;
    
    private static final ArrayList<Block> gristBlocks = new ArrayList<Block>() {{add(gristBlockAmber); add(gristBlockAmethyst); add(gristBlockArtifact); add(gristBlockCaulk); add(gristBlockChalk); add(gristBlockCobalt); add(gristBlockBuild); add(gristBlockDiamond); add(gristBlockGarnet); add(gristBlockGold); add(gristBlockIodine); add(gristBlockMarble);
        add(gristBlockMercury); add(gristBlockQuartz); add(gristBlockRuby); add(gristBlockRust); add(gristBlockSulfur); add(gristBlockShale); add(gristBlockTar); add(gristBlockUranium); add(gristBlockZillium);}};
    
    private static final ArrayList<BlockWoolTransportalizer> sleevedTPs = new ArrayList<BlockWoolTransportalizer>() {{add(cyanWoolTransportalizer); add(whiteWoolTransportalizer); add(orangeWoolTransportalizer); add(magentaWoolTransportalizer); add(lightBlueWoolTransportalizer); add(yellowWoolTransportalizer); add(limeWoolTransportalizer); add(pinkWoolTransportalizer);
    add(grayWoolTransportalizer); add(silverWoolTransportalizer); add(purpleWoolTransportalizer); add(blueWoolTransportalizer); add(brownWoolTransportalizer); add(greenWoolTransportalizer); add(redWoolTransportalizer); add(blackWoolTransportalizer);}};

    private static final TreeMap<EnumDyeColor, GristSet> dyeGrist = new TreeMap<>();

    public static void registerRecipes()
    {
        for(EnumDyeColor color : EnumDyeColor.values())
            dyeGrist.put(color, GristRegistry.getGristConversion(new ItemStack(Items.DYE, 1, color.getDyeDamage())));

        registerVanilla();
        registerMSU();
        registerGristBlockRecipes();
        registerSleevedTransportalizerRecipes();

        if(MinestuckUniverse.isThaumLoaded) registerThaumcraft();
        if(MinestuckUniverse.isBotaniaLoaded) registerBotania();
        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();

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

        
        if(MinestuckUniverse.isArsenalLoaded)
            GristRegistry.addGristConversion(new ItemStack(moonstone), new GristSet(new GristType[] {Build, getTypeFromString("minestuckarsenal:moonstone"), Uranium}, new int[] {5, 5, 2}));
        else
            GristRegistry.addGristConversion(new ItemStack(moonstone), new GristSet(new GristType[] {Build, Cobalt, Amethyst, Uranium}, new int[] {5, 4, 3, 2}));
        
        GristRegistry.addGristConversion(new ItemStack(zillystoneShard), new GristSet(new GristType[] {Zillium}, new int[] {1}));
    
        GristRegistry.addGristConversion(new ItemStack(spaceSalt), new GristSet(new GristType[] {Uranium, Tar, Zillium}, new int[] {10, 32, 1}));
    
    
        GristRegistry.addGristConversion(new ItemStack(trueUnbreakableKatana) , new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(cybersword), new GristSet(new GristType[] {Build, Cobalt, Mercury, Diamond}, new int[] {4000, 800, 250, 8}));
        
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

        CombinationRegistry.addCombination(new ItemStack(zillystoneShard), new ItemStack(Items.SUGAR), MODE_OR, Zillium.getCandyItem());

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
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.crystalEssence), new GristSet(new GristType[] {Vis, Quartz}, new int[] {2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.phial, 1, 1), new GristSet(new GristType[] {Build, Shale, Vis}, new int[] {1, 1, 20}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.primordialPearl), new GristSet(new GristType[] {Build, Vis, Zillium}, new int[] {1600, 240, 5}));

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

    public static void registerBotania()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(ModBlocks.livingwood, new GristSet(new GristType[] {Build, Mana}, new int[] {2,2}));
        GristRegistry.addGristConversion(ModBlocks.livingrock, new GristSet(new GristType[] {Build, Mana}, new int[] {2,2}));

        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 0), true, GristRegistry.getGristConversion(new ItemStack(Items.IRON_INGOT)).addGrist(Mana, 3));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 1), true, GristRegistry.getGristConversion(new ItemStack(Items.ENDER_PEARL)).addGrist(Mana, 6));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 2), true, GristRegistry.getGristConversion(new ItemStack(Items.DIAMOND)).addGrist(Mana, 10));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 16), true, GristRegistry.getGristConversion(new ItemStack(Items.STRING)).addGrist(Mana, 5));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaBottle, 1, 16), true, new GristSet(new GristType[] {Build, Mana}, new int[] {1, 5}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaCookie), false, new GristSet(new GristType[] {Amber, Iodine, Mana}, new int[] {1, 1, 20}));
        GristRegistry.addGristConversion(new ItemStack(ModBlocks.pistonRelay), false, new GristSet(new GristType[] {Build, Garnet, Rust, Mana}, new int[] {14, 4, 9, 15}));
        GristRegistry.addGristConversion(new ItemStack(ModBlocks.tinyPotato), false, GristRegistry.getGristConversion(new ItemStack(Items.POTATO)).addGrist(Mana, 1));
        GristRegistry.addGristConversion(new ItemStack(ModBlocks.manaGlass), false, GristRegistry.getGristConversion(new ItemStack(Blocks.GLASS)).addGrist(Mana, 1));

        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 23), true, new GristSet(new GristType[] {Mana, Chalk}, new int[] {5,5}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.quartz, 1, 1), true, new GristSet(new GristType[] {Quartz, Marble, Mana}, new int[] {4,1,1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.grassSeeds, 1, 0), true, new GristSet(new GristType[]{Iodine, Amber}, new int[]{1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.grassSeeds, 1, 1), true, new GristSet(new GristType[]{Sulfur, Amber}, new int[]{1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.grassSeeds, 1, 2), true, new GristSet(new GristType[]{Iodine, Ruby}, new int[]{1, 1}));

        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 4), true, new GristSet(new GristType[]{Build, Mana, Uranium, Rust, Diamond}, new int[] {210, 500, 22, 18, 24}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 5), true, new GristSet(new GristType[]{Mana, Uranium, Artifact}, new int[] {440, 84, 10}));


        GristRegistry.addGristConversion(new ItemStack(ModItems.overgrowthSeed), false, new GristSet(new GristType[]{Mana, Iodine, Amber, Build}, new int[] {18, 132, 128, 24}));

        if(MinestuckUniverse.isThaumLoaded)
            GristRegistry.addGristConversion(new ItemStack(ModItems.manaInkwell), false, new GristSet(new GristType[] {Mana, Build, Chalk, Shale, Tar}, new int[] {35, 1, 4, 1, 4}));


        GristRegistry.addGristConversion(new ItemStack(ModItems.blackLotus, 1, 0), true, new GristSet(new GristType[] {Mana, Iodine}, new int[] {8, 2}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.blackLotus, 1, 1), true, new GristSet(new GristType[] {Mana, Iodine}, new int[] {100, 2}));

        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 6), true, new GristSet(new GristType[] {Build, Garnet}, new int[] {1, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 15), true, new GristSet(new GristType[] {Mercury, Uranium, Build}, new int[] {2, 2, 3}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 7), true, new GristSet(new GristType[] {Mana, Ruby, Rust}, new int[] {5, 8, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 8), true, new GristSet(new GristType[] {Mana, Ruby, Uranium}, new int[] {5, 8, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.manaResource, 1, 9), true, new GristSet(new GristType[] {Mana, Ruby, Diamond}, new int[] {5, 16, 8}));
        GristRegistry.addGristConversion(new ItemStack(ModBlocks.elfGlass), false, new GristSet(new GristType[] {Build, Mana, Diamond}, new int[] {1, 2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.quartz, 1, 5), true, new GristSet(new GristType[] {Quartz, Marble, Mana}, new int[] {1, 1, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModBlocks.dreamwood), false, new GristSet(new GristType[]{Build, Mana}, new int[] {2, 4}));

        GristRegistry.addGristConversion(new ItemStack(ModBlocks.altar), false, new GristSet(Build, 10));

        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 0), true, new GristSet(new GristType[]{Mana, Marble, Rust, Cobalt}, new int[] {5, 8, 4, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 1), true, new GristSet(new GristType[]{Mana, Marble, Rust, Tar}, new int[] {5, 8, 4, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 2), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale}, new int[] {5, 8, 4, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 3), true, new GristSet(new GristType[]{Mana, Marble, Rust, Chalk}, new int[] {5, 8, 4, 16}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 4), true, new GristSet(new GristType[]{Mana, Marble, Rust, Cobalt, Tar, Iodine}, new int[] {8, 8, 4, 8, 8, 12}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 5), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale, Chalk, Caulk}, new int[] {8, 8, 4, 8, 8, 12}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 6), true, new GristSet(new GristType[]{Mana, Marble, Rust, Chalk, Tar, Sulfur}, new int[] {8, 8, 4, 8, 8, 12}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 7), true, new GristSet(new GristType[]{Mana, Marble, Rust, Cobalt, Shale}, new int[] {8, 8, 4, 16, 8}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 8), true, new GristSet(new GristType[]{Mana, Marble, Rust, Uranium}, new int[] {24, 8, 8, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 9), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale, Chalk, Caulk, Quartz}, new int[] {12, 8, 4, 8, 16, 8, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 10), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale, Tar, Cobalt, Iodine}, new int[] {12, 8, 4, 8, 8, 16, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 11), true, new GristSet(new GristType[]{Mana, Marble, Rust, Cobalt, Tar, Iodine, Gold}, new int[] {12, 8, 4, 8, 16, 8, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 12), true, new GristSet(new GristType[]{Mana, Marble, Rust, Chalk, Tar, Sulfur, Caulk}, new int[] {12, 8, 4, 8, 16, 8, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 13), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale, Cobalt, Garnet}, new int[] {12, 8, 4, 16, 16, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 13), true, new GristSet(new GristType[]{Mana, Marble, Rust, Shale, Cobalt, Amethyst}, new int[] {12, 8, 4, 8, 24, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.rune, 1, 12), true, new GristSet(new GristType[]{Mana, Marble, Rust, Chalk, Tar, Shale, Caulk, Diamond}, new int[] {12, 8, 4, 8, 8, 8, 8, 4}));

        GristRegistry.addGristConversion(new ItemStack(ModItems.gaiaHead), false, new GristSet(new GristType[]{Build, Mana, Artifact, Zillium}, new int[] {125, 340, 200, 10}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.recordGaia1), false, new GristSet(new GristType[]{Build, Mana, Artifact, Tar}, new int[] {84, 4, 5, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.recordGaia2), false, new GristSet(new GristType[]{Build, Mana, Artifact, Gold}, new int[] {88, 20, 50, 4}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.pinkinator), false, new GristSet(new GristType[]{Build, Mana, Artifact, Zillium}, new int[] {1200, 200, 924, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.ancientWill), false, new GristSet(new GristType[]{Build, Mana}, new int[] {800, 400}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.infiniteFruit), false, new GristSet(new GristType[]{Mana, Iodine, Zillium}, new int[] {800, 1600, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.kingKey), false, new GristSet(new GristType[]{Mana, Sulfur, Zillium}, new int[] {800, 1600, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.flugelEye), false, new GristSet(new GristType[]{Mana, Uranium, Zillium}, new int[] {800, 1200, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.odinRing), false, new GristSet(new GristType[]{Mana, Garnet, Ruby, Zillium}, new int[] {800, 600, 600, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.thorRing), false, new GristSet(new GristType[]{Mana, Uranium, Shale, Zillium}, new int[] {800, 400, 800, 1}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.lokiRing), false, new GristSet(new GristType[]{Mana, Cobalt, Gold, Zillium}, new int[] {800, 800, 400, 1}));

        GristRegistry.addGristConversion(new ItemStack(ModItems.starSword), false, new GristSet(new GristType[]{Build, Mana, Uranium, Rust, Diamond, Ruby, Garnet}, new int[] {450, 1020, 48, 36, 56, 84, 12}));
        GristRegistry.addGristConversion(new ItemStack(ModItems.thunderSword), false, new GristSet(new GristType[]{Build, Mana, Uranium, Rust, Diamond, Cobalt, Mercury}, new int[] {450, 1020, 48, 36, 56, 84, 12}));

        //Combination Recipes
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaBottle), new ItemStack(Items.SUGAR), MODE_OR, Mana.getCandyItem());
        CombinationRegistry.addCombination(new ItemStack(gristBlockMana), new ItemStack(Items.GLASS_BOTTLE), MODE_OR, false, false, new ItemStack(ModItems.manaBottle));
        CombinationRegistry.addCombination(new ItemStack(ModItems.grassSeeds), new ItemStack(Blocks.DIRT), MODE_OR, true, false, new ItemStack(Blocks.GRASS));
        CombinationRegistry.addCombination(new ItemStack(ModItems.grassSeeds, 1, 1), new ItemStack(Blocks.DIRT), MODE_OR, true, false, new ItemStack(Blocks.DIRT, 1, 2));
        CombinationRegistry.addCombination(new ItemStack(ModItems.grassSeeds, 1, 2), new ItemStack(Blocks.DIRT), MODE_OR, true, false, new ItemStack(Blocks.MYCELIUM));
        CombinationRegistry.addCombination(new ItemStack(ModItems.overgrowthSeed), new ItemStack(Blocks.DIRT), MODE_OR, false, false, new ItemStack(ModBlocks.enchantedSoil));
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaResource, 1, 5), new ItemStack(Items.SKULL, 1, 3), MODE_AND, true, true, new ItemStack(ModItems.gaiaHead));
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaResource, 1, 5), new ItemStack(Items.RECORD_WAIT), MODE_AND, true, false, new ItemStack(ModItems.recordGaia1));
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaResource, 1, 5), new ItemStack(Items.RECORD_13), MODE_AND, true, false, new ItemStack(ModItems.recordGaia1));
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaResource, 1, 14), new ItemStack(Items.RECORD_13), MODE_AND, true, false, new ItemStack(ModItems.recordGaia2));
        CombinationRegistry.addCombination(new ItemStack(ModItems.manaResource, 1, 14), new ItemStack(Items.RECORD_WAIT), MODE_AND, true, false, new ItemStack(ModItems.recordGaia2));
        CombinationRegistry.addCombination(new ItemStack(ModBlocks.dreamwood, 1, 1), new ItemStack(Blocks.CRAFTING_TABLE), MODE_AND, true, false, new ItemStack(ModBlocks.openCrate, 1, 1));
        CombinationRegistry.addCombination(new ItemStack(ModBlocks.livingwood, 1, 1), new ItemStack(Blocks.CRAFTING_TABLE), MODE_AND, true, false, new ItemStack(ModBlocks.openCrate, 1, 0));

        for(EnumDyeColor color : EnumDyeColor.values())
        {
            int i = color.getMetadata();
            //flowerGrist.addGrist(flowerGrist);
            //flowerGrist.addGrist(Iodine, 1);

            ItemStack dye = new ItemStack(Items.DYE, 1, color.getDyeDamage());
            ItemStack wool = new ItemStack(Blocks.WOOL, 1, color.getMetadata());
            GristSet dyeGrist = GristRegistry.getGristConversion(wool).addGrist(Chalk, -6);

            if(dyeGrist.getGrist(Chalk) == 0) dyeGrist.addGrist(Chalk, 0);

            GristSet flowerGrist = dyeGrist.copy().scaleGrist(2).addGrist(Iodine, 1);
            GristSet doubleFlowerGrist = dyeGrist.copy().scaleGrist(4).addGrist(Iodine, 2);

            GristRegistry.addGristConversion(ModBlocks.flower, i, flowerGrist);
            GristRegistry.addGristConversion( i < 8 ? ModBlocks.doubleFlower1 : ModBlocks.doubleFlower2, i % 8, doubleFlowerGrist);
            GristRegistry.addGristConversion(new ItemStack(ModItems.dye, 1, i), dyeGrist);
            CombinationRegistry.addCombination(new ItemStack(Items.SUGAR), new ItemStack(Items.DYE, 1, i), MODE_AND, false, true, new ItemStack(ModItems.dye, 1, i));
            CombinationRegistry.addCombination(new ItemStack(Blocks.TALLGRASS), new ItemStack(ModItems.petal, 1, i), MODE_AND, false, true, new ItemStack(ModBlocks.flower, 1, i));
            CombinationRegistry.addCombination(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), new ItemStack(ModItems.petal, 1, i), MODE_AND, true, true, new ItemStack( i < 8 ? ModBlocks.doubleFlower1 : ModBlocks.doubleFlower2, 1, i));
            CombinationRegistry.addCombination(new ItemStack(ModBlocks.flower, 1, i), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, true, false, new ItemStack(ModBlocks.shinyFlower, 1, i));
            CombinationRegistry.addCombination(new ItemStack(ModBlocks.shinyFlower, 1, i), new ItemStack(Blocks.GRASS), MODE_OR, true, false, new ItemStack(ModBlocks.floatingFlower, 1, i));
        }

        //Botania Recipes
        BotaniaAPI.registerManaInfusionRecipe(new ItemStack(magicBlock), new ItemStack(MinestuckBlocks.genericObject), 16000);

        BotaniaSupport.gristCosts = GristRegistry.getAllConversions();
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
