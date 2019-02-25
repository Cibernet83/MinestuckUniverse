package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.Minestuck;
import static com.mraof.minestuck.alchemy.GristHelper.secondaryGristMap;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.sun.jna.platform.unix.X11;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

public class MinestuckUniverseGrist
{

    //Mod Checks
    public static final boolean includeMagicGrist = MinestuckUniverse.isThaumLoaded || MinestuckUniverse.isBotaniaLoaded;

    //Magic Grist (Thaum, Botania, etc.)
    public static final GristType Flux = new GristType("flux", 0.1F, new ResourceLocation("minestuckuniverse", "fire")).setRegistryName("flux");
    public static final GristType Mana = new GristType("mana", 0.1F, new ResourceLocation("minestuckuniverse", "mana")).setRegistryName("mana");

    //Arsenal Grist (for when it's not available)

    //public static GristType Flourite = new GristType("flourite", 0.3F, new ResourceLocation("minestuckuniverse", "flourite")).setRegistryName("flourite");


    @SubscribeEvent
    public void registerGrist(RegistryEvent.Register<GristType> event)
    {
        IForgeRegistry<GristType> registry = event.getRegistry();

        if(MinestuckUniverse.isThaumLoaded)
            registry.register(Flux);
        if(MinestuckUniverse.isBotaniaLoaded)
            registry.register(Mana);


        if(!MinestuckUniverse.isArsenalLoaded)
        {
            //registry.register(Flourite);
        }
    }

}
