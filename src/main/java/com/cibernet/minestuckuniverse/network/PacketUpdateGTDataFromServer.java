package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.UUID;

public class PacketUpdateGTDataFromServer extends MSUPacket
{
    NBTTagCompound nbt;
    UUID uuid;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        ByteBufUtils.writeTag(data, ((EntityPlayer) args[0]).getCapability(MSUCapabilities.GOD_TIER_DATA, null).writeToNBT());
        ByteBufUtils.writeUTF8String(data, ((EntityPlayer) args[0]).getUniqueID().toString());
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        nbt = ByteBufUtils.readTag(data);
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(data));
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        if(nbt.hasKey("Reset"))
        {
            nbt.removeTag("Reset");
            MSUUtils.onResetGodTier(player);
        }
        player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).readFromNBT(nbt);
    }

    @Override
    public EnumSet<Side> getSenderSide() {
        return EnumSet.of(Side.SERVER);
    }
}
