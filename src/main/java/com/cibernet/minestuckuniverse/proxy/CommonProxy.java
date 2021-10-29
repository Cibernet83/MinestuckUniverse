package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.api.MSUSplatcraftSupport;
import com.cibernet.minestuckuniverse.blocks.BlockArtifact;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.events.handlers.*;
import com.cibernet.minestuckuniverse.modSupport.BotaniaSupport;
import com.cibernet.minestuckuniverse.modSupport.CarryOnSupport;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
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
import com.cibernet.minestuckuniverse.world.storage.loot.MSULoot;
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
        MinecraftForge.EVENT_BUS.register(MSUPotions.class);
        MinecraftForge.EVENT_BUS.register(MSUEnchantments.class);
        MinecraftForge.EVENT_BUS.register(MSUSoundHandler.class);

        MinecraftForge.EVENT_BUS.register(KindAbstratus.class);
        MinecraftForge.EVENT_BUS.register(MSUKindAbstrata.class);
        MinecraftForge.EVENT_BUS.register(StrifeEventHandler.class);

        MSUEntities.registerEntities();
        MSUCapabilities.registerCapabilities();

        GameRegistry.registerTileEntity(TileEntityGristHopper.class, MinestuckUniverse.MODID + ":grist_hopper");
        GameRegistry.registerTileEntity(TileEntityAutoWidget.class, MinestuckUniverse.MODID + ":auto_widget");
        GameRegistry.registerTileEntity(TileEntityAutoCaptcha.class, MinestuckUniverse.MODID + ":auto_captcha");
        GameRegistry.registerTileEntity(TileEntityMachineChasis.class, MinestuckUniverse.MODID + ":machine_chasis");
        GameRegistry.registerTileEntity(TileEntityRedTransportalizer.class, MinestuckUniverse.MODID + ":red_transportalizer");
        GameRegistry.registerTileEntity(TileEntityParadoxTransportalizer.class, MinestuckUniverse.MODID + ":paradox_transportalizer");
        GameRegistry.registerTileEntity(TileEntityHolopad.class, MinestuckUniverse.MODID + ":holopad");
        GameRegistry.registerTileEntity(TileEntityPlatinumTransportalizer.class, MinestuckUniverse.MODID + ":platinum_transportalizer");
        GameRegistry.registerTileEntity(TileEntityEffectBeacon.class, MinestuckUniverse.MODID + ":effect_beacon");
        GameRegistry.registerTileEntity(TileEntityBoondollarRegister.class, MinestuckUniverse.MODID + ":porkhollow_vault");

    }

    public void init()
    {
        MSUBannerPatterns.init();

        MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ArmorEventHandler.class);
        MinecraftForge.EVENT_BUS.register(MSULoot.class);
        MinecraftForge.EVENT_BUS.register(IDBasedAlchemyHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockArtifact.class);
        PropertyEventHandler.register();

        NetworkRegistry.INSTANCE.registerGuiHandler(MinestuckUniverse.instance, new MSUGuiHandler());

        MSULandAspectRegistry.registerLands();
        MSUAlchemyRecipes.registerRecipes();
        MachineChasisRecipes.registerRecipes();

        MSUChannelHandler.setupChannel();
        MSUUtils.registerDeployList();
        MSULoot.registerLootClasses();

        if(MinestuckUniverse.isCarryOnLoaded)
            MinecraftForge.EVENT_BUS.register(CarryOnSupport.class);
    }

    public void postInit()
    {
        if(MinestuckUniverse.isBotaniaLoaded)
            BotaniaSupport.generateGristCosts();
        MinestuckUniverseItems.setPostInitVariables();

        if(MinestuckUniverse.isSplatcraftLodaded)
        {
            MinecraftForge.EVENT_BUS.register(MSUSplatcraftSupport.class);
            MSUSplatcraftSupport.registerColors();
        }
    }

    public void serverStarted()
    {
        IDBasedAlchemyHandler.calculateMaxID();
    }
}
