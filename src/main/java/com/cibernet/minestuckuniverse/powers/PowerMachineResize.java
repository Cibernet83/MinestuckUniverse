package com.cibernet.minestuckuniverse.powers;

import com.cibernet.minestuckuniverse.util.SpaceSaltUtils;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PowerMachineResize extends MSUPowerBase
{
	public PowerMachineResize()
	{
		super(EnumClass.WITCH, EnumAspect.SPACE);
	}
	
	@Override
	public boolean use(World worldIn, EntityPlayer playerIn, boolean isNative)
	{
		return false;
	}
	
	@Override
	public boolean useOnBlock(World worldIn, EntityPlayer playerIn, BlockPos pos,float hitX, float hitY, float hitZ, boolean isNative)
	{
		ItemStack heldStack = new ItemStack(MinestuckBlocks.sburbMachine);
		
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		BlockPos mainPos;
		EnumFacing mchnFacing;
		
		if(block instanceof BlockLargeMachine)
		{
			if(block instanceof BlockAlchemiter)
			{
				mainPos = ((BlockAlchemiter)block).getMainPos(state, pos, worldIn);
				TileEntity te = worldIn.getTileEntity(mainPos);
				if(!(te instanceof TileEntityAlchemiter))
					return false;
				else if(((TileEntityAlchemiter)te).isBroken())
					return false;
				mchnFacing = ((TileEntityAlchemiter) te).getFacing();
				
				mainPos = mainPos.down();
				
				if(!worldIn.isRemote)
				{
					for(int x = 0; x < 4; x++)
						for(int z = 0; z < 4; z++)
							worldIn.destroyBlock(mainPos.offset(mchnFacing, x).offset(mchnFacing.rotateY(), z), true);
					for(int y = 0; y < 4; y++)
						worldIn.destroyBlock(mainPos.up(y), true);
					
					pos = new BlockPos(pos.getX(), mainPos.getY(), pos.getZ());
					worldIn.setBlockState(pos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.ALCHEMITER).withProperty(BlockSburbMachine.FACING, mchnFacing.rotateY()));
				}
				
				return true;
			}
			else if(block instanceof BlockTotemLathe)
			{
				mainPos = ((BlockTotemLathe)block).getMainPos(state, pos);
				TileEntity te = worldIn.getTileEntity(mainPos);
				if(!(te instanceof TileEntityTotemLathe))
					return false;
				else if(((TileEntityTotemLathe)te).isBroken())
					return false;
				mchnFacing = ((TileEntityTotemLathe) worldIn.getTileEntity(mainPos)).getFacing();
				if(!worldIn.isRemote)
				{
					TileEntity teLathe = worldIn.getTileEntity(mainPos);
					for(int x = 0; x < 4; x++)
						for(int y = 0; y < 3; y++)
						{
							//TODO lathe dowel not dropping for some reason
							BlockPos pos1 = mainPos.up(y).offset(mchnFacing.rotateYCCW(), x);
							if(x == 2 && y == 1)
								if(!((TileEntityTotemLathe) teLathe).getDowel().isEmpty())
									InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getDowel());
							if(x == 0 && y == 0)
							{
								if(!((TileEntityTotemLathe) teLathe).getCard1().isEmpty())
									InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard1());
								if(!((TileEntityTotemLathe) teLathe).getCard2().isEmpty())
									InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard2());
							}
							if(worldIn.getBlockState(pos1).getBlock() instanceof BlockTotemLathe)
								worldIn.destroyBlock(pos1, true);
						}
					
					pos = new BlockPos(pos.getX(), mainPos.getY(), pos.getZ());
					
					worldIn.setBlockState(pos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.TOTEM_LATHE).withProperty(BlockSburbMachine.FACING, mchnFacing));
				}
				return true;
			}else if(block instanceof BlockPunchDesignix)
			{
				mainPos = ((BlockPunchDesignix)block).getMainPos(state, pos);
				mchnFacing = (EnumFacing)state.getValue(BlockPunchDesignix.DIRECTION);
				
				TileEntity te = worldIn.getTileEntity(mainPos);
				if(!(te instanceof TileEntityPunchDesignix))
					return false;
				else if(((TileEntityPunchDesignix)te).broken)
					return false;
				if(!worldIn.isRemote)
				{
					for(int x = 0; x < 2; x++)
						for(int y = 0; y < 2; y++)
							worldIn.destroyBlock(mainPos.down(y).offset(mchnFacing.rotateYCCW(), x), true);
					
					pos = new BlockPos(pos.getX(), mainPos.getY()-1, pos.getZ());
					
					worldIn.setBlockState(pos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
							BlockSburbMachine.MachineType.PUNCH_DESIGNIX).withProperty(BlockSburbMachine.FACING, mchnFacing));
				}
				return true;
			}else if(block instanceof BlockCruxtruder)
			{
				mchnFacing = worldIn.getBlockState(((BlockCruxtruder)state.getBlock()).getMainPos(state, pos)).getValue(BlockCruxtruder.DIRECTION);
				mainPos = ((BlockCruxtruder)block).getMainPos(state, pos);
				TileEntity te = worldIn.getTileEntity(mainPos);
				if(!(te instanceof TileEntityCruxtruder))
					return false;
				else if(((TileEntityCruxtruder)te).isBroken())
					return false;
				
				if(!worldIn.isRemote)
				{
					for(int x = 0; x < 3; x++)
						for(int z = 0; z < 3; z++)
							for(int y = 1; y < 2; y++)
								worldIn.destroyBlock(mainPos.north(x - 1).east(z - 1).down(y), true);
					
					worldIn.destroyBlock(mainPos, true);
					if(worldIn.getBlockState(mainPos.up()).getBlock() instanceof BlockCruxtiteDowel)
						worldIn.destroyBlock(mainPos.up(), true);
					
					pos = new BlockPos(pos.getX(), mainPos.getY()-1,pos.getZ());
					worldIn.setBlockState(pos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
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
			BlockPos placePos = pos;
			
			switch(type)
			{
				case ALCHEMITER:
					placePos = placePos.offset(mchnFacing.rotateY(), 1).offset(mchnFacing.getOpposite(), 3);
					if (mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F) {
						placePos = placePos.offset(mchnFacing.rotateY());
					}
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
					canPlace = SpaceSaltUtils.canPlaceAlchemiter(heldStack, playerIn, worldIn, placePos, mchnFacing.rotateY(), pos);
					worldIn.setBlockState(pos, state);
					
					if(canPlace)
					{
						worldIn.destroyBlock(pos, true);
						SpaceSaltUtils.placeAlchemiter(worldIn, placePos, mchnFacing.rotateYCCW());
					}
					else return false;
					
					break;
				case CRUXTRUDER:
					placePos = pos.offset(mchnFacing.rotateYCCW());
					canPlace = SpaceSaltUtils.canPlaceCruxtruder(heldStack, playerIn, worldIn, pos.offset(mchnFacing.rotateY()).offset(mchnFacing, 2), mchnFacing, pos);
					
					if(canPlace)
					{
						worldIn.destroyBlock(pos, true);
						SpaceSaltUtils.placeCruxtruder(playerIn, worldIn, placePos, mchnFacing.getOpposite(), false);
					}
					else return false;
					
					break;
				case TOTEM_LATHE:
					placePos = pos.offset(mchnFacing.rotateY());
					
					if(mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
							|| mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
						placePos = placePos.offset(mchnFacing.rotateY());
					canPlace = SpaceSaltUtils.canPlaceTotemLathe(heldStack, playerIn, worldIn, placePos, mchnFacing, pos);
					
					if(canPlace)
					{
						worldIn.destroyBlock(pos, true);
						SpaceSaltUtils.placeTotemLathe(worldIn, placePos, mchnFacing);
					}
					else return false;
					
					break;
				case PUNCH_DESIGNIX:
					
					if (mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F) {
						placePos = pos.offset(mchnFacing.rotateY());
					}
					canPlace = SpaceSaltUtils.canPlacePunchDesignix(heldStack, playerIn, worldIn, placePos, mchnFacing, pos);
					
					
					if(canPlace)
					{
						worldIn.destroyBlock(pos, true);
						SpaceSaltUtils.placePunchDesignix(worldIn, placePos, mchnFacing);
					}
					else return false;
					
					break;
			}
			
			return true;
		}
		
		return false;
	}
}
