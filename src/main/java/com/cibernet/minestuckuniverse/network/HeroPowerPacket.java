package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.events.ServerEventHandler;
import com.cibernet.minestuckuniverse.powers.MSUHeroPowers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class HeroPowerPacket extends MSUPacket
{
	boolean toggleHold;
	
	@Override
	public MSUPacket generatePacket(Object... dat)
	{
		this.data.writeBoolean((Boolean) dat[0]);
		return this;
	}
	
	@Override
	public MSUPacket consumePacket(ByteBuf dat)
	{
		toggleHold = dat.readBoolean();
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(toggleHold)
			ServerEventHandler.useHeroPower(player);
		ServerEventHandler.heroHold = toggleHold;
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
