package com.cibernet.minestuckuniverse.network;


import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.UUID;

public class PacketAddSkillXp extends MSUPacket
{
    GodTierData.StatType skillType;
    int                   amount;
    UUID                  playerUUID;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        data.writeInt(((GodTierData.StatType)args[0]).ordinal());
        data.writeInt(args.length > 2 ? (Integer) args[2] : 1);
        ByteBufUtils.writeUTF8String(data, ((EntityPlayer)args[1]).getUniqueID().toString());
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        skillType = GodTierData.StatType.values()[data.readInt()];
        amount = data.readInt();
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(data));
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        if(player.getUniqueID().equals(playerUUID))
        {
            int actualAmount = player.isCreative() ? amount : Math.min(player.experienceLevel, amount);
            player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).increaseXp(skillType, actualAmount);
            if(!player.isCreative())
                player.experienceLevel -= actualAmount;
        }
    }

    /** Decreases player's experience properly */
    protected static void decreaseExp(EntityPlayer player, int cost)
    {
        player.experienceLevel -= cost;

        if (player.experienceLevel < 0)
        {
            player.experienceLevel = 0;
            player.experience = 0.0F;
            player.experienceTotal = 0;
        }
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }
}
