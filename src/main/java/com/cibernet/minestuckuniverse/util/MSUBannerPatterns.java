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

        for(EnumAspect aspect : EnumAspect.values())
            registerPattern(aspect.toString(), new ItemStack(MinestuckUniverseItems.heroStoneShards.get(aspect)));

        registerPattern("moon", new ItemStack(MinestuckUniverseItems.moonstone));
    }

    public static BannerPattern registerPattern(String id, ItemStack ingredient)
    {
        Class<?>[] paramTypes = new Class[] {String.class, String.class, ItemStack.class};
        Object[] paramVals = new Object[] {MinestuckUniverse.SHORT.toLowerCase() + "_" + id, MinestuckUniverse.SHORT.toLowerCase() + "." + id, ingredient};
        return EnumHelper.addEnum(BannerPattern.class, id.toUpperCase(), paramTypes, paramVals);
    }
}
