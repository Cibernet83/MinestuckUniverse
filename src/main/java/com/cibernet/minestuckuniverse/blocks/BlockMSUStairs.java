package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.items.IRegistryItem;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockMSUStairs extends BlockStairs implements IRegistryItem
{
	final String registryName;

	public BlockMSUStairs(IBlockState modelState, String name, String registryName)
	{
		super(modelState);
		setUnlocalizedName(name);
		this.registryName = registryName;
	}

	@Override
	public void setRegistryName() {
		setRegistryName(registryName);
	}
}
