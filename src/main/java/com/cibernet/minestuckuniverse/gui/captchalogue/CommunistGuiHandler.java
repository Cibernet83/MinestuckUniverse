package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.CommunistModus;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CommunistGuiHandler extends BaseModusGuiHandler
{
	public CommunistGuiHandler(Modus modus)
	{
		super(modus, 3);
	}
	
	@Override
	public void updateContent()
	{
		if(!(modus instanceof CommunistModus))
		{
			cards.clear();
			return;
		}
		
		NonNullList<ItemStack> stacks = ((CommunistModus)modus).getGlobalItems();
		
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i) {
			this.cards.add(new GuiCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
	}
}
