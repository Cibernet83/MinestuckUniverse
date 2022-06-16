package com.cibernet.minestuckuniverse.badges.heroAspect;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.badges.Badge;
import com.cibernet.minestuckuniverse.badges.BadgeLevel;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BadgeHeroAspect extends BadgeLevel
{
	public static final Collection<BadgeHeroAspect> HERO_ASPECT_BADGES = new ArrayList<>();

	protected final EnumAspect heroAspect;
	protected final EnumRole heroRole;
	protected final EnumAspect[] auxAxpects; // i refuse to believe this is a typo
	protected final int requiredXp;

	public BadgeHeroAspect(EnumAspect heroAspect, EnumRole heroRole, int requiredLevel, int requiredXp, EnumAspect... auxAspects)
	{
		super(heroRole.name().toLowerCase() + heroAspect.name().substring(0,1).toUpperCase() + heroAspect.name().substring(1).toLowerCase(), requiredLevel);
		this.heroAspect = heroAspect;
		this.heroRole = heroRole;
		this.auxAxpects = auxAspects;
		this.requiredXp = requiredXp;
		HERO_ASPECT_BADGES.add(this);
	}

	public BadgeHeroAspect(EnumAspect heroAspect, EnumRole heroRole, EnumAspect... auxAspects) {
		this(heroAspect, heroRole, 6, 40, auxAspects);
	}

	public abstract boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time);

	@Override
	public boolean canUse(World world, EntityPlayer player)
	{
		return !(player.isPotionActive(MSUPotions.GOD_TIER_LOCK) && player.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() >= 1);
	}

	@Override
	public String getDisplayTooltip()
	{
		String keyName = "GOD TIER ACTION";
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			keyName = MSUKeys.keyBindings[SkillKeyStates.Key.ASPECT.ordinal()].getDisplayName();

		return I18n.format(getUnlocalizedName() + ".tooltip", keyName);
	}

	@Override
	public String getUnlockRequirements()
	{
		if(MSUConfig.multiAspectUnlocks && auxAxpects.length > 0)
		{
			int auxShardAmount = 32/Math.max(1, auxAxpects.length);

			String entries = "";
			for(int i = 0; i < auxAxpects.length-1; i++)
				entries += I18n.format("badge.aspect.unlock.aux.entry", auxShardAmount, auxAxpects[i].getDisplayName());
			return I18n.format("badge.aspect.unlock.aux", heroAspect.getDisplayName(), entries, auxShardAmount, auxAxpects[auxAxpects.length-1].getDisplayName());
		}
		else return I18n.format("badge.aspect.unlock", heroAspect.getDisplayName());
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		if(player.experienceLevel >= requiredXp && findShards(player, false))
		{
			findShards(player, true);
			player.experienceLevel -= requiredXp;
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.ADD_PLAYER_XP, -requiredXp), player);
			return true;
		}
		return false;
	}

	protected boolean findShards(EntityPlayer player, boolean decr)
	{
		int auxShardAmount = 32/Math.max(1, auxAxpects.length);

		if(!Badge.findItem(player, new ItemStack(MinestuckUniverseItems.heroStoneShards.get(heroAspect), auxAxpects.length > 0 ? 64 : 96), decr))
			return false;

		if(MSUConfig.multiAspectUnlocks)
			for(EnumAspect aspect : auxAxpects)
				if(!Badge.findItem(player, new ItemStack(MinestuckUniverseItems.heroStoneShards.get(aspect), auxShardAmount), decr))
					return false;

		return true;
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		if(!super.canAppearOnList(world, player))
			return false;

		EnumAspect playerAspect;
		EnumRole playerRole;

		if(world.isRemote)
		{
			playerAspect = MinestuckPlayerData.title.getHeroAspect();
			playerRole = EnumRole.getRoleFromClass(MinestuckPlayerData.title.getHeroClass());
		}
		else
		{
			playerAspect = MinestuckPlayerData.getData(player).title.getHeroAspect();
			playerRole = EnumRole.getRoleFromClass(MinestuckPlayerData.getData(player).title.getHeroClass());
		}

		return heroAspect.equals(playerAspect) && heroRole.equals(playerRole);
	}

	public Badge setRegistryName()
	{
		return setRegistryName(MinestuckUniverse.MODID, heroRole.name().toLowerCase() + "_" + heroAspect.name().toLowerCase() + "_badge");
	}
}
