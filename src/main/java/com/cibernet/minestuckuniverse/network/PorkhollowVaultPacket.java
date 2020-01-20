package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.tileentity.TileEntityPorkhollowVault;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PorkhollowVaultPacket extends MSUPacket
{
    BlockPos pos;
    EnumType type;

    @Override
    public MSUPacket generatePacket(Object... dat)
    {
        this.data.writeInt(((EnumType)dat[0]).ordinal());
        TileEntity te = (TileEntity) dat[1];
        data.writeInt(te.getPos().getX());
        data.writeInt(te.getPos().getY());
        data.writeInt(te.getPos().getZ());
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf dat)
    {
        type = EnumType.values()[dat.readInt()];
        pos = new BlockPos(dat.readInt(), dat.readInt(), dat.readInt());
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        if(player.world.getTileEntity(pos) instanceof TileEntityPorkhollowVault)
        {
            TileEntityPorkhollowVault vault = (TileEntityPorkhollowVault) player.world.getTileEntity(pos);

            switch (type)
            {
                case AUTO: vault.auto = !vault.auto; break;
                case TAKE:
                    MinestuckPlayerData.addBoondollars(player, vault.storedBoons);
                    vault.storedBoons = 0;
                break;
            }

        }
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }

    public enum EnumType
    {
        TAKE,
        AUTO,
        ;
    }
}
