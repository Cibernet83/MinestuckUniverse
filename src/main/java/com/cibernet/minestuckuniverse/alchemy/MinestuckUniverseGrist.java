package com.cibernet.minestuckuniverse.alchemy;

import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckUniverseGrist
{

    //Magic Grist (Thaum, Botania, etc.)
    public static final GristType Ignis = new GristType("fire", 0.1F, new ResourceLocation("minestuckuniverse", "fire")).setRegistryName("ignis");
    public static final GristType Terra = new GristType("earth", 0.1F, new ResourceLocation("minestuckuniverse", "earth")).setRegistryName("terra");
    public static final GristType Aqua = new GristType("water", 0.1F, new ResourceLocation("minestuckuniverse", "water")).setRegistryName("aqua");
    public static final GristType Aer = new GristType("air", 0.1F, new ResourceLocation("minestuckuniverse", "air")).setRegistryName("aer");
    public static final GristType Ordo = new GristType("order", 0.05F, new ResourceLocation("minestuckuniverse", "order")).setRegistryName("ordo");
    public static final GristType Perditio = new GristType("chaos", 0.05F, new ResourceLocation("minestuckuniverse", "chaos")).setRegistryName("perditio");

    @SubscribeEvent
    public void registerGrist(RegistryEvent.Register<GristType> event)
    {
        IForgeRegistry<GristType> registry = event.getRegistry();

        registry.register(Ignis);
        registry.register(Terra);
        registry.register(Aqua);
        registry.register(Aer);
        registry.register(Ordo);
        registry.register(Perditio);

    }
}
