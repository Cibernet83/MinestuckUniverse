package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class MSUGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new Config(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	static class Config extends GuiConfig {
		public Config(GuiScreen parentScreen) {
			super(parentScreen, getConfigElements(), MinestuckUniverse.MODID, false, false, I18n.format("config.minestuckuniverse.title"));
		}

		private static List<IConfigElement> getConfigElements() {
			Configuration config = MSUConfig.config;
			List<IConfigElement> list = new ArrayList();

			for(String category : config.getCategoryNames())
				list.add(new ConfigElement(config.getCategory(category)));

			return list;
		}
	}
}
