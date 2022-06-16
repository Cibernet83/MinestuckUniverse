package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketUpdateGTDataFromClient extends MSUPacket
{
    NBTTagCompound nbt;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        ByteBufUtils.writeTag(data, Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).writeToNBT());
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        nbt = ByteBufUtils.readTag(data);
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
        return EnumSet.of(Side.CLIENT);
    }
}
