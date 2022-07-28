package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.doom;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.events.handlers.KarmaEventHandler;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class TechDoomDemise extends TechHeroAspect
{
	public TechDoomDemise(String name, long cost) {
		super(name, EnumAspect.DOOM, cost, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state != SkillKeyStates.KeyState.PRESS)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 16)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(target != null && MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, false)))
			target = null;

		float playerPctg = player.getHealth()/player.getMaxHealth();
		float targetPctg = target == null ? 1 : target.getHealth()/target.getMaxHealth();

		if (targetPctg > 0.4f)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 10);
			return true;
		}

		if (!player.isCreative() && targetPctg <= 0.4f)
		{
			karmakill(target, player);
			if (playerPctg <= 0.5f)
				karmakill(player, player);
		}

		if(!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 16);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 20);
		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 16 && super.isUsableExternally(world, player);
	}

	private static void karmakill(EntityLivingBase target, EntityPlayer player)
	{
		target.attackEntityFrom(new DoomDamageSource(player), Float.MAX_VALUE);
	}

	public static class DoomDamageSource extends CritDamageSource
	{
		protected Entity damageSourceEntity;


		public DoomDamageSource(Entity damageSourceEntityIn)
		{
			super("terminalDemise");
			setDamageBypassesArmor();
			setDamageAllowedInCreativeMode();
			this.damageSourceEntity = damageSourceEntityIn;
			setGodproof();
		}

		public Entity getTrueSource()
		{
			return this.damageSourceEntity;
		}

		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
		{
			String s = "death.attack." + this.damageType;
			String s1 = s + ".external";
			return entityLivingBaseIn != damageSourceEntity && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()});
		}

		/**
		 * Gets the location from which the damage originates.
		 */
		@Nullable
		public Vec3d getDamageLocation()
		{
			return new Vec3d(this.damageSourceEntity.posX, this.damageSourceEntity.posY, this.damageSourceEntity.posZ);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getSource() instanceof DoomDamageSource && event.getSource().canHarmInCreative())
			event.setCanceled(true);

	}
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public static void onLivingDeathPost(LivingDeathEvent event)
	{
		if(event.getSource() instanceof DoomDamageSource && event.getSource().canHarmInCreative())
			event.setCanceled(false);

	}
}
