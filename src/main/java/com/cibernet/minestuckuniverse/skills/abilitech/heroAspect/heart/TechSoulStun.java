package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.gui.GuiSoulStun;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class TechSoulStun extends TechHeroAspect
{
	public TechSoulStun(String name) {
		super(name, EnumAspect.HEART, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		EntityLivingBase target = state == SkillKeyStates.KeyState.NONE ? null : MSUUtils.getTargetEntity(player);
		EntityLivingBase oldTarget = badgeEffects.getTether(techSlot) instanceof EntityLivingBase ? (EntityLivingBase) badgeEffects.getTether(techSlot) : null;

		if(oldTarget != target)
		{
			if(oldTarget != null)
			{
				oldTarget.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setSoulShocked(false);
				if(oldTarget instanceof EntityLiving)
					((EntityLiving) oldTarget).setNoAI(false);
			}

			if(target != null)
			{
				IBadgeEffects targetEffects = target.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
				targetEffects.setSoulShocked(true);
				if(target instanceof EntityLiving)
					((EntityLiving) target).setNoAI(true);

				if(player.getName().equals("Cibernet"))
					targetEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, 1, 0xFFB745, 0xFF7929);
				else targetEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, 1);
			}

			badgeEffects.setTether(target, techSlot);
		}

		if(state == SkillKeyStates.KeyState.NONE) return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if (!player.isCreative() && time % 10 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);

		if(player.getName().equals("Cibernet"))
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, 2, 0xFFB745, 0xFF7929);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 2);

		return true;
	}

	@SubscribeEvent
	public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		event.player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setSoulShocked(false);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isSoulShocked())
		{
			if(Minecraft.getMinecraft().currentScreen instanceof GuiSoulStun)
			{
				if(Keyboard.isKeyDown(1))
				{
					GuiIngameMenu gui = new GuiIngameMenu();
					Minecraft.getMinecraft().displayGuiScreen(gui);
					Minecraft.getMinecraft().currentScreen = gui;

					Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
				}
			}
			else if(!(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu))
				Minecraft.getMinecraft().currentScreen = new GuiSoulStun();
		} else if(Minecraft.getMinecraft().currentScreen instanceof GuiSoulStun)
		{
			Minecraft.getMinecraft().currentScreen.onGuiClosed();
			Minecraft.getMinecraft().currentScreen = null;
		}

		if(Minecraft.getMinecraft().currentScreen instanceof GuiSoulStun)
		{
			Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
			Minecraft.getMinecraft().inGameHasFocus = false;
		}
	}

	public static class Target{}
}
