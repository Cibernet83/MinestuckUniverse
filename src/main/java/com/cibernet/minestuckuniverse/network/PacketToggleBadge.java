package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.badges.Badge;
import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketToggleBadge extends MSUPacket
{
    Badge badge;
    boolean sendMessage;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        ByteBufUtils.writeUTF8String(data, ((Badge)args[0]).getRegistryName().toString());

        data.writeBoolean(args.length > 1 && (Boolean) args[1]);

        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        badge = MSUBadges.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(data)));
        sendMessage = data.readBoolean();
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
        if(data.hasBadge(badge))
        {
            data.setBadgeEnabled(badge, !data.isBadgeEnabled(badge));
            data.update();

            if(sendMessage)
                player.sendStatusMessage(new TextComponentTranslation((!data.isBadgeEnabled(badge) ? "status.badgeDisabled" : "status.badgeEnabled"), badge.getDisplayComponent()), true);
        }
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }
}
