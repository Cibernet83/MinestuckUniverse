package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.light;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TechLightAutoGlorb extends TechLightGlorb
{
	public TechLightAutoGlorb(String name, long cost)
	{
		super(name, cost, EnumTechType.UTILITY, EnumTechType.PASSIVE);
	}

	@Override
	public boolean canUse(World world, EntityPlayer player) {
		return super.canUse(world, player) && player.capabilities.allowEdit;
	}

	@Override
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot)
	{
		if(((player.world.getLightFor(EnumSkyBlock.BLOCK, player.getPosition()) < 8)) &&
				   player.world.getBlockState(player.getPosition()).getMaterial() == Material.AIR)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 4);
			player.world.setBlockState(player.getPosition(), MinestuckUniverseBlocks.glorb.getDefaultState());
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}
}
