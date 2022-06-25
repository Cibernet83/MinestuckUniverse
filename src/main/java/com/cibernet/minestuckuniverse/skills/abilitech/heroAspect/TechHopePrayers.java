package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TechHopePrayers extends TechHeroAspect
{
	public TechHopePrayers(String name) {
		super(name, EnumAspect.HOPE, EnumTechType.DEFENSE, EnumAspect.LIGHT);
	}

	protected static final int RADIUS = 20;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time) {

		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8) {
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if (state == SkillKeyStates.KeyState.NONE || time > 40)
			return false;

		if(time == 20)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.HOPE, 10);

			List<Potion> potionPoolPos = new ArrayList<>();
			List<Potion> potionPoolNeg = new ArrayList<>();
			Potion.REGISTRY.forEach(potion ->
			{
				if (potion.isBadEffect())
					potionPoolNeg.add(potion);
				else potionPoolPos.add(potion);
			});

			Potion negativeEffect = potionPoolNeg.get(world.rand.nextInt(potionPoolNeg.size()));
			Potion positiveEffect = potionPoolPos.get(world.rand.nextInt(potionPoolPos.size()));

			for (EntityPlayer target : world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(RADIUS), target -> target != player)) {
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HOPE, 10);

				Potion potion = Math.signum(target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()) == Math.signum(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()) ? positiveEffect : negativeEffect;
				target.addPotionEffect(new PotionEffect(potion, potion.isInstant() ? 0 : 300, 2));
			}

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

			if (state == SkillKeyStates.KeyState.NONE || time >= 19)
				return false;

			if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 6) {
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}
		}
		if(time < 40)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HOPE, time < 20 ? 2 : 6);
		else
		{
			List<Potion> potionPoolPos = new ArrayList<>();
			Potion.REGISTRY.forEach(potion ->
			{
				if(!potion.isBadEffect())
					potionPoolPos.add(potion);
			});

			Potion potion = potionPoolPos.get(world.rand.nextInt(potionPoolPos.size()));
			player.addPotionEffect(new PotionEffect(potion, potion.isInstant() ? 0 : 300, 2));
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 6);
		}

		return true;
	}
}
