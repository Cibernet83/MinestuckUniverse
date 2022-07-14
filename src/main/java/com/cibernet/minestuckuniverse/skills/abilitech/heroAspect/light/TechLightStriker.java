package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.light;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Collections;
import java.util.List;

public class TechLightStriker extends TechHeroAspect
{
	public TechLightStriker(String name, long cost) {
		super(name, EnumAspect.LIGHT, cost, EnumTechType.OFFENSE);//, EnumAspect.DOOM);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state.equals(SkillKeyStates.KeyState.PRESS))
		{
			EntityLivingBase target = MSUUtils.getTargetEntity(player);
			if(target != null)
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
					return false;
				PotionEffect effect = new PotionEffect(MobEffects.GLOWING, 1200, 0);
				effect.setCurativeItems(Collections.emptyList());
				target.addPotionEffect(effect);
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);
				return true;
			}
		}

        if(state == SkillKeyStates.KeyState.NONE || time > 15)
            return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 13)
		{
			if(player.getName().equals("Cibernet"))
				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, 20, 0x1ACCEF, 0x1CFFE0);
			else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIGHT, 20);
		}
		else
		{
			if(player.getName().equals("Cibernet"))
				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, 10, 0x1ACCEF, 0x1CFFE0);
			else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);
		}

		if(time == 15)
		{
			for(EntityLivingBase target : world.getEntities(EntityLivingBase.class, (entity) -> entity != player && entity.isPotionActive(MobEffects.GLOWING)))
			{
				if(player.isOnSameTeam(target) || MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
					continue;

				EntityLightningBolt lightning = new EntityLightningBolt(world, target.posX, target.posY, target.posZ, true);
				world.spawnEntity(lightning);
				world.addWeatherEffect(lightning);

				List<Entity> list = lightning.world.getEntitiesWithinAABBExcludingEntity(lightning, new AxisAlignedBB(lightning.posX - 3.0D, lightning.posY - 3.0D, lightning.posZ - 3.0D,
						lightning.posX + 3.0D, lightning.posY + 6.0D + 3.0D, lightning.posZ + 3.0D));

				for (Entity entity : list)
					if (!ForgeEventFactory.onEntityStruckByLightning(entity, lightning))
					{
						entity.attackEntityFrom(new EntityCritDamageSource("lightningBolt", player), 10);
						entity.onStruckByLightning(lightning);
					}

				if(player.getName().equals("Cibernet"))
					target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, 10, 0x1ACCEF, 0x1CFFE0);
				else target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 10);

				target.removePotionEffect(MobEffects.GLOWING);
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 5);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 8 && super.isUsableExternally(world, player);
	}
}
