package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.BoondollarRegisterPacket;
import com.cibernet.minestuckuniverse.tileentity.TileEntityBoondollarRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.io.IOException;

public class GuiBoondollarRegister extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/porkhollow_atm.png");
	public EntityPlayer player;
	public TileEntityBoondollarRegister vault;
	public Minecraft mc;

	public int xSize = 176;
	public int ySize = 166;
	public int guiLeft;
	public int guiTop;

	public GuiButton withdrawButton;
	public GuiButton autoButton;


	public GuiBoondollarRegister(EntityPlayer player, TileEntityBoondollarRegister te)
	{
		vault = te;
		this.player = player;
		this.mc = Minecraft.getMinecraft();
		this.fontRenderer = mc.fontRenderer;
		
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURES);
		int yOffset = this.height / 2 - 83;
		int xOffset = this.width / 2 - 88;
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, 176, 166);
		
		xOffset = (this.width - 176) / 2;
		yOffset = (this.height - 166) / 2;

		this.fontRenderer.drawString(I18n.translateToLocalFormatted("gui.vault.amountLabel", vault.storedBoons),  xOffset+ 15,  yOffset+ 45, 4210752);

		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int yOffset = this.height / 2 - 83;
		int xOffset = this.width / 2 - 88;
		withdrawButton = new GuiButton(0,xOffset+9,yOffset+88,158,20, I18n.translateToLocal("gui.atm.withdraw"));
		autoButton = new GuiButton(0,xOffset+9,yOffset+138,158,20, I18n.translateToLocalFormatted("gui.vault.auto", vault.auto ? "ON" : "OFF"));
		updateButtons();
	}
	
	public void updateButtons()
	{
		buttonList.clear();

		buttonList.add(withdrawButton);
		buttonList.add(autoButton);
		
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if(button == autoButton)
		{
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.VAULT, new Object[] {BoondollarRegisterPacket.EnumType.AUTO, vault}));
		}
		
		if(button == withdrawButton)
		{
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.VAULT, new Object[] {BoondollarRegisterPacket.EnumType.TAKE, vault}));
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
		
	}
}
