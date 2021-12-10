package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiEditMode extends GuiScreen
{
	private static final ResourceLocation HOTBAR = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/edit_mode/edit_mode_hotbar.png");
	private static final ResourceLocation SPIRO = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/edit_mode/spiro.png");

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		mc.getTextureManager().bindTexture(HOTBAR);
		drawTexturedModalRect(0, 0, 0, 0, 72, 72);

		for(int i = 0; i < width; i += 64)
			drawTexturedModalRect(72+i, 0, 72, 0, 64, 72);

		mc.getTextureManager().bindTexture(SPIRO);
		drawScaledCustomSizeModalRect(2, 2, 0, 0, 256, 256, 20, 20, 256, 256);

		mc.getTextureManager().bindTexture(HOTBAR);
		int len = Tools.values().length;
		for(int i = 0; i < len; i++)
		{
			drawTexturedModalRect(width/2 - len*12 + (i*24), 8, 0, 82, 20, 20);
			drawTexturedModalRect(width/2 - len*12 + (i*24)+2, 10, i*16, 102, 16, 16);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);

		if(isPointInRegion(32, 40, 7, 7, mouseX, mouseY))
		{
			if(Mouse.isButtonDown(0))
			{
				mc.player.moveRelative(0, 1, 0, 0.025f);
				mc.player.capabilities.isFlying = true;
			}
			else if(Mouse.isButtonDown(1))
			{
				mc.player.moveRelative(0, -1, 0, 0.025f);
				mc.player.capabilities.isFlying = true;
			}
		}

		if(Mouse.isButtonDown(0))
		{
			mc.player.moveRelative((isPointInRegion(21, 38, 8, 11, mouseX, mouseY) ? 1 : 0) -
							(isPointInRegion(42, 38, 8, 11, mouseX, mouseY) ? 1 : 0), 0,
					(isPointInRegion(30, 29, 11, 8, mouseX, mouseY) ? 1 : 0) -
							(isPointInRegion(30, 50, 11, 8, mouseX, mouseY) ? 1 : 0), 0.025f);
		}
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();


	}

	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY) {
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}

	public enum Tools
	{
		SELECT,
		REVISE,
		DEPLOY,
		PHERNALIA,
		GRIST_CACHE,
		ATHENEUM,
		ALCHEMY_REGISTRY
	}
}
