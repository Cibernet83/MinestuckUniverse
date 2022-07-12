package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechTimeTickUp extends TechHeroAspect
{
	public TechTimeTickUp(String name) {
		super(name, EnumAspect.TIME, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		Entity target = badgeEffects.getTether(techSlot);

		if(target != null && target.getDistance(player) > 20)
		{
			target = null;
			badgeEffects.clearTether(techSlot);
		}

		if (target != null)
		{
			if(!player.isCreative() && time % 20 == 0)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-2);

			target.onUpdate();
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.TIME, 2);
		}
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, target == null ? 2 : 5);

		return true;
	}
}
