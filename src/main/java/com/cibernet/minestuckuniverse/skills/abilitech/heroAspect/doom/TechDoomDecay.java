package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.doom;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechDoomDecay extends TechHeroAspect
{
	public TechDoomDecay(String name, long cost) {
		super(name, EnumAspect.DOOM, cost, EnumTechType.OFFENSE);
	}

	protected static final int RADIUS = 16;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE || time >= 26)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 20)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.DOOM, 20);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 10);

		if(time >= 25)
		{
			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(RADIUS), p -> !p.equals(player)))
			{
				if(!(target instanceof EntityPlayer || target instanceof IMob) ||
						MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, null)))
					continue;

				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.DOOM, 10);

				PotionEffect effect = new PotionEffect(MSUPotions.DECAY, player.isOnSameTeam(target) ? 180 : 400, 1);
				target.addPotionEffect(effect);
			}
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 8 && super.isUsableExternally(world, player);
	}
}
