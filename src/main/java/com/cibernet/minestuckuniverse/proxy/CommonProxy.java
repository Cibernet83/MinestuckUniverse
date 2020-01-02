package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.fillerItems.MSUFillerAlchemyRecipes;
import com.cibernet.minestuckuniverse.fillerItems.MSUFillerBlocks;
import com.cibernet.minestuckuniverse.fillerItems.MSUFillerItems;
import com.cibernet.minestuckuniverse.tileentity.*;
import com.cibernet.minestuckuniverse.util.MSUBannerPatterns;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MSUAlchemyRecipes;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.entity.MSUEntities;
import com.cibernet.minestuckuniverse.gui.MSUGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import com.cibernet.minestuckuniverse.world.MSULandAspectRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public CommonProxy(){}

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new MinestuckUniverseGrist());
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseBlocks.class);
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseItems.class);
        MinecraftForge.EVENT_BUS.register(MSUSoundHandler.instance);
        
        if(MinestuckUniverse.fillerItemsEnabled)
        {
            MinecraftForge.EVENT_BUS.register(MSUFillerItems.class);
            MinecraftForge.EVENT_BUS.register(MSUFillerBlocks.class);
        }
        
        MSUSoundHandler.initSounds();
        MSUBannerPatterns.init();
        MSUEntities.registerEntities();

        GameRegistry.registerTileEntity(TileEntityGristHopper.class, MinestuckUniverse.MODID + ":grist_hopper");
        GameRegistry.registerTileEntity(TileEntityAutoWidget.class, MinestuckUniverse.MODID + ":auto_widget");
        GameRegistry.registerTileEntity(TileEntityAutoCaptcha.class, MinestuckUniverse.MODID + ":auto_captcha");
        GameRegistry.registerTileEntity(TileEntityMachineChasis.class, MinestuckUniverse.MODID + ":machine_chasis");
        GameRegistry.registerTileEntity(TileEntityRedTransportalizer.class, MinestuckUniverse.MODID + ":red_transportalizer");
        GameRegistry.registerTileEntity(TileEntityParadoxTransportalizer.class, MinestuckUniverse.MODID + ":paradox_transportalizer");
        GameRegistry.registerTileEntity(TileEntityHolopad.class, MinestuckUniverse.MODID + ":holopad");

    }

    public void init()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MinestuckUniverse.instance, new MSUGuiHandler());

        MSULandAspectRegistry.registerLands();
        MSUAlchemyRecipes.registerRecipes();
        MSUFillerAlchemyRecipes.registerRecipes();
        MachineChasisRecipes.registerRecipes();

        MSUChannelHandler.setupChannel();
        MSUUtils.registerDeployList();
    }
}
