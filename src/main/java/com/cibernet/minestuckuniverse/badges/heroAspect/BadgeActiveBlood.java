package com.cibernet.minestuckuniverse.badges.heroAspect;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;

public class BadgeActiveBlood extends BadgeHeroAspect
{
	public BadgeActiveBlood() {
		super(EnumAspect.BLOOD, EnumRole.ACTIVE, EnumAspect.RAGE);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time) {
		return false;
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if (event.getEntity().world.isRemote)
			return;

		EntityPlayer sourcePlayer = event.getSource().getTrueSource() instanceof EntityPlayer ? (EntityPlayer) event.getSource().getTrueSource() : null;
		IGodTierData sourceData = sourcePlayer != null ? sourcePlayer.getCapability(MSUCapabilities.GOD_TIER_DATA, null) : null;

		if(event.getEntityLiving() != null && sourcePlayer != null && sourcePlayer.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.BADGE_ACTIVE_BLOOD))
		{
			if(sourcePlayer.world.rand.nextFloat() < Math.min(0.8f, (sourcePlayer.getLuck()/25f)))
			{
				event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.BLOOD, 5);
				PotionEffect effect = new PotionEffect(MSUPotions.BLEEDING, (int) ((int) sourcePlayer.getHealth()/sourcePlayer.getMaxHealth()*600), (int) Math.min(5, sourceData.getSkillLevel(GodTierData.SkillType.ATTACK)/5f));
				effect.setCurativeItems(Collections.emptyList());
				event.getEntityLiving().addPotionEffect(effect);
			}
		}
	}
}
