package com.cibernet.minestuckuniverse.world.storage.loot;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.world.storage.loot.conditions.JujuLootCondition;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MSULoot
{
	public static final ResourceLocation CUE_BALL_LOOT = new ResourceLocation(MinestuckUniverse.MODID, "inject/cue_ball");

	public static void registerLootClasses()
	{
		LootConditionManager.registerCondition(new JujuLootCondition.Serializer());
	}

	private static final ResourceLocation TIME = new ResourceLocation("minestuck", "chests/medium_basic/clockwork");

	@SubscribeEvent
	public static void onLootInject(LootTableLoadEvent event)
	{
		//Inject Cue Ball
		if(!event.getName().equals(CUE_BALL_LOOT) && event.getName().getResourcePath().toLowerCase().contains("chests"))
		{
			LootPool cueBall = event.getLootTableManager().getLootTableFromLocation(CUE_BALL_LOOT).getPool("minestuckuniverse:cue_ball");
			event.getTable().addPool(cueBall);
		}

		//Inject RBT record into shop loot
		if(event.getName().equals(MinestuckLoot.CONSORT_GENERAL_STOCK))
		{
			LootPool inject = event.getLootTableManager().getLootTableFromLocation(new ResourceLocation(MinestuckUniverse.MODID, "inject/consort_general")).getPool("items");
			event.getTable().getPool("items").addEntry(inject.getEntry("minestuck:record_retro_battle"));
		}
		else if(event.getName().equals(TIME))
		{
			LootPool inject = event.getLootTableManager().getLootTableFromLocation(new ResourceLocation(MinestuckUniverse.MODID, "inject/medium_loot")).getPool("items");
			event.getTable().getPool("main").addEntry(inject.getEntry("minestuckuniverse:ticking_stopwatch"));
		}
	}
}
