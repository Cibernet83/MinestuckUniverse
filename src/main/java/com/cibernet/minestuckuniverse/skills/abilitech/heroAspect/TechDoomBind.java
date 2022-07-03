package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechDoomBind extends TechHeroAspect
{
	public TechDoomBind(String name)
	{
		super(name, EnumAspect.DOOM, 100000, EnumTechType.DEFENSE, EnumTechType.PASSIVE);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.DOOM_SURVIVORS_BIND) &&
			event.getEntityLiving().getHealth() > 9 && event.getAmount() >= event.getEntityLiving().getHealth())
		{
			event.setAmount(event.getEntityLiving().getHealth()-1);
			event.getEntityLiving().hurtResistantTime = event.getEntityLiving().maxHurtResistantTime*2;
			event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.DOOM, 15);
		}
	}
}
