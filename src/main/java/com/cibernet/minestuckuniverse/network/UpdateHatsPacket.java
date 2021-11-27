package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class UpdateHatsPacket extends MSUPacket
{
	int entityId;
	NBTTagCompound nbt;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		EntityLivingBase target = (EntityLivingBase) args[0];
		data.writeInt(target.getEntityId());
		ByteBufUtils.writeTag(data, target.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).writeToNBT());

		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		entityId = data.readInt();
		nbt = ByteBufUtils.readTag(data);

		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		player.world.getEntityByID(entityId).getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).readFromNBT(nbt);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
