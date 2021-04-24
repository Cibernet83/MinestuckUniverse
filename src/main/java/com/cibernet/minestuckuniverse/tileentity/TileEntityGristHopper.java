package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.IdentifierHandler;
import ibxm.Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityGristHopper extends TileEntity implements ITickable
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

    @Override
    public void update()
    {

        List<EntityGrist> entities = world.getEntitiesWithinAABB(EntityGrist.class, new AxisAlignedBB(pos.up()));
        if(world != null && !entities.isEmpty() && !world.getBlockState(pos.up()).isFullBlock())
        {
            if(owner != null && !world.isRemote)
            {
                for(EntityGrist grist : entities)
                {
                    if(grist.isEntityAlive())
                        grist.consumeGrist(owner, false);
                }
            }
        }

    }

}
