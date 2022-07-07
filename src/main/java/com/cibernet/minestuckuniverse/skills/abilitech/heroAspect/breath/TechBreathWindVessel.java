package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class TechBreathWindVessel extends TechHeroAspect
{

	public TechBreathWindVessel(String name) {
		super(name, EnumAspect.BREATH, EnumTechType.UTILITY);
	}

	@Override
	public boolean canUse(World world, EntityPlayer player) {
		return !player.isPotionActive(MSUPotions.EARTHBOUND) && super.canUse(world, player) && !player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isMindflayed();
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.HELD)
		{
			badgeEffects.setWindFormed(false);
			return false;
		}

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
        {
	        badgeEffects.setWindFormed(false);
            player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
            return false;
        }

		badgeEffects.setWindFormed(true);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BREATH, 10);
		MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, 0x47E2FA, 5, player.posX, player.posY+1, player.posZ), player);
		MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, 0x4379E6, 5, player.posX, player.posY+1, player.posZ), player);

		if(time % 20 == 1 && !player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		return true;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLivingRender(RenderLivingEvent.Pre event)
	{
		if(!(event.getEntity() instanceof EntityPlayer))
			return;

		if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isWindFormed())
			event.setCanceled(true);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void renderHand(RenderHandEvent event)
	{
		if(Minecraft.getMinecraft().player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isWindFormed())
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onPlayerCollision(GetCollisionBoxesEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer))
			return;

		if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isWindFormed())
			for(AxisAlignedBB aabb : new ArrayList<>(event.getCollisionBoxesList()))
				if(Math.abs(aabb.maxX - aabb.minX) < 1 || Math.abs(aabb.maxY - aabb.minY) < 1 || Math.abs(aabb.maxZ - aabb.minZ) < 1)
					event.getCollisionBoxesList().remove(aabb);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isWindFormed())
			event.player.distanceWalkedModified = 0;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onPlayerPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent event)
	{
		if(event.getEntityPlayer() == null)
			return;

		if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isWindFormed())
			event.getEntityPlayer().moveRelative(0, -MathHelper.sin(event.getEntityPlayer().rotationPitch * 0.017453292F), MathHelper.cos(event.getEntityPlayer().rotationPitch * 0.017453292F), 0.4f);
	}
}
