package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TechRogue extends TechHeroClass
{
	public TechRogue(String name)
	{
		super(name, EnumClass.ROGUE);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE || time > 60)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		ArrayList<PotionEffect> appliedPotions = new ArrayList<>();

		for(PotionEffect effect : player.getActivePotionEffects())
			appliedPotions.add(new PotionEffect(effect.getPotion(), Math.min(effect.getDuration(), 6000), effect.getAmplifier(), effect.getIsAmbient(), true));

		if(time > 59)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(5,1,5), (entity) -> entity != player))
				for(PotionEffect effect : appliedPotions)
				{
					target.addPotionEffect(effect);
					target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.ROGUE, 3);
				}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);
		if(time < 57)
			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.ROGUE, target != null ? 5 : 1);
		else
			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.ROGUE, 20);
		if(!state.equals(SkillKeyStates.KeyState.PRESS))
			return true;

		if(target != null)
		{
			for(PotionEffect effect : appliedPotions)
				target.addPotionEffect(effect);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.ROGUE, 3);
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);
		}

		return true;
	}
}