package com.cibernet.minestuckuniverse.events.handlers;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EditModeEventHandler
{
    @SubscribeEvent
    public static void onPlayerCollision(GetCollisionBoxesEvent event)
    {
        if(((event.getEntity() != null && event.getEntity().world.isRemote && event.getEntity().equals(Minecraft.getMinecraft().player) && ClientEditHandler.isActive()) ||
                (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote && ServerEditHandler.getData((EntityPlayer) event.getEntity()) != null)) && ((EntityPlayer) event.getEntity()).capabilities.isFlying)
            event.getCollisionBoxesList().clear();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onPlayerPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent event)
    {
        if((event.getEntity() != null && event.getEntity().world.isRemote && event.getEntity().equals(Minecraft.getMinecraft().player) && ClientEditHandler.isActive()))
            event.setCanceled(true);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderBlockOverlay(RenderBlockOverlayEvent event)
    {

        if((Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.world.isRemote && ClientEditHandler.isActive()))
            event.setCanceled(true);
    }

    public static boolean isInEditMode(EntityPlayer player)
    {
        return (player != null && ((player.world.isRemote ? ClientEditHandler.isActive() : ServerEditHandler.getData(player) != null)));
    }
}
