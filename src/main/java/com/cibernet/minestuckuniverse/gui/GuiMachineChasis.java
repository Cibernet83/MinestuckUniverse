package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.gui.container.ContainerMachineChasis;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMachineChasis extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/machine_chasis");
    private final InventoryPlayer player;
    private final TileEntityMachineChasis tileEntity;

    public GuiMachineChasis(InventoryPlayer player, TileEntityMachineChasis tileEntity)
    {
        super(new ContainerMachineChasis(player, tileEntity));
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
