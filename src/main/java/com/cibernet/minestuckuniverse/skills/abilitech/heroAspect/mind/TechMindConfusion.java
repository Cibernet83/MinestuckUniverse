package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
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

import java.util.Collections;

public class TechMindConfusion extends TechHeroAspect
{
	public TechMindConfusion(String name, long cost) {
		super(name, EnumAspect.MIND, cost, EnumTechType.OFFENSE);//, EnumAspect.HOPE);
	}

	protected static final int ENERGY_USE = 9;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < ENERGY_USE)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 5);

		if(target != null && !MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
		{
			PotionEffect effect = new PotionEffect(MSUPotions.MIND_CONFUSION, 400, 0);
			effect.setCurativeItems(Collections.emptyList());
			target.addPotionEffect(effect);
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.MIND, 10);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 9 && super.isUsableExternally(world, player);
	}
}
