package com.cibernet.minestuckuniverse.world.storage.loot.functions;

import com.cibernet.minestuckuniverse.items.ItemSkaianScroll;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class SetRandomSkill extends LootFunction
{
	public SetRandomSkill(LootCondition[] conditionsIn)
	{
		super(conditionsIn);
	}

	public ItemStack apply(ItemStack stack, Random rand, LootContext context)
	{
		return ItemSkaianScroll.storeRandomSkill(stack, rand);
	}

	public static class Serializer extends net.minecraft.world.storage.loot.functions.LootFunction.Serializer<SetRandomSkill>
	{
		public Serializer() {
			super(new ResourceLocation("minestuckuniverse:set_random_skill"), SetRandomSkill.class);
		}

		public void serialize(JsonObject object, SetRandomSkill functionClazz, JsonSerializationContext serializationContext)
		{
		}

		public SetRandomSkill deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
			return new SetRandomSkill(conditionsIn);
		}
	}
}
