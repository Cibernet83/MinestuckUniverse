package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class TechBreathGale extends TechHeroAspect
{
	public TechBreathGale(String name, long cost) {
		super(name, EnumAspect.BREATH, cost, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
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

			if(time < 10)
				return false;

			player.setSprinting(false);
			player.capabilities.isFlying = false;
			player.motionY = Math.max(1, Math.min(3f, time/20f)+1);
			player.motionX = Math.sin(Math.toRadians(-player.rotationYaw))*player.motionY*0.7f;
			player.motionZ = Math.cos(Math.toRadians(-player.rotationYaw))*player.motionY*0.7f;
			player.velocityChanged = true;

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.BREATH, 40);

			if(!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-(int)(player.motionY));

			EntityLightningBolt lightning = new EntityLightningBolt(world, player.posX, player.posY-1d, player.posZ, true);
			world.spawnEntity(lightning);
			world.addWeatherEffect(lightning);

			List<Entity> list = lightning.world.getEntitiesWithinAABBExcludingEntity(lightning, new AxisAlignedBB(lightning.posX - 3.0D, lightning.posY - 3.0D, lightning.posZ - 3.0D,
					lightning.posX + 3.0D, lightning.posY + 6.0D + 3.0D, lightning.posZ + 3.0D));

			for (Entity entity : list)
			{
				if(entity == player) continue;
				if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, lightning))
				{
					entity.attackEntityFrom(new EntityCritDamageSource("lightningBolt", player), 12);
					entity.onStruckByLightning(lightning);
				}
			}
		}
		else
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BREATH, time < 30 ? 2 : 8);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 2 && super.isUsableExternally(world, player);
	}
}
