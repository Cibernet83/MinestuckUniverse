package com.cibernet.minestuckuniverse.skills.abilitech.heroAspectUtil;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechLifeBoneMeal extends TechHeroAspect
{
	public TechLifeBoneMeal(String name) {
		super(name, EnumAspect.LIFE, EnumRole.NEUTRAL);
	}


	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state.equals(SkillKeyStates.KeyState.NONE) || time >= 16)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time % 10 == 1)
		{
			BlockPos targetPos = MSUUtils.getTargetBlock(player);

			if(targetPos != null && world.getBlockState(targetPos).getBlock() instanceof IGrowable)
			{
				IGrowable growable = (IGrowable) world.getBlockState(targetPos).getBlock();
				if(growable.canGrow(world, targetPos, world.getBlockState(targetPos), world.isRemote))
				{
					growable.grow(world, world.rand, targetPos, world.getBlockState(targetPos));

					if(!world.isRemote)
						world.playEvent(2005, targetPos, 0);

					badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 4);
					if (!player.isCreative() && !world.isRemote)
						player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
				}
			}
		}

		return true;
	}
}
