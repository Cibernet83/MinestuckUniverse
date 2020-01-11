package com.cibernet.minestuckuniverse.events;

import com.cibernet.minestuckuniverse.powers.MSUHeroPowers;
import com.cibernet.minestuckuniverse.powers.MSUPowerBase;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler
{
	public static ServerEventHandler instance = new ServerEventHandler();
	
	public static boolean heroHold = false;
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(event.player);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		//if(c == null || !c.enteredGame() || MinestuckConfig.aspectEffects == false || !MinestuckPlayerData.getEffectToggle(identifier))
			//return;
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		//EnumAspect aspect = MinestuckPlayerData.getTitle(identifier).getHeroAspect();
		//EnumClass heroClass = MinestuckPlayerData.getTitle(identifier).getHeroClass();
		
		if(rung >= 49)
			event.player.capabilities.allowFlying = true;
		
		if(heroHold)
		{
			MSUPowerBase power = MSUHeroPowers.getPlayerPower(event.player);
			if(power != null)
				power.onHeld(event.player.world, event.player, true);
		}
	}
	public static void useHeroPower(EntityPlayer player)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		Title title = MinestuckPlayerData.getTitle(identifier);
		if(title == null)
			return;
		
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		if(rung < 23)
		{
			player.sendStatusMessage(new TextComponentTranslation("message.power.locked"), true);
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		World world = player.getEntityWorld();
		MSUPowerBase power = MSUHeroPowers.getPower(title.getHeroClass(), title.getHeroAspect());
		
		BlockPos pos = mc.objectMouseOver.getBlockPos();
		EntityLiving entity = (EntityLiving) mc.objectMouseOver.entityHit;
		
		if(entity != null)
		{
			power.useOnEntity(world,player,entity,true);
		}
		else if(pos != null && !world.getBlockState(pos).getMaterial().equals(Material.AIR))
		{
			Vec3d vec = mc.objectMouseOver.hitVec;
			float hitX = (float)(vec.x - (double)pos.getX());
			float hitY = (float)(vec.y - (double)pos.getY());
			float hitZ = (float)(vec.z - (double)pos.getZ());
			if(!power.useOnBlock(world,player,pos,hitX, hitY, hitZ, true))
				power.use(world,player, true);
		}
		else
			power.use(world,player, true);
		
		
		
		
	}
}
