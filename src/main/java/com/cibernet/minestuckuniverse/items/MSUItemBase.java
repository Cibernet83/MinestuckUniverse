package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.item.Item;

public class MSUItemBase extends Item
{
    public MSUItemBase(String name, String unlocName)
    {
        this.setRegistryName(name);
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(MinestuckUniverse.tab);
    }

    public MSUItemBase(String name)
    {
        this(name, name);
    }
}
