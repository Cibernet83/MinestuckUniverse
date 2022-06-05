package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.AlchemyModus;
import com.cibernet.minestuckuniverse.gui.GuiModusGristSelector;
import com.google.common.collect.Lists;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class AlchemyGuiHandler extends BaseModusGuiHandler
{
	protected GristButton guiButton;
	
	public AlchemyGuiHandler(AlchemyModus modus)
	{
		super(modus, 19);
	}
	
	
	public void initGui()
	{
		super.initGui();
		emptySylladex.displayString = I18n.format("gui.alchemyClear");
		this.guiButton = new GristButton(0, (this.width - 256) / 2 + 15, (this.height - 202) / 2 + 175, 120, 20, "");
		this.buttonList.add(this.guiButton);
	}
	
	public void drawScreen(int xcor, int ycor, float f)
	{
		this.guiButton.x = (this.width - 256) / 2 + 15;
		this.guiButton.y = (this.height - 202) / 2 + 175;
		this.guiButton.gristType = ((AlchemyModus)modus).getWildcardGrist();
		this.guiButton.displayString = I18n.format("gui.alchemyWildcard", MinestuckConfig.alchemyIcons ? "" : guiButton.gristType.getDisplayName());
		super.drawScreen(xcor, ycor, f);
	}
	
	@Override
	public void updateContent() {
		NonNullList<ItemStack> stacks = ((AlchemyModus)this.modus).getDisplayItems();
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i)
			this.cards.add(new AlchemiterCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if (button == this.guiButton)
		{
			mc.currentScreen = new GuiModusGristSelector(this);
			mc.currentScreen.setWorldAndResolution(mc, width, height);
		}
		
	}
	
	public AlchemyModus getModus()
	{
		return (AlchemyModus) modus;
	}
	
	public static class AlchemiterCard extends GuiCard
	{
		AlchemyGuiHandler gui;
		public AlchemiterCard(ItemStack stack, AlchemyGuiHandler gui, int index, int xPos, int yPos)
		{
			super(stack, gui, index, xPos, yPos);
			this.gui = gui;
		}
		
		@Override
		protected void drawTooltip(int mouseX, int mouseY)
		{
			if (!this.item.isEmpty())
			{
				
				FontRenderer font = item.getItem().getFontRenderer(item);
				net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(item);
				gui.drawHoveringTextGrist(item, mouseX, mouseY, (font == null ? gui.fontRenderer : font));
				net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
			}
		}
	}
	
	public static class GristButton extends GuiButton
	{
		public GristType gristType = GristType.Build;
		
		public GristButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
		{
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			super.drawButton(mc, mouseX, mouseY, partialTicks);
			
			if(!MinestuckConfig.alchemyIcons)
				return;
			
			FontRenderer fontrenderer = mc.fontRenderer;
			int gristX = this.x + (this.width / 2)  - ((fontrenderer.getStringWidth(this.displayString)+8) / 2) + fontrenderer.getStringWidth(this.displayString);
			int gristY = this.y + (this.height - 8) / 2;
			
			GlStateManager.color(1,1,1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(gristType.getIcon().getResourceDomain(), "textures/grist/" + gristType.getIcon().getResourcePath()+ ".png"));
			Gui.drawModalRectWithCustomSizedTexture(gristX, gristY, 0, 0, 8, 8, 8, 8);
		}
		
		@Override
		public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
		{
			fontRendererIn.drawStringWithShadow(text, (float)(x - (fontRendererIn.getStringWidth(text)+(MinestuckConfig.alchemyIcons ? 8 : 0)) / 2), (float)y, color);
		}
	}
	
	@Override
	public List<String> getItemToolTip(ItemStack stack)
	{
		List<String> list = Lists.newArrayList();
		String s = stack.getDisplayName();
		ITooltipFlag.TooltipFlags advanced = this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;
		
		if (stack.hasDisplayName())
			s = TextFormatting.ITALIC + s;
		
		s = s + TextFormatting.RESET;
		
		if (advanced.isAdvanced())
		{
			String s1 = "";
			
			if (!s.isEmpty())
			{
				s = s + " (";
				s1 = ")";
			}
			
			int i = Item.getIdFromItem(stack.getItem());
			
			if (stack.getHasSubtypes())
				s = s + String.format("#%04d/%d%s", i, stack.getMetadata(), s1);
			else
				s = s + String.format("#%04d%s", i, s1);
		}
		else if (!stack.hasDisplayName() && stack.getItem() == Items.FILLED_MAP)
			s = s + " #" + stack.getMetadata();
		
		
		list.add(s);
		
		/*
		if (advanced.isAdvanced())
			list.add(TextFormatting.DARK_GRAY + Item.REGISTRY.getNameForObject(stack.getItem()).toString());
		*/
		return list;
	}
	
	protected void drawHoveringTextGrist(ItemStack stack, int x, int y, FontRenderer font)
	{
		List<String> textLines = getItemToolTip(stack);
		//net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, y, width, height, -1, font);
		if (!textLines.isEmpty())
		{
			AlchemyModus modus = ((AlchemyModus)this.modus);
			GristSet grist = modus.getGristCost(stack);
			
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int i = 0;
			
			for (String s : textLines)
			{
				int j = this.fontRenderer.getStringWidth(s);
				
				if (j > i)
				{
					i = j;
				}
			}
			
			i = Math.max(i, getGristBoardWidth(grist, stack.getItem() == MinestuckItems.captchaCard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, this.fontRenderer));
			
			int l1 = x + 12;
			int i2 = y - 12;
			int k = 8 + getGristBoardHeight(grist, stack.getItem() == MinestuckItems.captchaCard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, this.fontRenderer);
			
			if (textLines.size() > 1)
			{
				k += 2 + (textLines.size() - 1) * 10;
			}
			
			if (l1 + i > this.width)
			{
				l1 -= 28 + i;
			}
			
			if (i2 + k + 6 > this.height)
			{
				i2 = this.height - k - 6;
			}
			
			this.zLevel = 300.0F;
			this.itemRender.zLevel = 300.0F;
			int l = -267386864;
			this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
			this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
			this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
			this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
			int i1 = 1347420415;
			int j1 = 1344798847;
			this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
			this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
			this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);
			
			for (int k1 = 0; k1 < textLines.size(); ++k1)
			{
				String s1 = textLines.get(k1);
				this.fontRenderer.drawStringWithShadow(s1, (float)l1, (float)i2, -1);
				
				if (k1 == 0)
				{
					i2 += 2;
				}
				
				i2 += 10;
			}
			
			this.zLevel = 0.0F;
			this.itemRender.zLevel = 0.0F;
			
			GuiUtil.drawGristBoard(grist, stack.getItem() == MinestuckItems.captchaCard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, l1, i2, this.fontRenderer);
			
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}
	
	public static int getGristBoardHeight(GristSet grist, GuiUtil.GristboardMode mode, FontRenderer fontRenderer)
	{
		if(grist == null || grist.isEmpty())
			return 12;
		
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		Iterator<GristAmount> it = grist.getArray().iterator();
		
		if (!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			int height = 0;
			
			while (it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				height = Math.max(height, GuiUtil.GRIST_BOARD_HEIGHT/3*row);
				
				//ensure that one line is rendered on the large alchemiter
				if(mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER||mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT)
					place+=2;
				
				place++;
				
			}
			return height+12;
		}
		else
		{
			int index = 0;
			int row = 0;
			
			while(it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				row = index/ GuiUtil.GRIST_BOARD_WIDTH;
				
				String needStr = GuiUtil.addSuffix(need), haveStr = '('+ GuiUtil.addSuffix(have)+')';
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				if(index + needStrWidth + 10 + fontRenderer.getStringWidth(haveStr) > (row + 1)* GuiUtil.GRIST_BOARD_WIDTH)
				{
					row++;
					index = row* GuiUtil.GRIST_BOARD_WIDTH;
				}
				
				//ensure the large alchemiter gui has one grist type to a line
				if(mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER||mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT) {
					index=(row+1)*158;
				}else {
					index += needStrWidth + 10 + fontRenderer.getStringWidth(haveStr);
					index = Math.min(index + 6, (row + 1)*158);
				}
			}
			
			return 8*(row+1)+4;
		}
	}
	
	public static int getGristBoardWidth(GristSet grist, GuiUtil.GristboardMode mode, FontRenderer fontRenderer)
	{
		if(grist == null)
			return fontRenderer.getStringWidth(I18n.format("gui.notAlchemizable"));
		if(grist.isEmpty())
			return fontRenderer.getStringWidth(I18n.format("gui.free"));
		
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		Iterator<GristAmount> it = grist.getArray().iterator();
		
		int width = 0;
		
		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			while (it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				String needStr = GuiUtil.addSuffix(need), haveStr = GuiUtil.addSuffix(have);
				width = Math.max(width, fontRenderer.getStringWidth(needStr + " " + type.getDisplayName() + " (" + haveStr + ")")+ (GuiUtil.GRIST_BOARD_WIDTH/2*col));
				
				//ensure that one line is rendered on the large alchemiter
				if(mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER||mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT)
					place+=2;
				
				place++;
				
			}
		} else
		{
			int index = 0;
			while(it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				int row = index/ GuiUtil.GRIST_BOARD_WIDTH;
				
				String needStr = GuiUtil.addSuffix(need), haveStr = '('+ GuiUtil.addSuffix(have)+')';
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				if(index + needStrWidth + 10 + fontRenderer.getStringWidth(haveStr) > (row + 1)* GuiUtil.GRIST_BOARD_WIDTH)
				{
					row++;
					index = row* GuiUtil.GRIST_BOARD_WIDTH;
				}
				width = Math.max(width, fontRenderer.getStringWidth(haveStr) + ( + needStrWidth + 10 + index% GuiUtil.GRIST_BOARD_WIDTH));
				
				//ensure the large alchemiter gui has one grist type to a line
				if(mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER||mode== GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT) {
					index=(row+1)*158;
				}else {
					index += needStrWidth + 10 + fontRenderer.getStringWidth(haveStr);
					index = Math.min(index + 6, (row + 1)*158);
				}
			}
		}
		
		return width;
	}
	
}
