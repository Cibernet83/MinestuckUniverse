package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.CapitalistModus;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CapitalistGuiHandler extends BaseModusGuiHandler
{
	public CapitalistGuiHandler(Modus modus)
	{
		super(modus, 2);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		emptySylladex.displayString = I18n.format("gui.capitalistBuyAll");
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
			this.cards.add(new GuiPricedCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
		
	}
	
	
	public static class GuiPricedCard extends GuiCard
	{
		public int price;
		public CapitalistGuiHandler gui;
		
		public GuiPricedCard(ItemStack item, CapitalistGuiHandler gui, int index, int xPos, int yPos)
		{
			super(item, gui, index, xPos, yPos);
			this.price = CapitalistModus.getItemPrice(item);
			this.gui = gui;
		}
		
		@Override
		protected void drawTooltip(int x, int y)
		{
			if (!this.item.isEmpty())
			{
				FontRenderer font = item.getItem().getFontRenderer(item);
				net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(item);
				List<String> tooltip = gui.getItemToolTip(item);
				tooltip.add(new TextComponentTranslation("tooltip.capitalistPrice", price).setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());
				gui.drawHoveringText(tooltip, x, y, (font == null ? Minecraft.getMinecraft().fontRenderer : font));
				net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
			}
		}
	}
}
