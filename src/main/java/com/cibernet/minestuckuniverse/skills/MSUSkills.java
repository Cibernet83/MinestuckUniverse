package com.cibernet.minestuckuniverse.skills;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.skills.abilitech.TechDragonAura;
import com.cibernet.minestuckuniverse.skills.abilitech.TechReturn;
import com.cibernet.minestuckuniverse.skills.abilitech.TechSling;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.blood.TechBloodBleeding;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.blood.TechBloodBubble;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.blood.TechBloodReformer;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.blood.TechBloodTransfusion;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.doom.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechHeartBond;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechHeartProject;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechHeartSoulSwitcher;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechSoulStun;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope.TechHopeCleansing;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope.TechHopeGolem;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope.TechHopePrayers;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope.TechHopeyShit;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.light.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind.TechMindCloak;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind.TechMindConfusion;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind.TechMindControl;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind.TechMindKarmaHeal;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind.TechMindStrike;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage.TechRageBerserk;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage.TechRageFrenzy;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage.TechRageManagement;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage.TechRageOutburst;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.space.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.voidAspect.*;
import com.cibernet.minestuckuniverse.skills.badges.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.*;
import com.cibernet.minestuckuniverse.skills.abilitech.heroClass.*;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData.StatType;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
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
		public boolean canAppearOnList(World world, EntityPlayer player) {
			return MinestuckUniverse.isTrophySlotsLoaded && super.canAppearOnList(world, player);
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
	
	public static final Abilitech RETURN_JUMP = new TechReturn("return_jump", 2000);
	public static final Abilitech SYLLADEX_SLING = new TechSling("sylladex_sling", 235);
	public static final Abilitech DRACONIC_AURA = new TechDragonAura("draconic_aura", 0);
	public static final Abilitech ASPECTRAL_BOLT = new TechAspectralBolt("aspectral_bolt", 5900);

	public static final Abilitech KNIGHT_WARD = new TechKnightWard("knight_ward", 54000);
	public static final Abilitech GUARDIAN_HALT = new TechKnightHalt("guardian_halt", 39000);
	public static final Abilitech SYLPH_MEND = new TechSylph("sylph_mend", 995000);
	public static final Abilitech KARMIC_RESTORATION = new TechSylphKarmaRestore("karmic_restoration", 1500000);
	public static final Abilitech PRINCE_WRATH = new TechPrinceWrath("prince_wrath", 57300);
	public static final Abilitech RULING_SLASH = new TechPrinceSlash("ruling_slash", 64660);
	public static final Abilitech WITCH_INHIBITION = new TechWitch("witch_inhibition", 905000);
	public static final Abilitech WICKED_TRAP = new TechWitchTrap("wicked_trap", 6500);
	public static final Abilitech THIEF_FILCH = new TechThief("thief_filch", 42405);
	public static final Abilitech HERMES_DASH = new TechThiefDash("hermes_dash", 350);
	public static final Abilitech MAGE_AWARENESS = new TechMage("mage_awareness", 100);
	public static final Abilitech ARCANE_STUDY = new TechMageStudy("arcane_study", 625000);

	public static final Abilitech SEER_PREDICTION = new TechSeer("seer_prediction", 1850);
	public static final Abilitech FORESIGHT_DODGE = new TechSeerDodge("foresight_dodge", 3900);
	public static final Abilitech ROGUE_CONTRIBUTION = new TechRogue("rogue_contribution", 75950);
	public static final Abilitech ROGUELIKE_ADAPTABILITY = new TechRogueSteal("roguelike_adaptability", 88950);
	public static final Abilitech BARD_DISSONANCE = new TechBard("bard_dissonance", 4000);
	public static final Abilitech BARD_METRONOME = new TechBardMetronome("magic_metronome", 115000);
	public static final Abilitech MAID_FAVOR = new TechMaid("maid_favor", 49550);
	public static final Abilitech IRRADIANT_SERVITUDE = new TechMaidServe("irradiant_servitude", 95500);
	public static final Abilitech HEIR_WILL = new TechHeir("heir_will", 9000, EnumTechType.PASSIVE, EnumTechType.DEFENSE);
	public static final Abilitech UNIVERSAL_REVERSE = new TechHeir("universal_reverse", 5500, EnumTechType.PASSIVE, EnumTechType.OFFENSE);
	public static final Abilitech PERSEVERANT_AWAKENING = new TechHeroClass("perseverant_awakening", EnumClass.PAGE, 1000000){
		@Override
		public boolean isUsableExternally(World world, EntityPlayer player) {
			return false;
		}
		
		@Override
		public boolean canAppearOnList(World world, EntityPlayer player)
		{
			return super.canAppearOnList(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
		}
		
		@Override
		public boolean canUnlock(World world, EntityPlayer player)
		{
			return super.canUnlock(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getSkillLevel(StatType.GENERAL) >= 10;
		}
	};

	public static final Abilitech MUSE_REQUIEM = new TechMuse("muse_requiem", 730000);
	public static final Abilitech LORD_DECREE = new TechLord("lord_decree", 1850000);

	public static final Abilitech SPACE_WORMHOLE_TROTTER = new TechSpaceTele("wormhole_trotter", 2500);
	public static final Abilitech SPACE_ANCHORED_WORMHOLE = new TechSpaceAnchoredTele("anchored_wormhole", 100000);
	public static final Abilitech SPACE_SPATIAL_WARP = new TechSpaceTargetTele("spatial_warp", 695000);
	public static final Abilitech SPACE_MATTER_MANIPULATOR = new TechSpaceManipulator("matter_manipulator", 560000);
	public static final Abilitech SPACE_SPATIAL_MANIPULATOR = new TechSpaceResize("spatial_manipulator", 1000);
	public static final Abilitech SPACE_KINETIC_GRAB = new TechSpaceGrab("kinetic_grab", 515000);

	public static final Abilitech TIME_TEMPORAL_RECALL = new TechTimeRecall("temporal_recall", 68460);
	public static final Abilitech TIME_CHRONOFREEZE = new TechTimeStop("chronofreeze", 2500000);
	public static final Abilitech TIME_FLOW_ACCELERATOR = new TechTimeAcceleration("flow_accelerator", 1520);
	public static final Abilitech TIME_ACCELERANDO = new TechTimeTickUp("accelerando", 1300000);
	public static final Abilitech TIME_RAPID_REWIND = new TechTimeTables("rapid_rewind", 1000);
	public static final Abilitech TIME_CELESTIAL_SHIFT = new TechTimeShift("celestial_shift", 150);

	public static final Abilitech BREATH_TEMPESTING_ASCENSION = new TechBreathGale("tempesting_ascension", 1776);
	public static final Abilitech BREATH_WINDSWEEPING_TYPHOON = new TechBreathKnockback("windsweeping_typhoon", 413000);
	public static final Abilitech BREATH_SUPERSONIC_SPEED = new TechBreathSpeed("supersonic_speed", 1776);
	public static final Abilitech BREATH_VESSEL_OF_THE_WIND = new TechBreathWindVessel("vessel_of_the_wind", 85460);
	public static final Abilitech BREATH_ENTOMBING_TWISTER = new TechBreathBubble("entombing_twister", 79000);
	public static final Abilitech BREATH_SPACE_VERTIGO_BLOCK = new TechBreathSpaceFallProof("vertigo_block", 475);

	public static final Abilitech LIGHT_STORM_OF_THE_STRIKER = new TechLightStriker("storm_of_the_striker", 7650);
	public static final Abilitech LIGHT_LIGHTBOUND_WISDOM = new TechLightGlowing("lightbound_wisdom", 888);
	public static final Abilitech LIGHT_ORB_OF_LIGHT = new TechLightGlorb("orb_of_light", 185);
	public static final Abilitech LIGHT_ETERNAL_GLOW = new TechLightAutoGlorb("eternal_glow", 685);
	public static final Abilitech LIGHT_HARDLIGHT_BUBBLE = new TechLightBubble("hardlight_bubble", 9850);
	public static final Abilitech LIGHT_SKAIAN_INISHGT = new TechHeroAspect("skaian_insight", EnumAspect.LIGHT, 925000, EnumTechType.PASSIVE, EnumTechType.UTILITY)
	{
		@Override
		public boolean isUsableExternally(World world, EntityPlayer player) {
			return false;
		}
	};

	public static final Abilitech HEART_SOUL_SWITCHER = new TechHeartSoulSwitcher("soul_switcher", 84600);
	public static final Abilitech HEART_SOUL_SHOCK = new TechSoulStun("soul_shock", 960000);
	public static final Abilitech HEART_SPIRITUAL_BOND = new TechHeartBond("spiritual_bond", 34600);
	public static final Abilitech HEART_ASTRAL_PROJECTION = new TechHeartProject("astral_projection", 1150000);

	public static final Abilitech DOOM_TERMINAL_DEMISE = new TechDoomDemise("terminal_demise", 8250);
	public static final Abilitech DOOM_DEATHS_SHROUD = new TechDoomDemiseAoE("deaths_shroud", 8200);
	public static final Abilitech DOOM_WITHERING_WHISPER = new TechDoomDecay("withering_whisper", 985000);
	public static final Abilitech DOOM_CHAINS_OF_DESPAIR = new TechDoomChain("chains_of_despair", 8780);
	public static final Abilitech DOOM_SURVIVORS_BIND = new TechDoomBind("survivors_bind", 888888);
	public static final Abilitech DOOM_VOID_ABYSS_CAGE = new TechDoomVoidBubble("abyss_cage", 250000);

	public static final Abilitech LIFE_LIFEFORCE_LEECH = new TechLifeLeech("lifeforce_leech", 79700);
	public static final Abilitech LIFE_HEALING_AURA = new TechLifeAura("healing_aura", 72970);
	public static final Abilitech LIFE_MATING_SEASON = new TechLifeBreed("mating_season", 115);
	public static final Abilitech LIFE_CHLOROBALL = new TechLifeChloroball("chloroball", 715);
	public static final Abilitech LIFE_SONG_OF_FERTILITY = new TechLifeFertility("song_of_fertility", 630);
	public static final Abilitech LIFE_SAVING_GRACE = new TechLifeGrace("saving_grace", 890000);

	public static final Abilitech BLOOD_BLEEDING_EDGE = new TechBloodBleeding("bleeding_edge", 4900);
	public static final Abilitech BLOOD_REFORMERS_REACH = new TechBloodReformer("reformers_reach", 510);
	public static final Abilitech BLOOD_LIFEFORCE_TRANSFUSION = new TechBloodTransfusion("lifeforce_transfusion", 450000);
	public static final Abilitech BLOOD_HAEMODOME = new TechBloodBubble("haemodome", 9300);

	public static final Abilitech VOID_VOIDSTEP = new TechVoidStep("voidstep", 190000);
	public static final Abilitech VOID_RETURN_TO_DUST = new TechVoidSnap("return_to_dust", 40000);
	public static final Abilitech VOID_GRASP_OF_THE_VOID = new TechVoidGrasp("grasp_of_the_void", 3700);
	public static final Abilitech VOID_VACUUM_SIPHON = new TechVoidVacuum("vacuum_siphon", 430000);

	public static final Abilitech RAGE_ENRAGED_BERSERK = new TechRageBerserk("enraged_berserk", 7100);
	public static final Abilitech RAGE_VENGEFUL_OUTBURST = new TechRageOutburst("vengeful_outburst", 25740);
	public static final Abilitech RAGE_ANGER_MANAGEMENT = new TechRageManagement("anger_management", 1240);
	public static final Abilitech RAGE_FRENZIED_MAYHEM = new TechRageFrenzy("frenzied_mayhem", 1390);

	public static final Abilitech MIND_MINDFLAYERS_SPELL = new TechMindControl("mindflayers_spell", 1800000);
	public static final Abilitech MIND_SENSORY_BREAK = new TechMindConfusion("sensory_break", 38780);
	public static final Abilitech MIND_GODHOODS_JUSTICE = new TechMindKarmaHeal("godhoods_justice", 700000);
	public static final Abilitech MIND_CALCULATED_STRIKE = new TechMindStrike("calculated_strike", 62330);
	public static final Abilitech MIND_VOID_ILLUSORY_CLOAK = new TechMindCloak("illusory_cloak", 1790);

	public static final Abilitech HOPE_WILLED_ALLIANCE = new TechHopeGolem("willed_alliance", 29990);
	public static final Abilitech HOPE_ANSWERED_PRAYERS = new TechHopePrayers("answered_prayers", 65555);
	public static final Abilitech HOPE_DIVINE_CLEANSING = new TechHopeCleansing("divine_cleansing", 795000);
	public static final Abilitech HOPE_HOPEFUL_OUTBURST = new TechHopeyShit("hopeful_outburst", 2000000);

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

		registry.register(PATCH_OF_THE_HOARDER.setRegistryName("patch_of_the_hoarder"));

		registry.register(HOARD_OF_THE_ALCHEMIZER.setRegistryName("hoard_of_the_alchemizer"));
		registry.register(BUILDER_BADGE.setRegistryName("builder_badge"));
		registry.register(STRIFE_BADGE.setRegistryName("strife_badge"));
		registry.register(REVENANTS_RETALIATION.setRegistryName("revenants_retaliation"));
		registry.register(EFFECT_BUFF.setRegistryName("effect_buff"));
		registry.register(KARMA.setRegistryName("karma"));

		registry.register(BADGE_PAGE.setRegistryName("page_potential"));
		registry.register(BADGE_OVERLORD.setRegistryName("world_ender"));

		for(Abilitech tech : Abilitech.ABILITECHS)
			registry.register(tech.setRegistryName());
		
		/*
		for (TechHeroClass badge : TechHeroClass.HERO_CLASS_BADGES)
			registry.register(badge.setRegistryName());

		for (TechHeroAspect badge : TechHeroAspect.HERO_ASPECT_BADGES)
			registry.register(badge.setRegistryName());
		*/
	}

	@SubscribeEvent
	public static void onRegistryNewRegistry(RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry)(new RegistryBuilder()).setName(new ResourceLocation(MinestuckUniverse.MODID, "skills")).setDefaultKey(new ResourceLocation(MinestuckUniverse.MODID)).setType(Skill.class).create();
	}
}
