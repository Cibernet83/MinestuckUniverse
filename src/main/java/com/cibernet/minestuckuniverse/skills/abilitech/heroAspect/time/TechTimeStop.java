package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechTimeStop extends TechHeroAspect
{
	public TechTimeStop(String name, long cost) {
		super(name, EnumAspect.TIME, cost, EnumTechType.OFFENSE);//, EnumAspect.SPACE);
	}

	protected static final int ENERGY_USE = 8;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < ENERGY_USE)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, time >= 40 ? 5 : 2);

		if(state == SkillKeyStates.KeyState.RELEASED && time >= 40)
		{
			EntityLivingBase target = MSUUtils.getTargetEntity(player);

			if(target != null)
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
					return false;
				target.addPotionEffect(new PotionEffect(MSUPotions.TIME_STOP, 80, 0));
				if (!player.isCreative())
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);

				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.TIME, 10);
			}
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 8 && super.isUsableExternally(world, player);
	}
}
