package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MSUBlockBase extends Block
{
    public MSUBlockBase(Material blockMaterialIn, MapColor blockMapColorIn, String registryName, String unlocalizedName)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setRegistryName(registryName);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(MinestuckUniverse.tab);
    }

    public MSUBlockBase(Material blockMaterialIn, String registryName, String unlocalizedName)
    {
        this(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), registryName, unlocalizedName);
    }
}
