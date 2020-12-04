package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.tileentity.TileEntityBoondollarRegister;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class BoondollarRegisterPacket extends MSUPacket
{
    BlockPos pos;
    EnumType type;
    int mav;

    @Override
    public MSUPacket generatePacket(Object... dat)
    {
        this.data.writeInt(((EnumType)dat[0]).ordinal());
        TileEntityBoondollarRegister te = (TileEntityBoondollarRegister) dat[1];
        data.writeInt(te.getPos().getX());
        data.writeInt(te.getPos().getY());
        data.writeInt(te.getPos().getZ());

        data.writeInt(te.mav);
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf dat)
    {
        type = EnumType.values()[dat.readInt()];
        pos = new BlockPos(dat.readInt(), dat.readInt(), dat.readInt());
        mav = dat.readInt();
        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        if(player.world.getTileEntity(pos) instanceof TileEntityBoondollarRegister)
        {
            TileEntityBoondollarRegister vault = (TileEntityBoondollarRegister) player.world.getTileEntity(pos);

            switch (type)
            {
                case AUTO: vault.auto = !vault.auto; break;
                case TAKE:
                    MinestuckPlayerData.addBoondollars(player, vault.getStoredBoons());
                    vault.setStoredBoons(0);
                break;
                case MAV: vault.mav = mav; break;
            }
            //player.world.scheduleUpdate(pos, vault.getBlockType(), vault.getBlockType().tickRate(player.world));
            player.world.notifyBlockUpdate(vault.getPos(), player.world.getBlockState(pos), player.world.getBlockState(pos), 3);
        }
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }

    public enum EnumType
    {
        UPDATE,
        TAKE,
        AUTO,
        MAV,
        ;
    }
}
