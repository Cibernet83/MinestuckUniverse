package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockPillar extends BlockRotatedPillar implements IRegistryItem
{
    private final String name;

    protected BlockPillar(MapColor color, String unlocName, String name)
    {
        super(Material.ROCK, color);

        this.name = name;
        setUnlocalizedName(unlocName);

        setHarvestLevel("pickaxe", 3);
        setHardness(20.0F);
        setResistance(2000.0F);
        setCreativeTab(TabMinestuckUniverse.main);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(name);
    }
}
