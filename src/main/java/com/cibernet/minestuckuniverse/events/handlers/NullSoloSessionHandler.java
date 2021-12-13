package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.mraof.minestuck.event.GenerateLandTerrainEvent;
import com.mraof.minestuck.event.GenerateLandTitleEvent;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NullSoloSessionHandler
{
	@SubscribeEvent
	public static void onGenerateLandTitle(GenerateLandTitleEvent event)
	{
		if (MSUConfig.nullSoloSessions && event.getConnection().getClientIdentifier().equals(event.getConnection().getServerIdentifier()))
			event.setLandTitle(LandAspectRegistry.fromNameTitle("null"));
	}

	@SubscribeEvent
	public static void onGenerateLandTerrain(GenerateLandTerrainEvent event)
	{
		if (MSUConfig.nullSoloSessions && event.getConnection().getClientIdentifier().equals(event.getConnection().getServerIdentifier()))
			event.setLandTerrain(LandAspectRegistry.fromNameTerrain("null"));
	}
}
