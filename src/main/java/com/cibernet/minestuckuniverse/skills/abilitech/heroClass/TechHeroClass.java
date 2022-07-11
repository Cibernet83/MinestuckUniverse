package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TechHeroClass extends TechBoondollarCost
{
	public static final Collection<TechHeroClass> HERO_CLASS_BADGES = new ArrayList<>();

	private final String name;
	protected final EnumClass heroClass;

	public TechHeroClass(String name, EnumClass heroClass, int requiredLevel) {
		super(name, requiredLevel, EnumTechType.DEFENSE);
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

		Title title = world.isRemote ? MinestuckPlayerData.title : MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		return title != null && heroClass.equals(title.getHeroClass());
	}

	public Skill setRegistryName()
	{
		return setRegistryName(name);
	}

	@Override
	public List<String> getTags()
	{
		List<String> list = super.getTags();
		list.add("@"+heroClass.name()+"@");
		return list;
	}

	@Override
	public int getColor() {
		return BadgeEffects.getClassParticleColors(heroClass)[0];
	}
}
