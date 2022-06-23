package com.cibernet.minestuckuniverse.skills.abilitech.heroAspectUtil;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TechLightAutoGlorb extends TechLightGlorb
{
	public TechLightAutoGlorb(String name)
	{
		super(name);
	}

	@Override
	public void onPassiveToggle(World world, EntityPlayer player, boolean active)
	{
		super.onEquipped(world, player);
		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setGlorbbing(active);
	}

	@Override
	public void onEquipped(World world, EntityPlayer player) {
		onPassiveToggle(world, player, player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this));
	}

	@Override
	public void onUnequipped(World world, EntityPlayer player) {
		onPassiveToggle(world, player, false);
	}

	@Override
	public boolean canUse(World world, EntityPlayer player) {
		return super.canUse(world, player) && player.capabilities.allowEdit;
	}

	@Override
	public boolean onEquippedTick(World world, EntityPlayer player, IBadgeEffects badgeEffects)
	{
		if(((badgeEffects.isGlorbbing() &&
				player.world.getLightFor(EnumSkyBlock.BLOCK, player.getPosition()) < 8)) &&
				   player.world.getBlockState(player.getPosition()).getMaterial() == Material.AIR)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT, 4);
			player.world.setBlockState(player.getPosition(), MinestuckUniverseBlocks.glorb.getDefaultState());
			return true;
		}
		else
			return false;
	}
}
