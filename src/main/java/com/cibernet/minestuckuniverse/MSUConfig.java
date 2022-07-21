package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
import com.mraof.minestuck.util.Echeladder;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Arrays;

public class MSUConfig
{
	public static Configuration config;
	private static Side side;


	public static String[] protectedEntities;
	public static String[] protectedTileEntities;
	public static String[] skaiaScrollBlacklist;
	public static String[] abilitechExternalUseBlacklist;
	public static String[] strifeCardMobDropsWhitelist;
	public static String[] IDAlchemyBlacklist;
	public static String[] restrictedStrifeBypass;

	public static double zillystoneYields;
	public static double gristDropsMultiplier;
	public static int baseZillystoneLuck;
	public static boolean IDAlchemy;
	public static boolean nullSoloSessions;
	public static boolean unstableArtifactSpread;
	public static boolean localizedChat;
	public static boolean keepSylladexOnDeath;

	//Strife
	public static boolean combatOverhaul;
	public static boolean restrictedStrife;
	public static boolean keepPortfolioOnDeath;
	public static int abstrataSwitcherRung;
	public static int strifeDeckMaxSize;
	public static int strifeCardMobDrops;
	public static double weaponAttackMultiplier;
	public static int skaiaScrollLimit;

	//God Tier
	public static int requiredRungToGT;
	public static int maxGodTier;
	public static boolean multiAspectUnlocks;
	public static int godTierXpThreshold;
	public static int questBedSpawnDistance;
	public static int questBedSpawnArea;
	public static int godTierBadgeSlots;
	public static boolean godTierMasterControl;

	//Cyberware compat
	public static int addedPowerPerDeath = 1500;
	public static int acceptablePower = 900;

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
		localizedChat = config.get("General", "localizedChat", false, "Enabling this makes players only be able to receive chat messages from nearby players unless Gift of Gab is enabled.")
				.setLanguageKey("config.minestuckuniverse.general.localizedChat").getBoolean();
		keepSylladexOnDeath = config.get("General", "keepSylladexOnDeath", false, "Enabling this prevents you from losing your sylladex upon death.")
				.setLanguageKey("config.minestuckuniverse.general.keepSylladexOnDeath").getBoolean();
		zillystoneYields = config.get("General", "zillystoneYields", 0.1, "Determines how much luck affects the amount of Zillystone Shards get dropped by a Zillystone when chiseled.")
				.setLanguageKey("config.minestuckuniverse.general.zillystoneYields").getDouble();
		baseZillystoneLuck = config.get("General", "baseZillystoneLuck", -2, "Determines a player's base luck when chiseling a block of Zillystone.")
				.setLanguageKey("config.minestuckuniverse.general.baseZillystoneLuck").getInt();
		gristDropsMultiplier = config.get("General", "gristDropsMultiplier", 1, "Determines how much grist is dropped from Underlings.")
				.setLanguageKey("config.minestuckuniverse.general.gristDropsMultiplier").getDouble();
		nullSoloSessions = config.get("General", "nullSoloSessions", false, "Whether solo sessions should use the null title and terrain aspects.")
				.setLanguageKey("config.minestuckuniverse.general.nullSoloSessions").getBoolean();
		unstableArtifactSpread = config.get("General", "unstableArtifactSpread", false, "Do not enable this while playing on any world you care about if you don't know what it does.")
				.setLanguageKey("config.minestuckuniverse.general.unstableArtifactSpread").getBoolean();
		skaiaScrollLimit = config.get("General", "skaiaScrollLimit", 2, "Determines the total number of Skaia Scrolls a player can use in total. Set to negative to ignore the limit.").setLanguageKey("config.minestuckuniverse.general.skaiaScrollLimit").getInt();
		protectedEntities = config.get("General", "protectedEntities", new String[0], "A list that determines what entities cannot be affected by certain powers in the game, such as Return to Dust and Captchaloguing.").setLanguageKey("config.minestuckuniverse.general.protectedEntities").getStringList();
		protectedTileEntities = config.get("General", "protectedTileEntities", new String[0], "A list that determines what tile entities cannot be affected by certain powers in the game, such as Wallet Modus Captchaloguing.").setLanguageKey("config.minestuckuniverse.general.protectedTileEntities").getStringList();
		skaiaScrollBlacklist = config.get("General", "skaiaScrollBlacklist", new String[0], "Prevents the included Abilitechs from spawning as Skaian Scrolls.").setLanguageKey("config.minestuckuniverse.general.skaianScrollBlacklist").getStringList();
		abilitechExternalUseBlacklist = config.get("General", "abilitechExternalUseBlacklist", new String[0], "Prevents the included Abilitechs from being used by mimicking Abilitechs such as Magic Metronome, Roguelike Adaptability and Arcane Study.").setLanguageKey("config.minestuckuniverse.general.abilitechExternalUseBlacklist").getStringList();
		IDAlchemyBlacklist = config.get("General", "IDAlchemyBlacklist", new String[0], "Prevents the included from being obtainable through ID Alchemy, yielding an Artifact block instead.").setLanguageKey("config.minestuckuniverse.general.IDAlchemyBlacklist").getStringList();

		restrictedStrife = config.get("Strife", "restrictedStrife", false, "Prevents players from attacking without an allocated weapon in their main hand. It also restricts the use of certain items such as bows.")
				.setLanguageKey("config.minestuckuniverse.strife.restrictedStrife").getBoolean();
		restrictedStrifeBypass = config.get("Strife", "restrictedStrifeBypass", new String[]
				{
						"minecraft:egg",
						"minecraft:snowball",
						"minecraft:ender_eye",
						"minecraft:ender_pearl",
						"minecraft:potion",
						"minecraft:experience_bottle",
						"minestuckuniverse:yarn_ball",
						"minestuckuniverse:eight_ball",
						"minestuckuniverse:wallet_ball",
						"botania:managun",
						"bibliocraft:bibliodrill",
				}, "Determines what items still have right-click functionality, even if Restricted Strife is enabled.").setLanguageKey("config.minestuckuniverse.general.strifeCardMobDropsWhitelist").getStringList();
		keepPortfolioOnDeath = config.get("Strife", "keepPortfolioOnDeath", false, "Determines whether the player drops their Strife Portfolio after dying or not.")
				.setLanguageKey("config.minestuckuniverse.strife.keepPortfolioOnDeath").getBoolean();
		strifeDeckMaxSize = config.get("Strife", "strifeDeckMaxSize", 20, "Determines the max amount of weapons that can fit inside a single Strife Deck, set this to -1 to remove the limit.").setMinValue(-1)
				.setLanguageKey("config.minestuckuniverse.strife.strifeDeckMaxSize").getInt();
		abstrataSwitcherRung = config.get("Strife", "abstrataSwitcherRung", 17, "Determines the rung needed to unlock the Strife Specibus Quick Switcher. Set it to -1 to let all players use it, or " + Echeladder.RUNG_COUNT + " to completely disable it.").setMinValue(-1).setMaxValue(Echeladder.RUNG_COUNT)
				.setLanguageKey("config.minestuckuniverse.strife.abstrataSwitcherRung").getInt();
		weaponAttackMultiplier = config.get("Strife", "weaponAttackMultiplier", 0.15, "Allows players to tweak how much damage Minestuck and Minestuck Universe weapons do as a percentage against entities that aren't Underlings.").setMinValue(0).setMaxValue(1)
				.setLanguageKey("config.minestuckuniverse.strife.weaponAttackMultiplier").getDouble();
		strifeCardMobDrops = config.get("Strife", "strifeCardMobDrops", 5, "Some mobs have a chance at dropping Strife Specibus Cards allocated to whatever item they're holding when killed by a player. This config determines how many cards each player can get from this method at most.")
				.setLanguageKey("config.minestuckuniverse.strife.strifeCardMobDrops").getInt();
		strifeCardMobDropsWhitelist = config.get("Strife", "strifeCardMobDropsWhitelist", new String[]
				{
						"minestuckuniverse:sword",
						"minestuckuniverse:hammer",
						"minestuckuniverse:club",
						"minestuckuniverse:cane",
						"minestuckuniverse:sickle",
						"minestuckuniverse:spoon",
						"minestuckuniverse:fork",
						"minestuckuniverse:potion",
						"minestuckuniverse:projectile",
						"minestuckuniverse:claw",
						"minestuckuniverse:glove",
						"minestuckuniverse:bow",
						"minestuckuniverse:shield",
							"minestuckuniverse:needle",
						"minestuckuniverse:rock",
						"minestuckuniverse:bunny",
						"minestuckuniverse:sbahj",
						"minestuckuniverse:joker"
				}, "Determines what Kind Abstrata can be dropped by killing underlings.").setLanguageKey("config.minestuckuniverse.general.strifeCardMobDropsWhitelist").getStringList();


		requiredRungToGT = config.get("God Tier", "requiredRungToGodTier", 20, "Determines the minimum rung required to God Tier, set to -1 to bypass god tiering requirements.")
				.setLanguageKey("config.minestuckuniverse.godtier.maxGodTier").getInt();
		maxGodTier = config.get("God Tier", "maxGodTier", -1, "Determines max god tier you can reach. When reached you cannot level up further. -1 makes it so that there is no limit. Warning: can prevent players from accessing certain badges if too low.")
				.setLanguageKey("config.minestuckuniverse.godtier.maxGodTier").getInt();
		multiAspectUnlocks = config.get("God Tier", "multiAspectUnlocks", true, "Enabling this makes certain skills require multiple kinds of Hero Stone Shards to unlock.")
				.setLanguageKey("config.minestuckuniverse.godtier.multiAspectUnlocks").getBoolean();
		godTierXpThreshold = config.get("God Tier", "godTierXpThreshold", 30, "Determines the minimum number of levels required to upgrade God Tier Skills")
				.setLanguageKey("config.minestuckuniverse.godtier.godTierXpThreshold").getInt();
		questBedSpawnDistance = config.get("God Tier", "questBedSpawnDistance", 2500, "Determines how far away the Quest Bed can spawn from the center of a player's land")
				.setLanguageKey("config.minestuckuniverse.godtier.questBedSpawnDistance").getInt();
		questBedSpawnArea = config.get("God Tier", "questBedSpawnArea", 2500, "Determines the size of the area of which the Quest Bed can spawn on a player's land")
				.setLanguageKey("config.minestuckuniverse.godtier.questBedSpawnArea").getInt();
		godTierBadgeSlots = config.get("God Tier", "godTierBadgeSlots", 7, "Determines how many Badge Slots God Tiered players start out with")
				.setLanguageKey("config.minestuckuniverse.godtier.godTierBadgeSlots").getInt();
		godTierMasterControl = config.get("God Tier", "godTierMasterControl", false, "Determines whether God Tiered players start with Master Control enabled, allowing them to unlock all skills regardless of their classpect")
				.setLanguageKey("config.minestuckuniverse.godtier.godTierMasterControl").getBoolean();

		if(MinestuckUniverse.isCyberwareLoaded)
		{
			addedPowerPerDeath = config.get("General", "addedPowerPerDeath", 1500, "Determines how much Cyberware power you recieve when you experience a non-true death.")
					.setLanguageKey("config.minestuckuniverse.godtier.cyberware.addedPowerPerDeath").getInt();
			acceptablePower = config.get("General", "acceptablePower", 900, "Determines the minimum amount of power you need before replacing vital cyber organs. Recommended to be higher than 500 but less than addedPowerPerDeath.")
					.setLanguageKey("config.minestuckuniverse.godtier.cyberware.acceptablePower").getInt();
		}
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

	public static void writeToBuffer(ByteBuf data)
	{
		data.writeBoolean(localizedChat);
		data.writeBoolean(multiAspectUnlocks);
		data.writeInt(godTierXpThreshold);
		data.writeBoolean(restrictedStrife);
		data.writeInt(skaiaScrollLimit);
		data.writeInt(maxGodTier);

		data.writeInt(addedPowerPerDeath);
		data.writeInt(acceptablePower);

		data.writeBoolean(keepSylladexOnDeath);
	}

	public static void readFromBuffer(ByteBuf data)
	{
		localizedChat = data.readBoolean();
		multiAspectUnlocks = data.readBoolean();
		godTierXpThreshold = data.readInt();
		restrictedStrife = data.readBoolean();
		skaiaScrollLimit = data.readInt();
		maxGodTier = data.readInt();

		addedPowerPerDeath = data.readInt();
		acceptablePower = data.readInt();

		keepPortfolioOnDeath = data.readBoolean();
	}
}
