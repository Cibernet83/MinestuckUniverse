package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.space;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechSpaceTele extends TechHeroAspect
{
	public TechSpaceTele(String name, long cost) {
		super(name, EnumAspect.SPACE, cost, EnumTechType.UTILITY);//, EnumAspect.BREATH);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS || time != 0)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		int reach = Math.max(player.getFoodStats().getFoodLevel() * 2, 0);
		RayTraceResult target = MSUUtils.getMouseOver(player, reach);

		if(target == null)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.badgeError"), true);
			return false;
		}

		BlockPos pos = target.entityHit == null ? target.getBlockPos() == null ? player.getPosition() : target.getBlockPos() : target.entityHit.getPosition();

		if(target.entityHit != null)
		{
			player.rotationYaw = target.entityHit.getRotationYawHead();
			player.prevRotationYaw = target.entityHit.getRotationYawHead();

			pos = pos.offset(EnumFacing.fromAngle(target.entityHit.getRotationYawHead()).getOpposite(), (int)Math.ceil(target.entityHit.width/2d));
		}
		if(target.sideHit != null)
			pos = pos.offset(target.sideHit, target.sideHit == EnumFacing.DOWN ? 2 : 1);

		double distance = player.getDistance(pos.getX(), pos.getY(), pos.getZ());

		if(!player.isCreative())
			player.getFoodStats().setFoodLevel((int) Math.max(0, player.getFoodStats().getFoodLevel()- (Math.floor(distance)/6) ));


		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 10);
		MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, 0x4BEC13, 10, player.posX, player.posY, player.posZ), player);

		Teleport.localTeleport(player, null, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}
