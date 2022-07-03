package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.BadgeEventHandler;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mraof.minestuck.util.MinestuckPlayerData.title;

public class TechHeir extends TechHeroClass
{
	public TechHeir(String name)
	{
		super(name, EnumClass.HEIR);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		return false;
	}

	private static void doHeirThings(EntityPlayer target, EntityLivingBase trueSource, float amount)
	{
		if(target != null && target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.HEIR_WILL))
		{
			if(amount == -1 || (target.world.rand.nextFloat() < Math.max(0.1f, amount/target.getHealth()*0.8f * (target.getLuck()/4f-0.2f))))
			{
				Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(target));
				if(title != null)
				{
					if(title.getHeroAspect() == EnumAspect.HOPE)
					{
						trueSource.setFire(5);
						trueSource.setAir(0);
					}
					PotionEffect effect = BadgeEventHandler.NEGATIVE_EFFECTS.get(title.getHeroAspect());
					trueSource.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier()));
					trueSource.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.HEIR, 5);
				}
			}
		}
	}

	// Bluh, these have the same functions but are different classes so they need separate checks
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayer) || !(event.getSource().getTrueSource() instanceof EntityLivingBase))
			return;

		doHeirThings((EntityPlayer) event.getEntityLiving(), (EntityLivingBase) event.getSource().getTrueSource(), event.getAmount());

		if(event.getSource().getImmediateSource() instanceof EntityPlayer)
		{
			EntityPlayer source = (EntityPlayer) event.getSource().getImmediateSource();
			Title sourceTitle = MinestuckPlayerData.getTitle(IdentifierHandler.encode(source));
			Title targetTitle = event.getEntityLiving() instanceof EntityPlayer ? MinestuckPlayerData.getTitle(IdentifierHandler.encode((EntityPlayer) event.getEntityLiving())) : null;

			if((sourceTitle != null || targetTitle != null) && source.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.UNIVERSAL_REVERSE) &&
					(source.world.rand.nextFloat() < Math.max(0.1f, (source.getHealth()-source.getMaxHealth())*0.65f * (source.getLuck()/4f-0.2f))))
			{
				PotionEffect effect = BadgeEventHandler.NEGATIVE_EFFECTS.get(targetTitle == null ? sourceTitle.getHeroAspect() : targetTitle.getHeroAspect());
				event.getEntityLiving().addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier()));
				event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.HEIR, 3);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayer) || !(event.getSource().getTrueSource() instanceof EntityLivingBase))
			return;

		doHeirThings((EntityPlayer) event.getEntityLiving(), (EntityLivingBase) event.getSource().getTrueSource(), -1);
	}
}
