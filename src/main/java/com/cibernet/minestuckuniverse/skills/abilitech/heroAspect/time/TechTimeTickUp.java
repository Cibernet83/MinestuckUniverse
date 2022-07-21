package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TechTimeTickUp extends TechHeroAspect
{
	public TechTimeTickUp(String name, long cost) {
		super(name, EnumAspect.TIME, cost, EnumTechType.HYBRID);
	}


	private static boolean ticking = false;
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);

			if(badgeEffects.getTether(techSlot) != null && badgeEffects.getTether(techSlot).hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
				badgeEffects.getTether(techSlot).getCapability(MSUCapabilities.BADGE_EFFECTS, null).increaseTickUpStacks(-1);
			badgeEffects.clearTether(techSlot);

			return false;
		}
		
		if (state == SkillKeyStates.KeyState.RELEASED)
		{
			if(badgeEffects.getTether(techSlot) != null && badgeEffects.getTether(techSlot).hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
				badgeEffects.getTether(techSlot).getCapability(MSUCapabilities.BADGE_EFFECTS, null).increaseTickUpStacks(-1);
			badgeEffects.clearTether(techSlot);
			return true;
		}

		Entity target = badgeEffects.getTether(techSlot);
		
		if(target == null)
		{
			target = MSUUtils.getMouseOver(world, player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue(), true).entityHit;
			badgeEffects.setTether(target, techSlot);
			if(target != null)
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).increaseTickUpStacks(1);
		}
		
		if(target != null && target.getDistance(player) > 20)
		{
			target = null;
			badgeEffects.clearTether(techSlot);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).increaseTickUpStacks(-1);
		}

		if (target != null)
		{
			if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
				return false;
			
			if(!player.isCreative() && time % 20 == 0)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-2);

			if(!ticking)
			{
				ticking = true;
				target.onUpdate();
			}
			ticking = false;

			if(target.hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.TIME, 2);
			}
			else
				MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, BadgeEffects.getAspectParticleColors(EnumAspect.TIME)[0], 2, target), player);
		}
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, target == null ? 2 : 5);

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 2 && super.isUsableExternally(world, player);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player == null || event.phase == TickEvent.Phase.END || Minecraft.getMinecraft().isIntegratedServerRunning())
			return;

		for (int i = 0; i >= 0 && i < player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getTickUpStacks(); i++)
			player.onUpdate();
	}

	@SubscribeEvent
	public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		IGodTierData cap = event.player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		if(cap.isTechEquipped(MSUSkills.TIME_ACCELERANDO))
		{
			for(int i = 0; i < cap.getTechSlots(); i++)
				if(cap.getTech(i) instanceof TechTimeTickUp)
				{
					Entity tether = event.player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getTether(i);
					if(tether != null && tether.hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
						tether.getCapability(MSUCapabilities.BADGE_EFFECTS, null).increaseTickUpStacks(-1);
				}
		}

	}
}
