package com.cibernet.minestuckuniverse.util.color;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ColorGetter
{
	public static final ColorGetter INSTANCE = new ColorGetter();
	private static final String[] defaultColors = new String[]{
			"White:EEEEEE",
			"LightBlue:7492cc",
			"Cyan:00EEEE",
			"Blue:2222dd",
			"LapisBlue:25418b",
			"Teal:008080",
			"Yellow:cacb58",
			"GoldenYellow:EED700",
			"Orange:d97634",
			"Pink:D1899D",
			"HotPink:FC0FC0",
			"Magenta:b24bbb",
			"Purple:813eb9",
			"JadedPurple:43324f",
			"EvilPurple:2e1649",
			"Lavender:B57EDC",
			"Indigo:480082",
			"Sand:dbd3a0",
			"Tan:bb9b63",
			"LightBrown:A0522D",
			"Brown:634b33",
			"DarkBrown:3a2d13",
			"LimeGreen:43b239",
			"SlimeGreen:83cb73",
			"Green:008000",
			"DarkGreen:224d22",
			"GrassGreen:548049",
			"Red:963430",
			"BrickRed:b0604b",
			"NetherBrick:2a1516",
			"Redstone:ce3e36",
			"Black:181515",
			"CharcoalGray:464646",
			"IronGray:646464",
			"Gray:808080",
			"Silver:C0C0C0"
	};

	private ColorGetter()
	{}

	public static String[] getDefaultColors()
	{
		return defaultColors;
	}

	public static String getMostCommon(Color color)
	{
		String mostCommon = "";
		int mostCommonTracker = 770;
		for (int i = 0; i < defaultColors.length; i++)
		{
			String hex = defaultColors[i].substring(defaultColors[i].length() - 6);
			Color color2 = new Color(
					Integer.valueOf(hex.substring(0, 2), 16),
					Integer.valueOf(hex.substring(2, 4), 16),
					Integer.valueOf(hex.substring(4, 6), 16));
			int compare = compare(color, color2);
			if (compare <= mostCommonTracker)
			{
				mostCommonTracker = compare;
				mostCommon = defaultColors[i].substring(0, defaultColors[i].length() - 7);
			}
		}
		return mostCommon;
	}

	/*
	public static String getMostCommons(Color color, int colorCount)
	{
		String[] mostCommons = new String[colorCount];
		int[] mostCommonsTracker = new int[colorCount];
		for(int i = 0 ; i < mostCommonsTracker.length ; i ++)
			mostCommonsTracker[i] = 770;
		for(int i = 0 ; i < defaultColors.length ; i ++)
		{
			String hex = defaultColors[i].substring(defaultColors[i].length()-6);
			 Color color2 = new Color(
					Integer.valueOf( hex.substring( 0, 2 ), 16 ),
					Integer.valueOf( hex.substring( 2, 4 ), 16 ),
					Integer.valueOf( hex.substring( 4, 6 ), 16 ) );
			 int compare = compare(color, color2);
			 for(int j = 0 ; j < mostCommonsTracker.length ; j++)
			 {
				 if(compare <= mostCommonsTracker[j])
				 {
				 	int temp = mostCommonsTracker[j];
				 	mostCommonsTracker[j] = compare;

				 	for(int k = j ; k < mostCommonsTracker.length ; k ++)
					{
						temp =
					}

				 }
			 }

		}
	}
	*/

	public static int compare(Color color1, Color color2)
	{
		int diff = 0;
		diff += Math.abs(color1.getBlue() - color2.getBlue());
		diff += Math.abs(color1.getGreen() - color2.getGreen());
		diff += Math.abs(color1.getRed() - color2.getRed());
		return diff;
	}

	public static List<Color> getColors(ItemStack itemStack, int colorCount)
	{
		try
		{
			return ColorGetter.unsafeGetColors(itemStack, colorCount);
		}
		catch (Exception e)
		{
			return Collections.emptyList();
		}
	}

	public static List<Color> unsafeGetColors(ItemStack itemStack, int colorCount)
	{
		Item item = itemStack.getItem();
		if (itemStack.isEmpty())
			return Collections.emptyList();
		if (!(item instanceof ItemBlock))
			return ColorGetter.getItemColors(itemStack, colorCount);
		Block block = ((ItemBlock) item).getBlock();
		if (block != null)
			return ColorGetter.getBlockColors(itemStack, block, colorCount);
		return Collections.emptyList();
	}

	public static List<Color> getItemColors(ItemStack itemStack, int colorCount)
	{
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		int renderColor = itemColors.colorMultiplier(itemStack, 0);
		TextureAtlasSprite textureAtlasSprite = ColorGetter.getTextureAtlasSprite(itemStack);
		return ColorGetter.getColors(textureAtlasSprite, renderColor, colorCount);
	}

	private static List<Color> getBlockColors(ItemStack itemStack, Block block, int colorCount)
	{
		IBlockState blockState;
		int meta = itemStack.getMetadata();
		try
		{
			blockState = block.getStateFromMeta(meta);
		}
		catch (LinkageError | RuntimeException ignored)
		{
			blockState = block.getDefaultState();
		}
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		int renderColor = blockColors.colorMultiplier(blockState, null, null, 0);
		TextureAtlasSprite textureAtlasSprite = ColorGetter.getTextureAtlasSprite(blockState);
		if (textureAtlasSprite != null)
			return ColorGetter.getColors(textureAtlasSprite, renderColor, colorCount);
		return Collections.emptyList();
	}

	public static List<Color> getColors(TextureAtlasSprite textureAtlasSprite, int renderColor, int colorCount)
	{
		BufferedImage bufferedImage = ColorGetter.getBufferedImage(textureAtlasSprite);
		if (bufferedImage == null)
			return Collections.emptyList();
		ArrayList<Color> colors = new ArrayList<Color>(colorCount);
		int[][] palette = ColorThief.getPalette(bufferedImage, colorCount);
		if (palette == null)
			return colors;
		int[][] nArray = palette;
		int n = nArray.length;
		int n2 = 0;
		while (n2 < n)
		{
			int[] colorInt = nArray[n2];
			int red = (int) ((float) (colorInt[0] - 1) * (float) (renderColor >> 16 & 0xFF) / 255.0f);
			int green = (int) ((float) (colorInt[1] - 1) * (float) (renderColor >> 8 & 0xFF) / 255.0f);
			int blue = (int) ((float) (colorInt[2] - 1) * (float) (renderColor & 0xFF) / 255.0f);
			red = Math.max(0, Math.min(255, red));
			green = Math.max(0, Math.min(255, green));
			blue = Math.max(0, Math.min(255, blue));
			Color color = new Color(red, green, blue);
			colors.add(color);
			++n2;
		}
		return colors;
	}

	@Nullable
	private static BufferedImage getBufferedImage(TextureAtlasSprite textureAtlasSprite)
	{
		int iconWidth = textureAtlasSprite.getIconWidth();
		int iconHeight = textureAtlasSprite.getIconHeight();
		int frameCount = textureAtlasSprite.getFrameCount();
		if (iconWidth <= 0)
			return null;
		if (iconHeight <= 0)
			return null;
		if (frameCount <= 0)
			return null;
		BufferedImage bufferedImage = new BufferedImage(iconWidth, iconHeight * frameCount, 6);
		int i = 0;
		while (i < frameCount)
		{
			int[][] frameTextureData = textureAtlasSprite.getFrameTextureData(i);
			int[] largestMipMapTextureData = frameTextureData[0];
			bufferedImage.setRGB(0, i * iconHeight, iconWidth, iconHeight, largestMipMapTextureData, 0, iconWidth);
			++i;
		}
		return bufferedImage;
	}

	@Nullable
	private static TextureAtlasSprite getTextureAtlasSprite(IBlockState blockState)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		BlockRendererDispatcher blockRendererDispatcher = minecraft.getBlockRendererDispatcher();
		BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
		TextureAtlasSprite textureAtlasSprite = blockModelShapes.getTexture(blockState);
		if (textureAtlasSprite != minecraft.getTextureMapBlocks().getMissingSprite()) return textureAtlasSprite;
		return null;
	}

	private static TextureAtlasSprite getTextureAtlasSprite(ItemStack itemStack)
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
		IBakedModel itemModel = itemModelMesher.getItemModel(itemStack);
		TextureAtlasSprite particleTexture = itemModel.getParticleTexture();
		return Preconditions.checkNotNull(particleTexture);
	}
}


