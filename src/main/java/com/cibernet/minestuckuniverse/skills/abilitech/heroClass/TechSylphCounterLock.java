package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechSylphCounterLock extends TechHeroClass
{
	public TechSylphCounterLock(String name)
	{
		super(name, EnumClass.SYLPH);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(!state.equals(SkillKeyStates.KeyState.HELD))
			return false;


		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(target == null || !target.isPotionActive(MSUPotions.GOD_TIER_LOCK))
			return false;


		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.SYLPH, 5);

		for(int i = 0; i < 3; i++)
			target.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).onUpdate(target);

		if((time % 10) == 0)
		{
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
		}

		return true;
	}
}
