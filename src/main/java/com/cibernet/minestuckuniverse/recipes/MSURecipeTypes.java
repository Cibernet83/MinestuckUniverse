package com.cibernet.minestuckuniverse.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSURecipeTypes
{
    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistry<IRecipe> registry = event.getRegistry();

    }
}
