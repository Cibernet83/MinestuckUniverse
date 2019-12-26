package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TabMinestuckUniverse extends CreativeTabs
{
    public static final TabMinestuckUniverse instance = new TabMinestuckUniverse("tabMinestuckUniverse");
    public static TabMinestuckUniverse fillerItems;
    //public static final TabMinestuckUniverse GTArmor = new TabMinestuckUniverse("tabMSUGTArmor", new ItemStack(MinestuckUniverseItems.spaceSalt));
    
    public TabMinestuckUniverse(String label)
    {
        super(label);
    }
    
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(MinestuckUniverseItems.moonstone, 1, 0);
    }
}
