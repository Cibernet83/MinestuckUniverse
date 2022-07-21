package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

public class GuiFraymachine extends GuiScreen
{
	public static final HashMap<String, Pair<String, Integer>> TEXT_REPLACEMENTS = new HashMap<String, Pair<String, Integer>>(){{
		for(EnumTechType techType : EnumTechType.values())
			put(techType.name(), new Pair<>(techType.unloc, techType.color));

		put(EnumAspect.BREATH.name(), new Pair<>("title."+EnumAspect.BREATH.toString(), 0x47E2FA));
		put(EnumAspect.LIGHT.name(), new Pair<>("title."+EnumAspect.LIGHT.toString(), 0xF6FA4E));
		put(EnumAspect.SPACE.name(), new Pair<>("title."+EnumAspect.SPACE.toString(), 0x202020));
		put(EnumAspect.TIME.name(), new Pair<>("title."+EnumAspect.TIME.toString(), 0xFF2106));
		put(EnumAspect.LIFE.name(), new Pair<>("title."+EnumAspect.LIFE.toString(), 0x72EB34));
		put(EnumAspect.VOID.name(), new Pair<>("title."+EnumAspect.VOID.toString(), 0x001856));
		put(EnumAspect.HEART.name(), new Pair<>("title."+EnumAspect.HEART.toString(), 0xBD1864));
		put(EnumAspect.HOPE.name(), new Pair<>("title."+EnumAspect.HOPE.toString(), 0xFFDE55));
		put(EnumAspect.BLOOD.name(), new Pair<>("title."+EnumAspect.BLOOD.toString(), 0xB71015));
		put(EnumAspect.RAGE.name(), new Pair<>("title."+EnumAspect.RAGE.toString(), 0x9C4DAC));
		put(EnumAspect.MIND.name(), new Pair<>("title."+EnumAspect.MIND.toString(), 0x06FFC9));
		put(EnumAspect.DOOM.name(), new Pair<>("title."+EnumAspect.DOOM.toString(), 0x306800));

		put(EnumClass.BARD.name(), new Pair<>("title."+EnumClass.BARD.toString(), 0xDB5397));
		put(EnumClass.HEIR.name(), new Pair<>("title."+EnumClass.HEIR.toString(), 0x6D9EEB));
		put(EnumClass.KNIGHT.name(), new Pair<>("title."+EnumClass.KNIGHT.toString(), 0xEF7F34));
		put(EnumClass.MAGE.name(), new Pair<>("title."+EnumClass.MAGE.toString(), 0xB55BFF));
		put(EnumClass.MAID.name(), new Pair<>("title."+EnumClass.MAID.toString(), 0x31E0AB));
		put(EnumClass.PAGE.name(), new Pair<>("title."+EnumClass.PAGE.toString(), 0xFFFF9B));
		put(EnumClass.PRINCE.name(), new Pair<>("title."+EnumClass.PRINCE.toString(), 0x7C1D1D));
		put(EnumClass.ROGUE.name(), new Pair<>("title."+EnumClass.ROGUE.toString(), 0x39C4C6));
		put(EnumClass.SEER.name(), new Pair<>("title."+EnumClass.SEER.toString(), 0xD670FF));
		put(EnumClass.SYLPH.name(), new Pair<>("title."+EnumClass.SYLPH.toString(), 0xFF8377));
		put(EnumClass.THIEF.name(), new Pair<>("title."+EnumClass.THIEF.toString(), 0x996543));
		put(EnumClass.WITCH.name(), new Pair<>("title."+EnumClass.WITCH.toString(), 0x7F7F7F));
		put(EnumClass.LORD.name(), new Pair<>("title."+EnumClass.LORD.toString(), 0xFF0000));
		put(EnumClass.MUSE.name(), new Pair<>("title."+EnumClass.MUSE.toString(), 0x00FF00));

		put("PROSPIT", new Pair<>("lunarSway.prospit", 0xFFD800));
		put("DERSE", new Pair<>("lunarSway.derse", 0xB200FF));
		put("HEROIC", new Pair<>("alignment.heroic", 0xFFD800));
		put("JUST", new Pair<>("alignment.just", 0xB200FF));
		put("NEUTRAL", new Pair<>("alignment.neutral", 0x00FF15));
	}};


	public static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/abilitechnosynth.png");

	public EntityPlayer player;
	public Minecraft mc;

	private int xSize = 256;
	private int ySize = 182;
	private int textBoxWidth = 100;
	private int textBoxHeight = 110;
	private int maxTech = 24;

	private int guiLeft;
	private int guiTop;

	private int techTab = 0;
	private float scrollPos = 0;
	private int descLines = 0;

	int selected = -1;
	int selOffX = 0;
	int selOffY = 0;

	boolean mouseClicked = false;

	int xOffset;
	int yOffset;

	protected final ArrayList<Abilitech> tech = new ArrayList<>();

	public GuiFraymachine(EntityPlayer player)
	{
		this.player = player;
		this.mc = Minecraft.getMinecraft();
		this.fontRenderer = mc.fontRenderer;

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		setupTech();

	}

	private void setupTech()
	{
		tech.clear();
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		tech.addAll(data.getAllAbilitechs());
		tech.sort(Comparator.comparingInt(Skill::getSortIndex));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		yOffset = this.height / 2 - ySize/2;
		xOffset = this.width / 2 - (xSize-16)/2;

		mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, xSize, ySize);

		fontRenderer.drawString(I18n.format("gui.abilitechnosynth"), xOffset+124, yOffset + 18, 0xFFFFFF, true);

		boolean resetSel = !mouseClicked;

		mc.getTextureManager().bindTexture(TEXTURES);
		String tabLabel = techTab+1 + "/" + (tech.size()/maxTech+1);
		drawTexturedModalRect(xOffset+30, yOffset+130, 0, techTab > 0 ? 229 : 243, 14, 11);
		drawTexturedModalRect(xOffset+76, yOffset+130, 14, techTab < tech.size()/maxTech ? 229 : 243, 14, 11);
		fontRenderer.drawString(tabLabel, xOffset + 60 - fontRenderer.getStringWidth(tabLabel)/2, yOffset + 131, 0xFFFFFF, true);

		mc.getTextureManager().bindTexture(TEXTURES);
		GlStateManager.color(1, 1, 1);
		for(int i = 0; i+techTab*maxTech < tech.size() && i < maxTech; i++)
		{
			Abilitech abilitech = tech.get(i+techTab*maxTech);
			int x = xOffset + (i/4 % 2 == 0 ? 6 : 18) + (i%4)*24;
			int y = yOffset + 10 + (i/4)*18;

			if(selected != i || !mouseClicked)
			{
				if(!abilitech.canUse(mc.player.world, mc.player))
					GlStateManager.color(0.5f,0.5f,0.5f);
				mc.getTextureManager().bindTexture(new ResourceLocation(abilitech.getRegistryName().getResourceDomain(), "textures/gui/abilitechs/icons/"+abilitech.getRegistryName().getResourcePath()+".png"));
				drawScaledCustomSizeModalRect(x, y, 0, 0, 256, 256, 24, 24, 256, 256);
				GlStateManager.color(1,1,1);
			}

			if(!mouseClicked && pointInTechSlot(x, y, mouseX, mouseY))
			{
				if(selected != i)
					scrollPos = 0;

				selected = i;
				resetSel = false;
				selOffX = x - mouseX;
				selOffY = y - mouseY;
			}
		}

		for(int i = 0; i < data.getTechSlots(); i++)
		{
			Abilitech abilitech = data.getTechLoadout()[i];
			if(abilitech == null) continue;

			int x = xOffset + 120 - data.getTechSlots()*12 + i*24;
			int y = yOffset + 153;

			if(!mouseClicked || selected != maxTech+i)
			{
				if(!abilitech.canUse(mc.player.world, mc.player))
					GlStateManager.color(0.5f,0.5f,0.5f);
				mc.getTextureManager().bindTexture(new ResourceLocation(abilitech.getRegistryName().getResourceDomain(), "textures/gui/abilitechs/icons/"+abilitech.getRegistryName().getResourcePath()+".png"));
				drawScaledCustomSizeModalRect(x, y, 0, 0, 256, 256, 24, 24, 256, 256);
				GlStateManager.color(1,1,1);
			}

			if(!mouseClicked && pointInTechSlot(x, y, mouseX, mouseY))
			{
				if(selected != maxTech + i)
					scrollPos = 0;

				selected = maxTech + i;
				resetSel = false;
				selOffX = x - mouseX;
				selOffY = y - mouseY;
			}
		}
		if(resetSel) selected = -1;

		if(selected >= 0)
		{
			int x = (selected >= maxTech) ? xOffset + 120 - data.getTechSlots()*12 + (selected-maxTech)*24 : xOffset + (selected/4 % 2 == 0 ? 6 : 18) + (selected%4)*24;
			int y = (selected >= maxTech) ? yOffset + 153 : yOffset + 10 + (selected/4)*18;

			mc.getTextureManager().bindTexture(TEXTURES);
			drawTexturedModalRect(x-1, y-1, 0, 202, 26, 26);

			Abilitech selectedTech = (selected >= maxTech) ? data.getTechLoadout()[selected-maxTech] : tech.get(selected+techTab*maxTech);
			String name = selectedTech.getDisplayName();
			String description = "\n" + selectedTech.getDisplayTooltip();

			for(String tag : selectedTech.getTags())
				description = "[" + tag + "]\n" + description;

			description = new String(new char[fontRenderer.listFormattedStringToWidth(name, textBoxWidth).size()]).replace('\0', '\n') + description;

			descLines = Math.max(0, fontRenderer.listFormattedStringToWidth(description, textBoxWidth).size() - (textBoxHeight/fontRenderer.FONT_HEIGHT));

			drawSplitString(name, xOffset + 121, yOffset + 36, textBoxWidth, textBoxHeight, 0xFFFFFF, (int) (scrollPos*descLines), true);
			drawSplitString(description, xOffset + 121, yOffset + 36, textBoxWidth, textBoxHeight, 0xFFFFFF, (int) (scrollPos*descLines), false);

			GlStateManager.color(1,1,1);
			mc.getTextureManager().bindTexture(TEXTURES);
			drawTexturedModalRect(xOffset+222, yOffset+35 + (int)(scrollPos*95), descLines > 0 ? 28 : 38, 241, 10, 15);

		} else
		{
			descLines = 0;
			mc.getTextureManager().bindTexture(TEXTURES);
			drawTexturedModalRect(xOffset+222, yOffset+35, 38, 241, 10, 15);
		}

		for(int i = 0; i < data.getTechSlots(); i++)
		{
			Abilitech abilitech = data.getTechLoadout()[i];
			if(abilitech == null) continue;

			int x = xOffset + 120 - data.getTechSlots()*12 + i*24;
			int y = yOffset + 153;

			if((!mouseClicked || selected != maxTech+i))
			{
				if(abilitech.getTags().contains("@"+EnumTechType.PASSIVE+"@") && !data.isTechPassiveEnabled(abilitech))
				{
					mc.getTextureManager().bindTexture(TEXTURES);
					drawTexturedModalRect(x, y, 26, 203, 24, 24);
				}
			}
		}

		if(selected >= 0)
		{
			Abilitech abilitech = selected >= maxTech ? data.getTech(selected-maxTech) : tech.get(selected+techTab*maxTech);

			if(mouseClicked)
			{
				if(!abilitech.canUse(mc.player.world, mc.player))
					GlStateManager.color(0.5f,0.5f,0.5f);
				mc.getTextureManager().bindTexture(TEXTURES);
				mc.getTextureManager().bindTexture(new ResourceLocation(abilitech.getRegistryName().getResourceDomain(), "textures/gui/abilitechs/icons/"+abilitech.getRegistryName().getResourcePath()+".png"));
				drawScaledCustomSizeModalRect(mouseX + selOffX, mouseY + selOffY, 0, 0, 256, 256, 24, 24, 256, 256);
				GlStateManager.color(1,1,1);
			}
			else
			{
				if(!abilitech.canUse(player.world, player))
					drawHoveringText(I18n.format("gui.abilitechBlocked"), mouseX, mouseY);
				else if(selected >= maxTech && abilitech.getTags().contains("@"+EnumTechType.PASSIVE+"@") && !data.isTechPassiveEnabled(abilitech))
					drawHoveringText(I18n.format("gui.abilitechDisabled"), mouseX, mouseY);
			}
		}
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		float s = Mouse.getDWheel();
		scrollPos = Math.max(Math.min(scrollPos + 1.0F/descLines * -Math.signum(s), 1), 0);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if(mouseButton == 0)
			mouseClicked = true;
		else if(mouseButton == 1)
		{
			IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			if(!mouseClicked && selected >= maxTech)
			{
				Abilitech abilitech = data.getTechLoadout()[selected-maxTech];
				if(abilitech != null)
				{
					data.setSkillPassiveEnabled(abilitech, !data.isTechPassiveEnabled(abilitech));
					data.update();
				}
			}
		}

		if(pointInRegion(xOffset + 30, yOffset + 130, 14, 11, mouseX, mouseY) && techTab > 0)
			techTab--;
		if(pointInRegion(xOffset + 76, yOffset + 130, 14, 11, mouseX, mouseY) && techTab < tech.size()/maxTech)
			techTab++;

	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		if(mouseClicked && selected >= 0)
		{
			Abilitech sel = selected >= maxTech ? data.getTechLoadout()[selected-maxTech] : tech.get(selected+techTab*maxTech);
			if(selected >= maxTech)
			{
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.UNEQUIP_ABILITECH, selected-maxTech, data.getTech(selected-maxTech)));
				data.unequipTech(selected-maxTech);

				data.update();
			}

			for(int i = 0; i < data.getTechSlots(); i++)
			{
				int x = xOffset + 120 - data.getTechSlots()*12 + i*24;
				int y = yOffset + 153;

				if(pointInTechSlot(x, y, mouseX, mouseY))
				{
					if(data.getTech(i) != null)
						MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.UNEQUIP_ABILITECH, i, data.getTech(i)));

					data.equipTech(sel, i);
					data.update();
				}
			}
		}


		if(state == 0)
			mouseClicked = false;
	}

	protected void drawSplitString(String str, int x, int y, int wrapWidth, int maxHeight, int textColor, int startingLine, boolean dropShadow)
	{
		drawSplitString(fontRenderer, str, x, y, wrapWidth, maxHeight, textColor, startingLine, dropShadow);
	}

	public static void drawSplitString(FontRenderer fontRenderer, String str, int x, int y, int wrapWidth, int maxHeight, int textColor, int startingLine, boolean dropShadow)
	{
		List<String> splitStr = fontRenderer.listFormattedStringToWidth(str, wrapWidth);

		int yTop = y;

		for(int i = startingLine; i < splitStr.size(); i++)
		{
			if(splitStr.get(i) != null)
			{
				String baseStr = splitStr.get(i);

				for(Map.Entry<String, Pair<String, Integer>> replacement : TEXT_REPLACEMENTS.entrySet())
					baseStr = baseStr.replace("@" + replacement.getKey() + "@", I18n.format(replacement.getValue().object1));
				fontRenderer.drawString(baseStr, x, y, textColor, dropShadow);

				for(Map.Entry<String, Pair<String, Integer>> replacement : TEXT_REPLACEMENTS.entrySet())
				{
					int xx = x;
					String typeName = "@" + replacement.getKey() + "@";
					String line = splitStr.get(i);

					for(Map.Entry<String, Pair<String, Integer>> r : TEXT_REPLACEMENTS.entrySet())
						if(r != replacement) line = line.replace("@" + r.getKey() + "@", I18n.format(r.getValue().object1));

					for(String s : line.split("(?<="+typeName+")|(?="+typeName+")"))
					{
						if(s.equals(typeName))
						{
							s = I18n.format(replacement.getValue().object1);
							fontRenderer.drawString(s, xx, y, replacement.getValue().object2, dropShadow);
						}
						xx += fontRenderer.getStringWidth(s);
					}
				}
			}
			y += fontRenderer.FONT_HEIGHT;
			if(y-yTop > Math.floor(maxHeight/fontRenderer.FONT_HEIGHT-1)*fontRenderer.FONT_HEIGHT) return;
		}
	}

	public static boolean pointInRegion(int x, int y, int u, int v, int pointX, int pointY)
	{
		return pointX >= x && pointX < x+u && pointY >= y && pointY < y+v;
	}

	public static boolean pointInTechSlot(int techX, int techY, int pointX, int pointY)
	{
		techX += 12;
		techY += 12;

		if(pointX < techX-12 || pointX > techX+12) return false;

		Vec2f A = new Vec2f(techX-24, techY);
		Vec2f B = new Vec2f(techX, techY+12);
		Vec2f C = new Vec2f(techX+24, techY);
		Vec2f D = new Vec2f(techX, techY-12);

		double a = (0.5*distance(A,C));     // half-width (in the x-direction)
		double b = (0.5*distance(B,D));     // half-height (y-direction)
		Vec2f U = new Vec2f((float)((C.x - A.x)/(2*a)), (float)((C.y - A.y)/(2*a)));         // unit vector in x-direction
		Vec2f V = new Vec2f((float)((D.x - B.x)/(2*b)), (float)((D.y - B.y)/(2*b)));         // unit vector in y-direction

		Vec2f W = new Vec2f(pointX - techX, pointY - techY);

		float xabs = (float) Math.abs(dotProduct(W, U));    // here W*U is the dot product of W and U
		float yabs = (float) Math.abs(dotProduct(W, V));    // here W*V is the dot product of W and V

		return (xabs/a + yabs/b <= 1);
	}

	private static double distance(Vec2f a, Vec2f b)
	{
		double d0 = a.x - b.x;
		double d1 = a.y - b.y;
		return (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
	}

	private static double dotProduct(Vec2f a, Vec2f b)
	{
		return a.x * b.x + a.y * b.y;
	}

	public List<String> listFormattedStringToWidth(String str, int wrapWidth)
	{
		return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
	}

	/**
	 * Inserts newline and formatting into a string to wrap it within the specified width.
	 */
	private String wrapFormattedStringToWidth(String str, int wrapWidth)
	{
		String checkStr = str;

		for(Map.Entry<String, Pair<String, Integer>> replacement : TEXT_REPLACEMENTS.entrySet())
			checkStr = checkStr.replace("@" + replacement.getKey() + "@", I18n.format(replacement.getValue().object1));

		int i = sizeStringToWidth(checkStr, wrapWidth);

		boolean found = false;
		for (Map.Entry<String, Pair<String, Integer>> replacement : TEXT_REPLACEMENTS.entrySet())
		{
			String key = "@" + replacement.getKey() + "@";
			for(int j = str.indexOf(key); j != -1; j = str.indexOf(key, j+1))
				if(j <= i && j+key.length() > i)
				{
					i += key.length() - (str.substring(0, i).length()-j);
					found = true;

					i += sizeStringToWidth(checkStr.substring(i), wrapWidth-fontRenderer.getStringWidth(checkStr.substring(0, i)));

					break;
				}
			if(found) break;
		}

		if (checkStr.length() <= i)
		{
			return str;
		}
		else
		{

			String s = str.substring(0, i);
			char c0 = str.charAt(i);
			boolean flag = c0 == ' ' || c0 == '\n';
			String s1 = FontRenderer.getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
			return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
		}
	}

	private int sizeStringToWidth(String str, int wrapWidth)
	{
		int i = str.length();
		int j = 0;
		int k = 0;
		int l = -1;

		for (boolean flag = false; k < i; ++k)
		{
			char c0 = str.charAt(k);

			switch (c0)
			{
				case '\n':
					--k;
					break;
				case ' ':
					l = k;
				default:
					j += fontRenderer.getCharWidth(c0);

					if (flag)
					{
						++j;
					}

					break;
				case '\u00a7':

					if (k < i - 1)
					{
						++k;
						char c1 = str.charAt(k);

						if (c1 != 'l' && c1 != 'L')
						{
							if (c1 == 'r' || c1 == 'R' || isFormatColor(c1))
							{
								flag = false;
							}
						}
						else
						{
							flag = true;
						}
					}
			}

			if (c0 == '\n')
			{
				++k;
				l = k;
				break;
			}

			if (j > wrapWidth)
			{
				break;
			}
		}

		return k != i && l != -1 && l < k ? l : k;
	}

	private static boolean isFormatColor(char colorChar)
	{
		return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
	}
}
