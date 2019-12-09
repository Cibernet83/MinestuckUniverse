package com.cibernet.minestuckuniverse.recipes;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import java.util.TreeMap;

public class MachineChasisRecipes
{
    protected static TreeMap<String, Block> recipes = new TreeMap<>();
    protected static int inputLimit = 5;

    public static void registerRecipes()
    {
        addRecipe(gristHopper, new ItemStack(Blocks.HOPPER), new ItemStack(gristBlockBuild),
                new ItemStack(gristBlockUranium), new ItemStack(MinestuckBlocks.blockComputerOff), new ItemStack(gristBlockUranium));
    }

    public static boolean addRecipe(Block output, ItemStack... input)
    {
        String key = toKey(input);
        if(recipes.containsKey(key) || key.isEmpty() || key == null)
            return false;
        recipes.put(key, output);
        return true;
    }

    public static Block getOutput(ItemStack... input)
    {
        String key = toKey(input);
        if(recipes.containsKey(key))
            return recipes.get(key);
        return Blocks.AIR;
    }

    public static boolean recipeExists(ItemStack... input)
    {
        return recipes.containsKey(toKey(input));
    }

    public static String toKey(ItemStack... input)
    {
        String key = "";
        for(int i = 0; i < inputLimit; i++) {
            if (i >= input.length)
                key += ItemStack.EMPTY.toString();
            else {
                input[i].setCount(1);
                key += input[i].toString() + ";";
            }
        }
        return key;
    }
}
