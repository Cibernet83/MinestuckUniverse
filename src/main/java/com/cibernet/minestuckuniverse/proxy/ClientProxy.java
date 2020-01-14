package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.entity.render.RenderHologram;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.client.MSURenderMachineOutline;
import com.cibernet.minestuckuniverse.entity.classes.EntityAcheron;
import com.cibernet.minestuckuniverse.entity.models.ModelAcheron;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();

        MinestuckUniverseItems.setClientsideVariables();

        RenderingRegistry.registerEntityRenderingHandler(EntityAcheron.class, RenderEntityMinestuck.getFactory(new ModelAcheron(), 0.5F));
        MinecraftForge.EVENT_BUS.register(MSUModelManager.class);
        MinecraftForge.EVENT_BUS.register(MSURenderMachineOutline.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHolopad.class, new RenderHologram());
    }

    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(MSUChannelHandler.instance);
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}
