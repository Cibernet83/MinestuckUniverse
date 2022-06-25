package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechHopeGolem extends TechHeroAspect
{
	public TechHopeGolem(String name) {
		super(name, EnumAspect.HOPE, EnumTechType.OFFENSE, EnumAspect.SPACE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.HELD)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if(target instanceof EntityHopeGolem && ((EntityHopeGolem) target).getOwner() == player)
		{
			((EntityHopeGolem) target).setHopeTicks(((EntityHopeGolem) target).getHopeTicks() + Math.max(10-(int) (player.getHealth()/player.getMaxHealth()*10), 1)+10);

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HOPE, 4);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HOPE, 10);

			if((player.ticksExisted % 10) == 0 && !player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);
		}

		else if(time <= 160)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HOPE, (int) ((float) time / 320f * 20));

			if (time == 160)
			{
				EntityHopeGolem golem = new EntityHopeGolem(world);
				golem.setPosition(player.posX + world.rand.nextDouble() * 10 - 5d, player.posY, player.posZ + world.rand.nextDouble() * 10 - 5d);
				golem.setCreatedBy(player);
				golem.getLookHelper().setLookPosition(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, (float) golem.getHorizontalFaceSpeed(), (float) golem.getVerticalFaceSpeed());
				world.spawnEntity(golem);
			}
			if ((player.ticksExisted % 10) == 0 && !player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
		}

		return true;
	}
}
