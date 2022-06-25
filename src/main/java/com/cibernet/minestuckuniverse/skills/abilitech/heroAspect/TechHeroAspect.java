package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TechHeroAspect extends Abilitech
{
	public static final Collection<TechHeroAspect> HERO_ASPECT_BADGES = new ArrayList<>();

	protected final EnumAspect heroAspect;
	protected final EnumTechType heroRole;
	protected final EnumAspect[] auxAspects;

	public TechHeroAspect(String name, EnumAspect heroAspect, EnumTechType heroRole, long cost, EnumAspect... auxAspects)
	{
		super(name, cost);
		this.heroAspect = heroAspect;
		this.heroRole = heroRole;
		this.auxAspects = auxAspects;
		HERO_ASPECT_BADGES.add(this);
		setUnlocalizedName(name);
	}

	public TechHeroAspect(String name, EnumAspect heroAspect, EnumTechType heroRole, EnumAspect... auxAspects) {
		this(name, heroAspect, heroRole, 100000, auxAspects);
	}

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
			keyName = MSUKeys.skillKeys[SkillKeyStates.Key.SECONDARY.ordinal()].getDisplayName();

		return I18n.format(getUnlocalizedName() + ".tooltip", keyName);
	}

	@Override
	public String getUnlockRequirements()
	{
		if(MSUConfig.multiAspectUnlocks && auxAspects.length > 0)
		{
			int auxShardAmount = 32/Math.max(1, auxAspects.length);

			String entries = "";
			for(int i = 0; i < auxAspects.length-1; i++)
				entries += I18n.format("badge.aspect.unlock.aux.entry", auxShardAmount, auxAspects[i].getDisplayName());
			return I18n.format("badge.aspect.unlock.aux", heroAspect.getDisplayName(), entries, auxShardAmount, auxAspects[auxAspects.length-1].getDisplayName());
		}
		else return I18n.format("badge.aspect.unlock", heroAspect.getDisplayName());
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		if(MinestuckPlayerData.getData(player).boondollars >= cost && findShards(player, false))
		{
			findShards(player, true);
			MinestuckPlayerData.addBoondollars(player, -cost);
			//MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.ADD_PLAYER_XP, -requiredXp), player);
			return true;
		}
		return false;
	}

	protected boolean findShards(EntityPlayer player, boolean decr)
	{
		/*
		int auxShardAmount = 32/Math.max(1, auxAspects.length);

		if(!Badge.findItem(player, new ItemStack(MinestuckUniverseItems.heroStoneShards.get(heroAspect), auxAspects.length > 0 ? 64 : 96), decr))
			return false;

		if(MSUConfig.multiAspectUnlocks)
			for(EnumAspect aspect : auxAspects)
				if(!Badge.findItem(player, new ItemStack(MinestuckUniverseItems.heroStoneShards.get(aspect), auxShardAmount), decr))
					return false;
		*/
		return true;
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		if(!super.canAppearOnList(world, player))
			return false;

		EnumAspect playerAspect;
		EnumTechType playerRole;

		if(world.isRemote)
			playerAspect = MinestuckPlayerData.title.getHeroAspect();
		else playerAspect = MinestuckPlayerData.getData(player).title.getHeroAspect();

		return heroAspect.equals(playerAspect);// && heroRole.equals(playerRole);
	}
}