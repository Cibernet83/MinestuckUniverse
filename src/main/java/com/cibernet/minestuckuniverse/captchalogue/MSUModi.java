package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MSUModi
{
	public static void register()
	{
		registerModus("cyclone_modus", CycloneModus.class, MinestuckUniverseItems.cycloneModus);
		registerModus("wild_magic_modus", WildMagicModus.class, MinestuckUniverseItems.wildMagicModus);
		registerModus("capitalist_modus", CapitalistModus.class, MinestuckUniverseItems.capitalistModus);
		registerModus("mod_us", CommunistModus.class, MinestuckUniverseItems.modUs);
		registerModus("deck_modus", DeckModus.class, MinestuckUniverseItems.deckModus);
		registerModus("pop_tart_modus", PopTartModus.class, MinestuckUniverseItems.popTartModus);
		registerModus("hue_modus", HueModus.class, MinestuckUniverseItems.hueModus);
		registerModus("hue_stack_modus", HueStackModus.class, MinestuckUniverseItems.hueStackModus);
		registerModus("chat_modus", ChatModus.class, MinestuckUniverseItems.chatModus);
		registerModus("onion_modus", OnionModus.class, MinestuckUniverseItems.onionModus);
		registerModus("slime_modus", SlimeModus.class, MinestuckUniverseItems.slimeModus);
		registerModus("scratch_and_sniff_modus", ScratchAndSniffModus.class, MinestuckUniverseItems.scratchAndSniffModus);
		registerModus("eight_ball_modus", EightBallModus.class, MinestuckUniverseItems.eightBallModus);
		registerModus("juju_modus", JujuModus.class, MinestuckUniverseItems.jujuModus);
		registerModus("alchemodus", AlchemyModus.class, MinestuckUniverseItems.alcheModus);
		registerModus("operandi_modus", OperandiModus.class, MinestuckUniverseItems.operandiModus);
		registerModus("weight_modus", WeightModus.class, MinestuckUniverseItems.weightModus);
		registerModus("book_modus", BookModus.class, MinestuckUniverseItems.bookModus);
		registerModus("energy_modus", EnergyModus.class, MinestuckUniverseItems.energyModus);
		registerModus("chasity_modus", ChasityModus.class, MinestuckUniverseItems.chasityModus);


		registerModus("array_modus", ArrayModus.class, MinestuckUniverseItems.arrayModus);
		registerModus("monster_modus", MonsterModus.class, MinestuckUniverseItems.monsterModus);
		registerModus("wallet_modus", WalletModus.class, MinestuckUniverseItems.walletModus);
		registerModus("crystal_ball_modus", CrystalBallModus.class, MinestuckUniverseItems.walletBallModus);

		/*
		registerModus("ouija_modus", OuijaModus.class, MinestuckUniverseItems.ouijaModus);
		registerModus("cipher_modus", CipherModus.class, MinestuckUniverseItems.cipherModus);
		registerModus("memory_modus", MemoryModus.class, MinestuckUniverseItems.memoryModus);
		*/

		registerModus("hashchat_modus", HashchatModus.class, MinestuckUniverseItems.hashchatModus);
		registerModus("sacrifice_modus", SacrificeModus.class, MinestuckUniverseItems.sacrificeModus);
	}

	private static void registerModus(String name, Class<? extends Modus> clazz, ItemStack stack)
	{
		CaptchaDeckHandler.registerModusType(new ResourceLocation(MinestuckUniverse.MODID,name), clazz, stack);
	}

	private static void registerModus(String name, Class<? extends Modus> clazz, Item item)
	{
		registerModus(name, clazz, new ItemStack(item));
	}
}
