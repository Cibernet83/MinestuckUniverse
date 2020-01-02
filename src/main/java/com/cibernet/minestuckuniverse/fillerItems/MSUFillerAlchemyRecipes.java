package com.cibernet.minestuckuniverse.fillerItems;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.cibernet.minestuckuniverse.fillerItems.MSUFillerItems.*;
import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.MODE_AND;
import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.MODE_OR;
import static com.mraof.minestuck.alchemy.GristType.*;

public class MSUFillerAlchemyRecipes
{
	public static void registerRecipes()
	{
		//Grist Conversions
		GristRegistry.addGristConversion(new ItemStack(diverHelmet), new GristSet(new GristType[] {Rust}, new int[] {64}));
		GristRegistry.addGristConversion(new ItemStack(murica), new GristSet(Artifact, -1));
		GristRegistry.addGristConversion(new ItemStack(muricaSouth), new GristSet(new GristType[] {Artifact, Garnet}, new int[] {-10, 1}));
		GristRegistry.addGristConversion(new ItemStack(MinestuckUniverseItems.chc), new GristSet(new GristType[] {Build, Uranium}, new int[] {100, 34}));
		GristRegistry.addGristConversion(new ItemStack(MinestuckUniverseItems.ctd), new GristSet(new GristType[] {Shale, Tar, Mercury, Sulfur}, new int[] {100, 34, 236, 152}));
		GristRegistry.addGristConversion(new ItemStack(MinestuckUniverseItems.cht), new GristSet(new GristType[] {Shale, Build, Uranium, Mercury, Chalk, Quartz, Diamond, Zillium}, new int[] {999999999, 999999999, 999, 9999, 9999, 20, 40, 20}));
		GristRegistry.addGristConversion(new ItemStack(MSUFillerItems.CHS), new GristSet(new GristType[] {Shale, Build, Uranium, Mercury, Chalk, Quartz, Diamond, Zillium}, new int[] {999999999, 999999999, 999, 9999, 9999, 999, 999, 999}));
		//Alchemy
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_HELMET), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, false, false, new ItemStack(diverHelmet));
		CombinationRegistry.addCombination(new ItemStack(MinestuckItems.itemFrog, 1, 1), new ItemStack(Items.LEATHER_HELMET), MODE_AND, true, false, new ItemStack(froghat));
		CombinationRegistry.addCombination(new ItemStack(MinestuckItems.cruxiteDowel), new ItemStack(MinestuckItems.rawUranium), MODE_AND, false, false, new ItemStack(MinestuckUniverseItems.chc));
		CombinationRegistry.addCombination(new ItemStack(MinestuckUniverseItems.chc), new ItemStack(Items.POTIONITEM), MODE_AND, false, false, new ItemStack(MinestuckUniverseItems.ctd));
		CombinationRegistry.addCombination(new ItemStack(MinestuckUniverseItems.ctd), new ItemStack(Items.PRISMARINE_SHARD), MODE_AND, false, false, new ItemStack(MinestuckUniverseItems.cht));
		CombinationRegistry.addCombination(new ItemStack(MinestuckUniverseItems.ctd), new ItemStack(Items.PRISMARINE_SHARD), MODE_OR, false, false, new ItemStack(MSUFillerItems.CHS));
		CombinationRegistry.addCombination(new ItemStack(MinestuckItems.sbahjPoster), new ItemStack(Items.COMPASS), MODE_OR, new ItemStack(murica));
		CombinationRegistry.addCombination(new ItemStack(murica), new ItemStack(Blocks.WOOL, 1, EnumDyeColor.RED.getMetadata()), MODE_AND, false, true, new ItemStack(muricaSouth));
		
	}
}
