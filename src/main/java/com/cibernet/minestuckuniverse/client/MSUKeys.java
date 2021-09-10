package com.cibernet.minestuckuniverse.client;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.gui.GuiStrifeSwitcher;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.skaianet.SburbHandler;
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

	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(MSUKeys.class);
		ClientRegistry.registerKeyBinding(strifeKey);
	}

	@SubscribeEvent
	public static void onInput(InputEvent event)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		if(player == null)
			return;

		if(strifeKey.isPressed() && !ClientEditHandler.isActive())
		{
			if(player.getHeldItemMainhand().isEmpty() || StrifeEventHandler.isStackAssigned(player.getHeldItemMainhand()) || StrifeEventHandler.isStackAssigned(player.getHeldItemOffhand()))
				GuiStrifeSwitcher.showSwitcher = true;
			else if(!player.getHeldItemMainhand().isEmpty())
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ASSIGN_STRIFE, EnumHand.MAIN_HAND));
		}
	}

}