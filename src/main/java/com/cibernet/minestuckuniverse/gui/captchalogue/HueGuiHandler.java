package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.HueModus;
import com.cibernet.minestuckuniverse.util.color.ColorGetter;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;
import java.util.List;

public class HueGuiHandler extends BaseModusGuiHandler
{
	public HueGuiHandler(Modus modus)
	{
		super(modus, 20);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = this.modus.getItems();
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i)
		{
			int id = ((HueModus)modus).getItemIndex(stacks.get(i));
			id = id ==-1 ? i : id;
			this.cards.add(new ColoredCard(getCardColor(stacks.get(i)),stacks.get(i), this, id, start + i * 26, (this.mapHeight - 26) / 2, i == 0));
		}
	}
	
	public void drawGuiMap(int xcor, int ycor) {
		super.drawGuiMap(xcor, ycor);
		if (!(this instanceof HueStackGuiHandler) && !this.cards.isEmpty()) {
			int startX = Math.max(0, ((GuiCard)this.cards.get(0)).xPos + 21 - this.mapX);
			int endX = Math.min(this.mapWidth, ((GuiCard)this.cards.get(this.cards.size() - 1)).xPos - this.mapX);
			int y = this.mapHeight / 2 + 1;
			drawRect(startX, y, endX, y + 2, -16777216);
		}
		
	}
	
	public static CardColor getCardColor(ItemStack stack)
	{
		return getCardColor(stack, true);
	}
	
	public static CardColor getCardColor(ItemStack stack, boolean includeRainbow)
	{
		if(stack.isEmpty())
			return CardColor.RAINBOW;
		
		List<Color> colors = ColorGetter.getColors(stack, 3);
		
		if(colors == null || colors.size() < 1)
			return CardColor.GRAY;
		if(colors.size() == 1)
			return getCardColor(colors.get(0));
		
		CardColor[] cardColors = new CardColor[colors.size()];
		
		for(int i = 0; i < cardColors.length; i++)
			cardColors[i] = getCardColor(colors.get(i));
		
		if(cardColors.length <= 2)
		{
			return cardColors[0] == cardColors[1] || !includeRainbow ? cardColors[0] : CardColor.RAINBOW;
		}
		
		if(cardColors[0] == cardColors[1] || cardColors[0] == cardColors[2])
			return cardColors[0];
		if(cardColors[1] == cardColors[2])
			return cardColors[1];
		
		if(!includeRainbow)
			return cardColors[0];
		
		return CardColor.RAINBOW;
		
	}
	
	public static CardColor getCardColor(Color color)
	{
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		
		if(hsb[1] <= 0.1f || hsb[2] <= 0.3f)
			return hsb[2] <= 0.3f ? CardColor.DARK_GRAY : CardColor.GRAY;
		
		hsb[0] *= 360.0f;
		if(hsb[0] < 16 || hsb[0] > 345)
			return CardColor.RED;
		else if(hsb[0] < 40)
			return CardColor.ORANGE;
		else if(hsb[0] < 65)
			return CardColor.YELLOW;
		else if(hsb[0] < 140)
			return CardColor.GREEN;
		else if(hsb[0] < 190)
			return CardColor.CYAN;
		else if(hsb[0] < 260)
			return CardColor.BLUE;
		else return CardColor.MAGENTA;
	}
	
	
	
	public static class ColoredCard extends GuiCard
	{
		public HueGuiHandler gui;
		public CardColor color;
		public boolean canUse;
		
		public ColoredCard(CardColor color, ItemStack item, HueGuiHandler gui, int index, int xPos, int yPos, boolean canUse)
		{
			super(item, gui, index, xPos, yPos);
			this.gui = gui;
			this.color = color;
			this.canUse = canUse;
		}
		
		@Override
		public void onClick(int mouseButton)
		{
			if(canUse || item.isEmpty())
				super.onClick(mouseButton);
		}
		
		@Override
		public String toString()
		{
			return index + ":" + item + " ("+color+")";
		}
		
		@Override
		protected void drawItemBackground()
		{
			this.gui.mc.getTextureManager().bindTexture(this.gui.getCardTexture(this));
			int minX = 0;
			int maxX = 21;
			int minY = 0;
			int maxY = 26;
			if (this.xPos + minX < this.gui.mapX) {
				minX += this.gui.mapX - (this.xPos + minX);
			} else if (this.xPos + maxX > this.gui.mapX + this.gui.mapWidth) {
				maxX -= this.xPos + maxX - (this.gui.mapX + this.gui.mapWidth);
			}
			
			if (this.yPos + minY < this.gui.mapY) {
				minY += this.gui.mapY - (this.yPos + minY);
			} else if (this.yPos + maxY > this.gui.mapY + this.gui.mapHeight) {
				maxY -= this.yPos + maxY - (this.gui.mapY + this.gui.mapHeight);
			}
			
			float r = ((color.color & 16711680) >> 16) / 255.0f;
			float g = ((color.color & '\uff00') >> 8) / 255.0f;
			float b = ((color.color & 255) >> 0) / 255.0f;
			
			GlStateManager.color(1,1,1);
			this.gui.drawTexturedModalRect(this.xPos + minX - this.gui.mapX, this.yPos + minY - this.gui.mapY, this.gui.getCardTextureX(this) + minX, this.gui.getCardTextureY(this) + minY, maxX - minX, maxY - minY);
			GlStateManager.color(r,g,b);
			this.gui.drawTexturedModalRect(this.xPos + minX - this.gui.mapX, this.yPos + minY - this.gui.mapY, this.gui.getCardOverlayTextureX(this) + minX, this.gui.getCardOverlayTextureY(this) + minY, maxX - minX, maxY - minY);
			GlStateManager.color(1,1,1);
			
		}
	}
	
	private int getCardOverlayTextureX(ColoredCard card)
	{
		return ((getTextureIndex(card)+(card.color == CardColor.RAINBOW ? 2 : 1)) % 12)* 21;
	}
	
	public int getCardOverlayTextureY(ColoredCard card)
	{
		return (int) Math.floor((getTextureIndex(card) +(card.color == CardColor.RAINBOW ? 2 : 1)) / 12)*26;
	}
	
	public enum CardColor
	{
		RED(0xFF0000, "Red"),
		ORANGE(0xFF6A00, "Orange"),
		YELLOW(0xFFFF00, "Yellow"),
		GREEN(0x00FF00, "Green"),
		CYAN(0x00FFFF, "Cyan"),
		BLUE(0x0000FF, "Blue"),
		MAGENTA(0xFF00FF, "Magenta"),
		GRAY(0xD8D8D8, "Silver"),
		DARK_GRAY(0x404040, "Gray"),
		RAINBOW(0xFFFFFF, "Rainbow"),
		;
		
		public final int color;
		public final String name;
		
		CardColor(int color, String name)
		{
			this.color = color;
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
