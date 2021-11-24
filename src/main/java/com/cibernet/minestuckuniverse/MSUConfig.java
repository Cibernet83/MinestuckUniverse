package com.cibernet.minestuckuniverse;

import com.mraof.minestuck.util.Echeladder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

public class MSUConfig
{
	public static Configuration config;
	private static Side side;

	public static double zillystoneYields;
	public static double gristDropsMultiplier;
	public static int baseZillystoneLuck;
	public static boolean IDAlchemy;

	//Strife
	public static boolean combatOverhaul;
	public static boolean restrictedStrife;
	public static boolean keepPortfolioOnDeath;
	public static int abstrataSwitcherRung;
	public static int strifeDeckMaxSize;
	public static int strifeCardMobDrops;
	public static double weaponAttackMultiplier;

	public static void load(File file, Side sideIn)
	{
		MinecraftForge.EVENT_BUS.register(MSUConfig.class);

		side = sideIn;
		config = new Configuration(file, true);

		config.load();

		combatOverhaul = config.get("Strife", "combatOverhaul", true, "Enables the Strife Portfolio and overrides every Minestuck and Minestuck Universe weapon to better balance them. Other options in the Strife category will only take effect if this is set to true")
				.setRequiresMcRestart(true).setLanguageKey("config.minestuckuniverse.strife.combatOverhaul").getBoolean();

		loadConfigOptions(false);

		config.save();
	}

	private static void loadConfigOptions(boolean isRunning)
	{
		IDAlchemy = config.get("General", "IDAlchemy", true, "Enabling this makes the Totem Lathe and the Punch designix use Item IDs to determine a combination result if one doesn't exist already.")
				.setLanguageKey("config.minestuckuniverse.general.IDAlchemy").getBoolean();
		zillystoneYields = config.get("General", "zillystoneYields", 0.1, "Determines how much luck affects the amount of Zillystone Shards get dropped by a Zillystone when chiseled.")
				.setLanguageKey("config.minestuckuniverse.general.zillystoneYields").getDouble();
		baseZillystoneLuck = config.get("General", "baseZillystoneLuck", -2, "Determines a player's base luck when chiseling a block of Zillystone.")
				.setLanguageKey("config.minestuckuniverse.general.baseZillystoneLuck").getInt();
		gristDropsMultiplier = config.get("General", "gristDropsMultiplier", 1, "Determines how much grist is dropped from Underlings.")
				.setLanguageKey("config.minestuckuniverse.general.gristDropsMultiplier").getDouble();

		restrictedStrife = config.get("Strife", "restrictedStrife", false, "Prevents players from attacking without an allocated weapon in their main hand. It also restricts the use of certain items such as bows.")
				.setLanguageKey("config.minestuckuniverse.strife.restrictedStrife").getBoolean();
		keepPortfolioOnDeath = config.get("Strife", "keepPortfolioOnDeath", false, "Determines whether the player drops their Strife Portfolio after dying or not.")
				.setLanguageKey("config.minestuckuniverse.strife.keepPortfolioOnDeath").getBoolean();
		strifeCardMobDrops = config.get("Strife", "strifeCardMobDrops", 5, "Some mobs have a chance at dropping Strife Specibus Cards allocated to whatever item they're holding when killed by a player. This config determines how many cards each player can get from this method at most.")
				.setLanguageKey("config.minestuckuniverse.strife.strifeCardMobDrops").getInt();
		strifeDeckMaxSize = config.get("Strife", "strifeDeckMaxSize", 20, "Determines the max amount of weapons that can fit inside a single Strife Deck, set this to -1 to remove the limit.").setMinValue(-1)
				.setLanguageKey("config.minestuckuniverse.strife.strifeDeckMaxSize").getInt();
		abstrataSwitcherRung = config.get("Strife", "abstrataSwitcherRung", 17, "Determines the rung needed to unlock the Strife Specibus Quick Switcher. Set it to -1 to let all players use it, or " + Echeladder.RUNG_COUNT + " to completely disable it.").setMinValue(-1).setMaxValue(Echeladder.RUNG_COUNT)
				.setLanguageKey("config.minestuckuniverse.strife.abstrataSwitcherRung").getInt();
		weaponAttackMultiplier = config.get("Strife", "weaponAttackMultiplier", 0.15, "Allows players to tweak how much damage Minestuck and Minestuck Universe weapons do as a percentage against entities that aren't Underlings.").setMinValue(0).setMaxValue(1)
				.setLanguageKey("config.minestuckuniverse.strife.weaponAttackMultiplier").getDouble();


	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MinestuckUniverse.MODID))
		{
			loadConfigOptions(event.isWorldRunning());
			config.save();
		}

	}
}
