package com.cibernet.minestuckuniverse.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockLunarStone extends MSUBlockBase
{
    public BlockLunarStone(MapColor blockMapColorIn, String unlocName, String name)
    {
        super(Material.ROCK, blockMapColorIn, name, unlocName);

        setHarvestLevel("pickaxe", 3);
        setHardness(20.0F);
        setResistance(2000.0F);

        setLightLevel(8/15f);
    }
}
