package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketUnequipTech extends MSUPacket
{
	private int slot;
	private Abilitech tech;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		ByteBufUtils.writeUTF8String(data, ((Abilitech)args[1]).getRegistryName().toString());
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		slot = data.readInt();
		tech = (Abilitech) MSUSkills.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(data)));
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		tech.onUnequipped(player.world, player, slot);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
