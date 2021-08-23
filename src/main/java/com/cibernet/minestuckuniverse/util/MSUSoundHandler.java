package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUSoundHandler
{
	public static final MSUSoundHandler instance = new MSUSoundHandler();
	
	public static SoundEvent homeRunBat = new SoundEvent(new ResourceLocation(MinestuckUniverse.MODID, "item.home_run_bat"));
	public static SoundEvent shieldParry = new SoundEvent(new ResourceLocation(MinestuckUniverse.MODID, "item.shield_parry"));
	public static SoundEvent whipCrack = new SoundEvent(new ResourceLocation(MinestuckUniverse.MODID, "item.whip_crack"));
	public static SoundEvent whipCrock = new SoundEvent(new ResourceLocation(MinestuckUniverse.MODID, "item.whip_crock"));

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		
		registry.register(homeRunBat.setRegistryName("item.home_run_bat"));
		registry.register(shieldParry.setRegistryName("item.shield_parry"));
		registry.register(whipCrack.setRegistryName("item.whip_crack"));
		registry.register(whipCrock.setRegistryName("item.whip_crock"));
	}
}
