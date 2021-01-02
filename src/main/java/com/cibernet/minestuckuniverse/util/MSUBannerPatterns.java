package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class MSUBannerPatterns
{

    public static void init()
    {
        if(MinestuckUniverse.isMSGTLoaded)
        {
            for(EnumAspect aspect : EnumAspect.values())
                registerPattern(aspect.toString(), new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("minestuckgodtier", "hero_stone_shard_"+aspect.toString()))));
        }
        else
        {
            registerPattern("light", new ItemStack(Items.GLOWSTONE_DUST));
            registerPattern("void", new ItemStack(Items.COAL));
            registerPattern("space", new ItemStack(Items.COMPASS));
            registerPattern("time", new ItemStack(Items.CLOCK));
            registerPattern("mind", new ItemStack(Items.BOOK));
            registerPattern("heart", new ItemStack(Items.ENDER_PEARL));
            registerPattern("doom", new ItemStack(Items.FLINT_AND_STEEL));
            registerPattern("life", new ItemStack(Items.WHEAT));
            registerPattern("breath", new ItemStack(Blocks.ICE));
            registerPattern("blood", new ItemStack(Items.REDSTONE));
            registerPattern("rage", new ItemStack(Items.ROTTEN_FLESH));
            registerPattern("hope", new ItemStack(Items.GHAST_TEAR));
        }

        registerPattern("moon", new ItemStack(MinestuckUniverseItems.moonstone));
    }

    public static BannerPattern registerPattern(String id, ItemStack ingredient)
    {
        Class<?>[] paramTypes = new Class[] {String.class, String.class, ItemStack.class};
        Object[] paramVals = new Object[] {MinestuckUniverse.SHORT.toLowerCase() + "_" + id, MinestuckUniverse.SHORT.toLowerCase() + "." + id, ingredient};
        return EnumHelper.addEnum(BannerPattern.class, id.toUpperCase(), paramTypes, paramVals);
    }
}
