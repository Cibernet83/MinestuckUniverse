package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechHopeCleansing extends TechHeroAspect
{
	public TechHopeCleansing(String name, long cost) {
		super(name, EnumAspect.HOPE, cost, EnumTechType.UTILITY);
	}

	protected static final int ENERGY_USE = 4;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(!state.equals(SkillKeyStates.KeyState.PRESS))
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < ENERGY_USE)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(target == null || target.getActivePotionMap().isEmpty())
			target = player;

		if(!target.getActivePotionMap().isEmpty() && !MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
		{
			target.clearActivePotions();
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HOPE, target != player ? 14 : 10);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 4 && super.isUsableExternally(world, player);
	}
}
