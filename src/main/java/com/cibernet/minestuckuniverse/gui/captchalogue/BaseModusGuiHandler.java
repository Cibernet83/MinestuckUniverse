package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class BaseModusGuiHandler extends SylladexGuiHandler
{
	public static final ResourceLocation MS_ICONS = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/icons.png");
	public static final ResourceLocation EXTRAS = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/icons.png");
	
	protected Modus modus;
	
	public BaseModusGuiHandler(Modus modus, int cardIndex)
	{
		this.modus = modus;
		this.textureIndex = cardIndex;
	}
	
	@Override
	public ResourceLocation getCardTexture(GuiCard card)
	{
		return new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/captcha_cards.png");
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
			this.cards.add(new GuiCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
	}
	
	@Override
	public void updatePosition() {
		this.maxWidth = Math.max(this.mapWidth, 10 + this.cards.size() * 21 + (this.cards.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		int start = Math.max(5, (this.mapWidth - (this.cards.size() * 21 + (this.cards.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < this.cards.size(); ++i) {
			GuiCard card = this.cards.get(i);
			card.xPos = start + i * 26;
			card.yPos = (this.mapHeight - 26) / 2;
		}
		
	}
	
	@Override
	public int getCardTextureX(GuiCard card)
	{
		return (getTextureIndex(card) % 12)* 21;
	}
	
	public int getTextureIndex(GuiCard card)
	{
		return this.textureIndex;
	}
	
	@Override
	public int getCardTextureY(GuiCard card)
	{
		return (int) Math.floor((getTextureIndex(card) ) / 12)*26;
	}
	
	protected int getMapX()
	{
		return mapX;
	}
	protected int getMapY()
	{
		return mapY;
	}
	
	protected void updateContentSuper()
	{
		super.updateContent();
	}
	
	protected boolean isMouseInContainer(int xcor, int ycor) {
		int xOffset = (this.width - 256) / 2;
		int yOffset = (this.height - 202) / 2;
		return xcor >= xOffset + 16 && xcor < xOffset + 16 + 224 && ycor >= yOffset + 17 && ycor < yOffset + 17 + 153;
	}
}