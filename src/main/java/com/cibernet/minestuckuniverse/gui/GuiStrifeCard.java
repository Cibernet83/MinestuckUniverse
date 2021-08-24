package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.client.MSUFontRenderer;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class GuiStrifeCard extends GuiScreen
{
	private static int guiWidth = 147, guiHeight = 185;
	private static int xOffset, yOffset;
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/strife_specibus/strife_selector.png");
	private float scale = 1;
	private static final int columnWidth = 50,columns = 2;
	private static EntityPlayer player;
	private static final FontRenderer font = MSUFontRenderer.fontSpecibus;

	private final ArrayList<KindAbstratus> abstrataList;
	private static int size = 26;

	private final int maxScroll;
	private float scrollPos = 0F;
	private boolean isClicking = false;
	private int extraLines = 0;

	public GuiStrifeCard(EntityPlayer player)
	{
		this.player = player;
		ArrayList<KindAbstratus> list = new ArrayList<>();
		for(KindAbstratus i : KindAbstratus.REGISTRY.getValuesCollection())
			if(i.canSelect()) list.add(i);
		abstrataList = list;
		maxScroll = Math.max(0, (abstrataList.size() - size)/2);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight)/2;
		mc = Minecraft.getMinecraft();

	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		scale = 1;
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawRect(xOffset+27, yOffset+23, xOffset+127, yOffset+178, 0xFF000000);

		int listOffsetX = xOffset + 16;
		int listOffsetY = yOffset + 59 - extraLines*font.FONT_HEIGHT;


		int itemMin = (int) (scrollPos * maxScroll * 2) - extraLines*2;
		if(itemMin%2 == 1) itemMin--;

		int i = 0, count = 0;
		for(KindAbstratus type : abstrataList)
		{
			count++;
			if(count-1 < itemMin) continue;
			i++;
			if(i > size) break;

			String typeName = type.getDisplayName();

			//txPos += 78 (0.1625*width)
			//tyPos += 36 (9540 / height)

			int color = 0xFFFFFF;
			int listX = (columnWidth*((i-1) % columns));
			int listY = (font.FONT_HEIGHT*((i-1) / columns));
			int xPos = listOffsetX + listX + 11;
			int yPos = listOffsetY + listY + 1;
			int sxPos = (int)((listOffsetX + listX)/scale);
			int syPos = (int)((listOffsetY + listY)/scale);
			int txPos = (sxPos + columnWidth - font.getStringWidth(typeName))+ (int)(10/scale);
			int tyPos = syPos + (int)(3/scale);

			if(isPointInRegion(xPos, yPos, columnWidth, font.FONT_HEIGHT, mouseX, mouseY))
			{
				drawRect(xPos, yPos, xPos+columnWidth, yPos+font.FONT_HEIGHT, 0xFFAFAFAF);
				color = 0x000000;
				if(Mouse.getEventButtonState() && Mouse.isButtonDown(0))
				{
					EnumHand hand = (player.getHeldItemMainhand().isItemEqual(new ItemStack(MinestuckUniverseItems.strifeCard)) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
					ItemStack card = player.getHeldItem(hand);

					if(card.isItemEqual(new ItemStack(MinestuckUniverseItems.strifeCard)))
					{
						MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ASSIGN_STRIFE, hand, new StrifeSpecibus(type)));
						this.mc.displayGuiScreen(null);
					}

				}
			}
			font.drawString(typeName, txPos, tyPos, color);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int scroll = (int) (140*scrollPos);
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		this.drawTexturedModalRect(xOffset+128, yOffset+23+scroll, (maxScroll > 0) ? 232 : 244, 0, 12, 15);

		//96*265/width

		setScale(1.8F);
		String label = I18n.translateToLocal("gui.strifeCard.label");
		int xLabel = (int)(-(((height+guiHeight)/2)-8)/scale);
		int yLabel = (int)((((width+guiWidth)/2)-135)/scale);
		//178

		GL11.glRotatef(270, 0, 0, 1);
		font.drawString(label, xLabel, yLabel, 0xFFFFFF);

	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		int k = Mouse.getEventButton();
		int s = Mouse.getEventDWheel();

		float sp = scrollPos;
		if(s != 0) sp += 1.0F/maxScroll * -Math.signum(s);
		if(isPointInRegion(xOffset+128, yOffset+23, 12, 155, i, j) && Mouse.isButtonDown(0))
			sp = (j-23-yOffset)/140.0F;

		if(maxScroll <= 0) return;
		scrollPos = Math.min(1, Math.max(0, sp));

		extraLines = (int) Math.min(4, scrollPos*maxScroll);
		size = 26 + extraLines*2;

	}

	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}

	public void updateScalePos()
	{

	}


	protected static boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
}
