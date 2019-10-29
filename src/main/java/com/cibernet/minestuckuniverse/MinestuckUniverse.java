package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.proxy.CommonProxy;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(
        modid = MinestuckUniverse.MODID,
        name = MinestuckUniverse.NAME,
        version = MinestuckUniverse.VERSION,
        dependencies = "required-after:minestuck@[1.2.283,);required-after:baubles@[1.5.2,);"
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
    public static boolean isArsenalLoaded;
    public static CreativeTabs tab = TabMinestuckUniverse.instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        isThaumLoaded = Loader.isModLoaded("thaumcraft");
        isBotaniaLoaded = Loader.isModLoaded("botania");
        isArsenalLoaded = Loader.isModLoaded("minestucarsenal");

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
