package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MSUConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketUpdateConfig extends MSUPacket
{
	@Override
	public MSUPacket generatePacket(Object... args)
	{
		MSUConfig.writeToBuffer(data);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		MSUConfig.readFromBuffer(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{

	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
}
