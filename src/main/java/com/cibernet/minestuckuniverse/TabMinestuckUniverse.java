package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TabMinestuckUniverse
{
    public static final CreativeTabs main = new CreativeTabs("minestuckUniverse")
    {
        @Override
        public ItemStack getTabIconItem() {
            return  new ItemStack(MinestuckUniverseBlocks.gristBlockBuild);
        }
    };
    public static final CreativeTabs weapons = new CreativeTabs("minestuckUniverseWeapons")
    {
        @Override
        public String getTabLabel() {
            return MSUConfig.combatOverhaul ? "minestuckWeapons" : super.getTabLabel();
        }

        @Override
        public ItemStack getTabIconItem() {
            return  new ItemStack(MinestuckUniverseItems.batteryBeamBlade);
        }
    };
}
