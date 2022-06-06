package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.captchalogue.OperandiBlockItem;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.item.block.ItemTransportalizer;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.lwjgl.Sys;

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

	public static Block uniqueObject = new MSUBlockBase(Material.CORAL, MapColor.DIAMOND, "unique_object", "uniqueObject");
	public static Block artifact = new BlockArtifact(Material.FIRE, MapColor.PURPLE, "artifact", "artifact");

	//Thaumcraft
    public static Block thaumChasis = new MSUBlockBase(Material.IRON, "thaumic_machine_frame", "thaumChasis");

    @SubscribeEvent
    public static void remapBlocks(RegistryEvent.MissingMappings<Block> event)
    {
    	event.getMappings().forEach(mapping ->
	    {
    		if(mapping.key.getResourcePath().equals("porkhollow_atm"))
    			mapping.remap(ceramicPorkhollow);

	    });
    }

	@SubscribeEvent
	public static void remapItems(RegistryEvent.MissingMappings<Item> event)
	{
		event.getMappings().forEach(mapping ->
		{
			if(mapping.key.getResourcePath().equals("porkhollow_atm"))
				mapping.remap(Item.getItemFromBlock(MinestuckUniverseBlocks.ceramicPorkhollow));

		});
	}

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registerBlock(registry, dungeonDoor);
        registerBlock(registry, dungeonDoorKeyhole);
		registerBlock(registry, dungeonShield);
		registerBlock(registry, flightBeacon);
		registerBlock(registry, flightInhibitor);

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
        {
        	registerBlock(registry, gristBlockVis);
        }
        
        if(MinestuckUniverse.isBotaniaLoaded)
			registerBlock(registry, gristBlockMana);

	    registerBlock(registry, artifact);
	    registerBlock(registry, uniqueObject);
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
