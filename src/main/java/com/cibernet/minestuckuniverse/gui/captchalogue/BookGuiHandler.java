package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.BookModus;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.google.common.collect.Lists;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookGuiHandler extends BaseModusGuiHandler
{
	protected GuiButton guiButton;
	
	BookModus modus;
	public BookGuiHandler(BookModus modus)
	{
		super(modus, 28);
		this.modus = modus;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.guiButton = new GuiButton(0, (this.width - 256) / 2 + 15, (this.height - 202) / 2 + 175, 120, 20, I18n.format("gui.bookPublish"));
		this.buttonList.add(this.guiButton);
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = this.modus.getItems();
		this.cards.clear();
		this.maxWidth = this.mapWidth;
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		
		ArrayList<GuiCard> unsortedCards = new ArrayList<>(stacks.size());
		
		for(int i = 0; i < stacks.size(); ++i)
			unsortedCards.add(new GuiCard(stacks.get(i), this, i, 0, 0));
		
		unsortedCards.sort((c1, c2) ->
		{
			if(c1.item.isEmpty())
				return 1;
			if(c2.item.isEmpty())
				return -1;
			return c1.item.getDisplayName().compareTo(c2.item.getDisplayName());
		});
		
		if(!unsortedCards.isEmpty())
		{
			int index = modus.page*2;
			cards.add(unsortedCards.get(index));
			if(unsortedCards.size() > index+1)
				cards.add(unsortedCards.get(index + 1));
		}
		
		updatePosition();
	}
	
	@Override
	public void updatePosition()
	{
		int start = Math.max(5, (this.mapWidth - 69) / 2);
		
		this.maxWidth = this.mapWidth;
		this.maxHeight = this.mapHeight;
		
		cards.forEach(card -> card.yPos = (this.mapHeight - 26) / 2);
		
		if(!cards.isEmpty())
		{
			cards.get(0).xPos = start;
			if(cards.size() > 1)
				cards.get(1).xPos = (int) ((start + 48));
		}
	}
	
	@Override
	public void drawGuiMap(int mouseX, int mouseY)
	{
		ArrayList<GuiCard> cards = new ArrayList<>(this.cards);
		
		super.drawGuiMap(mouseX, mouseY);
		
		if(cards.isEmpty())
			return;
		
		int y = (this.mapHeight - 74) / 2 - mapX;
		int x = (this.mapWidth - 97) / 2 - mapY;
		int index = modus.page*2 + 1;
		
		GlStateManager.color(1,1,1);
		mc.getTextureManager().bindTexture(EXTRAS);
		drawTexturedModalRect(x, y, 0, 16, 97, 74);
		
		int labelY = 5;
		
		mc.fontRenderer.drawString(index + "", (int) ((x+6)), (int) ((y+labelY)), 0);
		
		if(!cards.get(0).item.isEmpty())
			mc.fontRenderer.drawString(cards.get(0).item.getDisplayName().toUpperCase().charAt(0) + "", (int) ((x+38)), (int) ((y+labelY)), 0);
		if(cards.size() > 1)
		{
			mc.fontRenderer.drawString((index+1) + "", (int) ((x+91-mc.fontRenderer.getStringWidth((index+1) + ""))), (int) ((y+labelY)), 0);
			if(!cards.get(1).item.isEmpty())
				mc.fontRenderer.drawString(cards.get(1).item.getDisplayName().toUpperCase().charAt(0) + "", (int) ((x+51)), (int) ((y+labelY)), 0);
		}
		
		mc.fontRenderer.drawStringWithShadow(modus.bookName, (mapWidth - mc.fontRenderer.getStringWidth(modus.bookName))/2 - mapX, y-10, 0xFFFFFF);
		
		GlStateManager.color(1,1,1);
		mc.getTextureManager().bindTexture(EXTRAS);
		
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
		int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);
		
		
		if(modus.getSize() > 2 && modus.page < Math.floor((modus.getSize()-1)/2))
		{
			boolean hovered = isMouseInContainer(mouseX, mouseY) && (translX >= x + 62 - mapX && translX < x + 62 + 18 - mapX &&
					translY >= y + 52 - mapY && translY < y + 52 + 10 - mapY);
			drawTexturedModalRect(x + 62, y + 52, 62, hovered ? 100 : 90, 18, 10);
		}
		if(modus.page > 0)
		{
			boolean hovered = isMouseInContainer(mouseX, mouseY) && (translX >= x + 17 - mapX && translX < x + 17 + 18 - mapX &&
					translY >= y + 52 - mapY && translY < y + 52 + 10 - mapY);
			drawTexturedModalRect(x + 17, y + 52, 17, hovered ? 100 : 90, 18, 10);
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		
		if(button == guiButton)
		{
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.BOOK_PUBLISH));
			modus.clear();
			updateContent();
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		ArrayList<GuiCard> cards = new ArrayList<>(this.cards);
		
		if(cards.isEmpty() || mouseButton != 0)
			return;
		
		int y = (int) ((mapHeight - 54) / 2 - mapX) - 10;
		int x = (int) ((mapWidth - 73) / 2 - mapY) - 12;
		
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
		int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);
		if(isMouseInContainer(mouseX, mouseY))
		{
			if(modus.getSize() > 2 && (translX >= x + 62 - mapX && translX < x + 62 + 18 - mapX &&
					translY >= y + 52 - mapY && translY < y + 52 + 10 - mapY) && modus.page < Math.floor((modus.getSize()-1)/2))
			{
				modus.page++;
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.BOOK_UPDATE_PAGE, modus.page));
				updateContent();
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			}
			if(modus.page > 0 && (translX >= x + 17 - mapX && translX < x + 17 + 18 - mapX &&
					translY >= y + 52 - mapY && translY < y + 52 + 10 - mapY))
			{
				modus.page--;
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.BOOK_UPDATE_PAGE, modus.page));
				updateContent();
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			}
		}
	}
	
	@Override
	public List<String> getItemToolTip(ItemStack stack)
	{
		
		List<String> list = Lists.<String>newArrayList();
		String s = stack.getDisplayName();
		
		if (stack.hasDisplayName())
			s = TextFormatting.ITALIC + s;
		
		s = s + TextFormatting.RESET;
		
		if (!stack.hasDisplayName() && stack.getItem() == Items.FILLED_MAP)
			s = s + " #" + stack.getMetadata();
		
		list.add(s);
		
		List<String> tooltip = Lists.newArrayList();
		stack.getItem().addInformation(stack, this.mc.world, tooltip, ITooltipFlag.TooltipFlags.NORMAL);
		
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("BookDescription"))
			list.add(stack.getTagCompound().getString("BookDescription"));
		else if(stack.getItem().getHasSubtypes() && I18n.hasKey(stack.getItem().getRegistryName().toString() + "." + stack.getMetadata() + ".book_desc"))
			list.add(I18n.format(stack.getItem().getRegistryName().toString() + "." + stack.getMetadata() + ".book_desc"));
		else if(I18n.hasKey(stack.getItem().getRegistryName().toString() + ".book_desc"))
			list.add(I18n.format(stack.getItem().getRegistryName().toString() + ".book_desc"));
		
		else if(stack.getItem().getHasSubtypes() && I18n.hasKey(stack.getUnlocalizedName() + "." + stack.getMetadata() + ".book_desc"))
			list.add(I18n.format(stack.getUnlocalizedName() + "." + stack.getMetadata() + ".book_desc"));
		else if(I18n.hasKey(stack.getUnlocalizedName() + ".book_desc"))
			list.add(I18n.format(stack.getUnlocalizedName() + ".book_desc"));
		else if(!tooltip.isEmpty())
			list.addAll(tooltip);
		else if(I18n.hasKey(stack.getUnlocalizedName()+".tooltip"))
			list.add(I18n.format(stack.getUnlocalizedName()+".tooltip"));
		else list.add(I18n.format("book_desc." + (stack.isEmpty() ? "empty" : "missing")));
		
		if(stack.isItemStackDamageable())
		{
			if(stack.getMaxStackSize()-stack.getItemDamage() >= stack.getMaxStackSize()*2/3)
				list.add(I18n.format("book_desc.durability_high"));
			else if(stack.getMaxStackSize()-stack.getItemDamage() <= stack.getMaxStackSize()/3)
				list.add(I18n.format("book_desc.durability_low"));
		}
		
		if(stack.getItem().getRegistryName().getResourceDomain().isEmpty() || stack.getItem().getRegistryName().getResourceDomain().equals("minecraft"))
			list.add(I18n.format("book_desc.vanilla"));
		else
		{
			String modName = "???";
			for(ModContainer mod : Loader.instance().getActiveModList())
				if(mod.getModId().equals(stack.getItem().getRegistryName().getResourceDomain()))
				{
					modName = mod.getName();
					break;
				}
			list.add(I18n.format("book_desc.mod_name", modName));
		}
		
		if(this.mc.gameSettings.advancedItemTooltips)
			list.add(I18n.format("book_desc.item_id", stack.getItem().getRegistryName().toString()));
		
		
		for (int i = 0; i < list.size(); ++i)
		{
			if (i == 0)
				list.set(i, stack.getRarity().rarityColor + list.get(i));
			else
				list.set(i, TextFormatting.GRAY + list.get(i));
		}
		
		return list;
	}
}
