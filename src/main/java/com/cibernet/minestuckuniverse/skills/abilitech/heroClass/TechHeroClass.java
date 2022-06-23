package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TechHeroClass extends Abilitech
{
	public static final Collection<TechHeroClass> HERO_CLASS_BADGES = new ArrayList<>();

	private final String name;
	protected final EnumClass heroClass;

	public TechHeroClass(String name, EnumClass heroClass, int requiredLevel) {
		super(heroClass.name().toLowerCase(), requiredLevel);
		this.name = name;
		this.heroClass = heroClass;
		HERO_CLASS_BADGES.add(this);
	}

	public TechHeroClass(String name, EnumClass heroClass) {
		this(name, heroClass, 5000);
	}

	public abstract boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time);

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

		return heroClass.equals(playerClass);
	}

	public Skill setRegistryName()
	{
		return setRegistryName(name);
	}
}
