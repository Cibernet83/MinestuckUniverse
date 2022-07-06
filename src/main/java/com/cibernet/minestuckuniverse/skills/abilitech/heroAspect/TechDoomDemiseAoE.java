package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TechDoomDemiseAoE extends TechHeroAspect
{
	private static final int RADIUS = 20;

	public TechDoomDemiseAoE(String name) {
		super(name, EnumAspect.DOOM, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().needFood())
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time < 100)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 25);
			return true;
		} else if(time > 100)
			return false;

		world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1, false);

		for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(RADIUS), (entity) -> entity != player && entity.getDistance(player) <= RADIUS) )
		{
			if (target.getHealth()/target.getMaxHealth() <= 0.2f)
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
