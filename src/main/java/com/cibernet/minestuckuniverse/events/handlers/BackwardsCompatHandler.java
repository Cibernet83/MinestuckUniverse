package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BackwardsCompatHandler
{
	@SubscribeEvent
	public static void remapBlocks(RegistryEvent.MissingMappings<Block> event)
	{
		event.getMappings().forEach(mapping ->
		{
			if(mapping.key.getResourcePath().equals("porkhollow_atm"))
				mapping.remap(MinestuckUniverseBlocks.ceramicPorkhollow);
		});

		event.getAllMappings().forEach(mapping ->
		{
			if(mapping.key.getResourceDomain().equals("minestuckgodtier") || mapping.key.getResourceDomain().equals("fetchmodiplus"))
			{
				Block remap = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(MinestuckUniverse.MODID, mapping.key.getResourcePath()));
				if(remap != null)
					mapping.remap(remap);
			}
		});
	}

	@SubscribeEvent
	public static void remapEffects(RegistryEvent.MissingMappings<Potion> event)
	{
		event.getAllMappings().forEach(mapping ->
		{
			if(mapping.key.getResourceDomain().equals("minestuckgodtier") || mapping.key.getResourceDomain().equals("fetchmodiplus"))
			{
				Potion remap = ForgeRegistries.POTIONS.getValue(new ResourceLocation(MinestuckUniverse.MODID, mapping.key.getResourcePath()));
				if(remap != null)
					mapping.remap(remap);
			}
		});
	}

	@SubscribeEvent
	public static void remapSounds(RegistryEvent.MissingMappings<SoundEvent> event)
	{
		event.getAllMappings().forEach(mapping ->
		{
			if(mapping.key.getResourceDomain().equals("minestuckgodtier") || mapping.key.getResourceDomain().equals("fetchmodiplus"))
			{
				SoundEvent remap = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(MinestuckUniverse.MODID, mapping.key.getResourcePath()));
				if(remap != null)
					mapping.remap(remap);
			}
		});
	}

	@SubscribeEvent
	public static void remapItems(RegistryEvent.MissingMappings<Item> event)
	{
		event.getMappings().forEach(mapping ->
		{
			if(mapping.key.getResourcePath().equals("porkhollow_atm"))
				mapping.remap(Item.getItemFromBlock(MinestuckUniverseBlocks.ceramicPorkhollow));

		});

		event.getAllMappings().forEach(mapping ->
		{
			if(mapping.key.getResourceDomain().equals("minestuckgodtier") || mapping.key.getResourceDomain().equals("fetchmodiplus"))
			{
				Item remap = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MinestuckUniverse.MODID, mapping.key.getResourcePath()));

				if(mapping.key.getResourcePath().equals("energy_cell"))
					mapping.remap(MinestuckUniverseItems.battery);
				else if(mapping.key.getResourcePath().equals("skill_reset_charm"))
					mapping.remap(MinestuckUniverseItems.godTierResetCharm);
				else if(mapping.key.getResourcePath().equals("tome_of_the_ancients"))
					mapping.remap(MinestuckUniverseItems.denizenTome);
				else if(mapping.key.getResourcePath().equals("crystal_eight_ball"))
					mapping.remap(MinestuckUniverseItems.walletBall);
				else if(mapping.key.getResourcePath().equals("crystal_ball_modus"))
					mapping.remap(MinestuckUniverseItems.walletBallModus);
				else if(mapping.key.getResourcePath().equals("sash_kit"))
					mapping.remap(MinestuckUniverseItems.sashKit);
				else if(mapping.key.getResourcePath().equals("armor_kit"))
					mapping.remap(MinestuckUniverseItems.gtArmorKit);
				else if(mapping.key.getResourcePath().equals("pants"))
					mapping.remap(MinestuckUniverseItems.gtPants);
				else if(mapping.key.getResourcePath().equals("shoes"))
					mapping.remap(MinestuckUniverseItems.gtShoes);
				else if(mapping.key.getResourcePath().equals("shirt"))
					mapping.remap(MinestuckUniverseItems.gtShirt);
				else if(mapping.key.getResourcePath().equals("hood"))
					mapping.remap(MinestuckUniverseItems.gtHood);
				else if(remap != null)
					mapping.remap(remap);
			}
		});
	}

	@SubscribeEvent
	public static void remapEntities(RegistryEvent.MissingMappings<EntityEntry> event)
	{
		event.getAllMappings().forEach(mapping ->
		{
			if(mapping.key.getResourceDomain().equals("minestuckgodtier") || mapping.key.getResourceDomain().equals("fetchmodiplus"))
			{
				EntityEntry remap = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MinestuckUniverse.MODID, mapping.key.getResourcePath()));
				String path = mapping.key.getResourcePath();
				if(path.equals("eight_ball") || path.equals("crystal_eight_ball") || path.equals("operandi_eight_ball") || path.equals("operandi_splash_potion"))
					mapping.remap(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MinestuckUniverse.MODID, "throwable")));
				if(remap != null)
					mapping.remap(remap);
			}
		});
	}

	@SubscribeEvent
	public static void remapAbstrata(RegistryEvent.MissingMappings<KindAbstratus> event)
	{
		event.getMappings().forEach(mapping ->
		{
			if(mapping.key.getResourcePath().equals("tridentkind"))
				mapping.remap(MSUKindAbstrata.tridentkind);

		});
	}
}
