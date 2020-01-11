package com.cibernet.minestuckuniverse.client;

import com.cibernet.minestuckuniverse.events.ServerEventHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MSUKeyHandler
{
	public static final MSUKeyHandler instance = new MSUKeyHandler();
	
	private boolean heroKeyHeld = false;
	
	public KeyBinding heroPowerKey;
	
	public void registerKeys()
	{
		heroPowerKey = new KeyBinding("key.heroPower", 44, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(heroPowerKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		while(heroPowerKey.isPressed() && !ServerEventHandler.heroHold)
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.HERO_POWER, new Object[] {true}));
		
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(!heroPowerKey.isKeyDown() && ServerEventHandler.heroHold)
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.HERO_POWER, new Object[] {false}));
	}
}
