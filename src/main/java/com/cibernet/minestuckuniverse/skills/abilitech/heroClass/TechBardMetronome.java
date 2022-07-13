package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.mraof.minestuck.util.EnumClass;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TechBardMetronome  extends TechHeroClass
{
	public TechBardMetronome(String name)
	{
		super(name, EnumClass.BARD);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		
		
		
		return true;
	}
}
