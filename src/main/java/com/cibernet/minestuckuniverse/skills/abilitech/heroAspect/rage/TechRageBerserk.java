package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechRageBerserk extends TechHeroAspect
{
	public TechRageBerserk(String name, long cost) {
		super(name, EnumAspect.RAGE, cost, EnumTechType.OFFENSE);//, EnumAspect.HOPE);
	}

	protected static final int ENERGY_USE = 3;

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

		if(!player.isPotionActive(MSUPotions.RAGE_BERSERK))
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);

			player.addPotionEffect(new PotionEffect(MSUPotions.RAGE_BERSERK, 1200, 0));
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 3 && super.isUsableExternally(world, player);
	}
}
