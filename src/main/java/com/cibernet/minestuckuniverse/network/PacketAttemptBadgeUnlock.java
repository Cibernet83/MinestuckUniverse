package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.badges.MasterBadge;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketAttemptBadgeUnlock extends MSUPacket
{
    Skill badge;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        ByteBufUtils.writeUTF8String(data, ((Badge)args[0]).getRegistryName().toString());
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf data)
    {
        badge = MSUSkills.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(data)));
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

        if(((badge instanceof MasterBadge && data.getMasterBadge() == null) || data.getBadgesLeft() > 0) && !data.hasSkill(badge) && ((player.isCreative() && data.hasMasterControl()) || badge.canUnlock(player.world, player)))
            data.addSkill(badge, true);
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }
}
