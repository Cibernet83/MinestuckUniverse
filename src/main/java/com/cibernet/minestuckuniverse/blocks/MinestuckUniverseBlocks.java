package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.items.ItemFraymachine;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.captchalogue.OperandiBlockItem;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.item.block.ItemTransportalizer;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;
import java.util.TreeMap;

@Mod.EventBusSubscriber(modid = MinestuckUniverse.MODID)
public class MinestuckUniverseBlocks
{
	public static final TreeMap<EnumDyeColor, BlockWoolTransportalizer> sleevedTransportalizers = new TreeMap<>();

	//Base
	public static Block dungeonDoor = new BlockDungeonDoor("dungeon_door", "dungeonDoor");
	public static Block dungeonDoorKeyhole = new BlockDungeonDoor("dungeon_door_keyhole", "dungeonDoorKeyhole");

    public static Block magicBlock = new MSUBlockBase(Material.CLAY, "magic_block", "magicBlock");
    public static Block wizardStatue = new BlockMSUDecor("wizardStatue", "wizard_statue");
    public static Block netherReactorCore = new MSUBlockBase(Material.ROCK, "nether_reactor_core", "netherReactorCore").setHardness(30.0F).setResistance(6);
	public static Block sbahjTree = new BlockSbahjTree("kringlefucker", "kringlefucker");
	public static Block fluoriteBlock = new MSUBlockBase(Material.ROCK, MapColor.LAPIS, "fluorite_block", "fluoriteBlock").setHardness(3.0F).setResistance(5.0F);
	public static Block fluoriteOre = new MSUBlockBase(Material.ROCK, "fluorite_ore", "fluoriteOre").setHardness(3.0F).setResistance(5.0F);
	public static Block moonstoneOre = new MSUBlockBase(Material.IRON, "moonstone_ore", "moonstoneOre").setHardness(3.0F).setResistance(5.0F);

    public static Block sbahjBedrock = new MSUBlockBase(Material.CAKE, "sbahj_bedrock", "sbahjBedrock").setResistance(0F).setHardness(-1F);
    public static Block zillyStone = new MSUBlockBase(Material.ROCK, "zillystone", "zillystone").setResistance(999F).setHardness(5.5F);
    public static Block smoothIron = new MSUBlockBase(Material.IRON, "smooth_iron", "smoothIron").setHardness(3.0F);
    public static Block bedrockStairs = new BlockMSUStairs(Blocks.BEDROCK.getDefaultState(), "bedrockStairs", "bedrock_stairs");
	public static final Block hardStone = new BlockHardStone("hard_stone", "hardStone").setCreativeTab(TabMinestuckUniverse.main);

	public static final Block operandiBlock = new OperandiBlock("operandi_block", 1.0f, 0, Material.GOURD, "");
	public static final Block operandiGlass = new OperandiGlassBlock("operandi_glass", 0.5f, 0, Material.GLASS, "");
	public static final Block operandiStone = new OperandiBlock("operandi_stone", 3.0f, 6.5f, Material.IRON, "pickaxe");
	public static final Block operandiLog = new OperandiLogBlock("operandi_log", 2.0f, 0, OperandiBlock.LOG, "axe");

    public static Block holopad = new BlockHolopad();
    public static Block fraymachine = new BlockFraymachine();
	
	public static Block machineChasis = new BlockMachineChasis();
    public static Block gristHopper = new BlockGristHopper();
    public static Block autoWidget = new BlockAutoWidget();
    public static Block autoCaptcha = new BlockAutoCaptcha();
    public static Block ceramicPorkhollow = new BlockCeramicPorkhollow();
    public static Block boondollarRegister = new BlockBoondollarRegister();

	public static BlockWoolTransportalizer whiteWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.WHITE);
	public static BlockWoolTransportalizer orangeWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.ORANGE);
	public static BlockWoolTransportalizer magentaWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.MAGENTA);
	public static BlockWoolTransportalizer lightBlueWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.LIGHT_BLUE);
	public static BlockWoolTransportalizer yellowWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.YELLOW);
	public static BlockWoolTransportalizer limeWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.LIME);
	public static BlockWoolTransportalizer pinkWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.PINK);
	public static BlockWoolTransportalizer grayWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.GRAY);
	public static BlockWoolTransportalizer silverWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.SILVER);
	public static BlockWoolTransportalizer cyanWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.CYAN);
	public static BlockWoolTransportalizer purpleWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.PURPLE);
	public static BlockWoolTransportalizer blueWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.BLUE);
	public static BlockWoolTransportalizer brownWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.BROWN);
	public static BlockWoolTransportalizer greenWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.GREEN);
	public static BlockWoolTransportalizer redWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.RED);
	public static BlockWoolTransportalizer blackWoolTransportalizer = new BlockWoolTransportalizer(EnumDyeColor.BLACK);
	public static Block rubyRedTransportalizer = new BlockRedTransportalizer();
	public static Block goldenTransportalizer = new BlockGoldTransportalizer();
	public static Block paradoxTransportalizer = new BlockParadoxTransportalizer();
	public static Block platinumTransportalizer = new BlockPlatinumTransportalizer();

	public static Block gristBlockAmber = new BlockGrist(GristType.Amber);
	public static Block gristBlockAmethyst = new BlockGrist(GristType.Amethyst);
	public static Block gristBlockArtifact = new BlockGrist(GristType.Artifact);
	public static Block gristBlockBuild = new BlockGrist(GristType.Build);
	public static Block gristBlockCaulk = new BlockGrist(GristType.Caulk);
	public static Block gristBlockChalk = new BlockGrist(GristType.Chalk);
	public static Block gristBlockCobalt = new BlockGrist(GristType.Cobalt);
	public static Block gristBlockDiamond = new BlockGrist(GristType.Diamond);
	public static Block gristBlockGarnet = new BlockGrist(GristType.Garnet);
	public static Block gristBlockGold = new BlockGrist(GristType.Gold);
	public static Block gristBlockIodine = new BlockGrist(GristType.Iodine);
	public static Block gristBlockMarble = new BlockGrist(GristType.Marble);
	public static Block gristBlockMercury = new BlockGrist(GristType.Mercury);
	public static Block gristBlockQuartz = new BlockGrist(GristType.Quartz);
	public static Block gristBlockRuby = new BlockGrist(GristType.Ruby);
	public static Block gristBlockRust = new BlockGrist(GristType.Rust);
	public static Block gristBlockShale = new BlockGrist(GristType.Shale);
	public static Block gristBlockSulfur = new BlockGrist(GristType.Sulfur);
	public static Block gristBlockTar = new BlockGrist(GristType.Tar);
	public static Block gristBlockUranium = new BlockGrist(GristType.Uranium);
	public static Block gristBlockZillium = new BlockGrist(GristType.Zillium);
	public static Block gristBlockVis = new BlockGrist(MinestuckUniverseGrist.Vis);
	public static Block gristBlockMana = new BlockGrist(MinestuckUniverseGrist.Mana);

	public static Block dungeonShield = new BlockEffectBeacon(MapColor.ADOBE, new PotionEffect(MSUPotions.CREATIVE_SHOCK, 40, 0), "dungeon_shield", "dungeonShield");
	public static Block flightBeacon = new BlockEffectBeacon(MapColor.ADOBE, new PotionEffect(MSUPotions.SKYHBOUND, 40, 0), "flight_beacon", "flightBeacon");
	public static Block flightInhibitor = new BlockEffectBeacon(MapColor.ADOBE, new PotionEffect(MSUPotions.EARTHBOUND, 40, 0), "flight_inhibitor", "flightInhibitor");
	public static Block badgeInhibitor = new BlockEffectBeacon(MapColor.ADOBE, new PotionEffect(MSUPotions.GOD_TIER_LOCK, 40, 2), "badge_inhibitor", "badgeInhibitor");


	public static final Block glorb = new BlockGlorb();
	public static final Block chloroball = new BlockChloroball();

	public static final Block prospitStone = new BlockLunarStone(MapColor.YELLOW, "prospitStone", "prospit_stone");
	public static final Block prospitSmoothstone = new BlockLunarStone(MapColor.YELLOW, "prospitSmoothstone", "prospit_smoothstone");
	public static final Block prospitBricks = new BlockLunarStone(MapColor.YELLOW, "prospitBricks", "prospit_bricks");
	public static final Block prospitBricksCracked = new BlockLunarStone(MapColor.YELLOW, "prospitBricksCracked", "cracked_prospit_bricks");
	public static final Block prospitBricksChiseled = new BlockLunarStone(MapColor.YELLOW, "prospitBricksChiseled", "chiseled_prospit_bricks");
	public static final Block prospitPillar = new BlockPillar(MapColor.YELLOW, "prospitPillar", "prospit_pillar");

	public static final BlockSlab prospitStoneSlab = new BlockMSUSlab.Half(MapColor.YELLOW, "prospitStoneSlab", "prospit_stone_slab");
	public static final BlockSlab prospitStoneDoubleSlab = new BlockMSUSlab.Double(MapColor.YELLOW, "prospitStoneSlab", "prospit_stone_slab_double", prospitStoneSlab);
	public static final BlockSlab prospitSmoothstoneSlab = new BlockMSUSlab.Half(MapColor.YELLOW, "prospitSmoothstoneSlab", "prospit_smoothstone_slab");
	public static final BlockSlab prospitSmoothstoneDoubleSlab = new BlockMSUSlab.Double(MapColor.YELLOW, "prospitSmoothstoneSlab", "prospit_smoothstone_slab_double", prospitSmoothstoneSlab);
	public static final BlockSlab prospitBricksSlab = new BlockMSUSlab.Half(MapColor.YELLOW, "prospitBricksSlab", "prospit_bricks_slab");
	public static final BlockSlab prospitBricksDoubleSlab = new BlockMSUSlab.Double(MapColor.YELLOW, "prospitBricksSlab", "prospit_bricks_slab_double", prospitBricksSlab);

	public static final Block prospitStoneStairs = new BlockMSUStairs(prospitStone.getDefaultState(), "prospitStoneStairs", "prospit_stone_stairs");
	public static final Block prospitBricksStairs = new BlockMSUStairs(prospitStone.getDefaultState(), "prospitBricksStairs", "prospit_bricks_stairs");

	public static final Block prospitStoneWall = new BlockMSUWall(MapColor.YELLOW, "prospitStoneWall", "prospit_stone_wall");
	public static final Block prospitBricksWall = new BlockMSUWall(MapColor.YELLOW, "prospitBricksWall", "prospit_bricks_wall");

	public static final Block derseStone = new BlockLunarStone(MapColor.PURPLE, "derseStone", "derse_stone");
	public static final Block derseSmoothstone = new BlockLunarStone(MapColor.PURPLE, "derseSmoothstone", "derse_smoothstone");
	public static final Block derseBricks = new BlockLunarStone(MapColor.PURPLE, "derseBricks", "derse_bricks");
	public static final Block derseBricksCracked = new BlockLunarStone(MapColor.PURPLE, "derseBricksCracked", "cracked_derse_bricks");
	public static final Block derseBricksChiseled = new BlockLunarStone(MapColor.PURPLE, "derseBricksChiseled", "chiseled_derse_bricks");
	public static final Block dersePillar = new BlockPillar(MapColor.PURPLE, "dersePillar", "derse_pillar");

	public static final BlockSlab derseStoneSlab = new BlockMSUSlab.Half(MapColor.PURPLE, "derseStoneSlab", "derse_stone_slab");
	public static final BlockSlab derseStoneDoubleSlab = new BlockMSUSlab.Double(MapColor.PURPLE, "derseStoneSlab", "derse_stone_slab_double", derseStoneSlab);
	public static final BlockSlab derseSmoothstoneSlab = new BlockMSUSlab.Half(MapColor.PURPLE, "derseSmoothstoneSlab", "derse_smoothstone_slab");
	public static final BlockSlab derseSmoothstoneDoubleSlab = new BlockMSUSlab.Double(MapColor.PURPLE, "derseSmoothstoneSlab", "derse_smoothstone_slab_double", derseSmoothstoneSlab);
	public static final BlockSlab derseBricksSlab = new BlockMSUSlab.Half(MapColor.PURPLE, "derseBricksSlab", "derse_bricks_slab");
	public static final BlockSlab derseBricksDoubleSlab = new BlockMSUSlab.Double(MapColor.PURPLE, "derseBricksSlab", "derse_bricks_slab_double", derseBricksSlab);

	public static final Block derseStoneStairs = new BlockMSUStairs(derseStone.getDefaultState(), "derseStoneStairs", "derse_stone_stairs");
	public static final Block derseBricksStairs = new BlockMSUStairs(derseStone.getDefaultState(), "derseBricksStairs", "derse_bricks_stairs");

	public static final Block derseStoneWall = new BlockMSUWall(MapColor.PURPLE, "derseStoneWall", "derse_stone_wall");
	public static final Block derseBricksWall = new BlockMSUWall(MapColor.PURPLE, "derseBricksWall", "derse_bricks_wall");
	
	public static Block uniqueObject = new MSUBlockBase(Material.CORAL, MapColor.DIAMOND, "unique_object", "uniqueObject");
	public static Block artifact = new BlockArtifact(Material.FIRE, MapColor.PURPLE, "artifact", "artifact");

	//God Tier

	public static final Map<EnumAspect, BlockHeroStone> heroStones = new TreeMap<>();
	public static final Map<EnumAspect, BlockHeroStone> chiseledHeroStones = new TreeMap<>();
	public static final Map<EnumAspect, BlockHeroStoneWall> heroStoneWalls = new TreeMap<>();
	public static final Map<EnumAspect, BlockSpectralHeroStone> spectralHeroStones = new TreeMap<>();

	public static final BlockHeroStone wildcardHeroStone = new BlockHeroStone(null, false);
	public static final BlockHeroStone wildcardChiseledHeroStone = new BlockHeroStone(null, true);
	public static final BlockHeroStoneWall wildcardHeroStoneWall = new BlockHeroStoneWall(null);
	public static final BlockSpectralHeroStone wildcardSpectralHeroStone = new BlockSpectralHeroStone(null);
	public static final Block glowingHeroStone = new BlockGlowingHeroStone();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registerBlock(registry, dungeonDoor);
        registerBlock(registry, dungeonDoorKeyhole);
		registerBlock(registry, dungeonShield);
		registerBlock(registry, flightBeacon);
		registerBlock(registry, flightInhibitor);
		registerBlock(registry, badgeInhibitor);

        registerBlock(registry, sbahjBedrock);
        registerBlock(registry, zillyStone);
        registerBlock(registry, bedrockStairs);

		registerBlock(registry, magicBlock);
        registerBlock(registry, smoothIron);
		registerBlock(registry, netherReactorCore);
		registerBlock(registry, wizardStatue);
		registerBlock(registry, sbahjTree);
		registerBlock(registry, hardStone);
		registerBlock(registry, fluoriteBlock);
		registerBlock(registry, fluoriteOre);
		registerBlock(registry, moonstoneOre);


		registerBlock(registry, operandiBlock, new OperandiBlockItem("operandi_block", MinestuckUniverseBlocks.operandiBlock));
		registerBlock(registry, operandiStone, new OperandiBlockItem("operandi_stone", MinestuckUniverseBlocks.operandiStone));
		registerBlock(registry, operandiLog, new OperandiBlockItem("operandi_log", MinestuckUniverseBlocks.operandiLog));
		registerBlock(registry, operandiGlass, new OperandiBlockItem("operandi_glass", MinestuckUniverseBlocks.operandiGlass));

        registerBlock(registry, holopad);
        registerBlock(registry, fraymachine, new ItemFraymachine());

        registerBlock(registry, machineChasis);
		registerBlock(registry, gristHopper);
		registerBlock(registry, autoWidget);
		registerBlock(registry, autoCaptcha);
		registerBlock(registry, ceramicPorkhollow, MinestuckUniverseItems.ceramicPorkhollow);
		registerBlock(registry, boondollarRegister);

		registerBlock(registry, whiteWoolTransportalizer, new ItemTransportalizer(whiteWoolTransportalizer));
		registerBlock(registry, orangeWoolTransportalizer, new ItemTransportalizer(orangeWoolTransportalizer));
		registerBlock(registry, magentaWoolTransportalizer, new ItemTransportalizer(magentaWoolTransportalizer));
		registerBlock(registry, lightBlueWoolTransportalizer, new ItemTransportalizer(lightBlueWoolTransportalizer));
		registerBlock(registry, yellowWoolTransportalizer, new ItemTransportalizer(yellowWoolTransportalizer));
		registerBlock(registry, limeWoolTransportalizer, new ItemTransportalizer(limeWoolTransportalizer));
		registerBlock(registry, pinkWoolTransportalizer, new ItemTransportalizer(pinkWoolTransportalizer));
		registerBlock(registry, grayWoolTransportalizer, new ItemTransportalizer(grayWoolTransportalizer));
		registerBlock(registry, silverWoolTransportalizer, new ItemTransportalizer(silverWoolTransportalizer));
		registerBlock(registry, cyanWoolTransportalizer, new ItemTransportalizer(cyanWoolTransportalizer));
		registerBlock(registry, purpleWoolTransportalizer, new ItemTransportalizer(purpleWoolTransportalizer));
		registerBlock(registry, blueWoolTransportalizer, new ItemTransportalizer(blueWoolTransportalizer));
		registerBlock(registry, brownWoolTransportalizer, new ItemTransportalizer(brownWoolTransportalizer));
		registerBlock(registry, greenWoolTransportalizer, new ItemTransportalizer(greenWoolTransportalizer));
		registerBlock(registry, redWoolTransportalizer, new ItemTransportalizer(redWoolTransportalizer));
		registerBlock(registry, blackWoolTransportalizer, new ItemTransportalizer(blackWoolTransportalizer));
		registerBlock(registry, rubyRedTransportalizer, new ItemTransportalizer(rubyRedTransportalizer));
		registerBlock(registry, goldenTransportalizer, new ItemTransportalizer(goldenTransportalizer));
		registerBlock(registry, paradoxTransportalizer, new ItemTransportalizer(paradoxTransportalizer));
		registerBlock(registry, platinumTransportalizer, new ItemTransportalizer(platinumTransportalizer));

		registerBlock(registry, gristBlockBuild);
		registerBlock(registry, gristBlockAmber);
		registerBlock(registry, gristBlockAmethyst);
		registerBlock(registry, gristBlockArtifact);
		registerBlock(registry, gristBlockCaulk);
		registerBlock(registry, gristBlockChalk);
		registerBlock(registry, gristBlockCobalt);
		registerBlock(registry, gristBlockDiamond);
		registerBlock(registry, gristBlockGarnet);
		registerBlock(registry, gristBlockGold);
		registerBlock(registry, gristBlockIodine);
		registerBlock(registry, gristBlockMarble);
		registerBlock(registry, gristBlockMercury);
		registerBlock(registry, gristBlockQuartz);
		registerBlock(registry, gristBlockRuby);
		registerBlock(registry, gristBlockRust);
		registerBlock(registry, gristBlockShale);
		registerBlock(registry, gristBlockSulfur);
		registerBlock(registry, gristBlockTar);
		registerBlock(registry, gristBlockUranium);
		registerBlock(registry, gristBlockZillium);

        if(MinestuckUniverse.isThaumLoaded)
        	registerBlock(registry, gristBlockVis);
        
        if(MinestuckUniverse.isBotaniaLoaded)
			registerBlock(registry, gristBlockMana);

	    registerBlock(registry, glorb, null);
	    registerBlock(registry, chloroball, null);

	    registerBlock(registry, prospitStone);
	    registerBlock(registry, prospitSmoothstone);
	    registerBlock(registry, prospitBricks);
	    registerBlock(registry, prospitBricksChiseled);
	    registerBlock(registry, prospitBricksCracked);
	    registerBlock(registry, prospitPillar);
	    registerBlock(registry, prospitStoneSlab);
	    registerBlock(registry, prospitStoneDoubleSlab, null);
	    registerBlock(registry, prospitStoneStairs);
	    registerBlock(registry, prospitSmoothstoneSlab);
	    registerBlock(registry, prospitSmoothstoneDoubleSlab, null);
	    registerBlock(registry, prospitBricksSlab);
	    registerBlock(registry, prospitBricksDoubleSlab, null);
	    registerBlock(registry, prospitBricksStairs);
	    registerBlock(registry, prospitStoneWall);
	    registerBlock(registry, prospitBricksWall);
	    
	    registerBlock(registry, derseStone);
	    registerBlock(registry, derseSmoothstone);
	    registerBlock(registry, derseBricks);
	    registerBlock(registry, derseBricksChiseled);
	    registerBlock(registry, derseBricksCracked);
	    registerBlock(registry, dersePillar);
	    registerBlock(registry, derseStoneSlab);
	    registerBlock(registry, derseStoneDoubleSlab, null);
	    registerBlock(registry, derseStoneStairs);
	    registerBlock(registry, derseSmoothstoneSlab);
	    registerBlock(registry, derseSmoothstoneDoubleSlab, null);
	    registerBlock(registry, derseBricksSlab);
	    registerBlock(registry, derseBricksDoubleSlab, null);
	    registerBlock(registry, derseBricksStairs);
	    registerBlock(registry, derseStoneWall);
	    registerBlock(registry, derseBricksWall);
        
	    registerBlock(registry, artifact);
	    registerBlock(registry, uniqueObject);

	    registerBlock(registry, glowingHeroStone);
	    registerBlock(registry, wildcardHeroStone);
	    registerBlock(registry, wildcardChiseledHeroStone);
	    registerBlock(registry, wildcardHeroStoneWall);
	    registerBlock(registry, wildcardSpectralHeroStone);

	    for(EnumAspect aspect : EnumAspect.values())
	    {
	    	heroStones.put(aspect, (BlockHeroStone) registerBlock(registry, new BlockHeroStone(aspect, false)));
	    	chiseledHeroStones.put(aspect, (BlockHeroStone) registerBlock(registry, new BlockHeroStone(aspect, true)));
	    	heroStoneWalls.put(aspect, (BlockHeroStoneWall) registerBlock(registry, new BlockHeroStoneWall(aspect)));
	    }
    }
    
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block)
    {
    	return registerBlock(registry, block, new ItemBlock(block));
    }
    
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, ItemBlock item)
    {
	    ((IRegistryItem)block).setRegistryName();
        registry.register(block);
        MSUModelManager.blocks.add(block);
        
        if(item != null)
			MinestuckUniverseItems.itemBlocks.add(item);
        
        return block;
    }
}
