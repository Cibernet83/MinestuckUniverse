package com.cibernet.minestuckuniverse.blocks;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGoldTransportalizer extends BlockCustomTransportalizer
{
	public BlockGoldTransportalizer()
	{
		super(MapColor.GOLD);
		setUnlocalizedName("goldenTransportalizer");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityTransportalizer();
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if(entity instanceof EntityPlayer)
			super.onEntityCollidedWithBlock(world, pos, state, entity);
	}

	@Override
	public void setRegistryName() {
		setRegistryName("golden_transportalizer");
	}
}
