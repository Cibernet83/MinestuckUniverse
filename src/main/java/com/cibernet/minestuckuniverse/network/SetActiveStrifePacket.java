package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class SetActiveStrifePacket extends MSUPacket
{
	boolean isSpecibus;
	int index;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		data.writeBoolean((boolean) args[1]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		index = data.readInt();
		isSpecibus = data.readBoolean();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		if(isSpecibus)
			cap.setSelectedSpecibusIndex(index);
		else cap.setSelectedWeaponIndex(index);

		MSUChannelHandler.sendToPlayer(makePacket(Type.UPDATE_STRIFE, player), player);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}
}
