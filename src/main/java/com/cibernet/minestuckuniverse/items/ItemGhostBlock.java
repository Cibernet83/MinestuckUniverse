package com.cibernet.minestuckuniverse.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.TreeMap;

public class ItemGhostBlock extends MSUItemBase
{
    private final Block block;

    public static final TreeMap<BlockEntry, Item> ghostItems = new TreeMap<>();

    public ItemGhostBlock(String regName, Block block)
    {
        super(regName);
        this.block = block;
        setUnlocalizedName(regName);
        ghostItems.put(new BlockEntry(block), this);
        setCreativeTab(null);
    }

    public static Item get(Block block)
    {
        return ghostItems.get(new BlockEntry(block));
    }

    public static boolean containsKey(Block block)
    {
        return ghostItems.containsKey(new BlockEntry(block));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return block.getUnlocalizedName().equals("tile.null") ? super.getUnlocalizedName(stack) : this.block.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName()
    {
        return block.getUnlocalizedName().equals("tile.null") ? super.getUnlocalizedName() : this.block.getUnlocalizedName();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {}

    static class BlockEntry implements Comparable<BlockEntry>
    {
        final Block block;

        BlockEntry(Block block)
        {
            this.block = block;
        }

        @Override
        public int compareTo(BlockEntry o)
        {
            return block.hashCode() - o.block.hashCode();
        }
    }
}
