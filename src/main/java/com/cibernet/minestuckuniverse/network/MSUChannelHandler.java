package com.cibernet.minestuckuniverse.network;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.network.LandRegisterPacket;
import com.mraof.minestuck.util.Debug;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.cibernet.minestuckuniverse.network.MSUPacket.Type;

import java.util.EnumMap;

public class MSUChannelHandler extends FMLIndexedMessageToMessageCodec<MSUPacket> 
{

public static MSUChannelHandler instance = new MSUChannelHandler();
public static EnumMap<Side, FMLEmbeddedChannel> channels;
public MSUChannelHandler() {
        for(Type type : Type.values())
        addDiscriminator(type.ordinal(), type.packetType);
        }

@Override
public void encodeInto(ChannelHandlerContext ctx, MSUPacket msg, ByteBuf target) throws Exception
        {
        target.writeBytes(msg.data);
        Debug.debug("Sending packet "+msg.toString()+" with size "+msg.data.writerIndex());
        }

@Override
public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, MSUPacket msg)
        {
        Debug.debug("Received packet "+msg.toString()+" with size "+source.readableBytes());
        msg.consumePacket(source);
        }

private static class MSUPacketHandler extends SimpleChannelInboundHandler<MSUPacket>
{
    private final Side side;
    private MSUPacketHandler(Side side)
    {
        this.side = side;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MSUPacket msg) throws Exception
    {
        switch (side)
        {
            case CLIENT:

                    ClientProxy.addScheduledTask(() -> msg.execute(ClientProxy.getClientPlayer()));

                break;
            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                EntityPlayerMP player = ((NetHandlerPlayServer) netHandler).player;
                player.getServerWorld().addScheduledTask(() -> msg.execute(player));
                break;
        }
    }
}

    public static void setupChannel()
    {
        if(channels == null)
        {
            channels = NetworkRegistry.INSTANCE.newChannel("MinestuckUniverse", MSUChannelHandler.instance);
            String targetName = channels.get(Side.CLIENT).findChannelHandlerNameForType(MSUChannelHandler.class);
            channels.get(Side.CLIENT).pipeline().addAfter(targetName, "MSUPacketHandler", new MSUChannelHandler.MSUPacketHandler(Side.CLIENT));
            targetName = channels.get(Side.SERVER).findChannelHandlerNameForType(MSUChannelHandler.class);	//Not sure if this is necessary
            channels.get(Side.SERVER).pipeline().addAfter(targetName, "MSUPacketHandler", new MSUChannelHandler.MSUPacketHandler(Side.SERVER));
        }
    }

    public static void sendToServer(MSUPacket packet)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeOutbound(packet);
    }

    public static void sendToPlayer(MSUPacket packet, EntityPlayer player)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeOutbound(packet);
    }

    public static void sendToAllPlayers(MSUPacket packet)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeOutbound(packet);
    }

    public static void sendToWorld(MSUPacket packet, World world)
    {
        for(EntityPlayer player : world.playerEntities)
            sendToPlayer(packet, player);
    }

    public static void sendToTracking(MSUPacket packet, Entity trackedEntity)
    {
        for (EntityPlayer trackingPlayer : ((WorldServer) trackedEntity.world).getEntityTracker().getTrackingPlayers(trackedEntity))
            sendToPlayer(packet, trackingPlayer);
    }

    public static void sendToTrackingAndSelf(MSUPacket packet, Entity trackedEntity)
    {
        sendToTracking(packet, trackedEntity);
        if (trackedEntity instanceof EntityPlayer)
            sendToPlayer(packet, (EntityPlayer) trackedEntity);
    }
}
