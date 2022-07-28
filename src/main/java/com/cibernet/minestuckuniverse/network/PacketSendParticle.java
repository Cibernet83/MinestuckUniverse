package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.particles.MSUParticles;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketSendParticle extends MSUPacket
{
    MSUParticles.ParticleType type;
    double x;
    double y;
    double z;
    int color;
    int count;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        data.writeInt(((MSUParticles.ParticleType)args[0]).ordinal());
        data.writeInt((Integer) args[1]);
        data.writeInt((Integer) args[2]);

        if(args[3] instanceof Entity)
        {
            data.writeDouble(((Entity)args[3]).posX);
            data.writeDouble(((Entity)args[3]).posY);
            data.writeDouble(((Entity)args[3]).posZ);
        }
        else if(args[3] instanceof BlockPos)
        {
            BlockPos pos = ((BlockPos)args[3]);
            data.writeDouble(pos.getX());
            data.writeDouble(pos.getY());
            data.writeDouble(pos.getZ());
        }
        else
        {
            data.writeDouble((Double) args[3]);
            data.writeDouble((Double) args[4]);
            data.writeDouble((Double) args[5]);
        }

        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        type = MSUParticles.ParticleType.values()[data.readInt()];
        color = data.readInt();
        count = data.readInt();

        x = data.readDouble();
        y = data.readDouble();
        z = data.readDouble();

        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        switch (type)
        {
            case AURA:
                MSUParticles.spawnAuraParticles(player.world, x, y, z, color, count);
                break;
            case BURST:
                MSUParticles.spawnBurstParticles(player.world, x, y, z, color, count);
                break;
        }
    }

    @Override
    public EnumSet<Side> getSenderSide() {
        return EnumSet.of(Side.SERVER);
    }

}
