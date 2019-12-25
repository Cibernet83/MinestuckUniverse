package com.cibernet.minestuckuniverse.modSupport.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AssemblyRecipeWrapper implements IRecipeWrapper
{
	private final List<List<ItemStack>> inputs;
	private final ItemStack result;
	
	public AssemblyRecipeWrapper(List<List<ItemStack>> inputs, ItemStack result)
	{
		this.inputs = inputs;
		this.result = result;
	}
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, this.inputs);
		ingredients.setOutput(ItemStack.class, this.result);
	}
}
