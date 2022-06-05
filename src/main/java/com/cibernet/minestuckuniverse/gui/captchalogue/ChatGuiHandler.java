package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ChatGuiHandler extends BaseModusGuiHandler
{
	public ChatGuiHandler(Modus modus)
	{
		super(modus, 12);
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
			this.cards.add(new GuiCard(stacks.get(i), this, -1, start + i * 26, (this.mapHeight - 26) / 2));
		}
	}

	private class ChatCard extends GuiCard {
		public ChatCard(ItemStack stack, ChatGuiHandler chatGuiHandler, int i, int i1, int i2)
		{
			super(stack, chatGuiHandler, i, i1, i2);
		}

		@Override
		public void onClick(int mouseButton)
		{
			if(mouseButton != 0)
				super.onClick(mouseButton);
		}
	}
}
