package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.util.SoulData;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Collections;

public class TechHeartSoulSwitcher extends TechHeroAspect
{
	public TechHeartSoulSwitcher(String name) {
		super(name, EnumAspect.HEART, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, (time < 60) ? 2 : 10);

		if(state == SkillKeyStates.KeyState.RELEASED && time >= 60)
		{

			EntityLivingBase target = MSUUtils.getTargetEntity(player);
			if (!(target instanceof EntityPlayer))
				return false;


			SoulData targetSoulData = new SoulData(target);
			SoulData playerSoulData = new SoulData(player);

			targetSoulData.apply(player);
			playerSoulData.apply(target);

			PotionEffect effect = new PotionEffect(MSUPotions.GOD_TIER_LOCK, 100);
			effect.setCurativeItems(Collections.emptyList());
			target.addPotionEffect(effect);

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 4);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, 2);

		}

		return true;
	}
}
