package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import java.util.ArrayList;
import java.util.List;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
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
		if(state == KeyState.NONE)
			return false;
		
		if(badgeEffects.getExternalTech(techSlot) == null)
		{
			ArrayList<Abilitech> POOL = new ArrayList<Abilitech>();
			POOL.addAll(ABILITECHS);
			
		}
		
		
		
		
		
		return true;
	}
	
	public static class slotA {}
	public static class slotB {}
	public static class slotC {}
}
