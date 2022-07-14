package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.commands.GlobalSayCommand;
import com.cibernet.minestuckuniverse.commands.MSGTCommand;
import com.cibernet.minestuckuniverse.commands.SkillCommand;
import com.cibernet.minestuckuniverse.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;


@Mod(
        modid = MinestuckUniverse.MODID,
        name = MinestuckUniverse.NAME,
        version = MinestuckUniverse.VERSION,
        dependencies = "required-after:minestuck@[1.4.302,);",
        guiFactory = "com.cibernet.minestuckuniverse.gui.MSUGuiFactory"
)
public class MinestuckUniverse
{
    public static final String MODID = "minestuckuniverse";
    public static final String NAME = "Minestuck Universe";
    public static final String SHORT = "MSU";
    public static final String VERSION = "@VERSION@";
    
    @Mod.Instance("minestuckuniverse")
    public static MinestuckUniverse instance;
    @SidedProxy(
            clientSide = "com.cibernet.minestuckuniverse.proxy.ClientProxy",
            serverSide = "com.cibernet.minestuckuniverse.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public static boolean isThaumLoaded;
    public static boolean isBotaniaLoaded;
    public static boolean isSplatcraftLodaded;
    public static boolean isCarryOnLoaded;
    public static boolean isArsenalLoaded;
    public static boolean isVcLoaded;
    public static boolean isMekanismLoaded;
    public static boolean isCyclicLoaded;
    public static boolean isBOPLoaded;
    public static boolean isChiselLoaded;
    public static boolean isVampirismLoaded;
    public static boolean isMysticalWorldLoaded;
    public static boolean isIndustrialForegoingLoaded;
    public static boolean isFutureMcLoaded;
    public static boolean isLocksLoaded;
    public static boolean isTrophySlotsLoaded;
    public static boolean isCyberwareLoaded;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MSUConfig.load(event.getSuggestedConfigurationFile(), event.getSide());

        isThaumLoaded = Loader.isModLoaded("thaumcraft");
        isBotaniaLoaded = Loader.isModLoaded("botania");
        isArsenalLoaded = Loader.isModLoaded("minestuckarsenal");
        isSplatcraftLodaded = Loader.isModLoaded("splatcraft");
        isCarryOnLoaded = Loader.isModLoaded("carryon");
        isVcLoaded = Loader.isModLoaded("variedcommodities");
        isMekanismLoaded = Loader.isModLoaded("mekanism");
        isCyclicLoaded = Loader.isModLoaded("cyclicmagic");
        isChiselLoaded = Loader.isModLoaded("chisel");
        isBOPLoaded = Loader.isModLoaded("biomesoplenty");
        isVampirismLoaded = Loader.isModLoaded("vampirism");
        isMysticalWorldLoaded = Loader.isModLoaded("mysticalworld");
        isIndustrialForegoingLoaded = Loader.isModLoaded("industrialforegoing");
        isFutureMcLoaded = Loader.isModLoaded("futuremc");
        isLocksLoaded = Loader.isModLoaded("locks");
        isTrophySlotsLoaded = Loader.isModLoaded("trophyslots");
        isCyberwareLoaded = Loader.isModLoaded("cyberware");

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        proxy.serverStarted();
    }


    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new MSGTCommand());
        event.registerServerCommand(new GlobalSayCommand());
        event.registerServerCommand(new SkillCommand());
    }
}
