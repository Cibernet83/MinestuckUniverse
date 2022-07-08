package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind;

import java.util.Collections;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechMindStrike extends TechHeroAspect
{
	
	public TechMindStrike(String name) {
		super(name, EnumAspect.MIND, EnumTechType.DEFENSE, EnumAspect.HOPE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == KeyState.NONE)
		{
			badgeEffects.setCalculating(badgeEffects.getCalculating() - 1);
			return false;
		}
		if(state == KeyState.RELEASED)
		{
			badgeEffects.setCalculating(Math.max(time + badgeEffects.getCalculating(), 100));
			return true;
		}
		
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 2);

		return true;
	}
	
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event)
	{
		if((event.getSource().getImmediateSource() instanceof EntityPlayer) &&
				((EntityPlayer) event.getSource().getImmediateSource()).getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechEquipped(MSUSkills.MIND_CALCULATED_STRIKE) &&
				((EntityPlayer) event.getSource().getImmediateSource()).getCapability(MSUCapabilities.BADGE_EFFECTS, null).getCalculating() != 0)
		{
			IBadgeEffects badgeEffects = ((EntityPlayer) event.getSource().getImmediateSource()).getCapability(MSUCapabilities.BADGE_EFFECTS, null);
			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.MIND, 2);
			event.setAmount((float) (event.getAmount()*Math.max(2, Math.sin(badgeEffects.getCalculating() * 1.1 + Math.PI * 1.5)/2 + badgeEffects.getCalculating() * .017 + .5)));
		}
	}
	
}
