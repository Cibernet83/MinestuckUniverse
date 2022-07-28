package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.concurrent.atomic.AtomicInteger;

public class OnionGuiHandler extends BaseModusGuiHandler
{
	public OnionGuiHandler(Modus modus)
	{
		super(modus, 4);
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
		
		NonNullList<ItemStack> nonEmptyStacks = NonNullList.create();
		AtomicInteger emptySize = new AtomicInteger();
		
		stacks.forEach(stack ->
		{
			if(!stack.isEmpty())
				nonEmptyStacks.add(stack);
			else emptySize.getAndIncrement();
		});
		
		for(int i = 0; i < stacks.size(); ++i)
		{
			if(stacks.get(i).isEmpty())
				continue;
			this.cards.add(new OnionCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
		
		if (emptySize.get() > 0)
			this.cards.add(new ModusSizeCard(this, emptySize.get(), 10, 10));
		
		updatePosition();
		
		mapX = maxWidth/2 - mapWidth/2;
		mapY = maxHeight/2 - mapHeight/2;
		
	}
	
	@Override
	public void updatePosition()
	{
		int maxLayer = 0;
		int cardsInLayer = 0;
		
		int j = 0;
		for(int i = 0; i < this.cards.size(); ++i)
		{
			GuiCard card = this.cards.get(i);
			if(card instanceof ModusSizeCard)
				continue;
			maxLayer = getLayer(j++);
		}
		
		j = 0;
		for(int i = 0; i < this.cards.size(); ++i)
		{
			GuiCard card = this.cards.get(i);
			if(card instanceof OnionCard)
				((OnionCard) card).canUse = getLayer(j++) == maxLayer;
		}
		
		this.maxWidth = Math.max(this.mapWidth, 10 + this.cards.size() * (this.cards.size() >= 25 ? 10 : 10));
		this.maxHeight = Math.max(this.mapHeight, 10 + this.cards.size() * (this.cards.size() >= 25 ? 10 : 7));
		
		int currentLayer = 0;
		j = 0;
		for(int i = 0; i < this.cards.size(); ++i)
		{
			GuiCard card = this.cards.get(i);
			if(card instanceof ModusSizeCard)
			{
				int off = maxLayer == 0 ? 0 : 18*(maxLayer+1);
				card.xPos = (maxWidth - 26)/2 - off;
				card.yPos = (this.maxHeight - 26) / 2 - off;
				continue;
			}
			
			int layer = getLayer(j++);
			double maxCards = (layer-1)*4;
			
			cardsInLayer++;
			if(currentLayer != layer)
			{
				cardsInLayer = 0;
				currentLayer = layer;
			}
			
			double angle = ((cardsInLayer % maxCards) / maxCards) * (2.0 * Math.PI);
			if(layer % 2 == 0)
				angle += 1 / maxCards * 2*Math.PI;
			int centerOffset = 0;
			if(layer > 1) centerOffset = maxCards <= 4 ? 30 : (int) (24 / Math.sin(Math.PI/maxCards));
			
			int x = (int) (Math.cos(angle)*centerOffset);
			int y = (int) (Math.sin(angle)*centerOffset);
			
			card.xPos = (maxWidth - 26)/2 + x;
			card.yPos = (this.maxHeight - 26) / 2 + y;
		}
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		return super.getTextureIndex(card) + (!(card instanceof OnionCard) || ((OnionCard)card).canUse ? 0 : 1);
	}
	
	protected static int getLayer(int x)
	{
		int layer = 0;
		int cardTotal = 0;
		while(true)
		{
			cardTotal += Math.max(1, layer*4);
			layer++;
			if(cardTotal > x)
				return layer;
		}
		
	}
	
	public static class OnionCard extends GuiCard
	{
		public boolean canUse = true;
		
		public OnionCard(ItemStack stack, SylladexGuiHandler onionGuiHandler, int i, int i1, int i2)
		{
			super(stack, onionGuiHandler, i, i1, i2);
		}
		
		@Override
		public void onClick(int mouseButton)
		{
			if(canUse || mouseButton == 1)
				super.onClick(mouseButton);
		}
	}
}
