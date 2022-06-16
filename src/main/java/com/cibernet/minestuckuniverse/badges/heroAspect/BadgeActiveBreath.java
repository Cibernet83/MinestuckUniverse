package com.cibernet.minestuckuniverse.badges.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class BadgeActiveBreath extends BadgeHeroAspect
{
	public BadgeActiveBreath()
	{
		super(EnumAspect.BREATH, EnumRole.ACTIVE, EnumAspect.LIGHT);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 0)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(state == SkillKeyStates.KeyState.RELEASED)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			if(time < 30)
				return false;

			player.setSprinting(false);
			player.capabilities.isFlying = false;
			player.motionY = Math.min(2, Math.max(1.2f, time/30f));
			player.velocityChanged = true;

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.BREATH, 40);

			if(!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-2);

			EntityLightningBolt lightning = new EntityLightningBolt(world, player.posX, player.posY-1d, player.posZ, true);
			world.spawnEntity(lightning);
			world.addWeatherEffect(lightning);

			List<Entity> list = lightning.world.getEntitiesWithinAABBExcludingEntity(lightning, new AxisAlignedBB(lightning.posX - 3.0D, lightning.posY - 3.0D, lightning.posZ - 3.0D,
					lightning.posX + 3.0D, lightning.posY + 6.0D + 3.0D, lightning.posZ + 3.0D));

			for (Entity entity : list)
			{
				if(entity == player) continue;
				if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, lightning))
					entity.onStruckByLightning(lightning);
			}
		}
		else
		{
			if(time % 10 == 0 && !player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BREATH, time < 30 ? 2 : 8);
		}

		return true;
	}
}
