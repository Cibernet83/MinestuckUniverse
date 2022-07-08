package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSkillShop extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/skill_shop.png");

	public EntityPlayer player;
	public Minecraft mc;

	private int xSize = 240;
	private int ySize = 162;
	private final int descBoxWidth = 100;
	private final int descBoxHeight = 90;
	private final int techBoxWidth = 90;
	private final int techBoxHeight = 100;

	private float techScrollPos = 0;
	private int selectedTech = -1;
	private float descScrollPos = 0;
	private int descLines = 0;
	private int selectedScrollBar = -1;

	int xOffset;
	int yOffset;
	GuiButton buyButton;

	List<Abilitech> availableTech = new ArrayList<>();

	public GuiSkillShop(EntityPlayer player)
	{
		super();
		this.player = player;
		mc = Minecraft.getMinecraft();

		buyButton = new GuiButton(0, 0, 0, 86, 20, "gui.skillShop.buy");
		setupTech();
	}

	private void setupTech()
	{
		availableTech.clear();

		IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		for(Abilitech abilitech : Abilitech.ABILITECHS)
			if((abilitech.isObtainable() && !data.hasSkill(abilitech) && (abilitech.canAppearOnList(player.world, player)) || data.hasMasterControl()))
				availableTech.add(abilitech);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		yOffset = this.height / 2 - ySize/2;
		xOffset = this.width / 2 - (xSize-16)/2;

		mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, xSize, ySize);

		for(int i = 0; i+techScrollPos*Math.max(0, availableTech.size()-techBoxHeight/fontRenderer.FONT_HEIGHT) < availableTech.size() && i < techBoxHeight/fontRenderer.FONT_HEIGHT; i++)
		{
			int index = (int) (i + techScrollPos*Math.max(0, availableTech.size()-techBoxHeight/fontRenderer.FONT_HEIGHT));
			Abilitech abilitech = availableTech.get(index);
			if(index == selectedTech)
				drawRect(xOffset+8, yOffset+28+(i*fontRenderer.FONT_HEIGHT), xOffset+8+techBoxWidth, yOffset+28+((i+1)*fontRenderer.FONT_HEIGHT), 0xFFAFAFAF);
			String name = abilitech.getDisplayName();
			if(fontRenderer.getStringWidth(name) > techBoxWidth)
				name = fontRenderer.trimStringToWidth(abilitech.getDisplayName(), techBoxWidth-fontRenderer.getStringWidth("...")).trim()+"...";
			fontRenderer.drawString(name, xOffset+9, yOffset+29 + i*fontRenderer.FONT_HEIGHT, index == selectedTech ? 0x000000 : 0xFFFFFF);
		}

		GlStateManager.color(1,1,1);
		if(selectedTech >= 0)
		{
			Abilitech abilitech = availableTech.get(selectedTech);
			mc.getTextureManager().bindTexture(new ResourceLocation(abilitech.getRegistryName().getResourceDomain(), "textures/gui/abilitechs/icons/"+abilitech.getRegistryName().getResourcePath()+".png"));
			drawScaledCustomSizeModalRect(xOffset+151, yOffset+5,0, 0, 256, 256, 48, 48, 256, 256);

			String name = abilitech.getDisplayName();
			String cost = abilitech.getUnlockRequirements();
			String description = "\n" + abilitech.getDisplayTooltip();

			for(String tag : abilitech.getTags())
				description = "[" + tag + "]\n" + description;
			description =  cost + "\n" + description;

			description = new String(new char[fontRenderer.listFormattedStringToWidth(name, descBoxWidth).size()]).replace('\0', '\n') + description;
			cost = new String(new char[fontRenderer.listFormattedStringToWidth(name, descBoxWidth).size()]).replace('\0', '\n') + cost;

			descLines = Math.max(0, fontRenderer.listFormattedStringToWidth(description, descBoxWidth).size() - (descBoxHeight/fontRenderer.FONT_HEIGHT));

			GuiFraymachine.drawSplitString(fontRenderer, name, xOffset + 119, yOffset + 56, descBoxWidth, descBoxHeight, 0xFFFFFF, (int) (descScrollPos*descLines), true);
			GuiFraymachine.drawSplitString(fontRenderer, description, xOffset + 119, yOffset + 56, descBoxWidth, descBoxHeight, 0xFFFFFF, (int) (descScrollPos*descLines), false);
			GuiFraymachine.drawSplitString(fontRenderer, cost, xOffset + 119, yOffset + 56, descBoxWidth, descBoxHeight, 38143, (int) (descScrollPos*descLines), false);

		}

		GlStateManager.color(1,1,1);
		mc.getTextureManager().bindTexture(TEXTURES);
		drawTexturedModalRect(xOffset+222, yOffset+56 + (int)(descScrollPos*75), descLines > 0 ? 0 : 10, 241, 10, 15);
		drawTexturedModalRect(xOffset+99, yOffset+28 + (int)(techScrollPos*85), availableTech.size() > techBoxHeight/fontRenderer.FONT_HEIGHT ? 0 : 10, 241, 10, 15);

		mc.getTextureManager().bindTexture(GuiPlayerStats.icons);
		this.drawTexturedModalRect(xOffset + 8, yOffset + 7, 238, 16, 18, 18);
		this.mc.fontRenderer.drawString(String.valueOf(MinestuckPlayerData.boondollars), xOffset + 28, yOffset + 12, 38143);

		buyButton.x = xOffset+16;
		buyButton.y = yOffset+135;
		buyButton.enabled = selectedTech >= 0 && availableTech.get(selectedTech).canUnlock(player.world, player);
		buyButton.displayString = selectedTech >= 0 ? I18n.format("gui.skillShop." + (buyButton.enabled ? "buy" : "cantAfford")) : "";
		buyButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		float s = Mouse.getDWheel();
		int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		if(GuiFraymachine.pointInRegion(xOffset+8, yOffset+28, 101, 107, x, y) && availableTech.size() > techBoxHeight/fontRenderer.FONT_HEIGHT)
			techScrollPos = Math.max(Math.min(techScrollPos + 1.0F/Math.max(0, availableTech.size()-techBoxHeight/fontRenderer.FONT_HEIGHT) * -Math.signum(s), 1), 0);
		else if(GuiFraymachine.pointInRegion(xOffset+119, yOffset+56, 113, 90, x, y) && descLines > 0)
			descScrollPos = Math.max(Math.min(descScrollPos + 1.0F/descLines * -Math.signum(s), 1), 0);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseButton == 0 && buyButton.mousePressed(mc, mouseX, mouseY))
		{
			Abilitech abilitech = availableTech.get(selectedTech);
			abilitech.onUnlock(player.world, player);
			player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).addSkill(abilitech, false);
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ATTEMPT_BADGE_UNLOCK, abilitech));

			availableTech.remove(selectedTech);
			if(selectedTech >= availableTech.size())
				selectedTech = availableTech.size()-1;
			buyButton.playPressSound(this.mc.getSoundHandler());
		}

		for(int i = 0; i+techScrollPos*Math.max(0, availableTech.size()-techBoxHeight/fontRenderer.FONT_HEIGHT) < availableTech.size() && i < techBoxHeight/fontRenderer.FONT_HEIGHT; i++)
		{
			if(GuiFraymachine.pointInRegion(xOffset+8, yOffset+28+(i*fontRenderer.FONT_HEIGHT), techBoxWidth, fontRenderer.FONT_HEIGHT, mouseX, mouseY))
			{
				selectedTech = (int) (i + techScrollPos*Math.max(0, availableTech.size()-techBoxHeight/fontRenderer.FONT_HEIGHT));
				descScrollPos = 0;
			}
		}

		if(GuiFraymachine.pointInRegion(xOffset+99, yOffset+28, 10, 107, mouseX, mouseY))
			selectedScrollBar = 0;
		else if(GuiFraymachine.pointInRegion(xOffset+222, yOffset+56, 10, 90, mouseX, mouseY))
			selectedScrollBar = 1;
		else selectedScrollBar = -1;
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		switch (selectedScrollBar)
		{
			case 0:
				if(availableTech.size() > techBoxHeight/fontRenderer.FONT_HEIGHT)
					techScrollPos = Math.min(Math.max((mouseY-(yOffset+28))/107f, 0f), 1f);
			break;
			case 1:
				if(descLines > 0)
					descScrollPos = Math.min(Math.max((mouseY-(yOffset+56))/90f, 0f), 1f);
			break;


		}
	}
}
