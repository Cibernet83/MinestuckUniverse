package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.MemoryModus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MemoryGuiHandler extends BaseModusGuiHandler
{
	MemoryCard selectedA = null;
	MemoryCard selectedB = null;
	
	public MemoryGuiHandler(MemoryModus modus)
	{
		super(modus, 35);
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<MemoryModus.MemoryEntry> stacks = ((MemoryModus)this.modus).getDisplayEntries();
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size()*2 - 1) * 5);
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size()*2 - 1) * 5)) / 2);
		
		for(MemoryModus.MemoryEntry entry : stacks)
			this.cards.add(new MemoryCard(entry.getStack(), this, entry.getIndex(), 0, 0));
		
		updatePosition();
	}
	
	@Override
	public void updatePosition()
	{
		int length = (int) Math.ceil(Math.sqrt(cards.size()));

		this.maxWidth = Math.max(this.mapWidth, 10 + length * 21 + (length - 1) * 5);
		this.maxHeight = Math.max(this.mapHeight, 10 + length * 21 + (length - 1) * 5);
		int startX = Math.max(5, (this.mapWidth - (length* 21 + (length - 1) * 5)) / 2);
		int startY = Math.max(5, (this.mapHeight - (length* 21 + (length - 1) * 5)) / 2);
		//(this.mapHeight - 26) / 2;

		for(int i = 0; i < this.cards.size(); ++i) {
			GuiCard card = this.cards.get(i);
			card.xPos = startX + ((i % length) * 26);
			card.yPos = startY + ((i/length) * 26);
		}
		
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		selectedA = null;
		selectedB = null;
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		return super.getTextureIndex(card) + (((MemoryCard)card).isSelected() || card.item.isEmpty() ? 0 : 1);
	}
	
	class MemoryCard extends GuiCard
	{
		MemoryGuiHandler gui;
		
		public MemoryCard(ItemStack stack, MemoryGuiHandler guiHandler, int index, int x, int y)
		{
			super(stack, guiHandler, index, x, y);
			gui = guiHandler;
		}
		
		public boolean isSelected()
		{
			return equals(selectedA) || equals(selectedB);
		}
		
		@Override
		public void onClick(int mouseButton)
		{
			if(item.isEmpty())
				onClickSuper(mouseButton);
			else if(!isSelected())
			{
				if(selectedA == null)
					selectedA = this;
				else if(selectedB == null)
					selectedB = this;
				else
				{
					if((selectedA.item.isEmpty() && selectedB.item.isEmpty()) || (selectedA.item.isItemEqual(selectedB.item)&& selectedA.item.getCount() == selectedB.item.getCount()))
						selectedA.onClickSuper(mouseButton);
					selectedA = null;
					selectedB = null;
				}
			} else
			{
				selectedA = null;
				selectedB = null;
			}
		}

		@Override
		protected void drawItem()
		{
			if(isSelected())
				super.drawItem();
		}

		@Override
		protected void drawTooltip(int mouseX, int mouseY)
		{
			if(isSelected())
				super.drawTooltip(mouseX, mouseY);
		}

		public void onClickSuper(int mouseButton)
		{
			super.onClick(mouseButton);
		}
	}
}
