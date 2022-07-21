package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.doom;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life.TechLifeChloroball;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechDoomDemiseAoE extends TechHeroAspect
{
	private static final int RADIUS = 20;

	public TechDoomDemiseAoE(String name, long cost) {
		super(name, EnumAspect.DOOM, cost, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(time < 100)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 25);
			return true;
		} else if(time > 100)
			return false;

		world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1, false);

		for(EntityPlayer target : world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(RADIUS), (entity) -> entity != player && entity.getDistance(player) <= RADIUS) )
		{
			if (!player.isCreative() && target.getHealth()/target.getMaxHealth() <= 0.2f &&
					!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
				karmakill(target, player);
		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.DOOM, 25);
		player.getFoodStats().setFoodLevel(0);
		karmakill(player, player);

		return true;
	}

	private static void karmakill(EntityLivingBase target, EntityPlayer player)
	{
		target.attackEntityFrom(new TechDoomDemise.DoomDamageSource(player), Float.MAX_VALUE);
	}
}
