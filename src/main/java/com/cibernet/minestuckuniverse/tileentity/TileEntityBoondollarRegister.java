package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
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
    public int storedBoons = 0;
    private String customName = "";
    public String customMessage = "";
    public boolean auto = false;

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
        this.owner = IdentifierHandler.load(compound, "owner");
        storedBoons = compound.getInteger("StoredBoondollars");
        customName = compound.getString("CustomName");
        customMessage = compound.getString("CustomMessage");
        auto = compound.getBoolean("Automatic");
    }

    public NBTTagCompound saveToNBT(NBTTagCompound compound)
    {
        if(owner != null)
            this.owner.saveToNBT(compound, "owner");
        compound.setInteger("StoredBoondollars", storedBoons);
        compound.setString("CustomName", customName);
        compound.setString("CustomMessage", customMessage);
        compound.setBoolean("Automatic", auto);
        return compound;
    }

    public void setName(String name) {customName = name;}

    @Override
    public String getName() {
        return hasCustomName() ? customName : I18n.translateToLocal("container.porkhollowVault");
    }

    @Override
    public boolean hasCustomName() {
        return customName != "";
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
