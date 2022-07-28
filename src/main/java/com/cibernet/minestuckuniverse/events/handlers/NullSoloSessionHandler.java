package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.world.LandAspectBedrock;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.event.GenerateLandTerrainEvent;
import com.mraof.minestuck.event.GenerateLandTitleEvent;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

	@SubscribeEvent
	public static void onSpawnUnderling(EntityJoinWorldEvent event)
	{

		if(event.getEntity() instanceof EntityUnderling && MinestuckDimensionHandler.isLandDimension(event.getWorld().provider.getDimension()) &&
				MinestuckDimensionHandler.getAspects(event.getWorld().provider.getDimension()).aspectTerrain instanceof LandAspectBedrock && event.getEntity().posZ + event.getEntity().posX > 64)
		{
			((EntityUnderling)event.getEntity()).applyGristType(GristType.Artifact, true);
		}
	}
}
