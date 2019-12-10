package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityRedTransportalizer extends TileEntityTransportalizer
{
    public IdentifierHandler.PlayerIdentifier owner;


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.owner = IdentifierHandler.load(compound, "owner");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if(owner != null)
            this.owner.saveToNBT(compound, "owner");
        return super.writeToNBT(compound);
    }
}
