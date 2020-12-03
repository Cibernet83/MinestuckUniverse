package com.cibernet.minestuckuniverse.recipes;

import com.cibernet.minestuckuniverse.items.ItemKnittingNeedles;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.CraftingRecipes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class MSUCraftingRecipes
{
    public static class KnittingRecipe extends CraftingRecipes.NonMirroredRecipe {
        public KnittingRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
            super(group, width, height, ingredients, result);
        }

        public boolean matches(InventoryCrafting crafting, World world) {
            for(int i = 0; i < crafting.getSizeInventory(); ++i) {
                ItemStack stack = crafting.getStackInSlot(i);
                if (stack.getItem() instanceof ItemKnittingNeedles && stack.getCount() < 2) {
                    return false;
                }
            }

            return super.matches(crafting, world);
        }

        @Override
        protected boolean checkMatch(InventoryCrafting inv, int x, int y) {
            for(int invX = 0; invX < 3; ++invX) {
                for(int invY = 0; invY < 3; ++invY) {
                    int posX = invX - x;
                    int posY = invY - y;
                    Ingredient ingredient = Ingredient.EMPTY;
                    if (posX >= 0 && posY >= 0 && posX < this.recipeWidth && posY < this.recipeHeight) {
                        ingredient = (Ingredient)this.recipeItems.get(posX + posY * this.recipeWidth);
                    }

                    ItemStack stack = inv.getStackInRowAndColumn(invX, invY).copy();
                    if(stack.isItemStackDamageable()) stack.setItemDamage(0);

                    if (!ingredient.apply(stack)){
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public NonNullList<Ingredient> getIngredients()
        {
            NonNullList<Ingredient> result = super.getIngredients();

            for(Ingredient ingredient : result)
                for(int i = 0; i < ingredient.getMatchingStacks().length; i++)
                {
                    ItemStack stack = ingredient.getMatchingStacks()[i];
                    if(stack.getItem() instanceof ItemKnittingNeedles)
                    {
                        stack.setCount(2);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setBoolean("JEIDisplay", true);
                        stack.setTagCompound(nbt);
                    }
                }

            return result;
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i).copy();

                if(itemstack.getItem() instanceof ItemKnittingNeedles)
                {
                    itemstack.setItemDamage(itemstack.getItemDamage()+1);
                    if (itemstack.getMetadata() > itemstack.getMaxDamage())
                    {
                        ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), itemstack, null);
                        itemstack = ItemStack.EMPTY;
                    }

                    inv.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                else itemstack = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);

                nonnulllist.set(i, itemstack);
            }

            return nonnulllist;
        }

        public static class Factory extends CraftingRecipes.ShapedFactory {
            public Factory() {
            }

            public IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
                return new KnittingRecipe(group, width, height, ingredients, result);
            }
        }
    }
}
