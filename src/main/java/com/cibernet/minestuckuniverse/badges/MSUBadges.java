package com.cibernet.minestuckuniverse.badges;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.badges.heroAspect.*;
import com.cibernet.minestuckuniverse.badges.heroAspectUtil.*;
import com.cibernet.minestuckuniverse.badges.heroClass.*;
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
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = MinestuckUniverse.MODID)
public class MSUBadges
{
	public static IForgeRegistry<Badge> REGISTRY;

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
			return Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).hasBadge(this);
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

	public static final Badge BADGE_KNIGHT = new BadgeKnight();
	public static final Badge BADGE_SYLPH = new BadgeSylph();
	public static final Badge BADGE_PRINCE = new BadgePrince();
	public static final Badge BADGE_WITCH = new BadgeWitch();
	public static final Badge BADGE_THIEF = new BadgeThief();
	public static final Badge BADGE_MAGE = new BadgeMage();

	public static final Badge BADGE_SEER = new BadgeSeer();
	public static final Badge BADGE_ROGUE = new BadgeRogue();
	public static final Badge BADGE_BARD = new BadgeBard();
	public static final Badge BADGE_MAID = new BadgeMaid();
	public static final Badge BADGE_HEIR = new BadgeHeir();
	public static final Badge BADGE_PAGE = new BadgePage();

	public static final Badge BADGE_MUSE = new BadgeMuse();
	public static final Badge BADGE_LORD = new BadgeLord();

	public static final Badge BADGE_ACTIVE_SPACE = new BadgeActiveSpace();
	public static final Badge BADGE_PASSIVE_SPACE = new BadgePassiveSpace();
	public static final Badge BADGE_UTIL_SPACE = new BadgeUtilSpace();

	public static final Badge BADGE_ACTIVE_TIME = new BadgeActiveTime();
	public static final Badge BADGE_PASSIVE_TIME = new BadgePassiveTime();
	public static final Badge BADGE_UTIL_TIME = new BadgeUtilTime();

	public static final Badge BADGE_ACTIVE_BREATH = new BadgeActiveBreath();
	public static final Badge BADGE_PASSIVE_BREATH = new BadgePassiveBreath();
	public static final Badge BADGE_UTIL_BREATH = new BadgeUtilBreath();

	public static final Badge BADGE_ACTIVE_LIGHT = new BadgeActiveLight();
	public static final Badge BADGE_PASSIVE_LIGHT = new BadgePassiveLight();
	public static final Badge BADGE_UTIL_LIGHT = new BadgeUtilLight();

	public static final Badge BADGE_ACTIVE_HEART = new BadgeActiveHeart();
	public static final Badge BADGE_PASSIVE_HEART = new BadgePassiveHeart();
	public static final Badge BADGE_UTIL_HEART = new BadgeUtilHeart();

	public static final Badge BADGE_ACTIVE_DOOM = new BadgeActiveDoom();
	public static final Badge BADGE_PASSIVE_DOOM = new BadgePassiveDoom();
	public static final Badge BADGE_UTIL_DOOM = new BadgeUtilDoom();

	public static final Badge BADGE_ACTIVE_LIFE = new BadgeActiveLife();
	public static final Badge BADGE_PASSIVE_LIFE = new BadgePassiveLife();
	public static final Badge BADGE_UTIL_LIFE = new BadgeUtilLife();

	public static final Badge BADGE_ACTIVE_BLOOD = new BadgeActiveBlood();
	public static final Badge BADGE_PASSIVE_BLOOD = new BadgePassiveBlood();
	public static final Badge BADGE_UTIL_BLOOD = new BadgeUtilBlood();

	public static final Badge BADGE_ACTIVE_VOID = new BadgeActiveVoid();
	public static final Badge BADGE_PASSIVE_VOID = new BadgePassiveVoid();
	public static final Badge BADGE_UTIL_VOID = new BadgeUtilVoid();

	public static final Badge BADGE_ACTIVE_RAGE = new BadgeActiveRage();
	public static final Badge BADGE_PASSIVE_RAGE = new BadgePassiveRage();
	public static final Badge BADGE_UTIL_RAGE = new BadgeUtilRage();

	public static final Badge BADGE_ACTIVE_MIND = new BadgeActiveMind();
	public static final Badge BADGE_PASSIVE_MIND = new BadgePassiveMind();
	public static final Badge BADGE_UTIL_MIND = new BadgeUtilMind();

	public static final Badge BADGE_ACTIVE_HOPE = new BadgeActiveHope();
	public static final Badge BADGE_PASSIVE_HOPE = new BadgePassiveHope();
	public static final Badge BADGE_UTIL_HOPE = new BadgeUtilHope();

	public static final Badge BADGE_OVERLORD = new BadgeOverlord();

	@SubscribeEvent
	public static void missingMappings(RegistryEvent.MissingMappings<Badge> event)
	{
		HashMap<String, Badge> remaps = new HashMap<String, Badge>()
		{{
			put("wise_badge", MASTER_BADGE_WISE);
			put("mighty_badge", MASTER_BADGE_MIGHTY);
			put("brave_badge", MASTER_BADGE_BRAVE);

			put("effect_buff_badge", EFFECT_BUFF);
			put("karma_badge", KARMA);

			put("mechanical_light_badge", BADGE_UTIL_LIGHT);
		}};

		for(RegistryEvent.MissingMappings.Mapping<Badge> badge : event.getMappings())
		{
			String key = badge.key.getResourcePath();
			if(remaps.containsKey(key))
				badge.remap(remaps.get(key));
		}
	}

	@SubscribeEvent
	public static void registerBadges(RegistryEvent.Register<Badge> event)
	{
		IForgeRegistry<Badge> registry = event.getRegistry();

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

		for (BadgeHeroClass badge : BadgeHeroClass.HERO_CLASS_BADGES)
			registry.register(badge.setRegistryName());

		for (BadgeHeroAspect badge : BadgeHeroAspect.HERO_ASPECT_BADGES)
			registry.register(badge.setRegistryName());

		for (BadgeHeroAspectUtil badge : BadgeHeroAspectUtil.HERO_ASPECT_UTIL_BADGES)
			registry.register(badge.setRegistryName());
	}

	@SubscribeEvent
	public static void onRegistryNewRegistry(RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry)(new RegistryBuilder()).setName(new ResourceLocation(MinestuckUniverse.MODID, "god_tier_badges")).setDefaultKey(new ResourceLocation(MinestuckUniverse.MODID)).setType(Badge.class).create();
	}
}
