package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.BoondollarRegisterPacket;
import com.cibernet.minestuckuniverse.tileentity.TileEntityBoondollarRegister;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class GuiBoondollarRegister extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/boondollar_register.png");
	public EntityPlayer player;
	public TileEntityBoondollarRegister vault;
	public Minecraft mc;

	public int xSize = 176;
	public int ySize = 154;
	public int guiLeft;
	public int guiTop;

	public GuiButton withdrawButton;
	public GuiButton autoButton;
	public GuiTextField mavTextField;

	protected List<GuiTextField> textboxList = Lists.newArrayList();


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
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURES);
		int yOffset = this.height / 2 - 77;
		int xOffset = this.width / 2 - 88;
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, 176, 166);
		
		xOffset = (this.width - 176) / 2;
		yOffset = (this.height - 166) / 2;

		this.fontRenderer.drawString(I18n.translateToLocalFormatted(vault.getStoredBoons() > 9999999 ? "gui.vault.amountLabelShort" : "gui.vault.amountLabel", vault.getStoredBoons()),  xOffset+ 12,  yOffset+ 50, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.vault.mavLabel"),  xOffset+ 8,  yOffset+ 100, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.vault.label.2"),  xOffset+ 88 - (fontRenderer.getStringWidth(I18n.translateToLocal("gui.vault.label.2"))/2),  yOffset+ 25, 0x61B3E7);

		mavTextField.drawTextBox();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onResize(Minecraft mcIn, int w, int h)
	{
		super.onResize(mcIn, w, h);
		updateButtons();
	}

	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		super.initGui();
		int yOffset = this.height / 2 - 77;
		int xOffset = this.width / 2 - 88;
		withdrawButton = new GuiButton(0,xOffset+8,yOffset+65,70,20, I18n.translateToLocal("gui.vault.collect"));
		autoButton = new GuiButton(1,xOffset+78,yOffset+65,90,20, I18n.translateToLocalFormatted("gui.vault.auto", vault.auto ? "ON" : "OFF"));
		mavTextField = new GuiTextField(2, fontRenderer,xOffset+9, yOffset+106, 158, 20);
		mavTextField.setText(String.valueOf(vault.mav));
		updateButtons();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		autoButton.displayString = I18n.translateToLocalFormatted("gui.vault.auto", vault.auto ? "ON" : "OFF");
	}

	public void updateButtons()
	{
		buttonList.clear();

		buttonList.add(withdrawButton);
		buttonList.add(autoButton);
		textboxList.add(mavTextField);
		
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		mavTextField.setFocused(mavTextField.mouseClicked(mouseX, mouseY, mouseButton));
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		if((Character.digit(typedChar, 10) >= 0 || keyCode == 14) && mavTextField != null)
		{
			mavTextField.textboxKeyTyped(typedChar, keyCode);
			try {vault.mav = mavTextField.getText().isEmpty() ? 0 : Integer.parseInt(mavTextField.getText()); }
			catch (NumberFormatException e) {vault.mav = Integer.MAX_VALUE;}
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.VAULT, new Object[] {BoondollarRegisterPacket.EnumType.MAV, vault}));
			mavTextField.setText(String.valueOf(vault.mav));
		}

	}


	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if(button == autoButton)
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.VAULT, new Object[] {BoondollarRegisterPacket.EnumType.AUTO, vault}));
		if(button == withdrawButton)
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.VAULT, new Object[] {BoondollarRegisterPacket.EnumType.TAKE, vault}));
	}
}
