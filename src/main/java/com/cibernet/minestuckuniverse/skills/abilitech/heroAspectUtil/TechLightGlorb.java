package com.cibernet.minestuckuniverse.skills.abilitech.heroAspectUtil;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TechLightGlorb extends TechHeroAspect
{
	public TechLightGlorb(String name) {
		super(name, EnumAspect.LIGHT, EnumTechType.UTILITY);
	}

	@Override
	public boolean canUse(World world, EntityPlayer player) {
		return super.canUse(world, player) && player.capabilities.allowEdit;
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.PRESS)
			if(player.world.getBlockState(player.getPosition()).getMaterial() == Material.AIR) {

				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 4);
				player.world.setBlockState(player.getPosition(), MinestuckUniverseBlocks.glorb.getDefaultState());
				return true;
			}
		return false;
	}
}
