package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class ScratchAndSniffGuiHandler extends BaseModusGuiHandler
{
	
	public ScratchAndSniffGuiHandler(Modus modus)
	{
		super(modus, 14);
	}
	
	
	public void updateContent() {
		NonNullList<ItemStack> stacks = this.modus.getItems();
		this.cards.clear();
		int columns = (stacks.size() + 1) / 2;
		this.maxWidth = Math.max(this.mapWidth, 10 + columns * 21 + (columns - 1) * 5);
		this.maxHeight = this.mapHeight;
		super.updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (columns * 21 + (columns - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i) {
			this.cards.add(new ScratchCard((ItemStack)stacks.get(i), this, i, start + i / 2 * 26, (this.mapHeight - 52 - 5) / 2 + i % 2 * 31));
		}
	}
	
	public void updatePosition() {
		int columns = (this.cards.size() + 1) / 2;
		this.maxWidth = Math.max(this.mapWidth, 10 + columns * 21 + (columns - 1) * 5);
		this.maxHeight = this.mapHeight;
		int start = Math.max(5, (this.mapWidth - (columns * 21 + (columns - 1) * 5)) / 2);
		
		for(int i = 0; i < this.cards.size(); ++i) {
			GuiCard card = (GuiCard)this.cards.get(i);
			card.xPos = start + i / 2 * 26;
			card.yPos = (this.mapHeight - 52 - 5) / 2 + i % 2 * 31;
		}
		
	}
	
	public static class ScratchCard extends GuiCard
	{
		ScratchAndSniffGuiHandler gui;
		
		public ScratchCard(ItemStack stack, ScratchAndSniffGuiHandler gui, int i, int i1, int i2)
		{
			super(stack, gui, i, i1, i2);
			this.gui = gui;
		}
		
		@Override
		protected void drawItem()
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			if (!this.item.isEmpty())
			{
				int x = this.xPos + 2 - this.gui.mapX;
				int y = this.yPos + 7 - this.gui.mapY;
				if (x >= this.gui.mapWidth || y >= this.gui.mapHeight || x + 16 < 0 || y + 16 < 0)
					return;
				
				HueGuiHandler.CardColor color = HueGuiHandler.getCardColor(item, false);
				int u = (color.ordinal() % 16)* 16;
				int v = (color.ordinal() / 16)* 16;
				
				this.gui.mc.getTextureManager().bindTexture(EXTRAS);
				gui.drawTexturedModalRect(x, y, u, v, 16, 16);
			}
		}
		
		@Override
		protected void drawTooltip(int x, int y)
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
