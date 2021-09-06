package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MSUThrowableBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.weapons.ItemMechanicalCrossbow;
import com.cibernet.minestuckuniverse.items.weapons.MSUBowBase;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUKindAbstrata
{
	public static KindAbstratus hammerkind = new KindAbstratus("hammer", MinestuckUniverseItems.toolHammer);
	public static KindAbstratus bladekind = new KindAbstratus("sword", MinestuckUniverseItems.toolSword).addKeywords("sword", "blade");
	public static KindAbstratus clubkind = new KindAbstratus("club", MinestuckUniverseItems.toolClub).addKeywords("mace", "club");
	public static KindAbstratus canekind = new KindAbstratus("cane", MinestuckUniverseItems.toolCane).addKeywords("cane", "staff");
	public static KindAbstratus sicklekind = new KindAbstratus("sickle", MinestuckUniverseItems.toolSickle).addKeywords("sickle");
	public static KindAbstratus spoonkind = new KindAbstratus("spoon", MinestuckUniverseItems.toolSpoon).addKeywords("spoon");
	public static KindAbstratus forkkind = new KindAbstratus("fork", MinestuckUniverseItems.toolFork).addKeywords("fork");

	public static KindAbstratus pickaxekind = new KindAbstratus("pickaxe", MinestuckUniverseItems.toolPickaxe).addItemClasses(ItemPickaxe.class);
	public static KindAbstratus axekind = new KindAbstratus("axe", MinestuckUniverseItems.toolAxe).addItemClasses(ItemAxe.class);
	public static KindAbstratus shovelkind = new KindAbstratus("shovel", MinestuckUniverseItems.toolShovel).addItemClasses(ItemSpade.class);
	public static KindAbstratus hoekind = new KindAbstratus("hoe").addItemClasses(ItemHoe.class);
	public static KindAbstratus fshngrodkind = new KindAbstratus("fishingRod").addItemClasses(ItemFishingRod.class);
	public static KindAbstratus throwkind = new KindAbstratus("projectile").addItemClasses(MSUThrowableBase.class).addItemTools(
					MinestuckUniverseItems.dragonCharge, Items.SNOWBALL, Items.EGG, Items.ENDER_PEARL, Items.ENDER_EYE, Items.EXPERIENCE_BOTTLE, Items.SPLASH_POTION, Items.LINGERING_POTION,
					getItem("botania", "chakram"));

	public static KindAbstratus rockkind = new KindAbstratus("rock").addKeywords("rock").addItemTools(MinestuckUniverseItems.pebble, MinestuckUniverseItems.rock, MinestuckUniverseItems.bigRock,
			Item.getItemFromBlock(Blocks.COBBLESTONE), Item.getItemFromBlock(Blocks.STONE), Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE));
	public static KindAbstratus paperkind = new KindAbstratus("paper").addKeywords("paper").addItemTools(Items.PAPER, Items.MAP, Items.FILLED_MAP, MinestuckUniverseItems.rolledUpPaper, MinestuckUniverseItems.yesterdaysNews);
	public static KindAbstratus scissorkind = new KindAbstratus("shears").addKeywords("scissor").addItemClasses(ItemShears.class);

	public static KindAbstratus clawkind = new KindAbstratus("claw", MinestuckUniverseItems.toolClaws).addKeywords("katar");
	public static KindAbstratus glovekind = new KindAbstratus("glove", MinestuckUniverseItems.toolGauntlet).addKeywords("glove", "gauntlet", "fist").addItemTools(getItem("thaumcraft", "caster_basic"));
	public static KindAbstratus needlekind = new KindAbstratus("needles", MinestuckUniverseItems.toolNeedles);
	public static KindAbstratus shieldkind = new KindAbstratus("shield").setConditional((i, itemStack) -> i.isShield(itemStack, null));
	public static KindAbstratus bowkind = new KindAbstratus("bow").addItemClasses(ItemBow.class, MSUBowBase.class, ItemMechanicalCrossbow.class);
	public static KindAbstratus dicekind = new KindAbstratus("dice").addItemTools(MinestuckUniverseItems.dice, MinestuckUniverseItems.fluoriteOctet);

	public static KindAbstratus pistolkind = new KindAbstratus("pistol").addItemTools(getItem("botania", "managun"));
	public static KindAbstratus riflekind = new KindAbstratus("rifle");

	public static KindAbstratus fistkind = new KindAbstratus("fist").setFist(true);
	public static KindAbstratus jokerkind = new KindAbstratus("joker").setConditional((i, stack) -> !stack.isEmpty()).setHidden(true);
	public static KindAbstratus sbahjkind = new KindAbstratus("sbahj")
			.addItemTools(MinestuckUniverseItems.sbahjWhip, MinestuckUniverseItems.unrealAir, MinestuckItems.batleacks, MinestuckItems.sord, MinestuckItems.sbahjPoster,
					Item.getItemFromBlock(MinestuckUniverseBlocks.sbahjBedrock), Item.getItemFromBlock(MinestuckUniverseBlocks.sbahjTree))
			.setConditional((i, stack) -> i.getRegistryName().getResourcePath().equals("sbahjaddon")).setHidden(true);
	public static KindAbstratus bunnykind = new KindAbstratus("bunny").addKeywords("bunny", "rabbit", "hare")
			.addItemTools(MinestuckUniverseItems.bunnySlippers, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_FOOT, Items.RABBIT_HIDE, Items.RABBIT_STEW).setHidden(true);

	//api abstrata
	public static KindAbstratus tridentkind = new KindAbstratus("trident").addKeywords("trident");
	public static KindAbstratus doubleTridentkind = new KindAbstratus("2x3dent");
	public static KindAbstratus lancekind = new KindAbstratus("lance").addKeywords("lance");
	public static KindAbstratus spearkind = new KindAbstratus("spear").addKeywords("spear");
	public static KindAbstratus drillkind = new KindAbstratus("drill").addItemTools(getItem("actuallyadditions", "item_drill"), getItem("industrialforegoing", "infinity_drill"));
	public static KindAbstratus chainsawkind = new KindAbstratus("chainsaw").addKeywords("chainsaw");
	public static KindAbstratus makeupkind = new KindAbstratus("makeup").addKeywords("lipstick", "lip_stick");
	public static KindAbstratus umbrellakind = new KindAbstratus("umbrella").addKeywords("umbrella");
	public static KindAbstratus flshlghtkind = new KindAbstratus("flashlight").addKeywords("flashlight");
	public static KindAbstratus fncysntatkind = new KindAbstratus("fancySanta").setHidden(true);
	public static KindAbstratus batonkind = new KindAbstratus("baton").addKeywords("baton");
	public static KindAbstratus knifekind = new KindAbstratus("knife").addKeywords("knife", "dagger", "katar").addItemTools(getItem("actiallyadditions", "item_knife"), getItem("cfm", "item_knife"),
			getItem("mysticalworld", "amethyst_knife"), getItem("mysticalworld", "copper_knife"), getItem("mysticalworld", "silver_knife"), getItem("mysticalworld", "wood_knife"),
			getItem("mysticalworld", "stone_knife"), getItem("mysticalworld", "iron_knife"), getItem("mysticalworld", "diamond_knife"), getItem("mysticalworld", "gold_knife"));
	public static KindAbstratus keykind = new KindAbstratus("key").addItemTools(getItem("locks", "key"), getItem("locks", "key_blank"),
			getItem("locks", "key_ring"), getItem("locks", "master_key"),
			getItem("randomthings", "spectrekey"), getItem("randomthings", "portkey"), getItem("tombstone", "grave_key"));
	public static KindAbstratus wandkind = new KindAbstratus("wand").addKeywords("wand").addItemTools(
			getItem("botania", "twigwand"), getItem("botania", "dirtrod"), getItem("botania", "skydirtrod"), getItem("botania", "cobblerod"),
			getItem("botania", "terraformrod"), getItem("botania", "waterrod"), getItem("botania", "rainbowrod"), getItem("botania", "tornadorod"),
			getItem("botania", "firerod"), getItem("botania", "smeltrod"), getItem("botania", "exchangerod"), getItem("botania", "diviningrod"),
			getItem("botania", "gravityrod"), getItem("botania", "missilerod"), getItem("thaumcraft", "pech_wand"), getItem("thaumcraft", "primal_crusher"),
			getItem("armourers_workshop", "item.wand-of-style"), getItem("betterbuilderswands", "wandstone"), getItem("betterbuilderswands", "wandstone"),
			getItem("betterbuilderswands", "wandiron"), getItem("betterbuilderswands", "wanddiamond"), getItem("betterbuilderswands", "wandunbreakable"),
			getItem("chisel", "offsettool"), getItem("customnpcs", "npcwand"), getItem("cyclicmagic", "cyclic_wand_build"), getItem("cyclicmagic", "tool_swap"),
			getItem("cyclicmagic", "tool_swap_match"), getItem("cyclicmagic", "tool_push"), getItem("roots", "staff"));

	@SubscribeEvent
	public static void register(RegistryEvent.Register<KindAbstratus> event)
	{
		IForgeRegistry<KindAbstratus> registry = event.getRegistry();

		registry.register(hammerkind.setRegistryName("hammer"));
		registry.register(bladekind.setRegistryName("sword"));
		registry.register(clubkind.setRegistryName("club"));
		registry.register(canekind.setRegistryName("cane"));
		registry.register(sicklekind.setRegistryName("sickle"));
		registry.register(spoonkind.setRegistryName("spoon"));
		registry.register(forkkind.setRegistryName("fork"));

		registry.register(pickaxekind.setRegistryName("pickaxe"));
		registry.register(axekind.setRegistryName("axe"));
		registry.register(shovelkind.setRegistryName("shovel"));
		registry.register(hoekind.setRegistryName("hoe"));
		registry.register(fshngrodkind.setRegistryName("fishing_rod"));
		registry.register(throwkind.setRegistryName("projectile"));

		registry.register(rockkind.setRegistryName("rock"));
		registry.register(paperkind.setRegistryName("paper"));
		registry.register(scissorkind.setRegistryName("shears"));

		registry.register(clawkind.setRegistryName("claw"));
		registry.register(glovekind.setRegistryName("gauntlet"));
		registry.register(needlekind.setRegistryName("needles"));
		registry.register(shieldkind.setRegistryName("shield"));
		registry.register(bowkind.setRegistryName("bow"));
		registry.register(dicekind.setRegistryName("dice"));
		registry.register(pistolkind.setRegistryName("pistol"));
		registry.register(riflekind.setRegistryName("rifle"));

		registry.register(fistkind.setRegistryName("fist"));
		registry.register(jokerkind.setRegistryName("joker"));
		registry.register(sbahjkind.setRegistryName("sbahj"));
		registry.register(bunnykind.setRegistryName("bunny"));

		registry.register(wandkind.setRegistryName("wand"));
		registry.register(keykind.setRegistryName("key"));
		registry.register(lancekind.setRegistryName("lance"));
		registry.register(spearkind.setRegistryName("spear"));
		registry.register(drillkind.setRegistryName("drill"));
		registry.register(chainsawkind.setRegistryName("chainsaw"));
		registry.register(makeupkind.setRegistryName("makeup"));
		registry.register(knifekind.setRegistryName("knife"));
		registry.register(batonkind.setRegistryName("baton"));
		registry.register(flshlghtkind.setRegistryName("flashlight"));
		registry.register(fncysntatkind.setRegistryName("fancy_santa"));
		registry.register(umbrellakind.setRegistryName("umbrella"));
		registry.register(tridentkind.setRegistryName("tridentkind"));
		registry.register(doubleTridentkind.setRegistryName("2x3dent"));

		if(MinestuckUniverse.isArsenalLoaded)
			registerArsenalApi();

		keykind.setHidden(keykind.getToolItems().isEmpty());
		keykind.addItemTools(MinestuckUniverseItems.dungeonKey);
		wandkind.setHidden(wandkind.getToolItems().isEmpty());
		wandkind.addItemTools(MinestuckUniverseItems.staffOfOvergrowth);
		knifekind.setHidden(knifekind.getToolItems().isEmpty());
		knifekind.addItemTools(MinestuckUniverseItems.katars, MinestuckUniverseItems.rocketKatars, MinestuckUniverseItems.sneakyDaggers, MinestuckUniverseItems.blizzardCutters, MinestuckUniverseItems.katarsOfZillywhomst);
	}

	protected static void registerArsenalApi()
	{
		//TODO

		sbahjkind.addItemTools(getItem("minestuckarsenal", "sbahjifier"), getItem("minestuckarsenal", "aks"));
		lancekind.addItemTools(getItem("minestuckarsenal", "cigarette_lance"), getItem("minestuckarsenal", "jousting_lance"), getItem("minestuckarsenal", "rocketpop_lance"),
				getItem("minestuckarsenal", "fiduspawn_lance"));
		keykind.addItemTools(getItem("minestuckarsenal", "keyblade"), getItem("minestuckarsenal", "candy_key"), getItem("minestuckarsenal", "yaldabaoths_keyton"),
				getItem("minestuckarsenal", "chronolatch"), getItem("minestuckarsenal", "key_axe"), getItem("minestuckarsenal", "stone_cold_key_axe"),
				getItem("minestuckarsenal", "allweddol"));

	}

	protected static void registerVariedCommoditiesApi()
	{
		//TODO
	}


	public static Item getItem(String modid, String regName)
	{
		return Item.REGISTRY.getObject(new ResourceLocation(modid, regName));
	}
}
