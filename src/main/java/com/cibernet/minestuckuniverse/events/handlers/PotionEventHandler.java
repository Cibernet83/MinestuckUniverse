package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.potions.*;
import net.minecraftforge.common.MinecraftForge;

public class PotionEventHandler
{
	public static void registerPotionEvents()
	{
		MinecraftForge.EVENT_BUS.register(PotionConceal.class);
		MinecraftForge.EVENT_BUS.register(PotionMouseSensitivityAdjusterBase.class);
		MinecraftForge.EVENT_BUS.register(PotionTimeStop.class);
		MinecraftForge.EVENT_BUS.register(PotionConfusion.class);
		MinecraftForge.EVENT_BUS.register(PotionComeback.class);
		MinecraftForge.EVENT_BUS.register(PotionDecay.class);
	}
}
