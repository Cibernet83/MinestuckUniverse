package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAutoWidget extends BlockContainer
{
	protected BlockAutoWidget()
	{
		super(Material.IRON);
		setUnlocalizedName("autoWidget");
		setRegistryName("auto_widget");
		setHarvestLevel("pickaxe", 0);
		setHardness(3.0F);
		setCreativeTab(TabMinestuckUniverse.instance);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return null;
	}
}
