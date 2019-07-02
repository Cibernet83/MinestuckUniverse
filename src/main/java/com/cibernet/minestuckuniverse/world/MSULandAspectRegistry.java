package com.cibernet.minestuckuniverse.world;

import com.mraof.minestuck.world.lands.LandAspectRegistry;

public class MSULandAspectRegistry
{
    public static void registerLands()
    {
        LandAspectRegistry.registerLandAspect(new LandAspectThaum());
    }
}
