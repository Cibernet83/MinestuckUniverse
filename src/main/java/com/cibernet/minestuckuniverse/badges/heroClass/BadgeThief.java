package com.cibernet.minestuckuniverse.badges.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;

public class BadgeThief extends BadgeHeroClass
{
	public BadgeThief()
	{
		super(EnumClass.THIEF);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
		{
			if (state.equals(SkillKeyStates.KeyState.HELD))
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if (state != SkillKeyStates.KeyState.RELEASED)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.THIEF, time > 20 ? 5 : 1);
			return true;
		}

		if (time < 20)
			return false;

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if (!(target instanceof EntityPlayer))
			return false;

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.THIEF, 20);
		target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.THIEF, 20);
		PotionEffect effect = new PotionEffect(MSUPotions.GOD_TIER_LOCK, 2400, 0);
		effect.setCurativeItems(Collections.emptyList());
		target.addPotionEffect(effect);

		IdentifierHandler.PlayerIdentifier targetPid = IdentifierHandler.encode((EntityPlayer) target);
		SburbConnection c = SkaianetHandler.getMainConnection(targetPid, true);

		if (c != null && c.enteredGame())
		{
			Collection<PotionEffect> effectsToSteal = GTEventHandler.getAspectEffects((EntityPlayer) target).values();

			for (PotionEffect potion : effectsToSteal)
				if (target.isPotionActive(potion.getPotion()))
					player.addPotionEffect(new PotionEffect(potion.getPotion(), 2400, potion.getAmplifier(), false, true));
		}

		if (!player.isCreative() && !world.isRemote)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

		return true;
	}
}
