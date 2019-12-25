package com.cibernet.minestuckuniverse.util;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpaceSaltUtils
{
	
	public static boolean canPlacePunchDesignix(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, BlockPos machinePos)
	{
		for(int x = 0; x < 2; ++x)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack) && !machinePos.equals(pos.offset(facing.rotateYCCW(), x)))
				return false;
			
			
			for(int y = 0; y < 2; ++y)
			{
				if (!world.mayPlace(MinestuckBlocks.punchDesignix, pos.offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, (Entity)null)
						&& !machinePos.equals(pos.offset(facing.rotateYCCW(), x).up(y)))
					return false;
				
			}
		}
		
		return true;
	}
	
	public static boolean canPlaceTotemLathe(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, BlockPos machinePos)
	{
		for (int x = 0; x < 4; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 3; y++)
			{
				if (!world.mayPlace(MinestuckBlocks.totemlathe[0], pos.offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null) && !pos.offset(facing.rotateYCCW(), x).up(y).equals(machinePos))
					return false;
			}
		}
		return true;
	}
	
	public static boolean canPlaceCruxtruder(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, BlockPos machinePos) {
		for(int x = 0; x < 3; ++x) {
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack)) {
				return false;
			}
			
			for(int y = 0; y < 3; ++y) {
				for(int z = 0; z < 3; ++z) {
					if (!world.mayPlace(MinestuckBlocks.cruxtruder, pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, (Entity)null)
						&& !pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y).equals(machinePos)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean canPlaceAlchemiter(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, BlockPos machinePos) {
		for(int x = 0; x < 4; ++x) {
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack)) {
				return false;
			}
			
			for(int y = 0; y < 4; ++y) {
				for(int z = 0; z < 4; ++z) {
					if (!world.mayPlace(MinestuckBlocks.alchemiter[0], pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, (Entity)null)
						&& !pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y).equals(machinePos)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean placeAlchemiter(World world, BlockPos pos, EnumFacing side)
	{
		EnumFacing facing = side;
		if (!world.isRemote)
		{
			world.setBlockState(pos.up(0), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.TOTEM_CORNER, facing));
			world.setBlockState(pos.up(1), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.TOTEM_PAD, facing));
			world.setBlockState(pos.up(2), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.LOWER_ROD, facing));
			world.setBlockState(pos.up(3), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.UPPER_ROD, facing));
			world.setBlockState(pos.offset(facing, 0).offset(facing.rotateY(), 1), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_LEFT, facing));
			world.setBlockState(pos.offset(facing, 0).offset(facing.rotateY(), 2), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_RIGHT, facing));
			world.setBlockState(pos.offset(facing, 0).offset(facing.rotateY(), 3), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CORNER, facing));
			world.setBlockState(pos.offset(facing, 1).offset(facing.rotateY(), 1), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CENTER_PAD, facing));
			world.setBlockState(pos.offset(facing, 1).offset(facing.rotateY(), 0), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_RIGHT, facing.rotateY()));
			world.setBlockState(pos.offset(facing, 1).offset(facing.rotateY(), 2), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CENTER_PAD, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing, 1).offset(facing.rotateY(), 3), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_LEFT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 0), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_LEFT, facing.rotateY()));
			world.setBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 1), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CENTER_PAD, facing.rotateY()));
			world.setBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 2), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CENTER_PAD, facing.getOpposite()));
			world.setBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 3), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_RIGHT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 0), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CORNER, facing.getOpposite()));
			world.setBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 1), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_RIGHT, facing.getOpposite()));
			world.setBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 2), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.SIDE_LEFT, facing.getOpposite()));
			world.setBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 3), BlockAlchemiter.getBlockState(BlockAlchemiter.EnumParts.CORNER, facing.rotateYCCW()));
		}
		return true;
	}
	
	public static boolean placeTotemLathe(World world, BlockPos pos, EnumFacing side) {
		EnumFacing facing = side;
		if (!world.isRemote) {
			world.setBlockState(pos, BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 1), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDLEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 2), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDRIGHT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 3), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_RIGHT, facing));
			world.setBlockState(pos.up(1), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(1), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(1), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_RIGHT, facing));
			world.setBlockState(pos.up(2), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(2), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDLEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDRIGHT, facing));
		}
		
		return true;
	}
	
	public static boolean placePunchDesignix(World world, BlockPos pos, EnumFacing side) {
		EnumFacing facing = side;
		IBlockState newState = MinestuckBlocks.punchDesignix.getDefaultState().withProperty(BlockPunchDesignix.DIRECTION, facing);
		world.setBlockState(pos, newState, 11);
		world.setBlockState(pos.offset(facing.rotateYCCW()), newState.withProperty(BlockPunchDesignix.PART, BlockPunchDesignix.EnumParts.BOTTOM_RIGHT), 11);
		world.setBlockState(pos.up().offset(facing.rotateYCCW()), newState.withProperty(BlockPunchDesignix.PART, BlockPunchDesignix.EnumParts.TOP_RIGHT), 11);
		world.setBlockState(pos.up(), newState.withProperty(BlockPunchDesignix.PART, BlockPunchDesignix.EnumParts.TOP_LEFT), 11);
		
		return true;
	}
	
	public static boolean placeCruxtruder(EntityPlayer player, World world, BlockPos pos, EnumFacing side, boolean withLid) {
		if (!world.isRemote) {
			EnumFacing facing = side;
			switch(facing) {
				case EAST:
					pos = pos.north(2).west(2);
					break;
				case NORTH:
					pos = pos.west(2);
					break;
				case SOUTH:
					pos = pos.north(2);
				case WEST:
			}
			
			IBlockState newState = MinestuckBlocks.cruxtruder.getDefaultState();
			
			world.setBlockState(pos.south(0).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.EAST));
			world.setBlockState(pos.south(1).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.EAST));
			world.setBlockState(pos.south(2).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.CENTER).withProperty(BlockCruxtruder.DIRECTION, facing));
			world.setBlockState(pos.south(1).up(1).east(1), newState.withProperty(BlockCruxtruder.PART, BlockCruxtruder.EnumParts.TUBE).withProperty(BlockCruxtruder.DIRECTION, facing));
			if(withLid) world.setBlockState(pos.south().up(2).east(), MinestuckBlocks.cruxtruderLid.getDefaultState());
			TileEntity te = world.getTileEntity(pos.add(1, 1, 1));
			if (te instanceof TileEntityCruxtruder) {
				EditData editData = ServerEditHandler.getData(player);
				int color;
				if (editData != null) {
					color = MinestuckPlayerData.getData(editData.getTarget()).color;
				} else {
					color = MinestuckPlayerData.getData(player).color;
				}
				
				((TileEntityCruxtruder)te).setColor(color);
			} else {
				Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", new Object[]{te});
			}
			
		}
		
		return true;
	}
}
