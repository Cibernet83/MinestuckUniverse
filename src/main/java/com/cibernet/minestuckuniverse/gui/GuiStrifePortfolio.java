package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.client.MSUFontRenderer;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiStrifePortfolio extends GuiPlayerStats
{

	private static final ResourceLocation guiStrifePortfolio = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/strife_specibus/strife_portfolio.png");
	private static final ResourceLocation guiPortfolioTabs = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/strife_specibus/portfolio_tabs.png");
	private static final ResourceLocation guiStrifePortfolioBg = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/strife_specibus/portfolio_bg.png");
	private static final ResourceLocation guiStrifeCard = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/strife_specibus/strife_card.png");
	public static final ResourceLocation icons = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/icons.png");
	private static final String iconsLoc = "textures/gui/strife_specibus/icons/";

	private static final int columnWidth = 70, columns = 3;

	private static final int iconsOffX = 23, iconsOffY = 166;
	private float scale;
	private int blankCardCount = 0;
	private int selectedCard;
	private boolean isSelectingCard = false;
	private int mouseX;
	private int mouseY;
	private boolean mousePressed;
	private final FontRenderer font = MSUFontRenderer.fontSpecibus;

	private StrifeSpecibus[] portfolio;
	private int activeSpecibus = -1;

	public GuiStrifePortfolio()
	{
		super();
		guiWidth = 226;
		guiHeight = 188;


	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		super.drawScreen(xcor, ycor, par3);

		isSelectingCard = false;
		mouseX = xcor;
		mouseY = ycor;

		this.drawDefaultBackground();
		scale = 1;
		float cardScale = 0.25f;
		blankCardCount = 0;

		portfolio = StrifePortfolioHandler.getPortfolio(mc.player);
		activeSpecibus = mc.player.getCapability(MSUCapabilities.STRIFE_DATA, null).getSelectedSpecibusIndex();

		if(MinestuckConfig.dataCheckerAccess)
		{
			mc.getTextureManager().bindTexture(icons);
			drawTexturedModalRect(xOffset+198, yOffset, 112, 32, 28, 35);
		}

		int i;

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifePortfolioBg);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);


		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset, yOffset+0, 20, 58, 98, 94);

		drawCard(11, 9, cardScale, 6, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+2, yOffset+6, 4, 44, 132, 120);

		drawCard(59, 7, cardScale, 7, true);
		drawCard(12, 50, cardScale, 9, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+21, yOffset+4, 0, 18, 152, 134);

		drawCard(107, 7, cardScale, 8, true);
		drawCard(56, 40, cardScale, 0, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+45, yOffset+10, 0, 0, 164, 152);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+173, yOffset+6, 124, 0, 7, 4);

		drawCard(107, 33, cardScale, 5, true);
		drawCard(56, 80, cardScale, 4, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+81, yOffset+28, 0, 8, 137, 120);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+218, yOffset+46, 142, 22, 2, 10);

		drawCard(159, 25, cardScale, 3, true);
		drawCard(107, 77, cardScale, 1, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+124, yOffset+52, 0, 32, 96, 96);

		drawCard(159, 69, cardScale, 2, true);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		GlStateManager.translate(0, 0, 1);
		this.drawTexturedModalRect(xOffset+168, yOffset+96, 204, 0, 52, 52);

		drawCard(10, 85, cardScale, activeSpecibus, false);

		onCardHover(10, 85, cardScale, activeSpecibus, false);
		onCardHover(159, 69, cardScale, 2, true);
		onCardHover(107, 77, cardScale, 1, true);
		onCardHover(159, 25, cardScale, 3, true);
		onCardHover(56, 80, cardScale, 4, true);
		onCardHover(107, 33, cardScale, 5, true);
		onCardHover(56, 40, cardScale, 0, true);
		onCardHover(107, 7, cardScale, 8, true);
		onCardHover(12, 50, cardScale, 9, true);
		onCardHover(59, 7, cardScale, 7, true);
		onCardHover(11, 9, cardScale, 6, true);

		setScale(1);
		drawTabs();

		this.mc.getTextureManager().bindTexture(guiStrifePortfolio);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		drawActiveTabAndOther(xcor, ycor);
		for(i = 0; i < portfolio.length; i++)
		{
			StrifeSpecibus specibus = portfolio[i];
			if(specibus == null || specibus.getKindAbstratus() == null)
				continue;

			ResourceLocation loc = specibus.getKindAbstratus().getRegistryName();
			setScale(0.0625f);
			this.mc.getTextureManager().bindTexture(new ResourceLocation(loc.getResourceDomain(), iconsLoc+loc.getResourcePath()+".png"));
			this.drawTexturedModalRect((xOffset+23+(20*i))/scale,(yOffset+166)/scale,0,0,256,256);
		}
		setScale(1);

		for(i = 0; i < portfolio.length; i++)
		{
			StrifeSpecibus specibus = portfolio[i];

			if(specibus != null && isPointInRegion((xOffset+22+(20*i)),(yOffset+165), 18, 18, mouseX, mouseY))
			{
				selectedCard = i;
				isSelectingCard = true;

				ArrayList<String> text = new ArrayList<>();
				if(!specibus.getDisplayName().isEmpty())
					text.add(specibus.getDisplayName());
				drawHoveringText(text, mouseX, mouseY);

				if(Mouse.isButtonDown(1))
				{
					if(!mousePressed)
					{
						StrifePortfolioHandler.retrieveCard(mc.player, i);
						MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.RETRIEVE_STRIFE, i));
					}
					mousePressed = true;
				}
				else if(Mouse.isButtonDown(0))
				{
					if(!mousePressed)
						MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.SET_ACTIVE_STRIFE, i, true));
					mousePressed = true;
				}
				else mousePressed = false;
			}
		}

		if(!isSelectingCard) selectedCard = -1;
	}

	public void drawCard(int cardX, int cardY, float cardScale, int index, boolean checkSelected)
	{
		if(index >= 0 && index < portfolio.length && (!checkSelected || activeSpecibus != index)) drawCard(cardX, cardY, cardScale, index, portfolio[index]);
	}

	public void onCardHover(int cardX, int cardY, float cardScale, int index, boolean checkSelected)
	{
		if(!(index >= 0 && index < portfolio.length && (!checkSelected || activeSpecibus != index)))
			return;

		if(portfolio[index] == null)
			return;

		setScale(cardScale);
		int x = (int) ((xOffset+cardX)) - (index == selectedCard ? 5 : 0);
		int y = (int) ((yOffset+cardY)) - (index == selectedCard ? 5 : 0);

		int x1 = (int)(x);
		int y1 = (int)(y);
		int xs = (int)((200+(selectedCard == index ? 20 : 0))*scale);
		int ys = (int)((256+(selectedCard == index ? 20 : 0))*scale);
		int x2 = x1+xs;
		int y2 = y1+ys;
		if(isPointInRegion(x1, y1, xs, ys, mouseX, mouseY) && (!isSelectingCard || selectedCard == index))
		{
			selectedCard = index;
			isSelectingCard = true;
			if(Mouse.isButtonDown(1))
			{
				if(!mousePressed)
					MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.RETRIEVE_STRIFE, index, true));
				mousePressed = true;
			}
			else if(Mouse.isButtonDown(0))
			{
				if(!mousePressed)
					MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.SET_ACTIVE_STRIFE, index, true));
				mousePressed = true;
			}
			else mousePressed = false;
		}
		setScale(1);
	}

	public void drawCard(int cardX, int cardY, float cardScale, int index, StrifeSpecibus specibus)
	{
		if(specibus == null) return;

		KindAbstratus abstratus = specibus.getKindAbstratus();
		ResourceLocation loc = abstratus == null ? null : abstratus.getRegistryName();
		String displayName = specibus.getDisplayNameForCard();

		int txOffset = mc.fontRenderer.getStringWidth(displayName);
		List<ItemStack> items = specibus.getContents();
		int totalItems = items.size();

		int x = (int) ((xOffset+cardX)) - (index == selectedCard ? 5 : 0);
		int y = (int) ((yOffset+cardY)) - (index == selectedCard ? 5 : 0);

		setScale(cardScale);
		this.mc.getTextureManager().bindTexture(guiStrifeCard);
		this.drawTexturedModalRect(x/scale, y/scale, 28, 0, 200, 256);

		if(loc != null)
		{
			setScale(cardScale/2.5f);
			this.mc.getTextureManager().bindTexture(new ResourceLocation(loc.getResourceDomain(), iconsLoc+loc.getResourcePath()+".png"));
			this.drawTexturedModalRect(x/scale+57, y/scale+148, 0, 0, 256, 256);
		}

		setScale(cardScale*2);
		font.drawString(I18n.format("gui.strifePortfolio.specibus"), x/scale+5, y/scale+4, 0xFF00E371, false);
		setScale(cardScale*2.5f);
		mc.fontRenderer.drawString(displayName, x/scale+52-font.getStringWidth(displayName), y/scale+91, 0xFF00E371, false);
		setScale(cardScale);
		font.drawString(I18n.format("gui.strifePortfolio.deck"), x/scale+16, y/scale+179, 0xFFFFFFFF, false);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int deckxpos = (int) (94 - 23*(Math.min(items.size(),5)/2f));
		int n = 0;
		for(ItemStack i : items)
		{
			int ixoff = (int)(deckxpos + (n%5)*(23)) - (int)(n/5f);
			int iyoff = 193 - (int)(n/5f)*2;
			setScale(cardScale);
			mc.getTextureManager().bindTexture(icons);
			drawTexturedModalRect(x/scale + ixoff, y/scale + iyoff, 0, 122, 21, 26);
			if(n >= items.size()-5)
				drawItemStack(i, (int)(x/scale) + ixoff+2, (int)(y/scale) + iyoff+4, i.getDisplayName());
			n++;
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		setScale(1);
	}


	/**
	 * Draws an ItemStack.
	 *
	 */

	private void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.translate(0, 0, -150);
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		GlStateManager.translate(0, 0, 150);
		RenderHelper.disableStandardItemLighting();
	}

	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}

}