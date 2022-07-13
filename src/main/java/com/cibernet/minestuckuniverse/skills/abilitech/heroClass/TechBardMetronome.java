package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import java.util.ArrayList;
import java.util.List;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.mraof.minestuck.util.EnumClass;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
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
		{
			if(badgeEffects.getExternalTech(techSlot) != null)
				badgeEffects.setExternalTech(techSlot, null);
			return false;
		}
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		String ID = badgeEffects.getExternalTech(techSlot);
		
		if(ID == null || ID.isEmpty())
		{
			ArrayList<Abilitech> POOL = new ArrayList<Abilitech>();
			POOL.addAll(ABILITECHS);
			POOL.removeIf(tech -> !tech.isUsableExternally(world, player));
			if(POOL.size() < 1)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.externalTech.notFound"), true);
				return false;
			}
			Abilitech tech = POOL.get(world.rand.nextInt(POOL.size()));
			player.sendStatusMessage(new TextComponentTranslation("status.externalTech.casting", tech.getDisplayComponent()), true);
			ID = MSUSkills.REGISTRY.getKey(tech).getResourcePath();
			badgeEffects.setExternalTech(techSlot, ID);
		}
		
		Abilitech externalTech = (Abilitech) MSUSkills.REGISTRY.getValue(new ResourceLocation(MinestuckUniverse.MODID, ID));
		
		externalTech.onUseTick(world, player, badgeEffects, techSlot, state, time);
		
		if(state == KeyState.RELEASED)
		{
			badgeEffects.stopPowerParticles(externalTech.getClass());
			player.getFoodStats().setFoodLevel(0);
		}
		
		return true;
	}
	
	public static class slotA {}
	public static class slotB {}
	public static class slotC {}
}
