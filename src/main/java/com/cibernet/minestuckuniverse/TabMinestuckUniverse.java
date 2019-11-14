package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMinestuckUniverse extends CreativeTabs
{
    private ItemStack iconItem;
    public static final TabMinestuckUniverse instance = new TabMinestuckUniverse("tabMinestuckUniverse", new ItemStack(MinestuckUniverseItems.moonstone));
    public static final TabMinestuckUniverse GTArmor = new TabMinestuckUniverse("tabMSUGTArmor", new ItemStack(MinestuckUniverseItems.spaceSalt));

    private TabMinestuckUniverse(String label, ItemStack iconItem)
    {
        super(label);
        this.iconItem = iconItem;
    }

    @Override
    public ItemStack getTabIconItem() {
        return iconItem;
    }
}
