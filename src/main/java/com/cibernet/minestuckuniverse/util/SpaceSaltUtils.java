package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.events.SpaceSaltEffectEvent;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class SpaceSaltUtils
{


	public static boolean onSpaceSaltUse(World world, @Nullable EntityPlayer player, EnumHand hand, BlockPos targetPos, EnumFacing blockFace, float hitX, float hitY, float hitZ)
	{
		return onSpaceSaltUse(world, player, hand, targetPos, blockFace, new RayTraceResult(new Vec3d(targetPos.getX()+hitX, targetPos.getY()+hitY, targetPos.getZ()+hitZ), blockFace, targetPos));
	}

	public static boolean onSpaceSaltUse(World world, @Nullable EntityPlayer player, EnumHand hand, BlockPos targetPos, EnumFacing blockFace, RayTraceResult raytrace)
	{
		if(!MinecraftForge.EVENT_BUS.post(new SpaceSaltEffectEvent(world, player, targetPos, blockFace, raytrace)))
		{
			float hitX = (float)(raytrace.hitVec.x - (double)targetPos.getX());
			float hitY = (float)(raytrace.hitVec.y - (double)targetPos.getY());
			float hitZ = (float)(raytrace.hitVec.z - (double)targetPos.getZ());

			return resizeMachine(world, targetPos, player, hand, hitX, hitY, hitZ);
		}

		return true;
	}

	public static boolean resizeMachine(World world, BlockPos targetPos, @Nullable EntityPlayer player, EnumHand hand, float hitX, float hitY, float hitZ)
	{

		IBlockState state = world.getBlockState(targetPos);
		Block block = state.getBlock();
		BlockPos mainPos;
		EnumFacing mchnFacing;

		ItemStack heldStack = player == null ? ItemStack.EMPTY : player.getHeldItem(hand);

		if(block instanceof BlockLargeMachine)
		{
			if(block instanceof BlockAlchemiter)
			{
				mainPos = ((BlockAlchemiter)block).getMainPos(state, targetPos, world);
				TileEntity te = world.getTileEntity(mainPos);
				if(!(te instanceof TileEntityAlchemiter))
					return false;
				else if(((TileEntityAlchemiter)te).isBroken())
					return false;
				mchnFacing = ((TileEntityAlchemiter) te).getFacing();

				mainPos = mainPos.down();

				if(!world.isRemote)
				{
					for(int x = 0; x < 4; x++)
						for(int z = 0; z < 4; z++)
							world.destroyBlock(mainPos.offset(mchnFacing, x).offset(mchnFacing.rotateY(), z), true);
					for(int y = 0; y < 4; y++)
						world.destroyBlock(mainPos.up(y), true);

					targetPos = new BlockPos(targetPos.getX(), mainPos.getY(), targetPos.getZ());
					world.setBlockState(targetPos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.ALCHEMITER).withProperty(BlockSburbMachine.FACING, mchnFacing.rotateY()));
				}

				return true;
			}
			else if(block instanceof BlockTotemLathe)
			{
				mainPos = ((BlockTotemLathe)block).getMainPos(state, targetPos);
				TileEntity te = world.getTileEntity(mainPos);
				if(!(te instanceof TileEntityTotemLathe))
					return false;
				else if(((TileEntityTotemLathe)te).isBroken())
					return false;
				mchnFacing = ((TileEntityTotemLathe) world.getTileEntity(mainPos)).getFacing();
				if(!world.isRemote)
				{
					TileEntity teLathe = world.getTileEntity(mainPos);
					for(int x = 0; x < 4; x++)
						for(int y = 0; y < 3; y++)
						{
							//TODO lathe dowel not dropping for some reason
							BlockPos pos1 = mainPos.up(y).offset(mchnFacing.rotateYCCW(), x);
							if(x == 2 && y == 1)
								if(!((TileEntityTotemLathe) teLathe).getDowel().isEmpty())
									InventoryHelper.spawnItemStack(world, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getDowel());
							if(x == 0 && y == 0)
							{
								if(!((TileEntityTotemLathe) teLathe).getCard1().isEmpty())
									InventoryHelper.spawnItemStack(world, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard1());
								if(!((TileEntityTotemLathe) teLathe).getCard2().isEmpty())
									InventoryHelper.spawnItemStack(world, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard2());
							}
							if(world.getBlockState(pos1).getBlock() instanceof BlockTotemLathe)
								world.destroyBlock(pos1, true);
						}

					targetPos = new BlockPos(targetPos.getX(), mainPos.getY(), targetPos.getZ());

					world.setBlockState(targetPos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.TOTEM_LATHE).withProperty(BlockSburbMachine.FACING, mchnFacing));
				}
				return true;
			}else if(block instanceof BlockPunchDesignix)
			{
				mainPos = ((BlockPunchDesignix)block).getMainPos(state, targetPos);
				mchnFacing = (EnumFacing)state.getValue(BlockPunchDesignix.DIRECTION);

				TileEntity te = world.getTileEntity(mainPos);
				if(!(te instanceof TileEntityPunchDesignix))
					return false;
				else if(((TileEntityPunchDesignix)te).broken)
					return false;
				if(!world.isRemote)
				{
					for(int x = 0; x < 2; x++)
						for(int y = 0; y < 2; y++)
							world.destroyBlock(mainPos.down(y).offset(mchnFacing.rotateYCCW(), x), true);

					targetPos = new BlockPos(targetPos.getX(), mainPos.getY()-1, targetPos.getZ());

					world.setBlockState(targetPos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.PUNCH_DESIGNIX).withProperty(BlockSburbMachine.FACING, mchnFacing));
				}
				return true;
			}else if(block instanceof BlockCruxtruder)
			{
				mchnFacing = world.getBlockState(((BlockCruxtruder)state.getBlock()).getMainPos(state, targetPos)).getValue(BlockCruxtruder.DIRECTION);
				mainPos = ((BlockCruxtruder)block).getMainPos(state, targetPos);
				TileEntity te = world.getTileEntity(mainPos);
				if(!(te instanceof TileEntityCruxtruder))
					return false;
				else if(((TileEntityCruxtruder)te).isBroken())
					return false;

				if(!world.isRemote)
				{
					for(int x = 0; x < 3; x++)
						for(int z = 0; z < 3; z++)
							for(int y = 1; y < 2; y++)
								world.destroyBlock(mainPos.north(x - 1).east(z - 1).down(y), true);

					world.destroyBlock(mainPos, true);
					if(world.getBlockState(mainPos.up()).getBlock() instanceof BlockCruxtiteDowel)
						world.destroyBlock(mainPos.up(), true);

					targetPos = new BlockPos(targetPos.getX(), mainPos.getY()-1,targetPos.getZ());
					world.setBlockState(targetPos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.CRUXTRUDER).withProperty(BlockSburbMachine.FACING, mchnFacing.getOpposite()));
				}
				return true;
			}
		}
		else if(block instanceof BlockSburbMachine)
		{
			BlockSburbMachine.MachineType type = state.getValue(BlockSburbMachine.MACHINE_TYPE);
			mchnFacing = state.getValue(BlockSburbMachine.FACING);
			boolean canPlace;
			BlockPos placePos = targetPos;

			switch(type)
			{
				case ALCHEMITER:
					placePos = placePos.offset(mchnFacing.rotateY(), 1).offset(mchnFacing.getOpposite(), 3);
					if (mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F) {
						placePos = placePos.offset(mchnFacing.rotateY());
					}
					world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
					canPlace = player == null || SpaceSaltUtils.canPlaceAlchemiter(heldStack, player, world, placePos, mchnFacing.rotateY(), targetPos);
					world.setBlockState(targetPos, state);

					if(canPlace)
					{
						world.destroyBlock(targetPos, true);
						SpaceSaltUtils.placeAlchemiter(world, placePos, mchnFacing.rotateYCCW());
					}
					else return false;

					break;
				case CRUXTRUDER:
					placePos = targetPos.offset(mchnFacing.rotateYCCW());
					canPlace = player == null || SpaceSaltUtils.canPlaceCruxtruder(heldStack, player, world, targetPos.offset(mchnFacing.rotateY()).offset(mchnFacing, 2), mchnFacing, targetPos);

					if(canPlace)
					{
						world.destroyBlock(targetPos, true);
						SpaceSaltUtils.placeCruxtruder(player, world, placePos, mchnFacing.getOpposite(), false);
					}
					else return false;

					break;
				case TOTEM_LATHE:
					placePos = targetPos.offset(mchnFacing.rotateY());

					if(mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
							|| mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
						placePos = placePos.offset(mchnFacing.rotateY());
					canPlace = player == null || SpaceSaltUtils.canPlaceTotemLathe(heldStack, player, world, placePos, mchnFacing, targetPos);

					if(canPlace)
					{
						world.destroyBlock(targetPos, true);
						SpaceSaltUtils.placeTotemLathe(world, placePos, mchnFacing);
					}
					else return false;

					break;
				case PUNCH_DESIGNIX:

					if (mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F) {
						placePos = targetPos.offset(mchnFacing.rotateY());
					}
					canPlace = player == null || SpaceSaltUtils.canPlacePunchDesignix(heldStack, player, world, placePos, mchnFacing, targetPos);


					if(canPlace)
					{
						world.destroyBlock(targetPos, true);
						SpaceSaltUtils.placePunchDesignix(world, placePos, mchnFacing);
					}
					else return false;

					break;
			}

			return true;
		}

		return false;
	}

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
