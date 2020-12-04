package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityBoondollarRegister extends TileEntity implements IWorldNameable
{
    public IdentifierHandler.PlayerIdentifier owner = IdentifierHandler.nullIdentifier;
    public String ownerName = "???";
    protected int storedBoons = 0;
    private String customName = "";
    public String customMessage = "";
    public int mav = 0;
    public int prevInput = 0;
    public boolean auto = false;

    public static final int maxCapacity = Integer.MAX_VALUE;

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        loadFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        return saveToNBT(compound);
    }

    public void loadFromNBT(NBTTagCompound compound)
    {
        owner = IdentifierHandler.load(compound, "owner");
        if(compound.hasKey("OwnerName"))
        ownerName = compound.getString("OwnerName");
        storedBoons = compound.getInteger("StoredBoondollars");
        customName = compound.getString("CustomName");
        customMessage = compound.getString("CustomMessage");
        auto = compound.getBoolean("Automatic");
        mav = compound.getInteger("MAV");
        prevInput = compound.getInteger("PreviousInput");
    }

    public NBTTagCompound saveToNBT(NBTTagCompound compound)
    {
        if(owner != null)
            this.owner.saveToNBT(compound, "owner");
        compound.setString("OwnerName", ownerName);
        compound.setInteger("StoredBoondollars", storedBoons);
        compound.setString("CustomName", customName);
        compound.setString("CustomMessage", customMessage);
        compound.setBoolean("Automatic", auto);
        compound.setInteger("MAV", mav);
        compound.setInteger("PreviousInput", prevInput);

        return compound;
    }

    public void setName(String name) {customName = name;}

    @Override
    public String getName() {
        return hasCustomName() ? customName : I18n.translateToLocal("container.boondollarRegister");
    }

    @Override
    public boolean hasCustomName() {
        return !customName.isEmpty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }



    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = this.getUpdateTag();
        return new SPacketUpdateTileEntity(this.pos, 2, tagCompound);
    }

    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    public void addBoondollars(int count)
    {
        if(auto)
        {
            if(owner != null)
                MinestuckPlayerData.addBoondollars(owner, count);
        }
        else storedBoons += count;
        prevInput = count;
    }

    public int getComparatorOutput()
    {
        if(mav == 0)
            return 15;
        return prevInput/mav;
    }

    public int getStoredBoons()
    {
        return storedBoons;
    }

    public void setStoredBoons(int v)
    {
        storedBoons = v;
    }

    public boolean isFull(int count)
    {
        try { return Math.addExact(storedBoons, count) > maxCapacity; }
        catch (ArithmeticException e) { return true;}
    }
}
