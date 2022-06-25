package com.cibernet.minestuckuniverse.skills;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.skills.badges.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspectUtil.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroClass.*;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MinestuckUniverse.MODID)
public class MSUSkills
{
	public static IForgeRegistry<Skill> REGISTRY;

	public static final MasterBadge MASTER_BADGE_MIGHTY = new MasterBadge("masterBadgeMighty", 3, 80, 0.4f, 40);
	public static final MasterBadge MASTER_BADGE_BRAVE = new MasterBadge("masterBadgeBrave", 3, 80, 0.2f, 40);
	public static final MasterBadge MASTER_BADGE_WISE = new MasterBadge("masterBadgeWise", 3, 80, 0.4f, 60);

	public static final Badge GIFT_OF_GAB = new BadgeConsort("giftOfGab", 2);
	public static final Badge SKELETON_KEY = new BadgeLevel("skeletonKey", 3)
	{
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			List<Entity> entityList = world.getEntitiesWithinAABB(EntitySkeleton.class, new AxisAlignedBB(player.getPosition()).grow(10));

			boolean canUnlock = !entityList.isEmpty();

			canUnlock = canUnlock && Badge.findItem(player, new ItemStack(MinestuckUniverseItems.chastityKey, 16), false);


			if(canUnlock)
			{
				Badge.findItem(player, new ItemStack(MinestuckUniverseItems.chastityKey, 16), true);

				Entity skeleton = entityList.get(0);
				((WorldServer)player.world).spawnParticle(EnumParticleTypes.TOTEM, skeleton.posX, skeleton.posY+0.25, skeleton.posZ, 30, 1, 0, 0, 0.2);
				skeleton.setDead();

				return true;
			}
			return false;
		}
	};
	public static final Badge PATCH_OF_THE_HOARDER = new BadgeLevel("patchOfTheHoarder", 3)
	{
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			GristSet cost = new GristSet(GristType.Shale, 5000);
			if(Badge.findItem(player, new ItemStack(MinestuckItems.captchaCard, 256), false) && GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost))
			{
				Badge.findItem(player, new ItemStack(MinestuckItems.captchaCard, 256), true);

				IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
				GristHelper.decrease(pid, cost);
				MinestuckPlayerTracker.updateGristCache(pid);
				return true;
			}
			return false;
		}

		@Override
		public boolean canDisable() {
			return false;
		}
	};

	public static final Badge HOARD_OF_THE_ALCHEMIZER = new BadgeLevel("hoardOfTheAlchemizer", 4)
	{
		private final int REQ = 2000;

		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			for(GristType type : GristType.REGISTRY.getValuesCollection())
				if(!GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), new GristSet(type, REQ)))
					return false;

			IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
			for(GristType type : GristType.REGISTRY.getValuesCollection())
				if(type.getRegistryName().getResourceDomain().equals(Minestuck.MOD_ID))
					GristHelper.decrease(pid, new GristSet(type, REQ));
			MinestuckPlayerTracker.updateGristCache(pid);
			return true;

		}

		@Override
		public void onBadgeUnlocked(World world, EntityPlayer player)
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.REQUEST_GRIST_HOARD), player);
		}

		@Override
		public String getUnlockRequirements() {
			return new TextComponentTranslation(getUnlocalizedName()+".unlock", REQ).getUnformattedText();
		}

		@Override
		public String getDisplayTooltip()
		{
			String str = new TextComponentTranslation(getUnlocalizedName()+".tooltip.any").getFormattedText();

			try
			{
				return new TextComponentTranslation(getUnlocalizedName()+".tooltip", hasBadge() ? new TextComponentTranslation("grist.format", getGristHoard().getDisplayName()).getFormattedText() : str).getFormattedText();
			} catch (NoClassDefFoundError error)
			{
				return new TextComponentTranslation(getUnlocalizedName()+".tooltip", str).getUnformattedText();
			}

		}

		@SideOnly(Side.CLIENT)
		private boolean hasBadge()
		{
			return Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).hasSkill(this);
		}
		@SideOnly(Side.CLIENT)
		private GristType getGristHoard()
		{
			return Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getGristHoard();
		}
	};

	public static final Badge BUILDER_BADGE = new BadgeBuilder();
	public static final Badge STRIFE_BADGE = new BadgeLevel("strifeBadge", 7)
	{
		private final ArrayList<Item> ZILLY_WEAPONS = new ArrayList<Item>()
		{{
			add(MinestuckItems.zillyhooHammer);
			add(MinestuckItems.zillywairCutlass);
			add(MinestuckUniverseItems.battleaxeOfZillywahoo);
			add(MinestuckUniverseItems.battlepickOfZillydew);
			add(MinestuckUniverseItems.battlesporkOfZillywut);
			add(MinestuckUniverseItems.gauntletOfZillywenn);
			add(MinestuckUniverseItems.katarsOfZillywhomst);
			add(MinestuckUniverseItems.scepterOfZillywuud);
			add(MinestuckUniverseItems.thistlesOfZillywitch);
		}};
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			if(!Badge.findItem(player, new ItemStack(MinestuckUniverseItems.strifeCard, 2), false))
				return false;

			ArrayList<Item> weaponsUsed = new ArrayList<>();
			for(Item i : ZILLY_WEAPONS)
				if(weaponsUsed.size() <= 5)
				{
					if(Badge.findItem(player, new ItemStack(i), false))
						weaponsUsed.add(i);
				}
			else break;

			if(weaponsUsed.size() < 5)
				return false;

			Badge.findItem(player, new ItemStack(MinestuckUniverseItems.strifeCard, 2), true);
			for(Item i : weaponsUsed)
				Badge.findItem(player, new ItemStack(i), true);

			return super.canUnlock(world, player);
		}

		@Override
		public boolean canAppearOnList(World world, EntityPlayer player) {
			return MSUConfig.restrictedStrife;
		}
	};


	public static final Badge REVENANTS_RETALIATION = new BadgeLevel("revenantsRetaliation", 4)
	{
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			GristSet cost = new GristSet(GristType.Diamond, 10000);
			List<Entity> entityList = world.getEntitiesWithinAABB(EntityCreeper.class, new AxisAlignedBB(player.getPosition()).grow(10));

			if(GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost) && !entityList.isEmpty())
			{
				Entity creeper = entityList.get(0);
				((WorldServer)player.world).spawnParticle(EnumParticleTypes.TOTEM, creeper.posX, creeper.posY+0.25, creeper.posZ, 30, 1, 0, 0, 0.2);
				creeper.setDead();
				IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
				GristHelper.decrease(pid, cost);
				MinestuckPlayerTracker.updateGristCache(pid);

				return true;
			}
			return false;
		}
	};

	public static final Badge EFFECT_BUFF = new BadgeLevel("effectBuff", 4)
	{
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			GristSet cost = new GristSet(GristType.Quartz, 5000);
			List<EntityFrog> entityList = world.getEntitiesWithinAABB(EntityFrog.class, new AxisAlignedBB(player.getPosition()).grow(10), (fog) -> fog.getType() == 6);

			if(GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost) && entityList.size() >= 5)
			{
				for(int i = 0; i < 5; i++)
				{
					Entity frog = entityList.get(i);
					((WorldServer)player.world).spawnParticle(EnumParticleTypes.TOTEM, frog.posX, frog.posY+0.25, frog.posZ, 30, 1, 0, 0, 0.2);
					frog.setDead();
				}
				IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
				GristHelper.decrease(pid, cost);
				MinestuckPlayerTracker.updateGristCache(pid);

				return true;
			}
			return false;
		}
	};
	public static final Badge KARMA = new BadgeLevel("karma", 5)
	{
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			GristSet cost = new GristSet(GristType.Gold, 8000);
			if(Badge.findItem(player, new ItemStack(MinestuckUniverseItems.moonstone, 128), false) && GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost))
			{
				Badge.findItem(player, new ItemStack(MinestuckUniverseItems.moonstone, 128), true);

				IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
				GristHelper.decrease(pid, cost);
				MinestuckPlayerTracker.updateGristCache(pid);
				return true;
			}
			return false;
		}
	};

	public static final Abilitech KNIGHT_WARD = new TechKnightWard("knight_ward");
	public static final Abilitech GUARDIAN_HALT = new TechKnightWard("guardian_halt");
	public static final Abilitech SYLPH_MEND = new TechSylph("sylph_mend");
	public static final Abilitech PRINCE_WRATH = new TechPrince("prince_wrath");
	public static final Abilitech WITCH_INHIBITION = new TechWitch("witch_inhibition");
	public static final Abilitech THIEF_FILCH = new TechThief("thief_filch");
	public static final Abilitech MAGE_AWARENESS = new TechMage("mage_awareness");

	public static final Abilitech SEER_PREDICTION = new TechSeer("seer_prediction");
	public static final Abilitech ROGUE_CONTRIBUTION = new TechRogue("rogue_contribution");
	public static final Abilitech BARD_DISSONANCE = new TechBard("bard_dissonance");
	public static final Abilitech MAID_FAVOR = new TechMaid("maid_favor");
	public static final Abilitech HEIR_WILL = new TechHeir("heir_will");

	public static final Abilitech MUSE_REQUIEM = new TechMuse("muse_requiem");
	public static final Abilitech LORD_DECREE = new TechLord("lord_decree");

	public static final Abilitech SPACE_WORMHOLE_TROTTER = new TechSpaceTele("wormhole_trotter");
	public static final Abilitech SPACE_ANCHORED_WORMHOLE = new TechSpaceAnchoredTele("anchored_wormhole");
	public static final Abilitech SPACE_SPATIAL_WARP = new TechSpaceTargetTele("spatial_warp");
	public static final Abilitech SPACE_MATTER_MANIPULATOR = new TechSpaceManipulator("matter_manipulator");
	public static final Abilitech SPACE_SPATIAL_MANIPULATOR = new TechSpaceResize("spatial_manipulator");

	public static final Abilitech TIME_TEMPORAL_RECALL = new TechTimeRecall("temporal_recall");
	public static final Abilitech TIME_CHRONOFREEZE = new TechTimeStop("chronofreeze");
	public static final Abilitech TIME_FLOW_ACCELERATOR = new TechTimeAcceleration("flow_accelerator");

	public static final Abilitech BREATH_TEMPESTING_ASCENSION = new TechBreathGale("tempesting_ascension");
	public static final Abilitech BREATH_WINDSWEEPING_TYPHOON = new TechBreathKnockback("windsweeping_typhoon");
	public static final Abilitech BREATH_VESSEL_OF_THE_WIND = new TechBreathWindVessel("vessel_of_the_wind");

	public static final Abilitech LIGHT_STORM_OF_THE_STRIKER = new TechLightStriker("storm_of_the_striker");
	public static final Abilitech LIGHT_LIGHTBOUND_WISDOM = new TechLightGlowing("lightbound_wisdom");
	public static final Abilitech LIGHT_ORB_OF_LIGHT = new TechLightGlorb("orb_of_light");
	public static final Abilitech LIGHT_ETERNAL_GLOW = new TechLightAutoGlorb("eternal_glow");

	public static final Abilitech HEART_SOUL_SWITCHER = new TechHeartSoulSwitcher("soul_switcher");

	public static final Abilitech DOOM_TERMINAL_DEMISE = new TechDoomDemise("terminal_demise");
	public static final Abilitech DOOM_WITHERING_WHISPER = new TechDoomDecay("withering_whisper");
	public static final Abilitech DOOM_CHAINS_OF_DESPAIR = new TechDoomChain("chains_of_despair");

	public static final Abilitech LIFE_LIFEFORCE_LEECH = new TechLifeLeech("lifeforce_leech");
	public static final Abilitech LIFE_HEALING_AURA = new TechLifeAura("healing_aura");
	public static final Abilitech LIFE_MATING_SEASON = new TechLifeBreed("mating_season");
	public static final Abilitech LIFE_GREEN_THUMBS = new TechLifeBoneMeal("green_thumbs");
	public static final Abilitech LIFE_SONG_OF_FERTILITY = new TechLifeFertility("song_of_fertility");

	public static final Abilitech BLOOD_BLEEDING_EDGE = new TechBloodBleeding("bleeding_edge");
	public static final Abilitech BLOOD_REFORMERS_REACH = new TechBloodReformer("reformers_reach");

	public static final Abilitech VOID_VOIDSTEP = new TechVoidStep("voidstep");
	public static final Abilitech VOID_RETURN_TO_DUST = new TechVoidSnap("return_to_dust");
	public static final Abilitech VOID_GRASP_OF_THE_VOID = new TechVoidGrasp("grasp_of_the_void");

	public static final Abilitech RAGE_ENRAGED_BERSERK = new TechRageBerserk("enraged_berserk");
	public static final Abilitech RAGE_VENGEFUL_OUTBURST = new TechRageOutburst("vengeful_outburst");
	public static final Abilitech RAGE_ANGER_MANAGEMENT = new TechRageManagement("anger_management");

	public static final Abilitech MIND_MINDFLAYERS_SPELL = new TechMindControl("mindflayers_spell");
	public static final Abilitech MIND_SENSORY_BREAK = new TechMindConfusion("sensory_break");
	public static final Abilitech MIND_GODHOODS_JUSTICE = new TechMindKarmaHeal("godhoods_justice");

	public static final Abilitech HOPE_WILLED_ALLIANCE = new TechHopeGolem("willed_alliance");
	public static final Abilitech HOPE_ANSWERED_PRAYERS = new TechHopePrayers("answered_prayers");
	public static final Abilitech HOPE_DIVINE_CLEANSING = new TechHopeCleansing("divine_cleansing");

	public static final Badge BADGE_PAGE = new BadgePage();
	public static final Badge BADGE_OVERLORD = new BadgeOverlord();

	@SubscribeEvent
	public static void registerSkills(RegistryEvent.Register<Skill> event)
	{
		IForgeRegistry<Skill> registry = event.getRegistry();

		registry.register(MASTER_BADGE_MIGHTY.setRegistryName("master_badge_mighty"));
		registry.register(MASTER_BADGE_BRAVE.setRegistryName("master_badge_brave"));
		registry.register(MASTER_BADGE_WISE.setRegistryName("master_badge_wise"));

		registry.register(GIFT_OF_GAB.setRegistryName("gift_of_gab"));
		registry.register(SKELETON_KEY.setRegistryName("skeleton_key_badge"));

		if(MinestuckUniverse.isTrophySlotsLoaded)
			registry.register(PATCH_OF_THE_HOARDER.setRegistryName("patch_of_the_hoarder"));

		registry.register(HOARD_OF_THE_ALCHEMIZER.setRegistryName("hoard_of_the_alchemizer"));
		registry.register(BUILDER_BADGE.setRegistryName("builder_badge"));
		registry.register(STRIFE_BADGE.setRegistryName("strife_badge"));
		registry.register(REVENANTS_RETALIATION.setRegistryName("revenants_retaliation"));
		registry.register(EFFECT_BUFF.setRegistryName("effect_buff"));
		registry.register(KARMA.setRegistryName("karma"));

		registry.register(BADGE_PAGE.setRegistryName("page_potential"));
		registry.register(BADGE_OVERLORD.setRegistryName("world_ender"));

		for (TechHeroClass badge : TechHeroClass.HERO_CLASS_BADGES)
			registry.register(badge.setRegistryName());

		for (TechHeroAspect badge : TechHeroAspect.HERO_ASPECT_BADGES)
			registry.register(badge.setRegistryName());
	}

	@SubscribeEvent
	public static void onRegistryNewRegistry(RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry)(new RegistryBuilder()).setName(new ResourceLocation(MinestuckUniverse.MODID, "skills")).setDefaultKey(new ResourceLocation(MinestuckUniverse.MODID)).setType(Skill.class).create();
	}
}