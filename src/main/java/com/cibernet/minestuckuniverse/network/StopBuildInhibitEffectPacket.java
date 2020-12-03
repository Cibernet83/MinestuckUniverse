package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.util.MSUUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class StopBuildInhibitEffectPacket extends MSUPacket
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
        if(!player.isCreative())
            player.capabilities.allowEdit = !MSUUtils.getPlayerGameType(player).hasLimitedInteractions();
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.SERVER);
    }
}
