package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.damage.IGodTierDamage;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class TechLifeLeech extends TechHeroAspect
{
	public TechLifeLeech(String name, long cost) {
		super(name, EnumAspect.LIFE, cost, EnumTechType.OFFENSE);//, EnumAspect.BLOOD);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		if (state == SkillKeyStates.KeyState.RELEASED)
		{
			badgeEffects.clearTether(techSlot);
			return true;
		}

		EntityLivingBase target = badgeEffects.getTether(techSlot) instanceof EntityLivingBase ? (EntityLivingBase) badgeEffects.getTether(techSlot) : null;

		if(target == null)
		{
			target = MSUUtils.getTargetEntity(player);
			badgeEffects.setTether(target, techSlot);
		}
		
		if(target != null && target.getDistance(player) > 20)
		{
			target = null;
			badgeEffects.clearTether(techSlot);
		}
			
		if((time+1) % 20 != 0)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 5);
			if(target != null)
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 2);
			return true;
		}

		if (target != null)
		{
			System.out.println("leeching: " + target);
			if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, false)))
				return false;
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 10);

			target.hurtResistantTime = 0;
			target.attackEntityFrom(new EntityCritDamageSource("lifeforceLeech", player), 2);
			player.heal(2);

			if(target.hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 5);

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
		}
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 5);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}
