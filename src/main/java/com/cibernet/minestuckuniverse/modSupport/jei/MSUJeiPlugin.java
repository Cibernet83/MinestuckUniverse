package com.cibernet.minestuckuniverse.modSupport.jei;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;

@JEIPlugin
public class MSUJeiPlugin implements IModPlugin
{
	AssemblyRecipeCategory assemblyRecipeCategory;
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		assemblyRecipeCategory = new AssemblyRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(assemblyRecipeCategory);
	}
	
	@Override
	public void register(IModRegistry registry)
	{
		ArrayList<AssemblyRecipeWrapper> assemblyRecipes = new ArrayList<>();
		for(Map.Entry<String, Block> entry : MachineChasisRecipes.getRecipes().entrySet())
			assemblyRecipes.add(new AssemblyRecipeWrapper(MachineChasisRecipes.getIngredientList(entry.getKey()), new ItemStack(entry.getValue())));
		
		registry.addRecipes(assemblyRecipes, assemblyRecipeCategory.getUid());
		registry.addRecipeCatalyst(new ItemStack(MinestuckUniverseBlocks.machineChasis), assemblyRecipeCategory.getUid());
	}
}
