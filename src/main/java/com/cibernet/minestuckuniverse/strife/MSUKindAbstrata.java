package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUKindAbstrata
{
	public static KindAbstratus hammerkind = new KindAbstratus("hammer", MinestuckUniverseItems.toolHammer);
	public static KindAbstratus bladekind = new KindAbstratus("sword", MinestuckUniverseItems.toolSword);
	public static KindAbstratus clubkind = new KindAbstratus("club", MinestuckUniverseItems.toolClub);
	public static KindAbstratus canekind = new KindAbstratus("cane", MinestuckUniverseItems.toolCane);
	public static KindAbstratus sicklekind = new KindAbstratus("sickle", MinestuckUniverseItems.toolSickle);
	public static KindAbstratus spoonkind = new KindAbstratus("spoon", MinestuckUniverseItems.toolSpoon);
	public static KindAbstratus forkkind = new KindAbstratus("fork", MinestuckUniverseItems.toolFork);

	public static KindAbstratus pickaxekind = new KindAbstratus("pickaxe").addItemClasses(ItemPickaxe.class);
	public static KindAbstratus axekind = new KindAbstratus("axe").addItemClasses(ItemAxe.class);
	public static KindAbstratus shovelkind = new KindAbstratus("shovel", MinestuckUniverseItems.toolShovel).addItemClasses(ItemSpade.class);
	public static KindAbstratus hoekind = new KindAbstratus("hoe").addItemClasses(ItemHoe.class);
	public static KindAbstratus fshngrodkind = new KindAbstratus("fishingRod").addItemClasses(ItemFishingRod.class);

	public static KindAbstratus clawkind = new KindAbstratus("claw", MinestuckUniverseItems.toolClaws);
	public static KindAbstratus glovekind = new KindAbstratus("glove", MinestuckUniverseItems.toolGauntlet);
	public static KindAbstratus needlekind = new KindAbstratus("needles", MinestuckUniverseItems.toolNeedles);
	public static KindAbstratus shieldkind = new KindAbstratus("shield").setConditional((i, itemStack) -> i.isShield(itemStack, null));

	public static KindAbstratus fistkind = new KindAbstratus("fist").setFist(true);
	public static KindAbstratus jokerkind = new KindAbstratus("joker").setConditional((i, stack) -> !stack.isEmpty()).setHidden(true);

	public static KindAbstratus sbahjkind = new KindAbstratus("sbahj")
			.addItemTools(MinestuckUniverseItems.sbahjWhip, MinestuckItems.batleacks, MinestuckItems.sord, MinestuckItems.sbahjPoster,
					Item.getItemFromBlock(MinestuckUniverseBlocks.sbahjBedrock), Item.getItemFromBlock(MinestuckUniverseBlocks.sbahjTree))
			.setConditional((i, stack) -> i.getRegistryName().getResourcePath().equals("sbahjaddon")).setHidden(true);
	public static KindAbstratus bunnykind = new KindAbstratus("bunny")
			.addItemTools(MinestuckUniverseItems.bunnySlippers, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_FOOT, Items.RABBIT_HIDE, Items.RABBIT_STEW).setHidden(true);

	@SubscribeEvent
	public static void register(RegistryEvent.Register<KindAbstratus> event)
	{
		IForgeRegistry<KindAbstratus> registry = event.getRegistry();

		registry.register(hammerkind.setRegistryName("hammer"));
		registry.register(bladekind.setRegistryName("sword"));
		registry.register(clubkind.setRegistryName("club"));
		registry.register(canekind.setRegistryName("cane"));
		registry.register(sicklekind.setRegistryName("sickle"));
		registry.register(spoonkind.setRegistryName("spoon"));
		registry.register(forkkind.setRegistryName("fork"));

		registry.register(pickaxekind.setRegistryName("pickaxe"));
		registry.register(axekind.setRegistryName("axe"));
		registry.register(shovelkind.setRegistryName("shovel"));
		registry.register(hoekind.setRegistryName("hoe"));
		registry.register(fshngrodkind.setRegistryName("fishing_rod"));

		registry.register(clawkind.setRegistryName("claw"));
		registry.register(glovekind.setRegistryName("gauntlet"));
		registry.register(needlekind.setRegistryName("needles"));
		registry.register(shieldkind.setRegistryName("shield"));

		registry.register(fistkind.setRegistryName("fist"));
		registry.register(jokerkind.setRegistryName("joker"));
		registry.register(sbahjkind.setRegistryName("sbahj"));
		registry.register(bunnykind.setRegistryName("bunny"));
	}
}
