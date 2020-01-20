package com.cibernet.minestuckuniverse.gui.container;

import com.cibernet.minestuckuniverse.tileentity.TileEntityPorkhollowVault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPorkhollowVault extends Container
{
    private final EntityPlayer player;
    private final TileEntityPorkhollowVault vault;

    public ContainerPorkhollowVault(EntityPlayer player, TileEntityPorkhollowVault te)
    {
        this.player = player;
        vault = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
