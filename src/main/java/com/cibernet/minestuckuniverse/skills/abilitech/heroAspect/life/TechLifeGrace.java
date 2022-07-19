package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechLifeGrace extends TechHeroAspect
{
	public TechLifeGrace(String name, long cost)
	{
		super(name, EnumAspect.LIFE, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().needFood())
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time >= 60 && state == SkillKeyStates.KeyState.RELEASED)
		{
			EntityLivingBase target = MSUUtils.getTargetEntity(player);

			if(target == null || badgeEffects.getSavingGraceTargets().contains(target.getUniqueID()) || MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, true))) return false;

			IBadgeEffects targetEffects = target.getCapability(MSUCapabilities.BADGE_EFFECTS, null);

			if(targetEffects.isSavingGraced()) return false;

			targetEffects.setSavingGraced(true);
			targetEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 20);
		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, time < 60 ? 2 : 10);
		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return !player.getFoodStats().needFood() && super.isUsableExternally(world, player);
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return super.canAppearOnList(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		return super.canUnlock(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDeathEvent(LivingDeathEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		IBadgeEffects badgeEffects = event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null);

		if(badgeEffects.isSavingGraced())
		{
			badgeEffects.setSavingGraced(false);
			event.setCanceled(true);

			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.LIFE, 20);
			target.world.playSound(null, target.posX, target.posY, target.posZ, SoundEvents.ENTITY_WITHER_SPAWN, target.getSoundCategory(), 1.0F, 3.0F);

			target.setHealth(target.getMaxHealth());
			target.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, (target.isPotionActive(MobEffects.ABSORPTION) ? target.getActivePotionEffect(MobEffects.ABSORPTION).getAmplifier() : 0) + 2, 1200, false, false));
		}

		badgeEffects.getSavingGraceTargets().clear();
	}
}
