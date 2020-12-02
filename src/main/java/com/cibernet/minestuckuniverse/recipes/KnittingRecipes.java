package com.cibernet.minestuckuniverse.recipes;

import com.cibernet.minestuckuniverse.items.ItemKnittingNeedles;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShulkerBoxRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class KnittingRecipes extends ShapedRecipes
{
    public KnittingRecipes(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(group, width, height, ingredients, result);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        boolean hasNeedles = false;

        for (int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j)
            {
                ItemStack stack = inv.getStackInRowAndColumn(i, j);

                if(!hasNeedles && this.checkEmpty(inv, i, j, true) && stack.getItem() instanceof ItemKnittingNeedles && stack.getCount() >= 2)
                    hasNeedles = true;

            }
        }
        return hasNeedles && super.matches(inv, worldIn);
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkEmpty(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
    {
        for (int i = 0; i < p_77573_1_.getWidth(); ++i)
        {
            for (int j = 0; j < p_77573_1_.getHeight(); ++j)
            {
                int k = i - p_77573_2_;
                int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
                {
                    if (p_77573_4_)
                    {
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    }
                    else
                    {
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                    }
                }

                if (ingredient.getMatchingStacks().length != 0)
                {
                    return false;
                }
            }
        }

        return true;
    }

}
