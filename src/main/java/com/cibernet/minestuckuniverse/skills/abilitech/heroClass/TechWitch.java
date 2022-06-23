package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Collections;

public class TechWitch extends TechHeroClass
{
	public TechWitch(String name)
	{
		super(name, EnumClass.WITCH);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8) {
			if (state == SkillKeyStates.KeyState.HELD)
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if (state != SkillKeyStates.KeyState.RELEASED)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.WITCH, time > 20 ? 5 : 1);
			return true;
		}

		if(time < 20)
			return false;

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if (target == null)
			return false;

		target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.WITCH, 20);
		PotionEffect effect = new PotionEffect(MSUPotions.GOD_TIER_LOCK, 800, 1);
		effect.setCurativeItems(Collections.emptyList());
		target.addPotionEffect(effect);
		if (!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

		return false;
	}
}
