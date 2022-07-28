package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.HueModus;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.TreeMap;

public class HueStackGuiHandler extends HueGuiHandler
{
	protected TreeMap<CardColor, ArrayList<ColoredCard>> cardRows = new TreeMap<>();
	
	public HueStackGuiHandler(Modus modus)
	{
		super(modus);
		textureIndex = 23;
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = this.modus.getItems();
		this.cards.clear();
		cardRows.clear();
		updateContentSuper();
		for(int i = 0; i < stacks.size(); ++i)
		{
			CardColor color = getCardColor(stacks.get(i));
			if(!cardRows.containsKey(color))
				cardRows.put(color, new ArrayList<>());
		}
		
		this.maxHeight = Math.max(this.mapHeight, cardRows.size()*45);
		
		for(int i = 0; i < stacks.size(); ++i)
		{
			int id = ((HueModus)modus).getItemIndex(stacks.get(i));
			id = id ==-1 ? i : id;
			
			
			CardColor color = getCardColor(stacks.get(i));
			
			ColoredCard card = new ColoredCard(color,stacks.get(i), this, id, 0, 0, ((HueModus)modus).canUse(id));
			
			cardRows.get(color).add(card);
			
			this.cards.add(card);
		}
		
		int maxColumns = 0;
		for(ArrayList<ColoredCard> list : cardRows.values())
			if(list.size() > maxColumns)
				maxColumns = list.size();
			
		int start = Math.max(5, (this.mapWidth - (maxColumns * 21 + (maxColumns - 1) * 5)) / 2);
		
		for(GuiCard card : cards)
		{
			CardColor color = getCardColor(card.item);
			int column = cardRows.get(color).indexOf(card);
			int row = 0;
			
			for(CardColor c : cardRows.keySet())
			{
				if(c.equals(color))
					break;
				row++;
			}
			
			card.xPos = start + column * 26;
			card.yPos = (this.mapHeight - 26*cardRows.size() + 5*(cardRows.size())) / 2 + row * 31;
		}
		
		this.maxWidth = Math.max(this.mapWidth, 10 + maxColumns * 21 + (maxColumns - 1) * 5);
	}
	
	@Override
	public void updatePosition()
	{
		int maxColumns = 0;
		for(ArrayList<ColoredCard> list : cardRows.values())
			if(list.size() > maxColumns)
				maxColumns = list.size();
		this.maxWidth = Math.max(this.mapWidth, 10 + maxColumns * 21 + (maxColumns - 1) * 5);
		this.maxHeight = Math.max(this.mapHeight, cardRows.size()*45);
		int start = Math.max(5, (this.mapWidth - (maxColumns * 21 + (maxColumns - 1) * 5)) / 2);
		
		for(int i = 0; i < this.cards.size(); ++i)
		{
			GuiCard card = this.cards.get(i);
			
			CardColor color = getCardColor(card.item);
			int row = 0;
			int column = cardRows.get(color).indexOf(card);
			
			for(CardColor c : cardRows.keySet())
			{
				if(c.equals(color))
					break;
				row++;
			}
			
			card.xPos = start + column * 26;
			card.yPos = (this.mapHeight - 26*cardRows.size() + 5*(cardRows.size())) / 2 + row * 31;
		}
	}
	
	public void drawGuiMap(int xcor, int ycor)
	{
		super.drawGuiMap(xcor, ycor);
		for(ArrayList<ColoredCard> row : cardRows.values())
			if (!row.isEmpty())
			{
				int startX = Math.max(0, (row.get(0)).xPos + 21 - this.mapX);
				int endX = Math.max(0,Math.min(this.mapWidth, (row.get(row.size() - 1)).xPos - this.mapX));
				int y = row.get(0).yPos + 12 - this.mapY;
				
				if(y <= mapHeight && y > 0)
					drawRect(startX, y, endX, y + 2, -16777216);
			}
		
	}
}
