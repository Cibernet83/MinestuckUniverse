package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.badges.MasterBadge;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class GuiManageBadges extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/god_tier_meditation.png");
	public static final ResourceLocation CURSOR = new ResourceLocation(MinestuckUniverse.MODID, "textures/items/sash_kit.png");
	public EntityPlayer player;
	public Minecraft mc;

	private static final float BADGES_PER_ROW = 16;
	private Badge hoveredBadge = null;

	protected static final HashMap<EnumAspect, Integer> mainColors = new HashMap<EnumAspect, Integer>()
	{{
		put(EnumAspect.BREATH, 0x47E2FA);
		put(EnumAspect.LIGHT, 0xF6FA4E);
		put(EnumAspect.SPACE, 0x333333);
		put(EnumAspect.TIME, 0xFF2106);
		put(EnumAspect.LIFE, 0x72EB34);
		put(EnumAspect.VOID, 0x001856);
		put(EnumAspect.HEART, 0xBD1864);
		put(EnumAspect.HOPE, 0xFFDE55);
		put(EnumAspect.BLOOD, 0xB71015);
		put(EnumAspect.RAGE, 0x9C4DAC);
		put(EnumAspect.MIND, 0x06FFC9);
		put(EnumAspect.DOOM, 0x306800);
	}};

	public int xSize = 240;
	public int ySize = 256;
	public int guiLeft;
	public int guiTop;

	boolean mouseClicked = false;
	int clickTime = 0;
	boolean showExtra = false;

	protected final ArrayList<Badge> badges = new ArrayList<>();
	protected final ArrayList<MasterBadge> masterBadges = new ArrayList<>();

	public GuiManageBadges(EntityPlayer player)
	{
		this.player = player;
		this.mc = Minecraft.getMinecraft();
		this.fontRenderer = mc.fontRenderer;

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		setupBadges();
	}

	protected void setupBadges()
	{
		badges.clear();
		masterBadges.clear();

		for(Badge badge : Badge.BADGES)
		{
			if(Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).hasSkill(badge))
			{
				if (badge instanceof MasterBadge)
					masterBadges.add((MasterBadge) badge);
				else badges.add(badge);
			}
		}

		Collections.sort(badges, Comparator.comparingInt(Badge::getSortIndex));
		Collections.sort(masterBadges, Comparator.comparingInt(Badge::getSortIndex));
	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


		int yOffset = this.height / 2 - ySize/2;
		int xOffset = this.width / 2 - xSize/2;

		//God Tier Title
		mc.getTextureManager().bindTexture(TEXTURES);
		setColor(0xFFFFFF);
		//drawTexturedModalRect(xOffset+xSize/2 - 88, yOffset+15, 0, 223, 176, 33);
		String gtTitle = data.getGodTierTitle(MinestuckPlayerData.title);
		//fontRenderer.drawString(gtTitle, xOffset+xSize/2 - fontRenderer.getStringWidth(gtTitle)/2, yOffset+30, 0xFFFFFF);

		yOffset = this.height / 2 - 22;
		boolean isOverlord = data.isBadgeActive(MSUSkills.BADGE_OVERLORD);

		EnumAspect aspect = MinestuckPlayerData.title.getHeroAspect();
		int mainColor = aspect == EnumAspect.SPACE ? 0xFAFAFA : mainColors.getOrDefault(aspect, 0x80FF20);

		//Sash
		mc.getTextureManager().bindTexture(new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/sash_"+data.getLunarSway().toString().toLowerCase()+".png"));
		float n = width/xSize;
		for(int i = 0; i <= n; i++)
			this.drawTexturedModalRect(xOffset+xSize*(i-n/2f), yOffset-38, 0, 0, xSize, 142);

		//Badges
		String badgesStr = I18n.format("gui.badgesLeft", data.getBadgesLeft());

		//fontRenderer.drawString(badgesStr, xOffset+xSize/2 - fontRenderer.getStringWidth(badgesStr)/2, yOffset-10, mainColor);
		setColor(0xFFFFFF);

		for(int i = 0; i < masterBadges.size(); i++)
		{
			MasterBadge badge = masterBadges.get(i);
			if(badge == null) continue;

			if(badge.isReadable(player.world, player) && (data.getMasterBadge() == null || data.getMasterBadge() == badge || isOverlord))
				mc.getTextureManager().bindTexture(badge.getTextureLocation());
			else mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_locked.png"));
			if(!data.hasSkill(badge))
				GlStateManager.color(0.5f, 0.5f, 0.5f);
			drawScaledCustomSizeModalRect(xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset-23, 0, 0, 256, 256, 20, 20, 256, 256);
			GlStateManager.color(1,1,1);

			if(data.hasSkill(badge) && !data.isBadgeActive(badge))
			{
				mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_disabled.png"));
				drawScaledCustomSizeModalRect(xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset-23, 0, 0, 256, 256, 20, 20, 256, 256);
			}
		}
		for(int i = 0; i < badges.size(); i++)
		{
			Badge badge = badges.get(i);
			if(badge == null) continue;

			if(badge.isReadable(player.world, player))
			{
				if(!data.hasSkill(badge))
					GlStateManager.color(0.5f, 0.5f, 0.5f);
				mc.getTextureManager().bindTexture(badge.getTextureLocation());
			}
			else mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_locked.png"));

			int rows = (int) Math.max(2, Math.ceil(badges.size()/BADGES_PER_ROW));

			drawScaledCustomSizeModalRect(xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset + (i%rows)*22, 0, 0, 256, 256, 20, 20, 256, 256);
			GlStateManager.color(1,1,1);

			if(data.hasSkill(badge) && !data.isBadgeActive(badge))
			{
				mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_disabled.png"));
				drawScaledCustomSizeModalRect(xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset + (i % rows) * 22, 0, 0, 256, 256, 20, 20, 256, 256);
			}
		}

		//Cursor
		mc.getTextureManager().bindTexture(CURSOR);
		drawScaledCustomSizeModalRect(mouseX, mouseY, 0, 0, 256, 256, 16, 16, 256, 256);

		hoveredBadge = null;
		//Hovering Text
		for(int i = 0; i < masterBadges.size(); i++)
		{
			if(!isPointInRegion(mouseX, mouseY, xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset-23, 20, 20))
				continue;
			MasterBadge badge = masterBadges.get(i);
			if(badge == null) continue;

			hoveredBadge = badge;

			ArrayList<String> tooltip = new ArrayList<>();

			if(!badge.isReadable(player.world, player))
			{
				tooltip.add(TextFormatting.OBFUSCATED + badge.getDisplayName());
				tooltip.add(badge.getReadRequirements());
			}
			else if(data.getMasterBadge() == null && !isOverlord)
			{
				tooltip.add(badge.getDisplayName());
				if(showExtra)
					tooltip.add(badge.getDisplayTooltip());
				else
				{
					tooltip.add(badge.getUnlockRequirements());
					tooltip.add(I18n.format("gui.masterBadgeWarning"));
					tooltip.add(I18n.format("gui.showBadgeInfo"));
				}
			}
			else if(data.getMasterBadge() == badge || isOverlord)
			{
				tooltip.add(badge.getDisplayName());
				tooltip.add(badge.getDisplayTooltip());

				if(!data.isBadgeActive(badge))
					tooltip.add(I18n.format("gui.badge" + (data.isBadgeEnabled(badge) ? "Blocked" : "Disabled")));
			}

			if(!tooltip.isEmpty())
				drawHoveringText(tooltip, mouseX, mouseY);

		}

		int rows = (int) Math.max(2, Math.ceil(badges.size()/BADGES_PER_ROW));
		for(int i = 0; i < badges.size(); i++)
		{
			if(!isPointInRegion(mouseX, mouseY, xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset + (i%rows)*22, 20, 20))
				continue;

			ArrayList<String> tooltip = new ArrayList<>();
			Badge badge = badges.get(i);
			if(badge == null) continue;

			hoveredBadge = badge;
			if(!badge.isReadable(player.world, player))
			{
				tooltip.add(TextFormatting.OBFUSCATED + badge.getDisplayName());
				tooltip.add(badge.getReadRequirements());

			} else if(data.hasSkill(badge))
			{
				tooltip.add(badge.getDisplayName());
				tooltip.add(badge.getDisplayTooltip());
				if(!data.isBadgeActive(badge))
					tooltip.add(I18n.format("gui.badge" + (data.isBadgeEnabled(badge) ? "Blocked" : "Disabled")));
			}
			else
			{
				tooltip.add(badge.getDisplayName());
				if(showExtra)
					tooltip.add(badge.getDisplayTooltip());
				else
				{
					if(data.getBadgesLeft() > 0)
						tooltip.add(badge.getUnlockRequirements());
					else tooltip.add(I18n.format("gui.noBadgesLeft"));
					tooltip.add(I18n.format("gui.showBadgeInfo"));

				}
			}

			if(!tooltip.isEmpty())
				drawHoveringText(tooltip, mouseX, mouseY);
		}

		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		super.drawScreen(mouseX, mouseY, partialTicks);

		if(mouseClicked)
			clickTime++;
		else clickTime = 0;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		mouseClicked = true;
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		if(hoveredBadge == null)
			return;

		if(hoveredBadge instanceof MasterBadge)
		{
			if((data.isBadgeActive(MSUSkills.BADGE_OVERLORD) || data.hasSkill(hoveredBadge) && data.getMasterBadge() != null))
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.TOGGLE_BADGE, hoveredBadge));
		}
		else
		{
			if(data.hasSkill(hoveredBadge))
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.TOGGLE_BADGE, hoveredBadge));
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
		mouseClicked = false;
	}

	@Override
	public void handleKeyboardInput() throws IOException
	{
		super.handleKeyboardInput();
		showExtra = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
	}

	protected void setColor(int hex)
	{
		float r = (float)((hex & 16711680) >> 16) / 255.0F;
		float g = (float)((hex & '\uff00') >> 8) / 255.0F;
		float b = (float)((hex & 255) >> 0) / 255.0F;
		GlStateManager.color(r, g, b);
	}

	protected void renderBorderedText(int x, int y, String text, int color, int borderColor)
	{
		//XP Color: 80FF20
		fontRenderer.drawString(text, x + 1, y, borderColor);
		fontRenderer.drawString(text, x - 1, y, borderColor);
		fontRenderer.drawString(text, x, y + 1, borderColor);
		fontRenderer.drawString(text, x, y - 1, borderColor);
		fontRenderer.drawString(text, x, y, color);
	}

	protected boolean isPointInRegion(int pointX, int pointY, int x, int y, int width, int height)
	{
		return pointX >= x && pointX <= x+width && pointY >= y && pointY <= y+height;
	}
}
