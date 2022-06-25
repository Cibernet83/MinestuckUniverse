package com.cibernet.minestuckuniverse.client;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.gui.GuiStrifeSwitcher;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class MSUKeys
{
	public static final KeyBinding strifeKey = new KeyBinding("key.minestuckuniverse.strife", Keyboard.KEY_V, "key.categories.minestuck");
	public static final KeyBinding strifeSelectorLeftKey = new KeyBinding("key.minestuckuniverse.strifeSelectorLeft", Keyboard.KEY_NONE, "key.categories.minestuck");
	public static final KeyBinding strifeSelectorRightKey = new KeyBinding("key.minestuckuniverse.strifeSelectorRight", Keyboard.KEY_NONE, "key.categories.minestuck");
	public static final KeyBinding swapOffhandStrifeKey = new KeyBinding("key.minestuckuniverse.swapOffhandStrife", Keyboard.KEY_NONE, "key.categories.minestuck");

	public static KeyBinding[] skillKeys;
	private static boolean[] downs;

	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(MSUKeys.class);
		ClientRegistry.registerKeyBinding(strifeKey);
		ClientRegistry.registerKeyBinding(strifeSelectorLeftKey);
		ClientRegistry.registerKeyBinding(strifeSelectorRightKey);
		ClientRegistry.registerKeyBinding(swapOffhandStrifeKey);

		skillKeys = new KeyBinding[SkillKeyStates.Key.values().length];
		downs = new boolean[SkillKeyStates.Key.values().length];

		skillKeys[SkillKeyStates.Key.PRIMARY.ordinal()] = new KeyBinding("key.minestuckuniverse.abilitechPrimary", Keyboard.KEY_J, "key.categories.minestuck");
		skillKeys[SkillKeyStates.Key.SECONDARY.ordinal()] = new KeyBinding("key.minestuckuniverse.abilitechSecondary", Keyboard.KEY_H, "key.categories.minestuck");
		skillKeys[SkillKeyStates.Key.TERTIARY.ordinal()] = new KeyBinding("key.minestuckuniverse.abilitechTertiary", Keyboard.KEY_K, "key.categories.minestuck");

		for (KeyBinding binding : skillKeys)
			ClientRegistry.registerKeyBinding(binding);
	}

	private static Boolean offhandMode = null;
	@SubscribeEvent
	public static void onInput(InputEvent event)
	{
		for(int i = 0; i < skillKeys.length; i++)
		{
			if (skillKeys[i].isKeyDown() ^ downs[i])
			{
				downs[i] = !downs[i];
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.KEY_INPUT, i, downs[i]));
				if(Minecraft.getMinecraft().player != null)
					Minecraft.getMinecraft().player.getCapability(MSUCapabilities.SKILL_KEY_STATES, null).updateKeyState(SkillKeyStates.Key.values()[i], downs[i]);
			}
		}

		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if(player == null)
			return;

		boolean strifeKeyDown = strifeKey.isKeyDown();
		boolean swapStrifeKeyDown = swapOffhandStrifeKey.isKeyDown();

		boolean strifePressed = offhandMode == null ? (strifeKeyDown || swapStrifeKeyDown) : offhandMode ? swapStrifeKeyDown : strifeKeyDown;
		if(strifePressed)
		{
			if(offhandMode == null)
			{
				offhandMode = !(!swapStrifeKeyDown && strifeKeyDown);
				GuiStrifeSwitcher.offhandMode = offhandMode;
				if(offhandMode || (player.getHeldItemMainhand().isEmpty() || StrifeEventHandler.isStackAssigned(player.getHeldItemMainhand()) || StrifeEventHandler.isStackAssigned(player.getHeldItemOffhand())))
					GuiStrifeSwitcher.showSwitcher = true;
				else if(!player.getHeldItemMainhand().isEmpty() && !player.getCapability(MSUCapabilities.STRIFE_DATA, null).isArmed())
				{
					MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ASSIGN_STRIFE, EnumHand.MAIN_HAND));
				}
			}
		} else offhandMode = null;
	}
}
