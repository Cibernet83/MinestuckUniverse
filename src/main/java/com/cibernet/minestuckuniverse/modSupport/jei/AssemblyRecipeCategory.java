package com.cibernet.minestuckuniverse.modSupport.jei;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AssemblyRecipeCategory implements IRecipeCategory<AssemblyRecipeWrapper>
{
	private IDrawable background;
	
	public AssemblyRecipeCategory(IGuiHelper helper)
	{
		background = helper.createDrawable(new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/machine_chassis.png"), 0, 166, 126, 68);
	}
	
	@Override
	public String getUid()
	{
		return MinestuckUniverse.MODID + ".assembly";
	}
	
	@Override
	public String getTitle()
	{
		return I18n.format("container.machineChassis");
	}
	
	@Override
	public String getModName()
	{
		return MinestuckUniverse.NAME;
	}
	
	@Override
	public IDrawable getBackground()
	{
		return background;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AssemblyRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		stackGroup.init(0, true, 23, 25);
		stackGroup.init(1, true, 23, 0);
		stackGroup.init(2, true, 46, 25);
		stackGroup.init(3, true, 23, 50);
		stackGroup.init(4, true, 0, 25);
		stackGroup.init(5, false, 104, 25);
		
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		for(int i = 0; i < inputs.size(); i++)
			stackGroup.set(i, inputs.get(i));
		
		List<ItemStack> output = new ArrayList<>(ingredients.getOutputs(ItemStack.class).get(0));
		stackGroup.set(5, output);
	}
}
