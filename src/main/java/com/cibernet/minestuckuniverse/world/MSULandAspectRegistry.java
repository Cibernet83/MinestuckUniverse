package com.cibernet.minestuckuniverse.world;

import com.mraof.minestuck.world.lands.LandAspectRegistry;

public class MSULandAspectRegistry
{
    public static void registerLands()
    {
        LandAspectRegistry.registerLandAspectHidden(new LandAspectBedrock());
        //LandAspectRegistry.registerLandAspect(new LandAspectThaum());
       // LandAspectRegistry.registerLandAspect(new LandAspectBlobs());
    }
}
