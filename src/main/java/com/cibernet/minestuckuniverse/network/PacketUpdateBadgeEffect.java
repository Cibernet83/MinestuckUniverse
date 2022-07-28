package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffect;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketUpdateBadgeEffect extends MSUPacket
{
	int entityId;
	String key;
	IBadgeEffect value;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt(((Entity) args[0]).getEntityId());
		ByteBufUtils.writeUTF8String(data, args[1].toString());
		if (args[2] == null)
			data.writeBoolean(false);
		else
		{
			data.writeBoolean(true);
			((IBadgeEffect) args[2]).serialize(data);
		}
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		entityId = data.readInt();
		key = ByteBufUtils.readUTF8String(data);
		if (data.readBoolean())
			value = IBadgeEffect.deserialize(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		player.world.getEntityByID(entityId).getCapability(MSUCapabilities.BADGE_EFFECTS, null).receive(key, value != null ? value.initialize(player.world) : null);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
