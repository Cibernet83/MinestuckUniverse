package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketOpenGui extends MSUPacket
{
	int ID;
	int posX;
	int posY;
	int posZ;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		for(int i = 0; i < 4; i++)
			data.writeInt((int) args[i]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		ID = data.readInt();
		posX = data.readInt();
		posY = data.readInt();
		posZ = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		player.openGui(MinestuckUniverse.instance, ID, player.world, posX, posY, posZ);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
}
