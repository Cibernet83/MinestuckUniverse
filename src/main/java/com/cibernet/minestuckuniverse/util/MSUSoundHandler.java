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
	
	public static SoundEvent murica;
	public static SoundEvent murica_south;
	
	public static void initSounds()
	{
		ResourceLocation loc = new ResourceLocation(MinestuckUniverse.MODID, "item.murica");
		murica = new SoundEvent(loc).setRegistryName(loc);
		loc = new ResourceLocation(MinestuckUniverse.MODID, "item.muricaSouth");
		murica_south = new SoundEvent(loc).setRegistryName(loc);
	}
	
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		
		registry.register(murica);
		registry.register(murica_south);
	}
}
