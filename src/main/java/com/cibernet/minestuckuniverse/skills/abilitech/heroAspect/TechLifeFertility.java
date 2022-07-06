package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechLifeFertility extends TechHeroAspect
{
	public TechLifeFertility(String name) {
		super(name, EnumAspect.LIFE, EnumTechType.UTILITY);
	}

	protected static final int RADIUS = 20;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state.equals(SkillKeyStates.KeyState.NONE) || time >= 11)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 5)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIFE, 20);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 6);


		if(time >= 10)
		{
			for(EntityAnimal target : world.getEntitiesWithinAABB(EntityAnimal.class, player.getEntityBoundingBox().grow(RADIUS)))
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 3);

				target.setInLove(player);
			}
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);

			for(int x = -RADIUS; x <= RADIUS; x++)for(int y = RADIUS; y >= -RADIUS; y--)for(int z = -RADIUS; z <= RADIUS; z++)
			{
				BlockPos targetPos = new BlockPos(player.posX+x, player.posY+y, player.posZ+z);
				if(targetPos != null && world.getBlockState(targetPos).getBlock() instanceof IGrowable)
				{
					if(((IGrowable) world.getBlockState(targetPos).getBlock()).canGrow(world, targetPos, world.getBlockState(targetPos), world.isRemote))
					{
						//for(int i = 0; i < 3; i++)
							((IGrowable) world.getBlockState(targetPos).getBlock()).grow(world, world.rand, targetPos, world.getBlockState(targetPos));

						if(!world.isRemote)
							world.playEvent(2005, targetPos, 0);

						badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 4);
					}
				}
			}
		}


		return true;
	}
}
