package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.EnergyModus;
import net.minecraft.client.renderer.GlStateManager;

public class EnergyGuiHandler extends BaseModusGuiHandler
{
	EnergyModus modus;
	public EnergyGuiHandler(EnergyModus modus)
	{
		super(modus, 29);
		this.modus = modus;
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		return super.getTextureIndex(card) + (modus.charge <= 0 ? 1 : 0);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		
		GlStateManager.color(1,1,1);
		GlStateManager.disableLighting();
		
		mc.getTextureManager().bindTexture(EXTRAS);
		drawTexturedModalRect(xOffset + 15, yOffset + 173, 97, 16, 80, 24);
		
		if(modus.charge > 0)
		{
			drawTexturedModalRect(xOffset + 15, yOffset + 173, 97, 40, 80*modus.charge/modus.getMaxCharge(), 24);
			drawTexturedModalRect(xOffset + 15, yOffset + 173, 97, 64, 17, 24);
		}
		
		String str = modus.charge + "/" + modus.getMaxCharge();
		mc.fontRenderer.drawStringWithShadow(str, xOffset + 75 - mc.fontRenderer.getStringWidth(str), yOffset + 180, 0xFFFFFF);
	}
}
