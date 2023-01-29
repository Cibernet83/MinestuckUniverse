package com.cibernet.minestuckuniverse.modSupport.crafttweaker;

import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("minestuckuniverse.Assemblificator")
@ZenRegister
public class CTAPIAssembly
{
	protected static List<IAction> actions = new ArrayList<>();

	public CTAPIAssembly() {}

	@ZenMethod
	public static void addRecipeForBlock(IBlock output, IItemStack[] input)
	{
		actions.add(new AssemblyRecipe(output, input));
	}

	@ZenMethod
	public static void addRecipeForItem(IItemStack output, IItemStack[] input)
	{
		actions.add(new AssemblyRecipe(output, input));
	}

	private static class AssemblyRecipe implements IAction
	{

		final Object output;
		final IItemStack[] input;

		AssemblyRecipe(Object output, IItemStack[] input)
		{
			this.output = output;
			this.input = input;
		}

		@Override
		public void apply()
		{
			ItemStack[] in = new ItemStack[input.length];

			for(int i = 0; i < in.length; i++)
			{
				in[i] = (ItemStack) input[i].getInternal();
				if(in[i] == null)
					in[i] = ItemStack.EMPTY;
			}

			if(output != null)
			{
				if(output instanceof IBlock)
					MachineChasisRecipes.addRecipe(Block.REGISTRY.getObject(new ResourceLocation(((IBlock)output).getDefinition().getId())), in);
				else if(output instanceof IItemStack)
					MachineChasisRecipes.addRecipe((ItemStack)((IItemStack)output).getInternal(), in);
			}
			else MachineChasisRecipes.getRecipes().remove(MachineChasisRecipes.toKey(in));
		}

		@Override
		public String describe()
		{
			ItemStack[] in = new ItemStack[input.length];

			for(int i = 0; i < in.length; i++)
				in[i] = (ItemStack) input[i].getInternal();

			return (output == null ? " Adding" : " Removing") + " recipe with key " + MachineChasisRecipes.toKey(in);
		}
	}
}
