package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketRequestGristHoard extends MSUPacket
{
    @Override
    public MSUPacket generatePacket(Object... args)
    {
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        player.openGui(MinestuckUniverse.instance, MSUUtils.GOD_TIER_HOARD_SELECTOR_UI, player.world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.SERVER);
    }
}
