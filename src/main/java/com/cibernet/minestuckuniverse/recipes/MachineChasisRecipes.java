package com.cibernet.minestuckuniverse.recipes;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

public class MachineChasisRecipes
{
    protected static Hashtable<String, Block> recipes = new Hashtable<>();
    protected static int inputLimit = 5;

    public static void registerRecipes()
    {
        addRecipe(gristHopper, new ItemStack(Blocks.HOPPER), new ItemStack(gristBlockBuild),
                new ItemStack(gristBlockUranium), new ItemStack(MinestuckBlocks.blockComputerOff), new ItemStack(gristBlockUranium));
        addRecipe(autoWidget, new ItemStack(MinestuckBlocks.crockerMachine), new ItemStack(MinestuckItems.boondollars),
                new ItemStack(MinestuckItems.boondollars), new ItemStack(MinestuckItems.energyCore), new ItemStack(zillystoneShard));
        addRecipe(autoCaptcha, new ItemStack(Blocks.DISPENSER), new ItemStack(MinestuckItems.captchaCard),
                new ItemStack(moonstone), ItemStack.EMPTY, new ItemStack(MinestuckItems.energyCore));
        addRecipe(porkhollowAtm, new ItemStack(MinestuckBlocks.blockComputerOff), new ItemStack(MinestuckItems.boondollars),
                new ItemStack(MinestuckItems.boondollars), new ItemStack(Items.PORKCHOP), new ItemStack(MinestuckItems.boondollars));
        addRecipe(boondollarRegister, new ItemStack(porkhollowAtm), new ItemStack(Blocks.HOPPER), new ItemStack(Items.COMPARATOR),
                new ItemStack(Blocks.CHEST), new ItemStack(Items.REDSTONE));
    }
    
    public static Hashtable<String, Block> getRecipes() {return recipes;}
    
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
                key += "0x" + ItemStack.EMPTY.getItem().getRegistryName() + "@0" + ";";
            else {
                input[i].setCount(1);
                key += input[i].getCount() + "x" + input[i].getItem().getRegistryName() + "@" + input[i].getMetadata() + ";";
            }
        }
        return key;
    }
    
    public static List<List<ItemStack>> getIngredientList(String key)
    {
        key = key.substring(0,key.length()-1);
        List<List<ItemStack>> out = new ArrayList<>();
        String[] list = key.split(";",inputLimit);
        for(String item : list)
        {
            int count = Integer.parseInt(item.substring(0, item.indexOf("x")));
            String regName = item.substring(item.indexOf("x")+1, item.indexOf("@"));
            int meta = Integer.parseInt(item.substring(item.indexOf("@")+1));
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(regName)), count, meta);
            out.add(new ArrayList<ItemStack>(){{add(stack);}});
        }
        return out;
    }
    
}
