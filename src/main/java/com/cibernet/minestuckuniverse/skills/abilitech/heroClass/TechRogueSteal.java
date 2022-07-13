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
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechRogueSteal extends TechHeroClass
{
	public TechRogueSteal(String name, long cost)
	{
		super(name, EnumClass.ROGUE, cost, EnumTechType.HYBRID);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
		{
			Skill stolenTech = badgeEffects.getExternalTech(techSlot) == null ? null : MSUSkills.REGISTRY.getValue(new ResourceLocation(badgeEffects.getExternalTech(techSlot)));

			if(stolenTech instanceof Abilitech)
				badgeEffects.stopPowerParticles(stolenTech.getClass());

			badgeEffects.setExternalTech(techSlot, null);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(state == SkillKeyStates.KeyState.PRESS && target != null)
		{
			IGodTierData targetData = target.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
			if(targetData != null && !MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, null)))
			{
				boolean cast = false;
				for(Abilitech tech : targetData.getTechLoadout())
					if(tech != null && !tech.getClass().equals(getClass()) && tech.isUsableExternally(world, player))
					{
						badgeEffects.setExternalTech(techSlot, tech.getRegistryName().toString());
						badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.ROGUE, 10);

						player.sendStatusMessage(new TextComponentTranslation("status.externalTech.casting", tech.getDisplayComponent()), true);
						cast = true;
						break;
					}
				if(!cast)
				{
					player.sendStatusMessage(new TextComponentTranslation("status.externalTech.notFound"), true);
					badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.ROGUE, 3);
				}
			}
		}
		Skill stolenTech = badgeEffects.getExternalTech(techSlot) == null ? null : MSUSkills.REGISTRY.getValue(new ResourceLocation(badgeEffects.getExternalTech(techSlot)));

		if(!(stolenTech instanceof Abilitech))
			return false;

		return ((Abilitech) stolenTech).onUseTick(world, player, badgeEffects, techSlot, state, time);
	}
}
