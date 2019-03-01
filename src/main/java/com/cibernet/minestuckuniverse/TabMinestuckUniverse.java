package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMinestuckUniverse extends CreativeTabs
{
    private ItemStack iconItem = new ItemStack(MinestuckUniverseBlocks.magicBlock);
    public static final TabMinestuckUniverse instance = new TabMinestuckUniverse("tabMinestuckUniverse", new ItemStack(MinestuckUniverseBlocks.magicBlock));

    private TabMinestuckUniverse(String label, ItemStack iconItem)
    {
        super(label);
        this.iconItem = iconItem;
    }

    @Override
    public ItemStack getTabIconItem() {
        return this.iconItem;
    }
}
