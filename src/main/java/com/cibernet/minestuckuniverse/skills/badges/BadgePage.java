package com.cibernet.minestuckuniverse.skills.badges;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BadgePage extends Badge
{
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

}
