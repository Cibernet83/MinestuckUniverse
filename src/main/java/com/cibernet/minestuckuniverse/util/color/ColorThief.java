package com.cibernet.minestuckuniverse.util.color;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class ColorThief
{

	private static final int DEFAULT_QUALITY = 2;
	private static final boolean DEFAULT_IGNORE_WHITE = false;

	@Nullable
	public static int[] getColor(BufferedImage sourceImage)
	{
		int[][] palette = ColorThief.getPalette(sourceImage, 5);
		if (palette != null) return palette[0];
		return null;
	}

	@Nullable
	public static int[][] getPalette(BufferedImage sourceImage, int colorCount)
	{
		MMCQ.CMap cmap = ColorThief.getColorMap(sourceImage, colorCount);
		if (cmap != null)
			return cmap.palette();
		return null;
	}

	@Nullable
	public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount)
	{
		return ColorThief.getColorMap(sourceImage, colorCount, 2, false);
	}

	@Nullable
	public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite)
	{
		int[][] pixelArray;
		switch (sourceImage.getType())
		{
			case 5:
			case 6:
			{
				pixelArray = ColorThief.getPixelsFast(sourceImage, quality, ignoreWhite);
				return MMCQ.quantize(pixelArray, colorCount);
			}
			default:
			{
				pixelArray = ColorThief.getPixelsSlow(sourceImage, quality, ignoreWhite);
			}
		}
		return MMCQ.quantize(pixelArray, colorCount);
	}

	private static int[][] getPixelsFast(BufferedImage sourceImage, int quality, boolean ignoreWhite)
	{
		int colorDepth;
		DataBufferByte imageData = (DataBufferByte) sourceImage.getRaster().getDataBuffer();
		byte[] pixels = imageData.getData();
		int pixelCount = sourceImage.getWidth() * sourceImage.getHeight();
		int type = sourceImage.getType();
		switch (type)
		{
			case 5:
			{
				colorDepth = 3;
				break;
			}
			case 6:
			{
				colorDepth = 4;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unhandled type: " + type);
			}
		}
		int expectedDataLength = pixelCount * colorDepth;
		if (expectedDataLength != pixels.length)
		{
			throw new IllegalArgumentException("(expectedDataLength = " + expectedDataLength + ") != (pixels.length = " + pixels.length + ")");
		}
		int numRegardedPixels = (pixelCount + quality - 1) / quality;
		int numUsedPixels = 0;
		int[][] pixelArray = new int[numRegardedPixels][];
		switch (type)
		{
			case 5:
			{
				int i = 0;
				while (i < pixelCount)
				{
					int offset = i * 3;
					int b = pixels[offset] & 0xFF;
					int g = pixels[offset + 1] & 0xFF;
					int r = pixels[offset + 2] & 0xFF;
					if (!ignoreWhite || r <= 250 || g <= 250 || b <= 250)
					{
						pixelArray[numUsedPixels] = new int[]{r, g, b};
						++numUsedPixels;
					}
					i += quality;
				}
				return Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
			}
			case 6:
			{
				int i = 0;
				while (i < pixelCount)
				{
					int offset = i * 4;
					int a = pixels[offset] & 0xFF;
					int b = pixels[offset + 1] & 0xFF;
					int g = pixels[offset + 2] & 0xFF;
					int r = pixels[offset + 3] & 0xFF;
					if (!(a < 125 || ignoreWhite && r > 250 && g > 250 && b > 250))
					{
						pixelArray[numUsedPixels] = new int[]{r, g, b};
						++numUsedPixels;
					}
					i += quality;
				}
				return Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
			}
		}
		throw new IllegalArgumentException("Unhandled type: " + type);
	}

	private static int[][] getPixelsSlow(BufferedImage sourceImage, int quality, boolean ignoreWhite)
	{
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int pixelCount = width * height;
		int numRegardedPixels = (pixelCount + quality - 1) / quality;
		int numUsedPixels = 0;
		int[][] res = new int[numRegardedPixels][];
		int i = 0;
		while (i < pixelCount)
		{
			int row = i / width;
			int col = i % width;
			int rgb = sourceImage.getRGB(col, row);
			int r = rgb >> 16 & 0xFF;
			int g = rgb >> 8 & 0xFF;
			int b = rgb & 0xFF;
			if (!ignoreWhite || r <= 250 || r <= 250 || r <= 250)
			{
				res[numUsedPixels] = new int[]{r, g, b};
				++numUsedPixels;
			}
			i += quality;
		}
		return Arrays.copyOfRange(res, 0, numUsedPixels);
	}

	@Nullable
	public static int[] getColor(BufferedImage sourceImage, int quality, boolean ignoreWhite)
	{
		int[][] palette = ColorThief.getPalette(sourceImage, 5, quality, ignoreWhite);
		if (palette != null) return palette[0];
		return null;
	}

	@Nullable
	public static int[][] getPalette(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite)
	{
		MMCQ.CMap cmap = ColorThief.getColorMap(sourceImage, colorCount, quality, ignoreWhite);
		if (cmap != null)
			return cmap.palette();
		return null;
	}

}
