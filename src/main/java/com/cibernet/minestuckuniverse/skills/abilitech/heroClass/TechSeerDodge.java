package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TechSeerDodge extends TechHeroClass
{
	public TechSeerDodge(String name)
	{
		super(name, EnumClass.SEER);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingAttack(LivingAttackEvent event)
	{
		IGodTierData data = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		IBadgeEffects effects = event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null);

		if(data != null && data.isTechPassiveEnabled(MSUSkills.FORESIGHT_DODGE) && effects != null && !effects.isForesightOnCooldown())
		{
			EntityLivingBase entity = event.getEntityLiving();

			if(event.getEntityLiving().world.isRemote)
			{
				effects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.SEER, 20);
				effects.setForesightCooldown(6000);
			}
			event.setCanceled(true);

			entity.moveRelative(0, (float)Math.cos((entity.rotationPitch+90)*Math.PI/180f), (float)Math.sin((entity.rotationPitch+90)*Math.PI/180f), 3);
			entity.velocityChanged = true;
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START || event.player.world.isRemote)
			return;

		if(MSUSkills.FORESIGHT_DODGE.canUse(event.player.world, event.player) &&
				event.player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechEquipped(MSUSkills.FORESIGHT_DODGE))
		{

			IBadgeEffects effects = event.player.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
			effects.setForesightCooldown(Math.max(0, effects.getForesightCooldown()-1));
		}
	}
}