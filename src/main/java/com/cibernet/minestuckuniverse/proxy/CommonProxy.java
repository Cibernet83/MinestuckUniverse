package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public CommonProxy(){}

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseGrist.class);
    }

    public void init()
    {

    }
}
