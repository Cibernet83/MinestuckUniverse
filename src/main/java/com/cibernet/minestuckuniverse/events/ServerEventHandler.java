package com.cibernet.minestuckuniverse.events;

import com.cibernet.minestuckuniverse.entity.classes.EntityItemFireproof;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler
{
	public static ServerEventHandler instance = new ServerEventHandler();
	
	@SubscribeEvent
	public void entityStruckByLightning(EntityStruckByLightningEvent event)
	{
		if(event.getEntity() instanceof EntityItemFireproof)
		{
			System.out.println(event.getEntity());
			EntityItemFireproof item = (EntityItemFireproof) event.getEntity();
			if(item.getItem().getItem().equals(MinestuckUniverseItems.obsidianBottle))
			{
				item.setItem(new ItemStack(MinestuckUniverseItems.bottledLightning, item.getItem().getCount()));
				item.bounce();
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(event.player);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		if(c == null || !c.enteredGame() || MinestuckConfig.aspectEffects == false || !MinestuckPlayerData.getEffectToggle(identifier))
			return;
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		EnumAspect aspect = MinestuckPlayerData.getTitle(identifier).getHeroAspect();
		EnumClass heroClass = MinestuckPlayerData.getTitle(identifier).getHeroClass();
		
		
		
	}
}
