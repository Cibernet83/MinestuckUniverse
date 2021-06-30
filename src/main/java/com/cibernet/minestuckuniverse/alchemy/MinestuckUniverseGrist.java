package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.Minestuck;
import static com.mraof.minestuck.alchemy.GristHelper.secondaryGristMap;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;

public class MinestuckUniverseGrist
{

    //Mod Checks
    public static final boolean includeMagicGrist = MinestuckUniverse.isThaumLoaded || MinestuckUniverse.isBotaniaLoaded;
    
    public static ArrayList<GristType> gristList = new ArrayList<GristType>()
    {{
        add(GristType.Amber);
        add(GristType.Iodine);
        add(GristType.Shale);
        add(GristType.Marble);
        add(GristType.Build);
        add(GristType.Mercury);
        add(GristType.Tar);
        add(GristType.Uranium);
        add(GristType.Quartz);
        add(GristType.Zillium);
        add(GristType.Gold);
        add(GristType.Rust);
        add(GristType.Ruby);
        add(GristType.Garnet);
        add(GristType.Chalk);
        add(GristType.Amethyst);
        add(GristType.Artifact);
        add(GristType.Cobalt);
        add(GristType.Caulk);
        add(GristType.Diamond);
        add(GristType.Sulfur);
    }};
    
    //Magic Grist (Thaum, Botania, etc.)
    public static final GristType Vis = new GristType("vis", 0F, 5.0F, new ResourceLocation("minestuckuniverse", "vis"));
    public static final GristType Mana = new GristType("mana", 0F, 5.0F, new ResourceLocation("minestuckuniverse", "mana"));

    //Arsenal Grist (for when it's not available)

    //public static GristType Flourite = new GristType("flourite", 0.3F, new ResourceLocation("minestuckuniverse", "flourite")).setRegistryName("flourite");


    @SubscribeEvent
    public void registerGrist(RegistryEvent.Register<GristType> event)
    {
        IForgeRegistry<GristType> registry = event.getRegistry();

        if(MinestuckUniverse.isThaumLoaded)
            register(registry, Vis.setRegistryName("vis"));
        if(MinestuckUniverse.isBotaniaLoaded)
            register(registry, Mana.setRegistryName("mana"));


        if(!MinestuckUniverse.isArsenalLoaded)
        {
            //registry.register(Fluorite);
        }
    }
    
    protected void register(IForgeRegistry<GristType> registry, GristType grist)
    {
        gristList.add(grist);
        registry.register(grist);
    }
}
