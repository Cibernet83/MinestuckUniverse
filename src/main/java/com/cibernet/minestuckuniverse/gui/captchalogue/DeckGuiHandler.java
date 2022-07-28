package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class DeckGuiHandler extends BaseModusGuiHandler
{
	public DeckGuiHandler(Modus modus)
	{
		super(modus, 8);
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = this.modus.getItems();
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		super.updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i) {
			this.cards.add(new DeckCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
		
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		int index = textureIndex;
		if(card.index % 2 == 1)
			index++;
		if(card.index != 0 && !card.item.isEmpty())
			index += 2;
		return index;
	}
	
	public static class DeckCard extends GuiCard
	{
		public DeckGuiHandler gui;
		public DeckCard(ItemStack item, DeckGuiHandler gui, int index, int xPos, int yPos)
		{
			super(item, gui, index, xPos, yPos);
			this.gui = gui;
		}
		
		@Override
		protected void drawItem()
		{
			if(index == 0)
			super.drawItem();
		}
		
		@Override
		protected void drawTooltip(int x, int y)
		{
			if(index == 0)
				super.drawTooltip(x, y);
			else
			{
				if (!this.item.isEmpty())
				{
					FontRenderer font = item.getItem().getFontRenderer(item);
					net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(item);
					List<String> tooltip = new ArrayList<String>(){{add("???");}};
					gui.drawHoveringText(tooltip, x, y, (font == null ? Minecraft.getMinecraft().fontRenderer : font));
					net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
				}
				
			}
		}
	}
}
