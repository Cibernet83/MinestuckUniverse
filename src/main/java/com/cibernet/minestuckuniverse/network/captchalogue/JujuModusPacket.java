package com.cibernet.minestuckuniverse.network.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.JujuModus;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class JujuModusPacket extends MSUPacket
{
	Type type;
	
	@Override
	public MSUPacket generatePacket(Object... args)
	{
		if(args[0] instanceof Type)
			data.writeInt(((Type)args[0]).ordinal());
		else if(args[0] instanceof Integer)
			data.writeInt((Integer) args[0]);
		else data.writeInt(0);
		
		return this;
	}
	
	@Override
	public MSUPacket consumePacket(ByteBuf buffer)
	{
		type = Type.values()[buffer.readInt()];
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(!(MinestuckPlayerData.getData(player).modus instanceof JujuModus))
			return;
		switch(type)
		{
			case LINK:
				
				EntityPlayer partner = JujuModus.getLinkPlayer(player);
				IdentifierHandler.PlayerIdentifier partnerIdentifier = null;
				
				if(partner != null)
					partnerIdentifier = IdentifierHandler.encode(partner);
				
				JujuModus.link(IdentifierHandler.encode(player), partnerIdentifier);
				((JujuModus) MinestuckPlayerData.getData(player).modus).sendUpdateToClients();
				
			break;
			case UNLINK:
				JujuModus.unlink(IdentifierHandler.encode(player));
				
			break;
			case REQUEST_CLIENT:
				((JujuModus) MinestuckPlayerData.getData(player).modus).sendUpdateToClients();
			break;
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
	
	public enum Type
	{
		LINK,
		UNLINK,
		REQUEST_CLIENT
	}
}
