package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.badges.Badge;
import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.badges.MasterBadge;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class GuiGodTierMeditation extends GuiScreen
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/god_tier_meditation.png");
	public static final ResourceLocation MOCKUP = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/god_tier_meditation_mockup.png");
	public EntityPlayer player;
	public Minecraft mc;

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

	public int xSize = 256;
	public int ySize = 210;
	public int guiLeft;
	public int guiTop;

	boolean mouseClicked = false;
	int clickTime = 0;
	boolean showExtra = false;

	protected final ArrayList<Badge> badges = new ArrayList<>();
	protected final ArrayList<MasterBadge> masterBadges = new ArrayList<>();

	public GuiGodTierMeditation(EntityPlayer player)
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

		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		for(Map.Entry<ResourceLocation, Badge> entry : MSUBadges.REGISTRY.getEntries())
		{
			if((data.hasMasterControl()) ||
			   entry.getValue().canAppearOnList(player.world, player) ||
					data.hasBadge(entry.getValue()))
			{
				if (entry.getValue() instanceof MasterBadge)
					masterBadges.add((MasterBadge) entry.getValue());
				else badges.add(entry.getValue());
			}

		}

		Collections.sort(badges, Comparator.comparingInt(Badge::getSortIndex));
		Collections.sort(masterBadges, Comparator.comparingInt(Badge::getSortIndex));

		if(data.hasMasterControl() && badges.contains(MSUBadges.BADGE_OVERLORD))
		{
			badges.remove(MSUBadges.BADGE_OVERLORD);
			badges.add(badges.indexOf(MSUBadges.BADGE_LORD)+1, MSUBadges.BADGE_OVERLORD);
		}
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
		mc.getTextureManager().bindTexture(MOCKUP);
		int yOffset = this.height / 2 - ySize/2;
		int xOffset = this.width / 2 - xSize/2;
		//this.drawTexturedModalRect(xOffset, yOffset, 0, 0, xSize, ySize);
		mc.getTextureManager().bindTexture(TEXTURES);

		boolean isOverlord = data.isBadgeActive(MSUBadges.BADGE_OVERLORD);

		//General XP
		int generalLevel = data.getSkillLevel(GodTierData.SkillType.GENERAL);
		float generalXp = data.getSkillXp(GodTierData.SkillType.GENERAL);
		float generalFilled = generalXp/data.getXpToNextLevel(GodTierData.SkillType.GENERAL);

		EnumAspect aspect = MinestuckPlayerData.title.getHeroAspect();
		int mainColor = aspect == EnumAspect.SPACE ? 0xFAFAFA : mainColors.getOrDefault(aspect, 0x80FF20);

		setColor(mainColor);
		drawTexturedModalRect(xOffset+37, yOffset+10, 0, 0, 182, 5);
		drawTexturedModalRect(xOffset+37, yOffset+10, 0, 5, (int) (182*generalFilled), 5);
		GlStateManager.color(0,0,0);
		renderBorderedText(xOffset+36 - fontRenderer.getStringWidth(String.valueOf(generalLevel)), yOffset+10, String.valueOf(generalLevel), mainColor, 0);

		//God Tier Title
		mc.getTextureManager().bindTexture(TEXTURES);
		setColor(0xFFFFFF);
		drawTexturedModalRect(xOffset+40, yOffset+15, 0, 223, 176, 33);
		String gtTitle = data.getGodTierTitle(MinestuckPlayerData.title);
		fontRenderer.drawString(gtTitle, xOffset+xSize/2 - fontRenderer.getStringWidth(gtTitle)/2, yOffset+30, 0xFFFFFF);

		//Badges

		String badgesStr = I18n.format("gui.badgesLeft", data.getBadgesLeft());

		fontRenderer.drawString(badgesStr, xOffset+xSize/2 - fontRenderer.getStringWidth(badgesStr)/2, yOffset+147, mainColor);
		setColor(0xFFFFFF);

		for(int i = 0; i < masterBadges.size(); i++)
		{
			MasterBadge badge = masterBadges.get(i);
			if(badge.isReadable(player.world, player) && (data.getMasterBadge() == null || data.getMasterBadge() == badge || isOverlord))
				mc.getTextureManager().bindTexture(badge.getTextureLocation());
			else mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_locked.png"));
			if(!data.hasBadge(badge))
				GlStateManager.color(0.5f, 0.5f, 0.5f);
			drawScaledCustomSizeModalRect(xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset+124, 0, 0, 256, 256, 20, 20, 256, 256);
			GlStateManager.color(1,1,1);

			if(data.hasBadge(badge) && !data.isBadgeActive(badge))
			{
				mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_disabled.png"));
				drawScaledCustomSizeModalRect(xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset+124, 0, 0, 256, 256, 20, 20, 256, 256);
			}
		}
		for(int i = 0; i < badges.size(); i++)
		{
			Badge badge = badges.get(i);
			if(badge.isReadable(player.world, player))
			{
				if(!data.hasBadge(badge))
					GlStateManager.color(0.5f, 0.5f, 0.5f);
				mc.getTextureManager().bindTexture(badge.getTextureLocation());
			}
			else mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_locked.png"));

			int rows = (int) Math.max(2, Math.ceil(badges.size()/20f));

			drawScaledCustomSizeModalRect(xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset+157 + (i%rows)*22, 0, 0, 256, 256, 20, 20, 256, 256);
			GlStateManager.color(1,1,1);

			if(data.hasBadge(badge) && !data.isBadgeActive(badge))
			{
				mc.getTextureManager().bindTexture(new ResourceLocation(badge.getRegistryName().getResourceDomain(), "textures/gui/badge_disabled.png"));
				drawScaledCustomSizeModalRect(xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset + 157 + (i % rows) * 22, 0, 0, 256, 256, 20, 20, 256, 256);
			}
		}

		//Skill XP
		for(int i = 1; i < GodTierData.SkillType.values().length; i++)
		{
			GodTierData.SkillType skill = GodTierData.SkillType.values()[i];
			int level = data.getSkillLevel(skill);
			float xp = data.getSkillXp(skill);
			float filled = xp/data.getXpToNextLevel(skill);

			int skillX = xOffset + (i % 2 == 0 ? 20 : 129);
			int skillY = yOffset + (Math.floor((i-1) / 2) == 0 ? 63 : 99);

			mc.getTextureManager().bindTexture(TEXTURES);
			GlStateManager.color(1,1,1);
			drawTexturedModalRect(skillX + 10, skillY + 6, 0, 10, 77, 5);
			drawTexturedModalRect(skillX + 10, skillY + 6, 0, 15, (int) (77*filled), 5);
			drawTexturedModalRect(skillX, skillY, 164 + (i*18), 0, 18, 18);
			drawTexturedModalRect(skillX + 89, skillY, ((player.isCreative() || player.experienceLevel >= MSUConfig.godTierXpThreshold) ? isPointInRegion(mouseX, mouseY, skillX+89, skillY, 18, 18) ? (mouseClicked ? 36 : 18) : 0 : 36), 20, 18, 18);
			renderBorderedText(skillX+9 - (fontRenderer.getStringWidth(String.valueOf(level))/2), skillY+9 - (fontRenderer.FONT_HEIGHT/2), String.valueOf(level),  data.isBadgeActive(MSUBadges.BADGE_PAGE) ? 0xFFD84C : (isOverlord ? 0xFF0000 : 0x80FF20), 0);
		}

		//Hovering Text
		for(int i = 1; i < GodTierData.SkillType.values().length; i++)
		{
			GodTierData.SkillType skill = GodTierData.SkillType.values()[i];
			int level = data.getSkillLevel(skill);
			float xp = data.getSkillXp(skill);
			int skillX = xOffset + (i % 2 == 0 ? 20 : 129);
			int skillY = yOffset + (Math.floor((i-1) / 2) == 0 ? 63 : 99);


			if(isPointInRegion(mouseX, mouseY, skillX, skillY, 18, 18))
			{
				ArrayList<String> tooltip = new ArrayList<>();

				tooltip.add(I18n.format("godTierSkill."+skill.getName().toLowerCase()+".tooltipName", level));
				tooltip.add(I18n.format("godTierSkill."+skill.getName().toLowerCase()+".tooltipDesc", ItemStack.DECIMALFORMAT.format(data.getSkillAttributeLevel(skill) * ((data.getSkillAttributeOperationType(skill) == 0) ? 1 : 100))));
				if(skill.equals(GodTierData.SkillType.DEFENSE))
					tooltip.add(I18n.format("godTierSkill.defense.tooltipDesc2", ItemStack.DECIMALFORMAT.format(level*0.2 * (data.isBadgeActive(MSUBadges.BADGE_PAGE) ? 2 : 1) * (data.isBadgeActive(MSUBadges.BADGE_OVERLORD) ? 3 : 1))));
				tooltip.add(I18n.format("godTierSkill.tooltipNextLevel", ItemStack.DECIMALFORMAT.format(data.getXpToNextLevel(skill)-xp)));

				drawHoveringText(tooltip, mouseX, mouseY);
			}
			else if((!player.isCreative() && player.experienceLevel < MSUConfig.godTierXpThreshold) && isPointInRegion(mouseX, mouseY, skillX+89, skillY, 18, 18))
				drawHoveringText(I18n.format("gui.needXp", MSUConfig.godTierXpThreshold), mouseX, mouseY);

		}
		for(int i = 0; i < masterBadges.size(); i++)
		{
			MasterBadge badge = masterBadges.get(i);

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

			if(!tooltip.isEmpty() && isPointInRegion(mouseX, mouseY, xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset+124, 20, 20))
				drawHoveringText(tooltip, mouseX, mouseY);

		}
		for(int i = 0; i < badges.size(); i++)
		{
			ArrayList<String> tooltip = new ArrayList<>();
			Badge badge = badges.get(i);
			if(!badge.isReadable(player.world, player))
			{
				tooltip.add(TextFormatting.OBFUSCATED + badge.getDisplayName());
				tooltip.add(badge.getReadRequirements());

			} else if(data.hasBadge(badge))
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

			int rows = (int) Math.max(2, Math.ceil(badges.size()/20f));
			if(!tooltip.isEmpty() && isPointInRegion(mouseX, mouseY, xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset+157 + (i%rows)*22, 20, 20))
				drawHoveringText(tooltip, mouseX, mouseY);
		}

		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		super.drawScreen(mouseX, mouseY, partialTicks);



		if(mouseClicked)
			clickTime++;
		else clickTime = 0;

		if(clickTime > 20 && (clickTime % 5) == 0)
			upgradeSkills(mouseX, mouseY);
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

		int yOffset = this.height / 2 - ySize/2;
		int xOffset = this.width / 2 - xSize/2;

		upgradeSkills(mouseX, mouseY);
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		for(int i = 0; i < masterBadges.size(); i++)
		{
			MasterBadge badge = masterBadges.get(i);

			if(isPointInRegion(mouseX, mouseY, xOffset+(xSize - masterBadges.size()*22)/2  + i*22, yOffset+124, 20, 20) && badge.isReadable(player.world, player))
				MSUChannelHandler.sendToServer(MSUPacket.makePacket((!data.isBadgeActive(MSUBadges.BADGE_OVERLORD) && !data.hasBadge(badge) && data.getMasterBadge() == null && mouseButton == 0) ? MSUPacket.Type.ATTEMPT_BADGE_UNLOCK : MSUPacket.Type.TOGGLE_BADGE, badge));

		}
		for(int i = 0; i < badges.size(); i++)
		{
			Badge badge = badges.get(i);
			int rows = (int) Math.max(2, Math.ceil(badges.size()/20f));
			if(isPointInRegion(mouseX, mouseY, xOffset+(xSize - ((badges.size()+1)/rows)*22)/2  + ((i)/rows)*22, yOffset+157 + (i%rows)*22, 20, 20) && badge.isReadable(player.world, player))
				MSUChannelHandler.sendToServer(MSUPacket.makePacket((!data.hasBadge(badge) && mouseButton == 0) ? MSUPacket.Type.ATTEMPT_BADGE_UNLOCK : MSUPacket.Type.TOGGLE_BADGE, badge));
		}
	}

	protected void upgradeSkills(int mouseX, int mouseY)
	{
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		int yOffset = this.height / 2 - ySize/2;
		int xOffset = this.width / 2 - xSize/2;

		for(int i = 1; i < GodTierData.SkillType.values().length; i++)
		{
			GodTierData.SkillType skill = GodTierData.SkillType.values()[i];
			float xp = data.getSkillXp(skill);

			int skillX = xOffset + (i % 2 == 0 ? 20 : 129);
			int skillY = yOffset + (Math.floor((i-1) / 2) == 0 ? 63 : 99);


			if((player.isCreative() || player.experienceLevel >= MSUConfig.godTierXpThreshold) && isPointInRegion(mouseX, mouseY, skillX+89, skillY, 18, 18))
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

				int amount = showExtra ? 5 : 1;
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.INCREASE_XP, skill, player, amount));

				if(!player.isCreative())
					player.experienceLevel -= amount;
			}
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
