package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class TechTimeAcceleration extends TechHeroAspect
{
	private static final Random random = new Random();

	public TechTimeAcceleration(String name, long cost) {
		super(name, EnumAspect.TIME, cost, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time) {
		if(!state.equals(SkillKeyStates.KeyState.HELD))
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		BlockPos target = MSUUtils.getTargetBlock(player);
		if (target == null)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, 2);
			return true;
		}

		IBlockState blockState = world.getBlockState(target);
		blockState.getBlock().updateTick(world, target, blockState, random);

		MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, BadgeEffects.getAspectParticleColors(EnumAspect.TIME)[0], 2, target), player);

		TileEntity tileEntity = world.getTileEntity(target);
		if (tileEntity instanceof ITickable)
			for(int i = 0; i < 3; i++)
				((ITickable)tileEntity).update();

		if (time % 20 == 0 && !player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, 2);

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player) {
		return super.isUsableExternally(world, player) && player.getFoodStats().getFoodLevel() > 0;
	}
}
