package com.cibernet.minestuckuniverse.potions;

import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUPotions
{

    public static final Potion CREATIVE_SHOCK = new PotionBuildInhibit( 0x993030, "disableBuilding");
    public static final Potion EARTHBOUND = new PotionFlight(true, 0xFFCD70, "disableFlight");
    public static final Potion SKYHBOUND = new PotionFlight(false, 0x70FFFF, "flight").setBeneficial();

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Potion> event)
    {
        IForgeRegistry<Potion> registry = event.getRegistry();

        registry.register(CREATIVE_SHOCK.setRegistryName("creative_shock"));
        registry.register(EARTHBOUND.setRegistryName("earthbound"));
        registry.register(SKYHBOUND.setRegistryName("skybound"));
    }
}
