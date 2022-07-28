package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import com.cibernet.minestuckuniverse.gui.container.ContainerItemVoid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiItemVoid extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/container/item_void.png");
	private final InventoryPlayer player;

	public GuiItemVoid(EntityPlayer player)
	{
		super(new ContainerItemVoid(player));
		this.player = player.inventory;

		xSize = 176;
		ySize = 176;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String displayName = GameData.getItemVoid().getDisplayName().getFormattedText();
		this.fontRenderer.drawString(displayName, this.xSize / 2 - this.fontRenderer.getStringWidth(displayName) / 2, 4, 0x000E33);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 94, 4210752);
	}
}
