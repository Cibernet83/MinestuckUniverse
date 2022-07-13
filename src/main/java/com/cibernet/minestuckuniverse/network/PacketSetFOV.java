package com.cibernet.minestuckuniverse.network;

import java.util.EnumSet;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSetFOV extends MSUPacket
{
	private int FOV;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		FOV = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		Minecraft.getMinecraft().gameSettings.fovSetting = Math.min(119, FOV + Minecraft.getMinecraft().gameSettings.fovSetting);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}