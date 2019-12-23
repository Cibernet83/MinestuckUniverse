package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MSUUtils
{
    public static final int MACHINE_CHASIS_GUI = 0;
    public static final int AUTO_CAPTCHA_GUI = 1;
    
    public static void registerDeployList()
    {
        DeployList.registerItem("holopad", new ItemStack(MinestuckUniverseBlocks.holopad), new GristSet(GristType.Build, MinestuckUniverse.isArsenalLoaded ? 10000 : 1000), 2);
    }
    
    public static boolean compareCards(ItemStack card1, ItemStack card2, boolean ignoreStacksize)
    {
        ItemStack stack1 = AlchemyRecipes.getDecodedItem(card1);
        ItemStack stack2 = AlchemyRecipes.getDecodedItem(card2);
        if(!card1.isItemEqual(card2))
            return false;
        if(!card1.hasTagCompound() || !card2.hasTagCompound())
            return true;
        if(card1.getTagCompound().getBoolean("punched") != card2.getTagCompound().getBoolean("punched"))
            return false;
        if(!ignoreStacksize && stack1.getCount() != stack2.getCount())
            return false;
        if(stack1.hasTagCompound() != stack2.hasTagCompound())
            return false;
        if(stack1.hasTagCompound() && stack2.hasTagCompound())
            return stack1.getTagCompound().equals(stack2.getTagCompound());
        else return true;
    }
}
