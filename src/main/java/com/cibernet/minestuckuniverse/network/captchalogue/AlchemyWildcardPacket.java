package com.cibernet.minestuckuniverse.network.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.AlchemyModus;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class AlchemyWildcardPacket extends MSUPacket
{
	GristType gristType;
	
	@Override
	public MSUPacket generatePacket(Object... args)
	{
		this.data.writeInt(GristType.REGISTRY.getID((GristType)args[0]));
		return this;
	}
	
	@Override
	public MSUPacket consumePacket(ByteBuf buffer)
	{
		gristType = GristType.REGISTRY.getValue(buffer.readInt());
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(MinestuckPlayerData.getData(player).modus instanceof AlchemyModus)
			((AlchemyModus) MinestuckPlayerData.getData(player).modus).setWildcardGrist(gristType);
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
