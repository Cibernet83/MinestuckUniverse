package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechLifeChloroball extends TechHeroAspect
{
	public TechLifeChloroball(String name, long cost) {
		super(name, EnumAspect.LIFE, cost, EnumTechType.UTILITY);
	}

	public TechLifeChloroball(String name, int cost, EnumTechType... types)
	{
		super(name, EnumAspect.LIFE, cost, types);
	}

	@Override
	public boolean canUse(World world, EntityPlayer player) {
		return super.canUse(world, player) && player.capabilities.allowEdit;
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.NONE )
			if(player.world.getBlockState(player.getPosition()).getMaterial() == Material.AIR)
			{
				if(time < 20 && !player.isCreative() && player.getFoodStats().getFoodLevel() < 6)
				{
					player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
					return false;
				}

				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 2);
				if(time == 20)
				{
					if (!player.isCreative())
						player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 6);

					player.world.setBlockState(player.getPosition(), MinestuckUniverseBlocks.chloroball.getDefaultState());
				}

				return true;
			}
		return false;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 6 && super.isUsableExternally(world, player);
	}
}
