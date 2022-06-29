package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class TechHeroClass extends TechBoondollarCost
{
	public static final Collection<TechHeroClass> HERO_CLASS_BADGES = new ArrayList<>();

	private final String name;
	protected final EnumClass heroClass;
	protected final EnumTechType heroRole = EnumTechType.DEFENSE;

	public TechHeroClass(String name, EnumClass heroClass, int requiredLevel) {
		super(name, requiredLevel);
		this.name = name;
		this.heroClass = heroClass;
		HERO_CLASS_BADGES.add(this);
	}

	public TechHeroClass(String name, EnumClass heroClass) {
		this(name, heroClass, 5000);
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

		return heroClass.equals(playerClass);
	}

	public Skill setRegistryName()
	{
		return setRegistryName(name);
	}

	@Override
	public List<String> getTags()
	{
		return Arrays.asList( "@"+heroClass.name()+"@", "@"+heroRole.name()+"@");
	}
}
