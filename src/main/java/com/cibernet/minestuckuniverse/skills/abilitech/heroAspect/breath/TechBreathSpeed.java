package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechBreathSpeed extends TechHeroAspect
{
	public TechBreathSpeed(String name)
	{
		super(name, EnumAspect.BREATH, EnumTechType.UTILITY);
	}

	@Override
	public void onUnequipped(World world, EntityPlayer player, int techSlot)
	{
		super.onUnequipped(world, player, techSlot);
		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).stopPowerParticles(getClass());
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		boolean windSpeeding = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this);
		if (state == SkillKeyStates.KeyState.PRESS)
		{
			player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setSkillPassiveEnabled(this, !windSpeeding);
			player.sendStatusMessage(new TextComponentTranslation(!windSpeeding ? "status.badgeEnabled" : "status.badgeDisabled", getDisplayComponent()), true);
		}
		return false;
	}

	@Override
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot)
	{
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 10, true, false));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 20, 4, true, false));

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BREATH, 4);

		return true;
	}
}
