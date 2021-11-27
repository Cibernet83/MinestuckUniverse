package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockGrist;
import com.cibernet.minestuckuniverse.blocks.BlockWoolTransportalizer;
import com.cibernet.minestuckuniverse.items.properties.PropertyBreakableItem;
import com.cibernet.minestuckuniverse.modSupport.BotaniaSupport;
import com.cibernet.minestuckuniverse.util.MSUCalendarUtil;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.items.ItemsTC;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public static final ArrayList<Block> gristBlocks = new ArrayList<Block>() {{add(gristBlockAmber); add(gristBlockAmethyst); add(gristBlockArtifact); add(gristBlockCaulk); add(gristBlockChalk); add(gristBlockCobalt); add(gristBlockBuild); add(gristBlockDiamond); add(gristBlockGarnet); add(gristBlockGold); add(gristBlockIodine); add(gristBlockMarble);
        add(gristBlockMercury); add(gristBlockQuartz); add(gristBlockRuby); add(gristBlockRust); add(gristBlockSulfur); add(gristBlockShale); add(gristBlockTar); add(gristBlockUranium); add(gristBlockZillium);}};

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
        if(MinestuckUniverse.isSplatcraftLodaded) registerSplatcraft();
        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();
        if(MinestuckUniverse.isChiselLoaded) registerChiselRecipes();
        if(MinestuckUniverse.isMysticalWorldLoaded) registerMysticalWorldRecipes();
        if(MinestuckUniverse.isBOPLoaded) registerBOPRecipes();
        if(MinestuckUniverse.isFutureMcLoaded) registerFutureMcRecipes();

        if(MinestuckUniverse.isMekanismLoaded)
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mekanism", "ingot")), 1, 1), true, new GristSet(new GristType[] {Mercury, Rust}, new int[] {8, 12}));
        if(MinestuckUniverse.isCyclicLoaded)
        {
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("cyclicmagic", "crystalized_amber"))), false, new GristSet(new GristType[] {Amber}, new int[] {20}));
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("cyclicmagic", "crystalized_obsidian"))), false, new GristSet(new GristType[] {Tar, Cobalt, Amber, Diamond}, new int[] {40, 20, 40, 8}));
        }
        if(MinestuckUniverse.isVampirismLoaded)
        {
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("vampirism", "blood_bottle"))), false, new GristSet(new GristType[] {Garnet, Iodine}, new int[] {16, 16}));
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("vampirism", "vampire_blood_bottle"))), false, new GristSet(new GristType[] {Garnet, Iodine, Ruby}, new int[] {20, 8, 4}));
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("vampirism", "vampirism_flower"))), false, new GristSet(new GristType[] {Iodine, Shale, Garnet}, new int[] {2, 4, 2}));
        }
        if(MinestuckUniverse.isIndustrialForegoingLoaded)
            GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("industrialforegoing", "plastic"))), false, new GristSet(new GristType[] {Build, Quartz}, new int[] {500, 1}));


        ConsortRewardHandler.registerPrice(new ItemStack(MinestuckItems.candy, 1, 21), 500, 600);
    }
    
    public static void registerVanilla()
    {
        //OreDictionary entries
        OreDictionary.registerOre("record", MinestuckItems.recordDanceStab);
        OreDictionary.registerOre("record", MinestuckItems.recordEmissaryOfDance);
        OreDictionary.registerOre("record", MinestuckItems.recordRetroBattle);

        GristRegistry.addGristConversion(new ItemStack(Blocks.SLIME_BLOCK), new GristSet(new GristType[] {Caulk}, new int[] {72}));

        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.clawSickle), new GristSet(new GristType[] {Build, Iodine, Artifact}, new int[] {5000, 4000, 1}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.dragonCane), new GristSet(new GristType[] {Rust, Ruby, Mercury}, new int[] {200, 400, 300}));

        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.royalDeringer), new GristSet(new GristType[]{GristType.Build, GristType.Gold, GristType.Garnet, GristType.Diamond}, new int[]{1000, 2400, 20, 350}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.caledfwlch), new GristSet(new GristType[]{GristType.Build, GristType.Chalk, GristType.Gold, GristType.Zillium}, new int[]{5000, 10000, 200, 1}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.caledscratch), new GristSet(new GristType[]{GristType.Build, GristType.Chalk, GristType.Garnet, GristType.Uranium}, new int[]{2500, 3500, 1500, 1550}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.scarletRibbitar), new GristSet(new GristType[]{GristType.Build, GristType.Ruby, GristType.Quartz, GristType.Diamond, GristType.Zillium}, new int[]{3000, 4000, 2000, 1000, 2000}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.doggMachete), new GristSet(new GristType[]{GristType.Build, GristType.Cobalt, GristType.Chalk, GristType.Shale, GristType.Diamond}, new int[]{1000, 1000, 2500, 500, 100}));

        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.scarletZillyhoo) , new GristSet(new GristType[] {Build, Zillium, Ruby, Quartz, Diamond}, new int[] {1200, 800, 600, 30, 15}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.zillyhooHammer) , new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.zillywairCutlass) , new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.popamaticVrillyhoo) , new GristSet(new GristType[]{Zillium, Cobalt, Diamond}, new int[]{10000, 32768, 4096}));

        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.clawHammer), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(MinestuckItems.zillyhooHammer));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(MinestuckItems.zillywairCutlass));
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
        
        GristRegistry.addGristConversion(new ItemStack(uniqueObject), new GristSet(new GristType[] {Zillium}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(artifact), new GristSet(new GristType[] {Artifact}, new int[] {1000} ));

        GristRegistry.addGristConversion(new ItemStack(diverHelmet), new GristSet(new GristType[] {Rust, Cobalt}, new int[] {80, 16}));
        GristRegistry.addGristConversion(new ItemStack(spikedHelmet), new GristSet(new GristType[] {Rust}, new int[] {260}));
        GristRegistry.addGristConversion(new ItemStack(frogHat), new GristSet(new GristType[] {Build}, new int[] {20}));
        GristRegistry.addGristConversion(new ItemStack(wizardHat), new GristSet(new GristType[] {Build, Amethyst}, new int[] {10, 8}));
        GristRegistry.addGristConversion(new ItemStack(cozySweater), new GristSet(new GristType[] {Build, Garnet}, new int[] {10, 8}));
        GristRegistry.addGristConversion(new ItemStack(archmageHat), new GristSet(new GristType[] {Build, Amethyst, Cobalt, Garnet}, new int[] {10, 800, 800, 800}));
        GristRegistry.addGristConversion(new ItemStack(bunnySlippers), new GristSet(new GristType[] {Build, Chalk}, new int[] {10, 8}));
        GristRegistry.addGristConversion(new ItemStack(cruxtruderHat), new GristSet(new GristType[] {Build}, new int[] {100}));

        GristRegistry.addGristConversion(new ItemStack(rubberBoots), new GristSet(new GristType[] {Build, Shale}, new int[] {10, 20}));
        GristRegistry.addGristConversion(new ItemStack(moonShoes), new GristSet(new GristType[] {Build, Shale}, new int[] {20, 10}));
        GristRegistry.addGristConversion(new ItemStack(sunShoes), new GristSet(new GristType[] {Build, Shale, Sulfur}, new int[] {200, 100, 250}));
        GristRegistry.addGristConversion(new ItemStack(rocketBoots), new GristSet(new GristType[] {Build, Tar, Sulfur}, new int[] {1600, 200, 80}));
        GristRegistry.addGristConversion(new ItemStack(windWalkers), new GristSet(new GristType[] {Build, Cobalt, Uranium, Chalk}, new int[] {120, 60, 1, 200}));
        GristRegistry.addGristConversion(new ItemStack(airJordans), new GristSet(new GristType[] {Build, Ruby, Chalk}, new int[] {800, 600, 800}));
        GristRegistry.addGristConversion(new ItemStack(cobaltJordans), new GristSet(new GristType[] {Build, Cobalt, Chalk}, new int[] {800, 600, 800}));

        GristRegistry.addGristConversion(new ItemStack(magicBlock), magicBlockCost);
        GristRegistry.addGristConversion(new ItemStack(sbahjBedrock), new GristSet(new GristType[] {Artifact}, new int[] {2000}));
        GristRegistry.addGristConversion(new ItemStack(zillyStone), new GristSet(new GristType[] {Zillium, Build}, new int[] {1, 100}));
        GristRegistry.addGristConversion(new ItemStack(smoothIron), new GristSet(new GristType[] {Build, Rust, Quartz}, new int[] {10, 8, 4}));
        GristRegistry.addGristConversion(new ItemStack(wizardStatue), new GristSet(new GristType[] {Build}, new int[] {12}));
        GristRegistry.addGristConversion(new ItemStack(netherReactorCore), new GristSet(new GristType[] {Uranium, Garnet, Rust, Diamond}, new int[] {250, 121, 50, 2}));
        GristRegistry.addGristConversion(new ItemStack(bigRock), new GristSet(new GristType[] {Build}, new int[] {1000}));
        GristRegistry.addGristConversion(new ItemStack(wizardbeardYarn), new GristSet(new GristType[] {Cobalt, Amethyst, Garnet}, new int[] {100, 200, 250}));
        GristRegistry.addGristConversion(new ItemStack(unrealAir), new GristSet(new GristType[] {Build}, new int[] {0}));
        GristRegistry.addGristConversion(new ItemStack(whip), new GristSet(new GristType[] {Iodine, Build}, new int[] {14, 8}));
        GristRegistry.addGristConversion(new ItemStack(sbahjWhip), new GristSet(new GristType[] {Iodine, Build, Artifact}, new int[] {10, 10, -10}));
        GristRegistry.addGristConversion(new ItemStack(laserPointer), new GristSet(new GristType[] {Mercury, Ruby}, new int[] {8, 10}));

        if(MSUCalendarUtil.isChristmas())
            GristRegistry.addGristConversion(new ItemStack(sbahjTree), new GristSet(new GristType[] {}, new int[] {}));
        else GristRegistry.addGristConversion(new ItemStack(sbahjTree), new GristSet(new GristType[] {Artifact}, new int[] {10}));

        GristRegistry.addGristConversion(new ItemStack(machineChasis), new GristSet(new GristType[] {Build, Rust, Uranium, Diamond}, new int[] {750, 98, 55, 4}));
        GristRegistry.addGristConversion(new ItemStack(gristHopper), new GristSet(new GristType[] {Build, Rust, Uranium}, new int[] {250, 55, 10}));
        GristRegistry.addGristConversion(new ItemStack(autoWidget), new GristSet(new GristType[] {Build, Rust, Uranium, Garnet, Zillium}, new int[] {550, 34, 24, 35, 1}));
        GristRegistry.addGristConversion(new ItemStack(autoCaptcha), new GristSet(new GristType[] {Build, Rust, Uranium, Cobalt, Ruby, Quartz}, new int[] {140, 36, 22, 16, 12, 1}));
        GristRegistry.addGristConversion(new ItemStack(porkhollowAtm), new GristSet(new GristType[] {Build, Rust, Uranium, Iodine, Diamond}, new int[] {180, 35, 16, 18, 4}));
        GristRegistry.addGristConversion(new ItemStack(boondollarRegister), new GristSet(new GristType[] {Build, Rust, Uranium, Garnet, Iodine, Quartz}, new int[] {280, 35, 20, 25, 16, 8}));
        
        GristRegistry.addGristConversion(new ItemStack(rubyRedTransportalizer), new GristSet(new GristType[] {Build, Ruby, Rust, Uranium}, new int[] {450, 100, 36, 24}));
        GristRegistry.addGristConversion(new ItemStack(goldenTransportalizer), new GristSet(new GristType[] {Build, Gold, Shale, Uranium}, new int[] {350, 36, 30, 20}));
        GristRegistry.addGristConversion(new ItemStack(paradoxTransportalizer), new GristSet(new GristType[] {Build, Rust, Uranium, Diamond, Zillium}, new int[] {750, 120, 256, 74, 25}));

        GristRegistry.addGristConversion(new ItemStack(battery), new GristSet(new GristType[] {Build, Mercury}, new int[] {1, 2}));
        GristRegistry.addGristConversion(new ItemStack(batteryBeamBlade), new GristSet(new GristType[]{Build, Uranium, Mercury}, new int[]{27, 54, 83}));
        GristRegistry.addGristConversion(new ItemStack(bloodKatana), new GristSet(new GristType[] {Rust, Iodine}, new int[] {2600, 30}));
        GristRegistry.addGristConversion(new ItemStack(trueUnbreakableKatana), new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(quantumEntangloporter), new GristSet(new GristType[] {Iodine, Uranium, Diamond}, new int[] {4400, 680, 2}));
        GristRegistry.addGristConversion(new ItemStack(lightbringer), new GristSet(new GristType[] {Gold, Tar, Sulfur, Diamond}, new int[] {3400, 1200, 800, 22}));
        GristRegistry.addGristConversion(new ItemStack(cybersword), new GristSet(new GristType[] {Gold, Cobalt, Uranium, Diamond}, new int[] {4400, 8030, 8, 220}));
        GristRegistry.addGristConversion(new ItemStack(valorsEdge), new GristSet(new GristType[] {Diamond, Ruby, Uranium, Gold}, new int[] {8400, 200, 200, 800}));

        GristRegistry.addGristConversion(new ItemStack(loghammer), new GristSet(new GristType[] {Build}, new int[] {24}));
        GristRegistry.addGristConversion(new ItemStack(overgrownLoghammer), new GristSet(new GristType[] {Build, Amber}, new int[] {48, 16}));
        GristRegistry.addGristConversion(new ItemStack(glowingLoghammer), new GristSet(new GristType[] {Build, Shale}, new int[] {48, 16}));
        GristRegistry.addGristConversion(new ItemStack(midasMallet), new GristSet(new GristType[]{GristType.Build, GristType.Gold, GristType.Quartz, GristType.Diamond}, new int[]{1500, 4000, 20, 20}));
        GristRegistry.addGristConversion(new ItemStack(aaaNailShocker), new GristSet(new GristType[] {Build, Mercury}, new int[] {20, 16}));
        GristRegistry.addGristConversion(new ItemStack(highVoltageStormCrusher), new GristSet(new GristType[] {Gold, Mercury, Uranium, Quartz}, new int[] {2000, 1230, 6060, 800}));
        GristRegistry.addGristConversion(new ItemStack(barrelsWarhammer), new GristSet(new GristType[] {Ruby, Diamond, Uranium, Gold}, new int[] {8400, 200, 200, 800}));
        GristRegistry.addGristConversion(new ItemStack(stardustSmasher), new GristSet(new GristType[] {Gold, Zillium, Artifact}, new int[] {5600, 10, 1}));
        GristRegistry.addGristConversion(new ItemStack(MinestuckItems.mwrthwl), new GristSet(new GristType[]{GristType.Build, GristType.Chalk, GristType.Gold, GristType.Zillium}, new int[]{2000, 12000, 200, 1}));

        GristRegistry.addGristConversion(new ItemStack(hereticusAurum), new GristSet(new GristType[] {Build, Gold, Amethyst}, new int[] {210, 2000, 256}));

        GristRegistry.addGristConversion(new ItemStack(rubyContrabat), new GristSet(new GristType[] {Ruby, Diamond, Quartz, Build}, new int[] {800, 30, 15, 1400}));
        GristRegistry.addGristConversion(new ItemStack(dynamiteStick), new GristSet(new GristType[] {Sulfur, Chalk, Build}, new int[] {400, 320, 800}));
        GristRegistry.addGristConversion(new ItemStack(nightmareMace), new GristSet(new GristType[] {Amethyst, Shale, Caulk, Tar}, new int[] {6000, 4000, 4000, 6000}));
        GristRegistry.addGristConversion(new ItemStack(cranialEnder), new GristSet(new GristType[] {Amethyst, Cobalt, Uranium, Mercury, Tar, Diamond}, new int[] {6800, 4020, 300, 540, 210, 4}));
        GristRegistry.addGristConversion(new ItemStack(homeRunBat), new GristSet(new GristType[] {Ruby, Gold, Garnet, Artifact}, new int[] {4500, 8000, 4500, 1}));
        GristRegistry.addGristConversion(new ItemStack(badaBat), new GristSet(new GristType[] {Cobalt, Chalk, Amethyst, Artifact}, new int[] {4500, 8000, 4500, 100}));

        GristRegistry.addGristConversion(new ItemStack(goldCane), new GristSet(new GristType[] {Gold, Build}, new int[] {20, 16}));
        GristRegistry.addGristConversion(new ItemStack(staffOfOvergrowth), new GristSet(new GristType[] {Iodine, Ruby, Amber}, new int[] {540, 800, 240}));
        GristRegistry.addGristConversion(new ItemStack(atomicIrradiator), new GristSet(new GristType[] {Uranium, Build}, new int[] {200, 2000}));
        GristRegistry.addGristConversion(new ItemStack(scepterOfZillywuud), new GristSet(Zillium, 1000));

        GristRegistry.addGristConversion(new ItemStack(battlepickOfZillydew), new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(battleaxeOfZillywahoo), new GristSet(Zillium, 1000));
        GristRegistry.addGristConversion(new ItemStack(battlesporkOfZillywut), new GristSet(Zillium, 1000));

        GristRegistry.addGristConversion(new ItemStack(gravediggerShovel), new GristSet(new GristType[] {Rust, Garnet}, new int[] {2000, 2}));
        GristRegistry.addGristConversion(new ItemStack(rolledUpPaper), new GristSet(new GristType[] {Amber, Iodine}, new int[] {3, 2}));
        GristRegistry.addGristConversion(new ItemStack(yesterdaysNews), new GristSet(new GristType[] {Amber, Quartz, Garnet}, new int[] {300, 200, 125}));
        GristRegistry.addGristConversion(new ItemStack(pebble), new GristSet(Build, 1));
        GristRegistry.addGristConversion(new ItemStack(rock), new GristSet(Build, 10));

        GristRegistry.addGristConversion(new ItemStack(fancyGlove), false, new GristSet(new GristType[] {GristType.Build, GristType.Chalk}, new int[] {5, 2}));
        GristRegistry.addGristConversion(new ItemStack(spikedGlove), false, new GristSet(new GristType[] {GristType.Build, GristType.Rust, GristType.Chalk}, new int[] {25, 5, 3}));
        GristRegistry.addGristConversion(new ItemStack(cobbleBasher), false, new GristSet(new GristType[] {GristType.Build, GristType.Marble}, new int[] {30, 5}));
        GristRegistry.addGristConversion(new ItemStack(pogoFist), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {16, 21}));
        GristRegistry.addGristConversion(new ItemStack(fluoriteGauntlet), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Caulk, GristType.Quartz, GristType.Shale}, new int[] {803, 500, 10, 2}));
        GristRegistry.addGristConversion(new ItemStack(goldenGenesisGauntlet), false, new GristSet(new GristType[] {GristType.Build, GristType.Gold, GristType.Uranium, GristType.Diamond, GristType.Artifact}, new int[] {8000, 5000, 300, 250, 10}));
        GristRegistry.addGristConversion(new ItemStack(rocketFist), false, new GristSet(new GristType[] {Build, Chalk, Sulfur, Tar}, new int[] {220, 40, 200, 80}));
        GristRegistry.addGristConversion(new ItemStack(eldrichGauntlet), false, new GristSet(new GristType[] {Tar, Amethyst, Shale, Quartz}, new int[] {3000, 4000, 5000, 66}));
        GristRegistry.addGristConversion(new ItemStack(jawbreaker), false, new GristSet(new GristType[] {Amber, Iodine, Marble}, new int[] {500, 400, 100}));
        GristRegistry.addGristConversion(new ItemStack(gasterBlaster), false, new GristSet(new GristType[] {Chalk, Cobalt, Diamond, Artifact}, new int[] {800, 4000, 200, 1}));
        GristRegistry.addGristConversion(new ItemStack(midasGlove), false, new GristSet(new GristType[] {Gold, Diamond, Quartz}, new int[] {4000, 200, 200}));
        GristRegistry.addGristConversion(new ItemStack(gauntletOfZillywenn), false, new GristSet(new GristType[] {Zillium}, new int[] {1000}));

        GristRegistry.addGristConversion(new ItemStack(pointySticks), false, new GristSet(new GristType[] {Build}, new int[] {12}));
        GristRegistry.addGristConversion(new ItemStack(drumstickNeedles), false, new GristSet(new GristType[] {Build, Garnet}, new int[] {200, 10}));
        GristRegistry.addGristConversion(new ItemStack(boneNeedles), false, new GristSet(new GristType[] {Chalk}, new int[] {24}));
        GristRegistry.addGristConversion(new ItemStack(needlewands), false, new GristSet(new GristType[] {Diamond, Chalk, Garnet, Gold}, new int[] {1000, 2000, 3000, 500}));
        GristRegistry.addGristConversion(new ItemStack(oglogothThorn), false, new GristSet(new GristType[] {Iodine, Chalk, Amethyst, Gold, Tar}, new int[] {6000, 5000, 4000, 3000, 666}));
        GristRegistry.addGristConversion(new ItemStack(litGlitterBeamTransistor), false, new GristSet(new GristType[] {Rust, Gold, Ruby, Cobalt, Uranium, Amethyst}, new int[] {333, 3300, 6000, 6000, 6000, 6000}));
        GristRegistry.addGristConversion(new ItemStack(echidnaQuills), false, new GristSet(new GristType[] {Chalk, Iodine, Artifact, Zillium}, new int[] {16000, 8000, 10000, 10}));
        GristRegistry.addGristConversion(new ItemStack(thistlesOfZillywitch), false, new GristSet(new GristType[] {Zillium}, new int[] {1000}));

        GristRegistry.addGristConversion(new ItemStack(actionClaws), new GristSet(new GristType[] {Build, Diamond, Cobalt}, new int[] {200, 80, 20}));
        GristRegistry.addGristConversion(new ItemStack(sneakyDaggers), new GristSet(new GristType[] {Tar, Chalk, Rust}, new int[] {200, 68, 90}));
        GristRegistry.addGristConversion(new ItemStack(candyCornClaws), new GristSet(new GristType[] {Amber, Chalk, Iodine}, new int[] {10, 35, 43}));
        GristRegistry.addGristConversion(new ItemStack(rocketKatars), new GristSet(new GristType[] {Build, Chalk, Sulfur, Tar}, new int[] {110, 64, 20, 80}));
        GristRegistry.addGristConversion(new ItemStack(blizzardCutters), new GristSet(new GristType[] {Cobalt, Shale}, new int[] {800, 640}));
        GristRegistry.addGristConversion(new ItemStack(thunderbirdTalons), new GristSet(new GristType[] {Cobalt, Mercury, Quartz, Gold}, new int[] {2400, 810, 20, 1000}));
        GristRegistry.addGristConversion(new ItemStack(archmageDaggers), new GristSet(new GristType[] {Amethyst, Garnet, Gold, Chalk}, new int[] {5000, 2000, 1500, 1500}));
        GristRegistry.addGristConversion(new ItemStack(katarsOfZillywhomst), new GristSet(Zillium, 1000));

        GristRegistry.addGristConversion(new ItemStack(energyBow), new GristSet(new GristType[] {Uranium, Build}, new int[] {24, 32}));
        GristRegistry.addGristConversion(new ItemStack(infernoShot), new GristSet(new GristType[] {Sulfur, Tar, Build}, new int[] {24, 16, 32}));
        GristRegistry.addGristConversion(new ItemStack(icicleBow), new GristSet(new GristType[] {Cobalt, Build}, new int[] {24, 32}));
        GristRegistry.addGristConversion(new ItemStack(shiverburnWing), new GristSet(new GristType[] {Cobalt, Sulfur, Ruby, Build}, new int[] {24, 24, 60, 100}));
        GristRegistry.addGristConversion(new ItemStack(sweetBow), new GristSet(new GristType[] {Chalk, Amber, Iodine, Gold}, new int[] {40, 32, 32, 20}));
        GristRegistry.addGristConversion(new ItemStack(tempestBow), new GristSet(new GristType[] {Chalk, Cobalt, Quartz}, new int[] {200, 490, 40}));
        GristRegistry.addGristConversion(new ItemStack(magneticHookshot), new GristSet(new GristType[] {Uranium, Mercury, Rust}, new int[] {400, 680, 850}));
        GristRegistry.addGristConversion(new ItemStack(wormholePiercer), new GristSet(new GristType[] {Uranium, Mercury, Ruby, Rust}, new int[] {4000, 860, 400, 2050}));
        GristRegistry.addGristConversion(new ItemStack(telegravitationalWarper), new GristSet(new GristType[] {Uranium, Mercury, Diamond, Artifact}, new int[] {4000, 1030, 100, 10}));
        GristRegistry.addGristConversion(new ItemStack(mechanicalCrossbow), new GristSet(new GristType[] {Build, Rust, Garnet}, new int[] {6000, 4000, 200}));
        GristRegistry.addGristConversion(new ItemStack(crabbow), new GristSet(new GristType[] {Build, Iodine, Quartz}, new int[] {6000, 6000, 20}));
        GristRegistry.addGristConversion(new ItemStack(kingOfThePond), new GristSet(new GristType[] {Gold, Iodine, Ruby, Tar}, new int[] {2000, 3000, 200, 1000}));
        GristRegistry.addGristConversion(new ItemStack(gildedGuidance), new GristSet(new GristType[] {Gold, Quartz, Diamond}, new int[] {3000, 400, 400}));
        GristRegistry.addGristConversion(new ItemStack(bowOfLight), new GristSet(new GristType[] {Gold, Sulfur, Diamond, Cobalt}, new int[] {2000, 4000, 200, 10}));
        GristRegistry.addGristConversion(new ItemStack(theChancemaker), new GristSet(new GristType[] {Gold, Ruby, Diamond, Cobalt}, new int[] {1800, 400, 2000, 10000}));
        GristRegistry.addGristConversion(new ItemStack(wisdomsPierce), new GristSet(new GristType[] {Uranium, Diamond, Ruby, Gold}, new int[] {8400, 200, 200, 800}));

        GristRegistry.addGristConversion(new ItemStack(clearShield), new GristSet(Build, 20));
        GristRegistry.addGristConversion(new ItemStack(woodenDoorshield), new GristSet(Build, 60));
        GristRegistry.addGristConversion(new ItemStack(ironDoorshield), new GristSet(Rust, 60));
        GristRegistry.addGristConversion(new ItemStack(bladedShield), new GristSet(new GristType[] {Build, Rust}, new int[] {50, 24}));
        GristRegistry.addGristConversion(new ItemStack(shockerShell), new GristSet(new GristType[] {Build, Mercury}, new int[] {1000, 800}));
        GristRegistry.addGristConversion(new ItemStack(windshield), new GristSet(new GristType[] {Build, Cobalt, Chalk}, new int[] {1600, 600, 600}));
        GristRegistry.addGristConversion(new ItemStack(clarityWard), new GristSet(new GristType[] {Gold, Quartz, Amethyst}, new int[] {240, 200, 2400}));
        GristRegistry.addGristConversion(new ItemStack(rocketRiotShield), new GristSet(new GristType[] {Rust, Tar, Sulfur}, new int[] {2000, 6040, 2010}));
        GristRegistry.addGristConversion(new ItemStack(ejectorShield), new GristSet(new GristType[] {Build, Sulfur}, new int[] {1330, 610}));
        GristRegistry.addGristConversion(new ItemStack(firewall), new GristSet(new GristType[] {Build, Tar, Amber}, new int[] {880, 1210, 624}));
        GristRegistry.addGristConversion(new ItemStack(obsidianShield), new GristSet(new GristType[] {Build, Rust, Tar, Cobalt}, new int[] {3000, 1000, 500, 500}));
        GristRegistry.addGristConversion(new ItemStack(wallOfThorns), new GristSet(new GristType[] {Amber, Gold, Ruby}, new int[] {1000, 200, 435}));
        GristRegistry.addGristConversion(new ItemStack(livingShield), new GristSet(new GristType[] {Amber, Gold, Ruby, Diamond}, new int[] {1100, 2000, 800, 4}));
        GristRegistry.addGristConversion(new ItemStack(hardRindHarvest), new GristSet(new GristType[] {Amber, Gold, Caulk}, new int[] {800, 500, 800}));
        GristRegistry.addGristConversion(new ItemStack(nuclearNeglector), new GristSet(new GristType[] {Uranium, Rust, Shale}, new int[] {4500, 432, 800}));
        GristRegistry.addGristConversion(new ItemStack(perfectAegis), new GristSet(new GristType[] {Cobalt, Uranium, Gold, Zillium}, new int[] {8000, 6000, 4500, 1}));

        GristRegistry.addGristConversion(new ItemStack(markedBoomerang), new GristSet(Build, 10));
        GristRegistry.addGristConversion(new ItemStack(redHotRang), new GristSet(new GristType[] {Build, Sulfur, Tar}, new int[] {200, 40, 20}));
        GristRegistry.addGristConversion(new ItemStack(goldenStar), new GristSet(Gold, 1));
        GristRegistry.addGristConversion(new ItemStack(suitarang), new GristSet(new GristType[] {Build, Tar}, new int[] {5, 1}));
        GristRegistry.addGristConversion(new ItemStack(psionicStar), new GristSet(new GristType[] {Build, Amethyst}, new int[] {20, 4}));
        GristRegistry.addGristConversion(new ItemStack(hotPotato), new GristSet(new GristType[] {Amber, Tar}, new int[] {20, 200}));
        GristRegistry.addGristConversion(new ItemStack(dragonCharge), new GristSet(new GristType[] {Ruby, Sulfur, Tar}, new int[] {200, 800, 450}));
        GristRegistry.addGristConversion(new ItemStack(tornadoGlaive), new GristSet(new GristType[] {Cobalt, Gold, Quartz, Uranium}, new int[] {260, 120, 30, 2}));

        if(MinestuckUniverse.isArsenalLoaded)
        {
            GristRegistry.addGristConversion(new ItemStack(moonstone), new GristSet(new GristType[] {Build, getTypeFromString("minestuckarsenal:moonstone"), Uranium}, new int[] {5, 5, 2}));
            GristRegistry.addGristConversion(new ItemStack(moonstoneOre), new GristSet(new GristType[] {Build, getTypeFromString("minestuckarsenal:moonstone"), Uranium, Build}, new int[] {5, 5, 2, 4}));
        }
        else
        {
            GristRegistry.addGristConversion(new ItemStack(moonstone), new GristSet(new GristType[] {Build, Cobalt, Amethyst, Uranium}, new int[] {5, 4, 3, 2}));
            GristRegistry.addGristConversion(new ItemStack(moonstoneOre), new GristSet(new GristType[] {Build, Cobalt, Amethyst, Uranium, Build}, new int[] {5, 4, 3, 2, 4}));
        }

        GristRegistry.addGristConversion(new ItemStack(fluorite), new GristSet(new GristType[] {Cobalt, Diamond}, new int[] {20, 1}));
        GristRegistry.addGristConversion(new ItemStack(zillystoneShard), new GristSet(new GristType[] {Zillium}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(spaceSalt), new GristSet(new GristType[] {Uranium, Tar, Zillium}, new int[] {10, 32, 1}));
        GristRegistry.addGristConversion(new ItemStack(timetable), new GristSet(new GristType[] {Uranium, Garnet, Rust, Zillium}, new int[] {300, 1600, 1600, 500}));

        GristRegistry.addGristConversion(new ItemStack(fluoriteOre), new GristSet(new GristType[] {Cobalt, Diamond, Build}, new int[] {20, 1, 4}));
    
        GristRegistry.addGristConversion(new ItemStack(trueUnbreakableKatana) , new GristSet(Zillium, 1000));

        GristRegistry.addGristConversion(new ItemStack(returnMedallion) , new GristSet(new GristType[] {Build, Rust, Artifact, Quartz}, new int[] {10000, 1000, 250, 500}));
        GristRegistry.addGristConversion(new ItemStack(teleportMedallion) , new GristSet(new GristType[] {Build, Uranium, Rust, Artifact}, new int[] {5000, 5000, 1000, 250}));
        GristRegistry.addGristConversion(new ItemStack(skaianMedallion) , new GristSet(new GristType[]
                {Amber, Amethyst, Caulk, Chalk, Cobalt, Diamond, Garnet, Gold, Iodine, Marble, Mercury, Quartz, Ruby, Rust, Shale, Sulfur, Tar, Uranium, Zillium, Artifact}, new int[]
                {250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 250000, 100000, 100000}));

        //Alchemy
        CombinationRegistry.addCombination(new ItemStack(zillystoneShard), new ItemStack(Items.SUGAR), MODE_OR, Zillium.getCandyItem());

        //armor
        CombinationRegistry.addCombination(new ItemStack(Items.FISH, 1, 3), new ItemStack(Items.IRON_HELMET), MODE_AND, true, false, new ItemStack(diverHelmet));
        CombinationRegistry.addCombination(new ItemStack(diverHelmet), new ItemStack(knittingNeedles), MODE_OR, false, false, new ItemStack(spikedHelmet));
        CombinationRegistry.addCombination(new ItemStack(Items.LEATHER_HELMET), new ItemStack(MinestuckItems.itemFrog, 1, 1), MODE_OR, false, true, new ItemStack(frogHat));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_HELMET), new ItemStack(MinestuckBlocks.cruxtruderLid), MODE_OR, false, true, new ItemStack(cruxtruderHat));
        CombinationRegistry.addCombination(new ItemStack(Items.SLIME_BALL), new ItemStack(Items.LEATHER_BOOTS), MODE_AND, new ItemStack(rubberBoots));
        CombinationRegistry.addCombination(new ItemStack(Items.SLIME_BALL), new ItemStack(Items.LEATHER_BOOTS), MODE_OR, new ItemStack(moonShoes));
        CombinationRegistry.addCombination(new ItemStack(moonShoes), new ItemStack(sun), MODE_AND, new ItemStack(sunShoes));
        CombinationRegistry.addCombination(new ItemStack(rubberBoots), new ItemStack(yarnBall, 1, 14), MODE_AND, new ItemStack(airJordans));
        CombinationRegistry.addCombination(new ItemStack(rubberBoots), new ItemStack(yarnBall, 1, 4), MODE_AND, new ItemStack(cobaltJordans));
        CombinationRegistry.addCombination(new ItemStack(airJordans), new ItemStack(Blocks.OBSIDIAN), MODE_AND, new ItemStack(cobaltJordans));
        CombinationRegistry.addCombination(new ItemStack(moonShoes), new ItemStack(bunnySlippers), MODE_OR, new ItemStack(windWalkers));
        CombinationRegistry.addCombination(new ItemStack(airJordans), new ItemStack(Items.FIREWORKS), MODE_OR, new ItemStack(rocketBoots));

        //materials
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.rawCruxite), new ItemStack(Blocks.END_STONE), MODE_OR, new ItemStack(moonstone));
        CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.DIAMOND), MODE_AND, true, false, new ItemStack(fluorite));
        CombinationRegistry.addCombination(new ItemStack(yarnBall, 1, 15), new ItemStack(wizardStatue), MODE_AND, true, false, new ItemStack(wizardbeardYarn));

        CombinationRegistry.addCombination(new ItemStack(Blocks.BEDROCK), new ItemStack(MinestuckItems.sbahjPoster), MODE_AND, new ItemStack(sbahjBedrock));
        CombinationRegistry.addCombination(new ItemStack(sbahjBedrock), Zillium.getCandyItem(), MODE_OR, false, true, new ItemStack(zillyStone));
        CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.QUARTZ_BLOCK, 1, 0), MODE_OR, false, true, new ItemStack(smoothIron));
        CombinationRegistry.addCombination(new ItemStack(Blocks.LAPIS_BLOCK), new ItemStack(Blocks.DIAMOND_BLOCK), MODE_AND, new ItemStack(fluoriteBlock));
        CombinationRegistry.addCombination(new ItemStack(fluorite), new ItemStack(Blocks.STONE), MODE_AND, new ItemStack(fluoriteOre));
        CombinationRegistry.addCombination(new ItemStack(moonstone), new ItemStack(Blocks.STONE), MODE_AND, new ItemStack(moonstoneOre));
        CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.uraniumBlock), new ItemStack(netherPortal), MODE_AND, new ItemStack(netherReactorCore));
        CombinationRegistry.addCombination(new ItemStack(wizardHat), new ItemStack(Blocks.STONE), MODE_OR, new ItemStack(wizardStatue));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.sbahjPoster), new ItemStack(Blocks.SAPLING), MODE_AND, new ItemStack(sbahjTree));
        CombinationRegistry.addCombination(new ItemStack(moonstone), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, new ItemStack(machineChasis));
        CombinationRegistry.addCombination(new ItemStack(gristBlockBuild), new ItemStack(wizardStatue), MODE_AND, new ItemStack(magicBlock));
        CombinationRegistry.addCombination(new ItemStack(Items.STRING), new ItemStack(Items.LEATHER), MODE_AND, new ItemStack(whip));
        CombinationRegistry.addCombination(new ItemStack(whip), new ItemStack(MinestuckItems.sbahjPoster), MODE_AND, new ItemStack(sbahjWhip));

        //transportalizers
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 2), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(rubyRedTransportalizer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 5), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(goldenTransportalizer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 6), new ItemStack(MinestuckBlocks.transportalizer), MODE_AND, true, false, new ItemStack(paradoxTransportalizer));

        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 6), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, true, false, new ItemStack(spaceSalt));
        CombinationRegistry.addCombination(new ItemStack(tickingStopwatch), new ItemStack(MinestuckItems.recordDanceStab), MODE_OR, false, false, new ItemStack(timetable));

        //hammerkind
        CombinationRegistry.addCombination(new ItemStack(Blocks.LOG), new ItemStack(MinestuckItems.sledgeHammer), MODE_OR, new ItemStack(loghammer));
        CombinationRegistry.addCombination(new ItemStack(Blocks.LOG2), new ItemStack(MinestuckItems.sledgeHammer), MODE_OR, new ItemStack(loghammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.glowingLog), new ItemStack(MinestuckItems.sledgeHammer), MODE_OR, new ItemStack(glowingLoghammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.glowingMushroom), new ItemStack(loghammer), MODE_OR, new ItemStack(glowingLoghammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.log, 1, 0), new ItemStack(MinestuckItems.sledgeHammer), MODE_OR, true, false, new ItemStack(overgrownLoghammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckBlocks.log, 1, 0), new ItemStack(MinestuckItems.sledgeHammer), MODE_OR, true, false, new ItemStack(overgrownLoghammer));
        CombinationRegistry.addCombination(new ItemStack(Blocks.VINE), new ItemStack(loghammer), MODE_AND, false, false, new ItemStack(overgrownLoghammer));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.regiHammer), new ItemStack(MinestuckItems.itemFrog, 1, 5), MODE_OR, false, true, new ItemStack(midasMallet));
        CombinationRegistry.addCombination(new ItemStack(midasMallet), new ItemStack(cueBall), MODE_AND, false, true, new ItemStack(MinestuckItems.mwrthwl));
        CombinationRegistry.addCombination(new ItemStack(battery), new ItemStack(MinestuckItems.clawHammer), MODE_AND, false, false, new ItemStack(aaaNailShocker));
        CombinationRegistry.addCombination(new ItemStack(aaaNailShocker), new ItemStack(midasMallet), MODE_AND, false, false, new ItemStack(highVoltageStormCrusher));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.scarletZillyhoo), new ItemStack(rocketFist), MODE_AND, false, false, new ItemStack(barrelsWarhammer));
        removeCombination(new ItemStack(MinestuckItems.blacksmithHammer), new ItemStack(Items.CLOCK), CombinationRegistry.Mode.MODE_OR, false, false);
        CombinationRegistry.addCombination(new ItemStack(midasMallet), new ItemStack(tickingStopwatch), MODE_OR, new ItemStack(MinestuckItems.fearNoAnvil));
        removeCombination(new ItemStack(MinestuckItems.fearNoAnvil), new ItemStack(Items.LAVA_BUCKET), CombinationRegistry.Mode.MODE_OR, false, false);
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.blacksmithHammer), new ItemStack(Items.LAVA_BUCKET), MODE_AND, new ItemStack(MinestuckItems.meltMasher));

        //bladekind
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.katana), new ItemStack(MinestuckItems.minestuckBucket, 1, 1), MODE_OR, false, true, new ItemStack(bloodKatana));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.unbreakableKatana), new ItemStack(Blocks.BEDROCK), MODE_AND, false, false, new ItemStack(trueUnbreakableKatana));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.katana), new ItemStack(battery), MODE_AND, false, false, new ItemStack(batteryBeamBlade));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.unbreakableKatana), new ItemStack(Blocks.BEDROCK), MODE_AND, false, false, new ItemStack(trueUnbreakableKatana));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.cactusCutlass), new ItemStack(wormholePiercer), MODE_AND, false, false, new ItemStack(quantumEntangloporter));
        CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(MinestuckItems.itemFrog, 1, 6), MODE_OR, false, true, new ItemStack(crystallineRibbitar));
        CombinationRegistry.addCombination(new ItemStack(cozySweater), new ItemStack(crystallineRibbitar), CombinationRegistry.Mode.MODE_AND, false, false, new ItemStack(valorsEdge));
        CombinationRegistry.addCombination(new ItemStack(batteryBeamBlade), new ItemStack(sun), CombinationRegistry.Mode.MODE_AND, false, false, new ItemStack(lightbringer));
        CombinationRegistry.addCombination(new ItemStack(lightbringer), new ItemStack(lightning), CombinationRegistry.Mode.MODE_OR, false, false, new ItemStack(cybersword));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.claymore), new ItemStack(MinestuckItems.itemFrog, 1, 5), CombinationRegistry.Mode.MODE_OR, false, true, new ItemStack(MinestuckItems.royalDeringer));
        CombinationRegistry.addCombination(PropertyBreakableItem.getBrokenStack(MinestuckItems.royalDeringer), new ItemStack(cueBall), CombinationRegistry.Mode.MODE_AND, true, true, new ItemStack(MinestuckItems.caledfwlch));
        CombinationRegistry.addCombination(PropertyBreakableItem.getBrokenStack(MinestuckItems.caledfwlch), new ItemStack(timetable), CombinationRegistry.Mode.MODE_AND, true, false, new ItemStack(MinestuckItems.caledscratch));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.caledscratch), new ItemStack(Blocks.PACKED_ICE), CombinationRegistry.Mode.MODE_OR, new ItemStack(MinestuckItems.doggMachete));

        removeCombination(new ItemStack(Items.STONE_SWORD), new ItemStack(Items.ROTTEN_FLESH), CombinationRegistry.Mode.MODE_AND, false, true);
        removeCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.ROTTEN_FLESH), CombinationRegistry.Mode.MODE_AND, false, true);
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(throwingStar), MODE_AND, new ItemStack(MinestuckItems.katana));
        removeCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BLAZE_ROD), CombinationRegistry.Mode.MODE_AND, false, true);
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BLAZE_ROD), CombinationRegistry.Mode.MODE_AND, false, true, new ItemStack(MinestuckItems.firePoker));
        removeCombination(new ItemStack(MinestuckItems.regisword), new ItemStack(Items.BLAZE_ROD), CombinationRegistry.Mode.MODE_OR, false, true);
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.regisword), new ItemStack(Items.BLAZE_ROD), CombinationRegistry.Mode.MODE_OR, false, true, new ItemStack(MinestuckItems.hotHandle));
        removeCombination(new ItemStack(MinestuckItems.hotHandle), new ItemStack(Blocks.LAPIS_BLOCK), CombinationRegistry.Mode.MODE_OR, false, true);
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.hotHandle), new ItemStack(fluorite), CombinationRegistry.Mode.MODE_AND, false, true, new ItemStack(MinestuckItems.cobaltSabre));
        removeCombination(new ItemStack(MinestuckItems.upStick), new ItemStack(MinestuckItems.energyCore), CombinationRegistry.Mode.MODE_AND, false, false);
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.regisword), new ItemStack(MinestuckItems.energyCore), CombinationRegistry.Mode.MODE_AND, false, false, new ItemStack(MinestuckItems.quantumSabre));
        removeCombination(new ItemStack(Blocks.BEACON), new ItemStack(Items.DIAMOND_SWORD), CombinationRegistry.Mode.MODE_AND, false, false);
        CombinationRegistry.addCombination(new ItemStack(Blocks.BEACON), new ItemStack(crystallineRibbitar), CombinationRegistry.Mode.MODE_AND, false, false, new ItemStack(MinestuckItems.shatterBeacon));
        CombinationRegistry.addCombination(new ItemStack(Blocks.BEACON), new ItemStack(crystallineRibbitar), CombinationRegistry.Mode.MODE_AND, false, false, new ItemStack(MinestuckItems.shatterBeacon));

        //gauntletkind
        CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BARS), new ItemStack(fancyGlove), MODE_AND, false, false, new ItemStack(spikedGlove));
        CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(fancyGlove), MODE_AND, false, false, new ItemStack(cobbleBasher));
        CombinationRegistry.addCombination(new ItemStack(Items.SLIME_BALL), new ItemStack(spikedGlove), MODE_OR, false, false, new ItemStack(pogoFist));
        CombinationRegistry.addCombination(new ItemStack(fluorite), new ItemStack(goldenGenesisGauntlet), MODE_OR, false, false, new ItemStack(fluoriteGauntlet));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 5), new ItemStack(cobbleBasher), MODE_OR, true, false, new ItemStack(goldenGenesisGauntlet));
        CombinationRegistry.addCombination(new ItemStack(Items.FIREWORKS), new ItemStack(spikedGlove), MODE_AND, false, false, new ItemStack(rocketFist));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.grimoire), new ItemStack(spikedGlove), MODE_AND, false, false, new ItemStack(eldrichGauntlet));
        CombinationRegistry.addCombination(Marble.getCandyItem(), new ItemStack(cobbleBasher), MODE_OR, true, false, new ItemStack(jawbreaker));
        CombinationRegistry.addCombination(new ItemStack(goldenGenesisGauntlet), new ItemStack(midasMallet), MODE_AND, false, false, new ItemStack(midasGlove));
        CombinationRegistry.addCombination(new ItemStack(fancyGlove), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(gauntletOfZillywenn));

        //needlekind
        CombinationRegistry.addCombination(new ItemStack(Items.FLINT), new ItemStack(Items.STICK), MODE_AND, false, false, new ItemStack(pointySticks));
        CombinationRegistry.addCombination(new ItemStack(knittingNeedles), new ItemStack(Items.BONE), MODE_OR, false, false, new ItemStack(boneNeedles));
        CombinationRegistry.addCombination(new ItemStack(knittingNeedles), new ItemStack(wizardStatue), MODE_OR, false, false, new ItemStack(needlewands));
        CombinationRegistry.addCombination(new ItemStack(needlewands), new ItemStack(MinestuckItems.grimoire), MODE_AND, false, false, new ItemStack(oglogothThorn));
        CombinationRegistry.addCombination(new ItemStack(laserPointer), new ItemStack(MinestuckItems.minestuckBucket, 1, 3), MODE_OR, false, true, new ItemStack(litGlitterBeamTransistor));
        CombinationRegistry.addCombination(new ItemStack(knittingNeedles), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(thistlesOfZillywitch));
        CombinationRegistry.addCombination(new ItemStack(pointySticks), new ItemStack(Blocks.NOTEBLOCK), MODE_OR, false, false, new ItemStack(drumstickNeedles));

        //clawkind
        removeCombination(new ItemStack(Blocks.IRON_BARS), new ItemStack(Items.LEATHER), CombinationRegistry.Mode.MODE_AND, false, true);
        CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BARS), new ItemStack(fancyGlove), MODE_OR, false, false, new ItemStack(catclaws));
        CombinationRegistry.addCombination(new ItemStack(catclaws), new ItemStack(fluorite), MODE_AND, false, false, new ItemStack(actionClaws));
        CombinationRegistry.addCombination(new ItemStack(yarnBall, 1, 0), new ItemStack(actionClaws), MODE_AND, true, false, new ItemStack(sneakyDaggers));
        CombinationRegistry.addCombination(new ItemStack(Blocks.ICE), new ItemStack(actionClaws), MODE_OR, false, false, new ItemStack(blizzardCutters));
        CombinationRegistry.addCombination(new ItemStack(battery), new ItemStack(blizzardCutters), MODE_AND, false, false, new ItemStack(thunderbirdTalons));
        CombinationRegistry.addCombination(new ItemStack(bladesOfTheWarrior), new ItemStack(archmageHat), MODE_OR, false, false, new ItemStack(archmageDaggers));
        CombinationRegistry.addCombination(new ItemStack(katars), new ItemStack(Items.FIREWORKS), MODE_AND, false, false, new ItemStack(rocketKatars));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.candy, 1, 0), new ItemStack(catclaws), MODE_OR, true, false, new ItemStack(candyCornClaws));
        CombinationRegistry.addCombination(new ItemStack(zillyStone), new ItemStack(catclaws), MODE_OR, false, false, new ItemStack(katarsOfZillywhomst));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.clawSickle), new ItemStack(MinestuckItems.grimoire), MODE_AND, false, false, new ItemStack(MinestuckItems.clawOfNrubyiglith));
        removeCombination(new ItemStack(MinestuckItems.catClaws), new ItemStack(MinestuckItems.grimoire), MODE_AND, false, true);

        //shieldkind
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(Blocks.OAK_DOOR), MODE_OR, false, false, new ItemStack(woodenDoorshield));
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(Blocks.IRON_DOOR), MODE_OR, false, false, new ItemStack(ironDoorshield));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(woodenDoorshield), MODE_AND, false, false, new ItemStack(ironDoorshield));
        CombinationRegistry.addCombination(new ItemStack(Items.FIREWORKS), new ItemStack(ironDoorshield), MODE_AND, false, false, new ItemStack(rocketRiotShield));
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(Items.IRON_SWORD), MODE_OR, false, false, new ItemStack(bladedShield));
        CombinationRegistry.addCombination(new ItemStack(diamondKatars), new ItemStack(bladedShield), MODE_AND, false, false, new ItemStack(clarityWard));
        CombinationRegistry.addCombination(new ItemStack(battery), new ItemStack(bladedShield), MODE_AND, false, false, new ItemStack(shockerShell));
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(MinestuckBlocks.stoneExplosiveButton), MODE_OR, false, false, new ItemStack(ejectorShield));
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(Items.FIRE_CHARGE), MODE_AND, false, false, new ItemStack(firewall));
        CombinationRegistry.addCombination(new ItemStack(firewall), new ItemStack(Items.WATER_BUCKET), MODE_AND, false, false, new ItemStack(obsidianShield));
        CombinationRegistry.addCombination(new ItemStack(ironDoorshield), new ItemStack(Blocks.OBSIDIAN), MODE_OR, false, false, new ItemStack(obsidianShield));
        CombinationRegistry.addCombination(new ItemStack(Items.SHIELD), new ItemStack(Blocks.GLASS_PANE), MODE_OR, false, false, new ItemStack(clearShield));
        CombinationRegistry.addCombination(new ItemStack(Items.FEATHER), new ItemStack(clearShield), MODE_AND, false, false, new ItemStack(windshield));
        CombinationRegistry.addCombination(new ItemStack(woodenDoorshield), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), MODE_AND, false, true, new ItemStack(wallOfThorns));
        CombinationRegistry.addCombination(new ItemStack(wallOfThorns), new ItemStack(MinestuckBlocks.uraniumBlock), MODE_OR, false, false, new ItemStack(nuclearNeglector));
        CombinationRegistry.addCombination(new ItemStack(wallOfThorns), new ItemStack(Items.SPECKLED_MELON), MODE_OR, false, false, new ItemStack(hardRindHarvest));
        CombinationRegistry.addCombination(new ItemStack(wallOfThorns), new ItemStack(Items.SPECKLED_MELON), MODE_AND, false, false, new ItemStack(livingShield));
        CombinationRegistry.addCombination(new ItemStack(nuclearNeglector), new ItemStack(moon), MODE_AND, false, false, new ItemStack(perfectAegis));

        //bowkind
        CombinationRegistry.addCombination(new ItemStack(Items.BOW), new ItemStack(MinestuckItems.energyCore), MODE_AND, false, false, new ItemStack(energyBow));
        CombinationRegistry.addCombination(new ItemStack(Items.BOW), new ItemStack(Items.BLAZE_POWDER), MODE_AND, false, false, new ItemStack(infernoShot));
        CombinationRegistry.addCombination(new ItemStack(Items.BOW), new ItemStack(Blocks.ICE), MODE_OR, false, false, new ItemStack(icicleBow));
        CombinationRegistry.addCombination(new ItemStack(Items.BOW), new ItemStack(MinestuckItems.candy), MODE_OR, false, true, new ItemStack(sweetBow));
        CombinationRegistry.addCombination(new ItemStack(Items.BOW), new ItemStack(MinestuckItems.clawSickle), MODE_OR, false, false, new ItemStack(crabbow));
        CombinationRegistry.addCombination(new ItemStack(infernoShot), new ItemStack(Blocks.DISPENSER), MODE_AND, false, false, new ItemStack(mechanicalCrossbow));
        CombinationRegistry.addCombination(new ItemStack(infernoShot), new ItemStack(icicleBow), MODE_OR, false, false, new ItemStack(shiverburnWing));
        CombinationRegistry.addCombination(new ItemStack(infernoShot), new ItemStack(MinestuckItems.itemFrog, 1, 5), MODE_AND, false, true, new ItemStack(kingOfThePond));
        CombinationRegistry.addCombination(new ItemStack(icicleBow), new ItemStack(windshield), MODE_OR, false, false, new ItemStack(tempestBow));
        CombinationRegistry.addCombination(new ItemStack(energyBow), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, false, false, new ItemStack(magneticHookshot));
        CombinationRegistry.addCombination(new ItemStack(energyBow), new ItemStack(teleportMedallion), MODE_AND, false, false, new ItemStack(wormholePiercer));
        CombinationRegistry.addCombination(new ItemStack(energyBow), new ItemStack(Items.ENDER_EYE), MODE_OR, false, false, new ItemStack(wormholePiercer));
        CombinationRegistry.addCombination(new ItemStack(energyBow), new ItemStack(kingOfThePond), MODE_OR, false, false, new ItemStack(gildedGuidance));
        CombinationRegistry.addCombination(new ItemStack(magneticHookshot), new ItemStack(Items.ENDER_EYE), MODE_OR, false, false, new ItemStack(wormholePiercer));
        CombinationRegistry.addCombination(new ItemStack(magneticHookshot), new ItemStack(tempestBow), MODE_AND, false, false, new ItemStack(telegravitationalWarper));
        CombinationRegistry.addCombination(new ItemStack(gildedGuidance), new ItemStack(fluoriteOctet), MODE_AND, false, false, new ItemStack(theChancemaker));
        CombinationRegistry.addCombination(new ItemStack(gildedGuidance), new ItemStack(lightbringer), MODE_AND, false, false, new ItemStack(bowOfLight));
        CombinationRegistry.addCombination(new ItemStack(gildedGuidance), new ItemStack(magneticHookshot), MODE_OR, false, false, new ItemStack(wisdomsPierce));

        //dicekind
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.dice), new ItemStack(fluorite), MODE_AND, new ItemStack(fluoriteOctet));

        //sicklekind
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.regiSickle), new ItemStack(MinestuckItems.itemFrog, 1, 5), MODE_AND, false, true, new ItemStack(hereticusAurum));

        //clubkind
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.metalBat), new ItemStack(Blocks.TNT), MODE_OR, new ItemStack(dynamiteStick));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.nightClub), new ItemStack(MinestuckItems.itemFrog, 1, 2), MODE_AND, false, true, new ItemStack(rubyContrabat));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.spikedClub), new ItemStack(eldrichGauntlet), MODE_AND, false, false, new ItemStack(nightmareMace));
        CombinationRegistry.addCombination(new ItemStack(nightmareMace), new ItemStack(Items.END_CRYSTAL), MODE_AND, false, false, new ItemStack(cranialEnder));
        CombinationRegistry.addCombination(new ItemStack(homeRunBat), new ItemStack(MinestuckItems.boondollars), MODE_AND, false, false, new ItemStack(badaBat));

        //canekind
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.spearCane), new ItemStack(Blocks.SKULL, 1, 5), MODE_AND, false, true, new ItemStack(MinestuckItems.dragonCane));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.paradisesPortabello), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), MODE_AND, false, true, new ItemStack(staffOfOvergrowth));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.upStick), new ItemStack(MinestuckItems.quantumSabre), MODE_AND, new ItemStack(atomicIrradiator));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.cane), new ItemStack(Items.GOLD_INGOT), MODE_OR, new ItemStack(goldCane));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.cane), new ItemStack(Items.IRON_INGOT), MODE_OR, new ItemStack(MinestuckItems.ironCane));
        CombinationRegistry.addCombination(new ItemStack(goldCane), new ItemStack(cueBall), MODE_AND, new ItemStack(goldenCuestaff));
        CombinationRegistry.addCombination(new ItemStack(goldCane), new ItemStack(zillyStone), MODE_AND, new ItemStack(scepterOfZillywuud));

        //throwkind
        CombinationRegistry.addCombination(new ItemStack(throwingStar), new ItemStack(MinestuckItems.crewPoster), MODE_AND, new ItemStack(suitarang));
        CombinationRegistry.addCombination(new ItemStack(throwingStar), new ItemStack(Items.GOLD_INGOT), MODE_OR, new ItemStack(goldenStar));
        CombinationRegistry.addCombination(new ItemStack(goldenStar), new ItemStack(MinestuckItems.minestuckBucket, 1, 2), MODE_OR, false, true, new ItemStack(psionicStar));
        CombinationRegistry.addCombination(new ItemStack(Items.POTATO), new ItemStack(Items.FIRE_CHARGE), MODE_OR, new ItemStack(hotPotato));
        CombinationRegistry.addCombination(new ItemStack(boomerang), new ItemStack(Items.LAVA_BUCKET), MODE_AND, new ItemStack(redHotRang));
        CombinationRegistry.addCombination(new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.FIRE_CHARGE), MODE_AND, new ItemStack(dragonCharge));

        //rockkind
        CombinationRegistry.addCombination(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Blocks.COBBLESTONE), MODE_AND, new ItemStack(pebble));
        CombinationRegistry.addCombination(new ItemStack(pebble), new ItemStack(Blocks.STONE), MODE_AND, new ItemStack(rock));
        CombinationRegistry.addCombination(new ItemStack(rock), new ItemStack(MinestuckItems.telescopicSassacrusher), MODE_AND, new ItemStack(bigRock));

        //misckind
        CombinationRegistry.addCombination(new ItemStack(battery), new ItemStack(Blocks.REDSTONE_TORCH), MODE_AND, new ItemStack(laserPointer));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.ROTTEN_FLESH), MODE_AND, new ItemStack(gravediggerShovel));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.fearNoAnvil), new ItemStack(rolledUpPaper), MODE_AND, new ItemStack(yesterdaysNews));
        CombinationRegistry.addCombination(new ItemStack(MinestuckItems.spork), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(battlesporkOfZillywut));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_PICKAXE), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(battlepickOfZillydew));
        CombinationRegistry.addCombination(new ItemStack(Items.IRON_AXE), new ItemStack(zillyStone), MODE_AND, false, false, new ItemStack(battleaxeOfZillywahoo));

        //ghost items
        CombinationRegistry.addCombination(new ItemStack(endPortal), new ItemStack(MinestuckBlocks.chessboard), MODE_OR, false, false, new ItemStack(MinestuckBlocks.skaiaPortal));
        CombinationRegistry.addCombination(new ItemStack(sun), new ItemStack(MinestuckItems.rawUranium), MODE_OR, false, false, new ItemStack(greenSun));

        //medalions
        CombinationRegistry.addCombination(new ItemStack(ironMedallion), new ItemStack(returnNode), MODE_OR, new ItemStack(returnMedallion));
        CombinationRegistry.addCombination(new ItemStack(returnMedallion), new ItemStack(MinestuckBlocks.transportalizer), MODE_OR, false, false, new ItemStack(teleportMedallion));
        CombinationRegistry.addCombination(new ItemStack(returnMedallion), new ItemStack(MinestuckBlocks.skaiaPortal), MODE_OR, false, false, new ItemStack(skaianMedallion));

        //dyed beam blades
        for(EnumDyeColor dyeColor : EnumDyeColor.values())
        {
            ItemStack dye = new ItemStack(Items.DYE, 1, dyeColor.getDyeDamage());
            ItemStack wool = new ItemStack(Blocks.WOOL, 1, dyeColor.getMetadata());
            GristSet dyeGrist = GristRegistry.getGristConversion(wool).addGrist(Chalk, -6);
            if(dyeGrist.getGrist(Chalk) == 0) dyeGrist.addGrist(Chalk, 0);

            ItemStack beamBlade = new ItemStack(dyedBeamBlade[dyeColor.getMetadata()]);

            GristRegistry.addGristConversion(beamBlade, GristRegistry.getGristConversion(new ItemStack(batteryBeamBlade)).addGrist(dyeGrist));
            CombinationRegistry.addCombination(new ItemStack(batteryBeamBlade), dye, MODE_OR, false, true, beamBlade);
        }


        if(MinestuckUniverse.isBotaniaLoaded)
            gristBlocks.add(gristBlockMana);
    }
    
    public static void registerSleevedTransportalizerRecipes()
    {
        for(BlockWoolTransportalizer block : sleevedTransportalizers.values())
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
    }

    public static void registerBotania()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(ModBlocks.livingwood, new GristSet(new GristType[] {Build}, new int[] {4}));
        GristRegistry.addGristConversion(ModBlocks.livingrock, new GristSet(new GristType[] {Build}, new int[] {4}));

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

            ItemStack wool = new ItemStack(Blocks.WOOL, 1, color.getMetadata());
            GristSet dyeGrist = GristRegistry.getGristConversion(wool).addGrist(Chalk, -6);

            if(dyeGrist.getGrist(Chalk) == 0) dyeGrist.addGrist(Chalk, 0);

            GristSet flowerGrist = dyeGrist.copy().scaleGrist(2).addGrist(Iodine, 1);
            GristSet doubleFlowerGrist = dyeGrist.copy().scaleGrist(4).addGrist(Iodine, 2);

            GristRegistry.addGristConversion(ModBlocks.flower, i, flowerGrist);
            GristRegistry.addGristConversion( i < 8 ? ModBlocks.doubleFlower1 : ModBlocks.doubleFlower2, i % 8, doubleFlowerGrist);
            GristRegistry.addGristConversion(new ItemStack(ModItems.dye, 1, i), dyeGrist);
            CombinationRegistry.addCombination(new ItemStack(Items.SUGAR), new ItemStack(Items.DYE, 1, color.getDyeDamage()), MODE_AND, false, true, new ItemStack(ModItems.dye, 1, i));
            CombinationRegistry.addCombination(new ItemStack(Blocks.TALLGRASS), new ItemStack(ModItems.petal, 1, i), MODE_AND, false, true, new ItemStack(ModBlocks.flower, 1, i));
            CombinationRegistry.addCombination(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), new ItemStack(ModItems.petal, 1, i), MODE_AND, true, true, new ItemStack( i < 8 ? ModBlocks.doubleFlower1 : ModBlocks.doubleFlower2, 1, i));
            CombinationRegistry.addCombination(new ItemStack(ModBlocks.flower, 1, i), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, true, false, new ItemStack(ModBlocks.shinyFlower, 1, i));
            CombinationRegistry.addCombination(new ItemStack(ModBlocks.shinyFlower, 1, i), new ItemStack(Blocks.GRASS), MODE_OR, true, false, new ItemStack(ModBlocks.floatingFlower, 1, i));
        }

        //Botania Recipes
        BotaniaAPI.registerManaInfusionRecipe(new ItemStack(magicBlock), new ItemStack(MinestuckBlocks.genericObject), 16000);

        BotaniaSupport.gristCosts = GristRegistry.getAllConversions();
    }
    
    
    private static void registerSplatcraft()
    {
        addGristConversion("splatcraft", "power_egg", new GristSet(new GristType[] {Amber, Iodine}, new int[] {6, 3}));
        addGristConversion("splatcraft", "sardinium", new GristSet(new GristType[] {Rust, Caulk}, new int[] {8, 2}));
        addGristConversion("splatcraft", "sardinium_ore", new GristSet(new GristType[] {Build, Rust, Caulk}, new int[] {4, 8, 2}));
        
        CombinationRegistry.addCombination(new ItemStack(Items.FISH), new ItemStack(Blocks.IRON_ORE), MODE_AND, true, false, new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("splatcraft", "sardinium_ore"))));
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
        if(i != null)
            GristRegistry.addGristConversion(new ItemStack(i), grist);
    }

    public static void addBalancedGristCost(ItemStack stack, int gristValue, GristType[] grist, float... ratio)
    {
        float total = 0;
        for(float f : ratio)
            total += f;
        int[] values = new int[grist.length];

        for(int i = 0; i < Math.min(ratio.length, grist.length); i++)
            values[i] = (int) (ratio[i]/total * gristValue/grist[i].getValue());

        if(GristRegistry.getGristConversion(stack) != null)
            GristRegistry.removeGristConversion(stack);
        GristRegistry.addGristConversion(stack, new GristSet(grist, values));
    }

    public static void removeCombination(@Nonnull ItemStack input1, @Nonnull ItemStack input2, CombinationRegistry.Mode mode, boolean useDamage1, boolean useDamage2)
    {
        removeCombination(input1.getItem(), useDamage1 ? input1.getItemDamage() : 32767, input2.getItem(), useDamage2 ? input2.getItemDamage() : 32767, mode);
    }

    private static void removeCombination(Object input1, int damage1, Object input2, int damage2, CombinationRegistry.Mode mode) {

        int index = input1.hashCode() - input2.hashCode();
        if (index == 0) {
            index = damage1 - damage2;
        }

        if(index > 0)
            CombinationRegistry.getAllConversions().remove(Arrays.asList(input1, damage1, input2, damage2, mode));
        else CombinationRegistry.getAllConversions().remove(Arrays.asList(input2, damage2, input1, damage1, mode));

    }


    private static final void registerMysticalWorldRecipes()
    {
        String modid = "mysticalworld";
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "amethyst_gem"))), false, new GristSet(new GristType[] {Amethyst}, new int[] {18}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "mud_block"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "carapace"))), false, new GristSet(new GristType[] {Shale, Iodine}, new int[] {8, 5}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "venison"))), false, new GristSet(new GristType[] {Iodine}, new int[] {12}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cooked_venison"))), false, new GristSet(new GristType[] {Tar, Iodine}, new int[] {1, 12}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "antlers"))), false, new GristSet(new GristType[] {Chalk, Build}, new int[] {12, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "pelt"))), false, new GristSet(new GristType[] {Chalk, Iodine}, new int[] {3, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "raw_squid"))), false, new GristSet(new GristType[] {Cobalt, Iodine}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cooked_squid"))), false, new GristSet(new GristType[] {Tar, Iodine}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "aubergine"))), false, new GristSet(new GristType[] {Amethyst, Amber}, new int[] {2, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cooked_aubergine"))), false, new GristSet(new GristType[] {Amethyst, Amber, Tar}, new int[] {2, 2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "silk_cocoon"))), false, new GristSet(new GristType[] {Chalk}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "silk_thread"))), false, new GristSet(new GristType[] {Chalk}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "gall_apple"))), false, new GristSet(new GristType[] {Shale, Iodine}, new int[] {2, 2}));
    }

    private static final void registerChiselRecipes()
    {
        String modid = "chisel";
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-oak"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-birch"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-spruce"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-jungle"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-acacia"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "planks-dark-oak"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "marble"))), false, new GristSet(new GristType[] {Build, Marble}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "marble1"))), false, new GristSet(new GristType[] {Build, Marble}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "marble2"))), false, new GristSet(new GristType[] {Build, Marble}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "marblepillar"))), false, new GristSet(new GristType[] {Build, Marble}, new int[] {1, 1}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "glass"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "stonebrick"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cobblestone"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cobblestone1"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "cobblestone2"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
    }

    private static final void registerBOPRecipes()
    {
        //dirt
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 0), new GristSet(new GristType[] {Build, Caulk}, new int[] {4, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 1), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 2), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 3), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 4), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 5), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 6), new GristSet(new GristType[] {Build, Tar}, new int[] {2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 7), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "grass")), 1, 8), new GristSet(new GristType[] {Build, Tar, Iodine, Ruby}, new int[] {2, 1, 2, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "dirt"))), false, new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mudball"))), false, new GristSet(new GristType[] {Build, Cobalt}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mud_brick"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));

        //sand
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "white_sand"))), false, new GristSet(new GristType[] {Build, Chalk}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "dried_sand"))), new GristSet(new GristType[] {Build}, new int[] {1}));

        //Flowers
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 0), true, new GristSet(new GristType[] {Iodine, Chalk, Tar}, new int[] {1, 6, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 1), true, new GristSet(new GristType[] {Iodine, Amber, Amethyst}, new int[] {2, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 2), true, new GristSet(new GristType[] {Iodine, Uranium, Tar}, new int[] {2, 2, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 3), true, new GristSet(new GristType[] {Iodine, Amber, Amethyst, Tar}, new int[] {1, 4, 4, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 4), true, new GristSet(new GristType[] {Iodine, Chalk, Amethyst}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 5), true, new GristSet(new GristType[] {Iodine, Garnet, Amber}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 6), true, new GristSet(new GristType[] {Iodine, Chalk, Garnet}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 7), true, new GristSet(new GristType[] {Iodine, Amethyst, Garnet}, new int[] {1, 2, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 8), true, new GristSet(new GristType[] {Iodine, Amethyst, Garnet}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 9), true, new GristSet(new GristType[] {Iodine, Chalk}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 10), true, new GristSet(new GristType[] {Iodine, Caulk, Tar}, new int[] {1, 2, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 11), true, new GristSet(new GristType[] {Iodine, Ruby}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 12), true, new GristSet(new GristType[] {Iodine, Chalk, Tar}, new int[] {1, 2, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 13), true, new GristSet(new GristType[] {Iodine, Chalk, Garnet}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 14), true, new GristSet(new GristType[] {Iodine, Chalk}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_0")), 1, 15), true, new GristSet(new GristType[] {Iodine, Amber, Garnet, Sulfur}, new int[] {1, 4, 4, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 0), true, new GristSet(new GristType[] {Iodine, Amethyst, Garnet}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 1), true, new GristSet(new GristType[] {Iodine, Amber}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 2), true, new GristSet(new GristType[] {Iodine, Amethyst}, new int[] {1, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 3), true, new GristSet(new GristType[] {Iodine, Chalk, Garnet}, new int[] {2, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 4), true, new GristSet(new GristType[] {Iodine, Chalk, Cobalt}, new int[] {2, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 5), true, new GristSet(new GristType[] {Iodine, Garnet}, new int[] {2, 8}));

        //Plant Things
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "log_0"))), false, new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "log_1"))), false, new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "log_2"))), false, new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "log_3"))), false, new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "log_4"))), false, new GristSet(new GristType[] {Build}, new int[] {8}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_0"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_1"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_2"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_3"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_4"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_5"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "leaves_6"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "sapling_0"))), false, new GristSet(new GristType[] {Build}, new int[] {16}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "sapling_1"))), false, new GristSet(new GristType[] {Build}, new int[] {16}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "sapling_2"))), false, new GristSet(new GristType[] {Build}, new int[] {16}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_0"))), false, new GristSet(new GristType[] {Build}, new int[] {1}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 0), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 1), new GristSet(new GristType[] {Build, Caulk}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 2), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 3), new GristSet(new GristType[] {Build, Iodine}, new int[] {1, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 4), new GristSet(new GristType[] {Build, Chalk}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 4), new GristSet(new GristType[] {Build, Chalk}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 5), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 6), new GristSet(new GristType[] {Amber, Iodine}, new int[] {3, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 7), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 8), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 9), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 10), new GristSet(new GristType[] {Garnet, Shale}, new int[] {4, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "plant_1")), 1, 11), new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "bamboo"))), new GristSet(new GristType[] {Build}, new int[] {2}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "double_plant")), 1, 0), true, new GristSet(new GristType[] {Iodine, Amethyst, Chalk}, new int[] {1, 4, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "double_plant")), 1, 1), true, new GristSet(new GristType[] {Build, Chalk}, new int[] {2, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "double_plant")), 1, 2), true, new GristSet(new GristType[] {Iodine, Sulfur}, new int[] {1, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "double_plant")), 1, 3), true, new GristSet(new GristType[] {Iodine, Build}, new int[] {1, 3}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "waterlily"))), false, new GristSet(new GristType[] {Amber, Iodine}, new int[] {4, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "coral"))), false, new GristSet(new GristType[] {Cobalt, Amber, Iodine}, new int[] {1, 4, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "seaweed"))), false, new GristSet(new GristType[] {Cobalt, Amber, Iodine}, new int[] {1, 4, 1}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "willow_vine"))), false, new GristSet(new GristType[] {Build, Amber}, new int[] {2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "ivy"))), false, new GristSet(new GristType[] {Build, Amber}, new int[] {2, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "bramble_plant"))), false, new GristSet(new GristType[] {Build}, new int[] {6}));

        //Mushrooms
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 0), true, new GristSet(new GristType[] {Iodine}, new int[] {5}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 1), true, new GristSet(new GristType[] {Iodine, Chalk}, new int[] {2, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 2), true, new GristSet(new GristType[] {Iodine, Cobalt}, new int[] {2, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 3), true, new GristSet(new GristType[] {Iodine, Uranium}, new int[] {2, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 4), true, new GristSet(new GristType[] {Iodine}, new int[] {5}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "mushroom")), 1, 5), true, new GristSet(new GristType[] {Iodine, Shale}, new int[] {3, 2}));

        //Fruit
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "berry")), 1), false, new GristSet(new GristType[] {Amber}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "pear")), 1), false, new GristSet(new GristType[] {Amber, Shale}, new int[] {3, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "persimmon")), 1), false, new GristSet(new GristType[] {Amber, Iodine}, new int[] {3, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "peach")), 1), false, new GristSet(new GristType[] {Amber, Caulk}, new int[] {3, 1}));

        //Misc.
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "hive")), 1, 0), true, new GristSet(new GristType[] {Build}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "hive")), 1, 2), true, new GristSet(new GristType[] {Build}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "honeycomb")), 1), false, new GristSet(new GristType[] {Build}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "filled_honeycomb")), 1), false, new GristSet(new GristType[] {Amber, Build}, new int[] {1, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "record_wanderer")), 1), false, new GristSet(new GristType[] {Build, Caulk, Quartz, Mercury}, new int[] {15, 8, 5, 5}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "jar_filled")), 1, 0), true, new GristSet(new GristType[] {Build, Amber}, new int[] {2, 8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "jar_filled")), 1, 1), true, new GristSet(new GristType[] {Build, Sulfur, Cobalt}, new int[] {2, 4, 4}));

        //Dyes
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "blue_dye")), 1), false, new GristSet(new GristType[] {Amethyst}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "black_dye")), 1), false, new GristSet(new GristType[] {Tar}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "brown_dye")), 1), false, new GristSet(new GristType[] {Amber, Iodine}, new int[] {1, 3}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "green_dye")), 1), false, new GristSet(new GristType[] {Amber, Iodine}, new int[] {3, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "white_dye")), 1), false, new GristSet(new GristType[] {Chalk}, new int[] {2}));

        //cold blocks
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "hard_ice"))), new GristSet(new GristType[] {Build, Cobalt}, new int[] {10, 6}));

        //warm blocks
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "ash"))), new GristSet(new GristType[] {Tar}, new int[] {1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "fleshchunk"))), new GristSet(new GristType[] {Iodine, Rust}, new int[] {1, 1}));

        //Gemstones
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "crystal_shard"))), new GristSet(new GristType[] {Amethyst, Tar}, new int[] {6, 4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 0), true, new GristSet(new GristType[] {Amethyst, Uranium}, new int[] {12, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 1), true, new GristSet(new GristType[] {Ruby}, new int[] {12}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 2), true, new GristSet(new GristType[] {Mercury, Rust}, new int[] {6, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 3), true, new GristSet(new GristType[] {Diamond, Amber}, new int[] {6, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 4), true, new GristSet(new GristType[] {Chalk, Amethyst}, new int[] {3, 9}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 5), true, new GristSet(new GristType[] {Amethyst, Quartz}, new int[] {6, 6}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 6), true, new GristSet(new GristType[] {Cobalt}, new int[] {12}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 7), true, new GristSet(new GristType[] {Amber}, new int[] {12}));

        //Alchemy Recipes
        CombinationRegistry.addCombination(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "brown_dye"))), new ItemStack(Items.WHEAT_SEEDS), MODE_OR, false, false, new ItemStack(Items.DYE, 1, 3));
        CombinationRegistry.addCombination(new ItemStack(Items.QUARTZ), new ItemStack(Items.DYE, 1, 4), MODE_OR, false, true, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "gem")), 1, 5));
        CombinationRegistry.addCombination(new ItemStack(Blocks.ICE), new ItemStack(Blocks.RED_FLOWER, 1, 1), MODE_AND, false, true, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 4));
        CombinationRegistry.addCombination(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "sapling_1")), 1, 0), new ItemStack(Blocks.RED_FLOWER, 1, 0), MODE_OR, true, true, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "flower_1")), 1, 5));
    }

    private static final void registerFutureMcRecipes()
    {
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_acacia_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_jungle_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_oak_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_birch_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_spruce_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "stripped_dark_oak_log"))), new GristSet(new GristType[] {Build}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "soul_soil"))), new GristSet(new GristType[] {Build, Caulk, Sulfur}, new int[] {4, 3, 2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "smooth_stone"))), new GristSet(new GristType[] {Build}, new int[] {2}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "smooth_sandstone"))), new GristSet(new GristType[] {Build}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "smooth_red_sandstone"))), new GristSet(new GristType[] {Build}, new int[] {4}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "honey_block"))), new GristSet(new GristType[] {Amber}, new int[] {32}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "honeycomb"))), new GristSet(new GristType[] {Amber}, new int[] {4}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "bell"))), new GristSet(new GristType[] {Gold}, new int[] {500}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "lily_of_the_valley"))), new GristSet(new GristType[] {Caulk, Iodine}, new int[] {3, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "cornflower"))), new GristSet(new GristType[] {Amethyst, Iodine}, new int[] {3, 1}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "wither_rose"))), new GristSet(new GristType[] {Tar}, new int[] {8}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "bamboo"))), new GristSet(new GristType[] {Build}, new int[] {1}));

        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "ancient_debris"))), new GristSet(new GristType[] {Build, Tar, Diamond}, new int[] {8, 18, 18}));
        GristRegistry.addGristConversion(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc", "netherite_scrap"))), new GristSet(new GristType[] {Tar, Diamond}, new int[] {18, 18}));
    }
}
