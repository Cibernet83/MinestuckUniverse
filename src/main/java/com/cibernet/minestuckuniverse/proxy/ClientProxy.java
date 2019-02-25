package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.MSUModelManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(MSUModelManager.class);
    }
}
