package com.cibernet.minestuckuniverse.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUEnchantments
{

	public static final Enchantment SUPERPUNCH = new EnchantmentGauntlet(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Enchantment> event)
	{
		IForgeRegistry<Enchantment> registry = event.getRegistry();

		registry.register(SUPERPUNCH.setRegistryName("superpunch"));
	}
}