package com.cibernet.minestuckuniverse.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OperandiGlassBlock extends OperandiBlock
{
	public OperandiGlassBlock(String name, float hardness, float resistance, Material blockMaterialIn, String toolClass)
	{
		super(name, hardness, resistance, blockMaterialIn, toolClass);
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isTranslucent(IBlockState state)
	{
		return true;
	}
}
