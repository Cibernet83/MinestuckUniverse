package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class UpdateBeamDataPacket extends MSUPacket
{
	int worldId;
	NBTTagCompound nbt;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt(((World)args[0]).provider.getDimension());
		ByteBufUtils.writeTag(data, ((World)args[0]).getCapability(MSUCapabilities.BEAM_DATA, null).writeToNBT());

		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		worldId = data.readInt();
		nbt = ByteBufUtils.readTag(data);

		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(player.world.provider.getDimension() == worldId)
			player.world.getCapability(MSUCapabilities.BEAM_DATA, null).readFromNBT(nbt);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
