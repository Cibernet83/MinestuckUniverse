package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.UUID;

public class PacketUpdateAllBadgeEffects extends MSUPacket
{
	private NBTTagCompound received;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		EntityLivingBase entity = (EntityLivingBase) args[0];
		NBTTagCompound nbt = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).writeToNBT();
		nbt.setUniqueId("UUID", entity.getUniqueID());
		ByteBufUtils.writeTag(data, nbt);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		received = ByteBufUtils.readTag(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		UUID uuid = received.getUniqueId("UUID");
		for (EntityLivingBase entity : player.world.getEntities(EntityLivingBase.class, (entity) -> entity.getUniqueID().equals(uuid)))
			entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).readFromNBT(received);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
