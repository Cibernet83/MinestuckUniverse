package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.network.BoondollarRegisterPacket;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.PorkhollowAtmPacket;
import com.google.common.collect.Lists;
import com.mraof.minestuck.client.gui.GuiSburbMachine;
import com.mraof.minestuck.util.MinestuckPlayerData;
import javafx.scene.input.KeyCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import scala.Char;

import java.io.IOException;
import java.util.List;

public class GuiPorkhollowAtm extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/porkhollow_atm.png");
	public EntityPlayer player;
	public Minecraft mc;
	
	public int xSize = 176;
	public int ySize = 166;
	public int guiLeft;
	public int guiTop;
	public int tab = 0;
	public GuiTextField selectedTextField;
	
	public GuiButton withdrawButton;
	public GuiButton goButton;
	public GuiTextField amountTextField;
	public GuiTextField nTextField;
	
	protected List<GuiTextField> textboxList = Lists.newArrayList();
	
	public GuiPorkhollowAtm(EntityPlayer player)
	{
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
		
		switch(tab)
		{
			case 1:
				this.fontRenderer.drawString(I18n.translateToLocal("gui.atm.amountLabel"),  xOffset+ 15,  yOffset+ 45, 4210752);
				this.fontRenderer.drawString(I18n.translateToLocal("gui.atm.nLabel"),  xOffset+ 15,  yOffset+ 80, 4210752);
			break;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		for(GuiTextField field : textboxList)
			field.drawTextBox();
	}
	
	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		super.initGui();
		int yOffset = this.height / 2 - 83;
		int xOffset = this.width / 2 - 88;
		withdrawButton = new GuiButton(0,xOffset+9,yOffset+38,158,20, I18n.translateToLocal("gui.atm.withdraw"));
		goButton = new GuiButton(0,xOffset+9,yOffset+138,158,20, I18n.translateToLocal("gui.atm.go"));
		amountTextField = new GuiTextField(1, fontRenderer,xOffset+9, yOffset+55, 158, 20);
		amountTextField.setText("0");
		nTextField = new GuiTextField(1, fontRenderer,xOffset+9, yOffset+89, 158, 20);
		nTextField.setMaxStringLength(2);
		nTextField.setText("1");
		updateButtons();
	}


	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	public void updateButtons()
	{
		buttonList.clear();
		textboxList.clear();
		
		switch(tab)
		{
			case 0:
				buttonList.add(withdrawButton);
			break;
			case 1:
				buttonList.add(goButton);
				textboxList.add(amountTextField);
				textboxList.add(nTextField);
			break;
			
		}
		
	}
	
	public void switchTab(int tab)
	{
		this.tab = tab;
		updateButtons();
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		if(keyCode == 15 && textboxList.size() > 0)
			setSelectedTextField(textboxList.get((textboxList.indexOf(selectedTextField)+1) % textboxList.size()));

		if((Character.digit(typedChar, 10) >= 0 || keyCode == 14) && selectedTextField != null)
		{
			int value;
			selectedTextField.textboxKeyTyped(typedChar, keyCode);
			try {value = selectedTextField.getText().isEmpty() ? 0 : Integer.parseInt(selectedTextField.getText()); }
			catch (NumberFormatException e) {value = Integer.MAX_VALUE;}

			if(String.valueOf(value).length() > selectedTextField.getMaxStringLength()-1)
				value = (int) (Math.pow(10,selectedTextField.getMaxStringLength()) -1);

			selectedTextField.setText(String.valueOf(value));
		}

	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		switch(tab)
		{
			case 1:
				if(amountTextField.mouseClicked(mouseX, mouseY, mouseButton))
					setSelectedTextField(amountTextField);
				else if(nTextField.mouseClicked(mouseX, mouseY, mouseButton))
					setSelectedTextField(nTextField);
				else setSelectedTextField(null);
			break;
		}
	}
	
	protected static int getInt(GuiTextField field)
	{
		String text = field.getText();
		
		try {return Integer.parseInt(text);}
		catch(NumberFormatException e) {return 0;}
	}
	
	protected void setSelectedTextField(GuiTextField field)
	{
		if(selectedTextField != null)
			selectedTextField.setFocused(false);
		selectedTextField = field;
		if(selectedTextField != null)
			selectedTextField.setFocused(true);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		
		
		if(button == withdrawButton)
			switchTab(1);
		
		if(button == goButton)
		{
			int amount = getInt(amountTextField);
			int n = getInt(nTextField);
			amount *= n;
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ATM, new Object[] {tab == 1 ? PorkhollowAtmPacket.Type.TAKE : PorkhollowAtmPacket.Type.SEND, player, amount, n}));
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
			
		}
		
	}
}
