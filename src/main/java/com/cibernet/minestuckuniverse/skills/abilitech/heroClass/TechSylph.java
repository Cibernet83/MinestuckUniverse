package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TechSylph extends TechHeroClass
{
	public TechSylph(String name)
	{
		super(name, EnumClass.SYLPH);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(!state.equals(SkillKeyStates.KeyState.HELD))
			return false;

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(target == null || !(target.getHealth() < target.getMaxHealth() || (target instanceof EntityPlayer && ((EntityPlayer) target).getFoodStats().needFood())))
			return false;

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.SYLPH, 5);

		if((time % 10) == 0)
		{
			target.heal(2);

			if((time % 20) == 0 && target instanceof EntityPlayer && ((EntityPlayer) target).getFoodStats().needFood())
				((EntityPlayer) target).getFoodStats().addStats(1, 2);



			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
			((WorldServer)world).spawnParticle(EnumParticleTypes.HEART, target.posX + ((Math.random()-0.5)/2), target.posY+1.5, target.posZ + ((Math.random()-0.5)/2), 1, 1, 0, 0.5, 0);
		}

		return true;
	}
}