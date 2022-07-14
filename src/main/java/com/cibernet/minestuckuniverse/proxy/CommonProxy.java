package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MSUAlchemyRecipes;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.blocks.BlockArtifact;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.consortCosmetics.ConsortHatsData;
import com.cibernet.minestuckuniverse.captchalogue.MSUModi;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.entity.MSUEntities;
import com.cibernet.minestuckuniverse.events.handlers.*;
import com.cibernet.minestuckuniverse.gui.MSUGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.modSupport.BotaniaSupport;
import com.cibernet.minestuckuniverse.modSupport.CarryOnSupport;
import com.cibernet.minestuckuniverse.modSupport.MSUSplatcraftSupport;
import com.cibernet.minestuckuniverse.modSupport.TrophySlotsSupport;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechHeartBond;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart.TechHeartProject;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
import com.cibernet.minestuckuniverse.tileentity.*;
import com.cibernet.minestuckuniverse.util.MSUBannerPatterns;
import com.cibernet.minestuckuniverse.util.MSUConsorts;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.world.MSULandAspectRegistry;
import com.cibernet.minestuckuniverse.world.gen.WorldGenHandler;
import com.cibernet.minestuckuniverse.world.storage.loot.MSULoot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public CommonProxy(){}

    public void preInit()
    {
    	MinecraftForge.EVENT_BUS.register(TechHeartBond.class);
    	
        MinecraftForge.EVENT_BUS.register(new MinestuckUniverseGrist());
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseBlocks.class);
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseItems.class);
        MinecraftForge.EVENT_BUS.register(BackwardsCompatHandler.class);
        MinecraftForge.EVENT_BUS.register(MSUEnchantments.class);
        MinecraftForge.EVENT_BUS.register(MSUSoundHandler.class);

        MinecraftForge.EVENT_BUS.register(KindAbstratus.class);
        MinecraftForge.EVENT_BUS.register(MSUKindAbstrata.class);
        MinecraftForge.EVENT_BUS.register(StrifeEventHandler.class);


        MinecraftForge.EVENT_BUS.register(GTEventHandler.class);
        MinecraftForge.EVENT_BUS.register(KarmaEventHandler.class);
        MinecraftForge.EVENT_BUS.register(EditModeEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        MinecraftForge.EVENT_BUS.register(MSUConsorts.class);
        MinecraftForge.EVENT_BUS.register(EntityBubble.class);
        MinecraftForge.EVENT_BUS.register(LocalChatEventHandler.class);

        if(MinestuckUniverse.isTrophySlotsLoaded)
            MinecraftForge.EVENT_BUS.register(TrophySlotsSupport.class);

        MSUEntities.registerEntities();
        MSUCapabilities.registerCapabilities();
        BadgeEventHandler.registerBadgeEvents();

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


        PotionEventHandler.registerPotionEvents();
    }

    public void init()
    {
        MSUBannerPatterns.init();

        MinecraftForge.EVENT_BUS.register(MSUSkills.class);

        MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ArmorEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CaptchalogueEventHandler.class);
        MinecraftForge.EVENT_BUS.register(SaveHandler.class);
        MinecraftForge.EVENT_BUS.register(MSULoot.class);
        MinecraftForge.EVENT_BUS.register(IDBasedAlchemyHandler.class);
        MinecraftForge.EVENT_BUS.register(ConsortHatsData.class);
        MinecraftForge.EVENT_BUS.register(NullSoloSessionHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockArtifact.class);
        PropertyEventHandler.register();

        NetworkRegistry.INSTANCE.registerGuiHandler(MinestuckUniverse.instance, new MSUGuiHandler());

        MSULandAspectRegistry.registerLands();
        MSUAlchemyRecipes.registerRecipes();
        MachineChasisRecipes.registerRecipes();

        MSUChannelHandler.setupChannel();
        MSUUtils.registerDeployList();
        MSULoot.registerLootClasses();
        MSUModi.register();

        if(MinestuckUniverse.isCarryOnLoaded)
            MinecraftForge.EVENT_BUS.register(CarryOnSupport.class);

        GameRegistry.registerWorldGenerator(new WorldGenHandler(), 0);
    }

    public void postInit()
    {
        if(MinestuckUniverse.isBotaniaLoaded)
            BotaniaSupport.generateGristCosts();
        MinestuckUniverseItems.setPostInitVariables();

        MSUConsorts.setupCustomConsortAttributes();

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
