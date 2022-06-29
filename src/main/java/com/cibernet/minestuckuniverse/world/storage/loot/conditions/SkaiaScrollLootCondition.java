package com.cibernet.minestuckuniverse.world.storage.loot.conditions;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public class SkaiaScrollLootCondition implements LootCondition
{
	private final float chance;


	public SkaiaScrollLootCondition(float chance)
	{
		this.chance = chance;
	}

	public float getChance()
	{
		return chance;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		return (rand.nextFloat() <= chance);
	}

	public static class Serializer extends LootCondition.Serializer<SkaiaScrollLootCondition>
	{
		public Serializer()
		{
			super(new ResourceLocation(MinestuckUniverse.MODID, "skaian"), SkaiaScrollLootCondition.class);
		}

		@Override
		public void serialize(JsonObject json, SkaiaScrollLootCondition value, JsonSerializationContext context)
		{
			json.addProperty("chance", value.chance);
		}

		@Override
		public SkaiaScrollLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			return new SkaiaScrollLootCondition(JsonUtils.getFloat(json, "chance", 1));
		}
	}
}
