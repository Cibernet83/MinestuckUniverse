package com.cibernet.minestuckuniverse.gui.container;

import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;

public class ContainerMachineChasis extends Container

{private final InventoryPlayer player;
    private final TileEntityMachineChasis tileEntity;

    public ContainerMachineChasis(InventoryPlayer player, TileEntityMachineChasis tileEntity)
    {
        super();
        this.player = player;
        this.tileEntity = tileEntity;
    }


}
