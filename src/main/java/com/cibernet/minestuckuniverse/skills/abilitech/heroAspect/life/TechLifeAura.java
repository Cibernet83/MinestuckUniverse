package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechLifeAura extends TechHeroAspect
{
	public TechLifeAura(String name, long cost) {
		super(name, EnumAspect.LIFE, cost, EnumTechType.DEFENSE);//, EnumAspect.LIGHT);
	}

	protected static final int RADIUS = 8;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.PRESS)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 6)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 10);

			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 4));

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 6);
		}

		if(state == SkillKeyStates.KeyState.NONE || time >= 19)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 15)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIFE, 20);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 10);

		if(time >= 18)
		{
			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(RADIUS)))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
					continue;
				
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 10);
				target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 3));
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 6 && super.isUsableExternally(world, player);
	}
}
