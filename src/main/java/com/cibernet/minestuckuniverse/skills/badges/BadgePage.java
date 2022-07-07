package com.cibernet.minestuckuniverse.skills.badges;

import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BadgePage extends Badge
{
	private final int requiredXp = 80;

	public BadgePage()
	{
		setUnlocalizedName("page");
	}

	@Override
	public boolean canUse(World world, EntityPlayer player)
	{
		return !(player.isPotionActive(MSUPotions.GOD_TIER_LOCK) && player.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() >= 1);
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		if(!super.canAppearOnList(world, player))
			return false;

		EnumClass playerClass;

		if(world.isRemote)
			playerClass = MinestuckPlayerData.title.getHeroClass();
		else playerClass = MinestuckPlayerData.getData(player).title.getHeroClass();

		return EnumClass.PAGE.equals(playerClass);
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		if(player.experienceLevel >= requiredXp)
		{
			player.experienceLevel -= requiredXp;
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.ADD_PLAYER_XP, -requiredXp), player);
			return true;
		}
		return false;
	}

	@Override
	public String getUnlockRequirements() {
		return new TextComponentTranslation("badge.class.unlock", requiredXp).getFormattedText();
	}
}
