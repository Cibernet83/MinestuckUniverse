package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechMageStudy extends TechHeroClass
{
	public TechMageStudy(String name, long cost)
	{
		super(name, EnumClass.MAGE, cost, EnumTechType.PASSIVE, EnumTechType.HYBRID);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{if(state == SkillKeyStates.KeyState.NONE)
	{
		Skill stolenTech = badgeEffects.getExternalTech(techSlot) == null ? null : MSUSkills.REGISTRY.getValue(new ResourceLocation(badgeEffects.getExternalTech(techSlot)));

		if(stolenTech instanceof Abilitech)
			badgeEffects.stopPowerParticles(stolenTech.getClass());

		badgeEffects.setExternalTech(techSlot, null);
		return false;
	}
		Skill stolenTech = badgeEffects.getExternalTech(techSlot) == null ? null : MSUSkills.REGISTRY.getValue(new ResourceLocation(badgeEffects.getExternalTech(techSlot)));

		if(state == SkillKeyStates.KeyState.PRESS)
		{
			if(stolenTech == null)
			{
				badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MAGE, 3);
				player.sendStatusMessage(new TextComponentTranslation("status.externalTech.notFound", stolenTech.getDisplayComponent()), true);
			}
			else
			{
				badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MAGE, 10);
				player.sendStatusMessage(new TextComponentTranslation("status.externalTech.casting", stolenTech.getDisplayComponent()), true);
			}
		}

		if(!(stolenTech instanceof Abilitech))
			return false;

		return ((Abilitech) stolenTech).onUseTick(world, player, badgeEffects, techSlot, state, time);
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}

	@SubscribeEvent
	public static void onTechTargeted(AbilitechTargetedEvent event)
	{
		IGodTierData data = event.getTarget().getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		if(data != null && data.isTechEquipped(MSUSkills.ARCANE_STUDY))
		{
			for(int i = 0; i < data.getTechSlots(); i++)
				if(MSUSkills.ARCANE_STUDY.equals(data.getTech(i)) && event.getTarget().getCapability(MSUCapabilities.SKILL_KEY_STATES, null).getKeyState(SkillKeyStates.Key.values()[i]) == SkillKeyStates.KeyState.NONE)
					event.getTarget().getCapability(MSUCapabilities.BADGE_EFFECTS, null).setExternalTech(i, event.getAbilitech().getRegistryName().toString());
		}
	}
}
