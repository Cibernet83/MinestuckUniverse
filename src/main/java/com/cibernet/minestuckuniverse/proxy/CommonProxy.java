package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.MSUBannerPatterns;
import com.cibernet.minestuckuniverse.MSUUtils;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MSUAlchemyRecipes;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.entity.MSUEntities;
import com.cibernet.minestuckuniverse.gui.MSUGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import com.cibernet.minestuckuniverse.tileentity.TileEntityGristHopper;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import com.cibernet.minestuckuniverse.tileentity.TileEntityRedTransportalizer;
import com.cibernet.minestuckuniverse.world.MSULandAspectRegistry;
import com.mraof.minestuck.client.gui.playerStats.GuiGristCache;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

import java.util.ArrayList;
import java.util.List;

public class CommonProxy
{
    public CommonProxy(){}

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new MinestuckUniverseGrist());
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseBlocks.class);
        MinecraftForge.EVENT_BUS.register(MinestuckUniverseItems.class);
        MSUBannerPatterns.init();
        MSUEntities.registerEntities();

        GameRegistry.registerTileEntity(TileEntityGristHopper.class, MinestuckUniverse.MODID + ":grist_hopper");
        GameRegistry.registerTileEntity(TileEntityMachineChasis.class, MinestuckUniverse.MODID + ":machine_chasis");
        GameRegistry.registerTileEntity(TileEntityRedTransportalizer.class, MinestuckUniverse.MODID + ":red_transportalizer");

    }

    public void init()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MinestuckUniverse.instance, new MSUGuiHandler());

        MSULandAspectRegistry.registerLands();
        MSUAlchemyRecipes.registerRecipes();
        MachineChasisRecipes.registerRecipes();

        MSUChannelHandler.setupChannel();
        MSUUtils.registerDeployList();
    }
}
