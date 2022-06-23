package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Collections;

public class TechLightGlowing extends TechHeroAspect
{
	public TechLightGlowing(String name) {
		super(name, EnumAspect.LIGHT, EnumRole.PASSIVE, EnumAspect.MIND);
	}

	protected static final int RADIUS = 64;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.PRESS)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIGHT, 10);

			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(RADIUS), (entity) -> (entity instanceof EntityPlayer)))
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);
				PotionEffect effect = new PotionEffect(MobEffects.GLOWING, 600, 0);
				effect.setCurativeItems(Collections.emptyList());
				target.addPotionEffect(effect);
			}

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);
		}

		if(state == SkillKeyStates.KeyState.NONE || time >= 19)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 15)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIGHT, 20);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);

		if(time >= 18)
		{
			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(RADIUS), (entity) -> entity != player))
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);
				PotionEffect effect = new PotionEffect(MobEffects.GLOWING, 600, 0);
				effect.setCurativeItems(Collections.emptyList());
				target.addPotionEffect(effect);
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);
		}

		return true;
	}
}
