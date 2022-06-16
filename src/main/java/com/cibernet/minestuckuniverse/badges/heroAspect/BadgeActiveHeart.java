package com.cibernet.minestuckuniverse.badges.heroAspect;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BadgeActiveHeart extends BadgeHeroAspect
{
	public static final DamageSource DAMAGE_SOURCE = new CritDamageSource("perseveringSpirit").setGodproof().setDamageBypassesArmor();

	public BadgeActiveHeart () {
		super(EnumAspect.HEART, EnumRole.ACTIVE, EnumAspect.LIFE);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.HELD || time > 200)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time == 200)
		{
			player.attackEntityFrom(DAMAGE_SOURCE, 20);
			return false;
		}

		player.setSprinting(false);

		if(!player.isCreative() && time % 30 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 4);

		return true;
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if (event.getEntity().world.isRemote)
			return;

		EntityPlayer targetPlayer = event.getEntityLiving() instanceof EntityPlayer ? (EntityPlayer) event.getEntityLiving() : null;
		IGodTierData targetData   = targetPlayer != null ? targetPlayer.getCapability(MSUCapabilities.GOD_TIER_DATA, null) : null;

		if (targetPlayer != null && targetData.isBadgeActive(MSUBadges.BADGE_ACTIVE_HEART) && targetPlayer.getCapability(MSUCapabilities.SKILL_KEY_STATES, null).getKeyState(SkillKeyStates.Key.ASPECT) == SkillKeyStates.KeyState.HELD && targetPlayer.getCapability(MSUCapabilities.SKILL_KEY_STATES, null).getKeyTime(SkillKeyStates.Key.ASPECT) <= 200) {
			if (targetPlayer.getFoodStats().getFoodLevel() > event.getAmount())
			{
				targetPlayer.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, (int) event.getAmount());

				targetPlayer.heal(event.getAmount());
				targetPlayer.getFoodStats().setFoodLevel(targetPlayer.getFoodStats().getFoodLevel() - (int)event.getAmount());
				event.setAmount(0);
			}
			else if (targetPlayer.getFoodStats().getFoodLevel() > 1)
			{
				targetPlayer.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, targetPlayer.getFoodStats().getFoodLevel());

				targetPlayer.heal(targetPlayer.getFoodStats().getFoodLevel());
				targetPlayer.getFoodStats().setFoodLevel(0);
				event.setAmount(event.getAmount() - targetPlayer.getFoodStats().getFoodLevel());
			}
			else
			{
				targetPlayer.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			}
		}
	}
}
