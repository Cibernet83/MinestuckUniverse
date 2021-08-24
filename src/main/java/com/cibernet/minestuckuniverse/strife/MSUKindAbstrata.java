package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUKindAbstrata
{
	public static KindAbstratus hammerkind = new KindAbstratus("hammer", MinestuckUniverseItems.toolHammer);
	public static KindAbstratus bladekind = new KindAbstratus("sword", MinestuckUniverseItems.toolSword);
	public static KindAbstratus clubkind = new KindAbstratus("club", MinestuckUniverseItems.toolClub);
	public static KindAbstratus glovekind = new KindAbstratus("glove", MinestuckUniverseItems.toolGauntlet);
	public static KindAbstratus needlekind = new KindAbstratus("needles", MinestuckUniverseItems.toolNeedles);

	@SubscribeEvent
	public static void register(RegistryEvent.Register<KindAbstratus> event)
	{
		IForgeRegistry<KindAbstratus> registry = event.getRegistry();

		registry.register(hammerkind.setRegistryName("hammer"));
		registry.register(bladekind.setRegistryName("sword"));
		registry.register(clubkind.setRegistryName("club"));
		registry.register(glovekind.setRegistryName("gauntlet"));
		registry.register(needlekind.setRegistryName("needles"));
	}
}
