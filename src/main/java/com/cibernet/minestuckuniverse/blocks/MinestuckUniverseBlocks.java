package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.TreeMap;

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

    public static Block holopad = new BlockHolopad();
	
	public static Block machineChasis = new BlockMachineChasis();
    public static Block gristHopper = new BlockGristHopper();
    public static Block autoWidget = new BlockAutoWidget();
    public static Block autoCaptcha = new BlockAutoCaptcha();
    public static Block porkhollowAtm = new BlockPorkhollowAtm();
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

	public static Block uniqueObject = new MSUBlockBase(Material.CORAL, MapColor.DIAMOND, "unique_object", "uniqueObject");
	public static Block artifact = new BlockArtifact(Material.FIRE, MapColor.PURPLE, "artifact", "artifact");

	//Thaumcraft
    public static Block thaumChasis = new MSUBlockBase(Material.IRON, "thaumic_machine_frame", "thaumChasis");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registerBlock(registry, dungeonDoor, true);
        registerBlock(registry, dungeonDoorKeyhole, true);
		registerBlock(registry, dungeonShield, true);
		registerBlock(registry, flightBeacon, true);
		registerBlock(registry, flightInhibitor, true);

        registerBlock(registry, sbahjBedrock, true);
        registerBlock(registry, zillyStone, true);
        registerBlock(registry, bedrockStairs, true);

		registerBlock(registry, magicBlock, true);
        registerBlock(registry, smoothIron, true);
		registerBlock(registry, netherReactorCore, true);
		registerBlock(registry, wizardStatue, true);
		registerBlock(registry, sbahjTree, true);
		registerBlock(registry, fluoriteBlock, true);
		registerBlock(registry, fluoriteOre, true);
		registerBlock(registry, moonstoneOre, true);

        registerBlock(registry, holopad, true);
        
        registerBlock(registry, machineChasis, true);
		registerBlock(registry, gristHopper, true);
		registerBlock(registry, autoWidget, true);
		registerBlock(registry, autoCaptcha, true);
		registerBlock(registry, porkhollowAtm, true);
		registerBlock(registry, boondollarRegister, true);

		registerBlock(registry, whiteWoolTransportalizer, true);
		registerBlock(registry, orangeWoolTransportalizer, true);
		registerBlock(registry, magentaWoolTransportalizer, true);
		registerBlock(registry, lightBlueWoolTransportalizer, true);
		registerBlock(registry, yellowWoolTransportalizer, true);
		registerBlock(registry, limeWoolTransportalizer, true);
		registerBlock(registry, pinkWoolTransportalizer, true);
		registerBlock(registry, grayWoolTransportalizer, true);
		registerBlock(registry, silverWoolTransportalizer, true);
		registerBlock(registry, cyanWoolTransportalizer, true);
		registerBlock(registry, purpleWoolTransportalizer, true);
		registerBlock(registry, blueWoolTransportalizer, true);
		registerBlock(registry, brownWoolTransportalizer, true);
		registerBlock(registry, greenWoolTransportalizer, true);
		registerBlock(registry, redWoolTransportalizer, true);
		registerBlock(registry, blackWoolTransportalizer, true);
		registerBlock(registry, rubyRedTransportalizer, true);
		registerBlock(registry, goldenTransportalizer, true);
		registerBlock(registry, paradoxTransportalizer, true);
		registerBlock(registry, platinumTransportalizer, true);

		registerBlock(registry, gristBlockBuild, true);
		registerBlock(registry, gristBlockAmber, true);
		registerBlock(registry, gristBlockAmethyst, true);
		registerBlock(registry, gristBlockArtifact, true);
		registerBlock(registry, gristBlockCaulk, true);
		registerBlock(registry, gristBlockChalk, true);
		registerBlock(registry, gristBlockCobalt, true);
		registerBlock(registry, gristBlockDiamond, true);
		registerBlock(registry, gristBlockGarnet, true);
		registerBlock(registry, gristBlockGold, true);
		registerBlock(registry, gristBlockIodine, true);
		registerBlock(registry, gristBlockMarble, true);
		registerBlock(registry, gristBlockMercury, true);
		registerBlock(registry, gristBlockQuartz, true);
		registerBlock(registry, gristBlockRuby, true);
		registerBlock(registry, gristBlockRust, true);
		registerBlock(registry, gristBlockShale, true);
		registerBlock(registry, gristBlockSulfur, true);
		registerBlock(registry, gristBlockTar, true);
		registerBlock(registry, gristBlockUranium, true);
		registerBlock(registry, gristBlockZillium, true);

        if(MinestuckUniverse.isThaumLoaded)
        {
        	registerBlock(registry, gristBlockVis, true);
        }
        
        if(MinestuckUniverse.isBotaniaLoaded)
			registerBlock(registry, gristBlockMana, true);

	    registerBlock(registry, artifact, true);
	    registerBlock(registry, uniqueObject, true);
    }
    
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, boolean hasItem)
    {
	    ((IRegistryItem)block).setRegistryName();
        registry.register(block);
        MSUModelManager.blocks.add(block);
        
        if(hasItem)
			MinestuckUniverseItems.itemBlocks.add(block);
        
        return block;
    }
}
