package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.ChasityModus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ChasityGuiHandler extends BaseModusGuiHandler
{
	ChasityModus modus;
	
	public ChasityGuiHandler(ChasityModus modus)
	{
		super(modus, 31);
		this.modus = modus;
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
		
		for(int i = 0; i < stacks.size(); ++i) {
			this.cards.add(new ChasityCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		return super.getTextureIndex(card) + (modus.canUse(card.index) ? 0 : 1);
	}
	
	public static class ChasityCard extends GuiCard
	{
		protected ChasityGuiHandler gui;
		
		public ChasityCard(ItemStack stack, ChasityGuiHandler gui, int index, int x, int y)
		{
			super(stack, gui, index, x, y);
			this.gui = gui;
		}
		
		@Override
		protected void drawTooltip(int mouseX, int mouseY)
		{
			if(gui.modus.canUse(index))
				super.drawTooltip(mouseX, mouseY);
		}
		
		@Override
		protected void drawItem()
		{
			if(gui.modus.canUse(index))
				super.drawItem();
		}
	}
}
