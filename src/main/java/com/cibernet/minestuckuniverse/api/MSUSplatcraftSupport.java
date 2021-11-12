package com.cibernet.minestuckuniverse.api;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.splatcraft.items.ItemFilter;
import com.cibernet.splatcraft.network.PacketPlayerReturnColor;
import com.cibernet.splatcraft.network.PacketPlayerSetColor;
import com.cibernet.splatcraft.network.SplatCraftPacketHandler;
import com.cibernet.splatcraft.recipes.RecipesInkwellVat;
import com.cibernet.splatcraft.registries.SplatCraftItems;
import com.cibernet.splatcraft.utils.InkColors;
import com.cibernet.splatcraft.world.save.SplatCraftPlayerData;
import com.mraof.minestuck.client.gui.GuiColorSelector;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class MSUSplatcraftSupport
{
	private static ArrayList<InkColors> cruxiteColors = new ArrayList<>();
	
	public static void registerColors()
	{
		addColor("CRUXITE",10082794, MapColor.LIGHT_BLUE, "cruxite");
		addColor("CRUXITE_BLUE",464333, MapColor.LIGHT_BLUE, "cruxiteBlue");
		addColor("CRUXITE_ORCHID",11876058, MapColor.LIGHT_BLUE, "cruxiteOrchid");
		addColor("CRUXITE_RED",14681863, MapColor.LIGHT_BLUE, "cruxiteRed");
		addColor("CRUXITE_GREEN",4901157, MapColor.LIGHT_BLUE, "cruxiteGreen");
		addColor("CRUXITE_CYAN",54770, MapColor.LIGHT_BLUE, "cruxiteCyan");
		addColor("CRUXITE_PINK",16740338, MapColor.LIGHT_BLUE, "cruxitePink");
		addColor("CRUXITE_ORANGE",15901696, MapColor.LIGHT_BLUE, "cruxiteOrange");
		addColor("CRUXITE_EMERALD",2069504, MapColor.LIGHT_BLUE, "cruxiteEmerald");
		addColor("CRUXITE_RUST",10551296, MapColor.LIGHT_BLUE, "cruxiteRust");
		addColor("CRUXITE_BRONZE",10571776, MapColor.LIGHT_BLUE, "cruxiteBronze");
		addColor("CRUXITE_GOLD",10592512, MapColor.LIGHT_BLUE, "cruxiteGold");
		addColor("CRUXITE_IRON",6447714, MapColor.LIGHT_BLUE, "cruxiteIron");
		addColor("CRUXITE_OLIVE",4285952, MapColor.LIGHT_BLUE, "cruxiteOlive");
		addColor("CRUXITE_JADE",33089, MapColor.LIGHT_BLUE, "cruxiteJade");
		addColor("CRUXITE_TEAL",33410, MapColor.LIGHT_BLUE, "cruxiteTeal");
		addColor("CRUXITE_COBALT",22146, MapColor.LIGHT_BLUE, "cruxiteCobalt");
		addColor("CRUXITE_INDIGO",86, MapColor.LIGHT_BLUE, "cruxiteIndigo");
		addColor("CRUXITE_PURPLE",2818135, MapColor.LIGHT_BLUE, "cruxitePurple");
		addColor("CRUXITE_VIOLET",6946922, MapColor.LIGHT_BLUE, "cruxiteViolet");
		addColor("CRUXITE_FUCSHIA",7798844, MapColor.LIGHT_BLUE, "cruxiteFucshia");

		RecipesInkwellVat.addRecipe((ItemFilter) MinestuckUniverseItems.splatcraftCruxiteFilter, cruxiteColors.toArray(new InkColors[21]));
		GameRegistry.addShapelessRecipe(new ResourceLocation(MinestuckUniverse.MODID, "splatcraft_cruxite_filter"), null, new ItemStack(MinestuckUniverseItems.splatcraftCruxiteFilter),
				Ingredient.fromItem(SplatCraftItems.filterEmpty), Ingredient.fromItem(MinestuckItems.rawCruxite), Ingredient.fromItem(MinestuckItems.rawUranium));

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onGuiClosed(GuiScreenEvent.ActionPerformedEvent event)
	{
		if(Minecraft.getMinecraft().player != null &&
				event.getGui() instanceof GuiColorSelector && (boolean)ObfuscationReflectionHelper.getPrivateValue(GuiColorSelector.class, ((GuiColorSelector)event.getGui()), "firstTime"))
		{
			EntityPlayer player = Minecraft.getMinecraft().player;
			int color = cruxiteColors.get((int)ObfuscationReflectionHelper.getPrivateValue(GuiColorSelector.class, ((GuiColorSelector)event.getGui()), "selectedColor")+1).getColor();
			SplatCraftPlayerData.setInkColor(player, color);
			SplatCraftPacketHandler.instance.sendToDimension(new PacketPlayerSetColor(player.getUniqueID(), color), player.dimension);

		}
	}

	private static void addColor(String name, int color, MapColor mapColor, String unlocalizedName)
	{
		cruxiteColors.add(InkColors.addColor(name, color, mapColor, unlocalizedName));
	}
}
