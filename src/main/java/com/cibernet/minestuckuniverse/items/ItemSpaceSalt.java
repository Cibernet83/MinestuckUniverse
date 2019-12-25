package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.util.SpaceSaltUtils;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpaceSalt extends MSUItemBase {

    public ItemSpaceSalt() {
        super("space_salt", "spaceSalt");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        
        
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockPos mainPos;
        EnumFacing mchnFacing;
        ItemStack heldStack = player.getHeldItem(hand);

        if(block instanceof BlockLargeMachine)
        {
            if(block instanceof BlockAlchemiter)
            {
                mainPos = ((BlockAlchemiter)block).getMainPos(state, pos, worldIn);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityAlchemiter))
                    return EnumActionResult.FAIL;
                else if(((TileEntityAlchemiter)te).isBroken())
                    return EnumActionResult.FAIL;
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
                
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            else if(block instanceof BlockTotemLathe)
            {
                mainPos = ((BlockTotemLathe)block).getMainPos(state, pos);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityTotemLathe))
                    return EnumActionResult.FAIL;
                else if(((TileEntityTotemLathe)te).isBroken())
                    return EnumActionResult.FAIL;
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
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }else if(block instanceof BlockPunchDesignix)
            {
                mainPos = ((BlockPunchDesignix)block).getMainPos(state, pos);
                mchnFacing = (EnumFacing)state.getValue(BlockPunchDesignix.DIRECTION);
                
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityPunchDesignix))
                    return EnumActionResult.FAIL;
                else if(((TileEntityPunchDesignix)te).broken)
                    return EnumActionResult.FAIL;
                if(!worldIn.isRemote)
                {
                    for(int x = 0; x < 2; x++)
                        for(int y = 0; y < 2; y++)
                            worldIn.destroyBlock(mainPos.down(y).offset(mchnFacing.rotateYCCW(), x), true);
                    
                        pos = new BlockPos(pos.getX(), mainPos.getY()-1, pos.getZ());
                    
                    worldIn.setBlockState(pos, MinestuckBlocks.sburbMachine.getDefaultState().withProperty(BlockSburbMachine.MACHINE_TYPE,
                            BlockSburbMachine.MachineType.PUNCH_DESIGNIX).withProperty(BlockSburbMachine.FACING, mchnFacing));
                }
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }else if(block instanceof BlockCruxtruder)
            {
                mchnFacing = worldIn.getBlockState(((BlockCruxtruder)state.getBlock()).getMainPos(state, pos)).getValue(BlockCruxtruder.DIRECTION);
                mainPos = ((BlockCruxtruder)block).getMainPos(state, pos);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityCruxtruder))
                    return EnumActionResult.FAIL;
                else if(((TileEntityCruxtruder)te).isBroken())
                    return EnumActionResult.FAIL;
    
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
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
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
                    canPlace = SpaceSaltUtils.canPlaceAlchemiter(heldStack, player, worldIn, placePos, mchnFacing.rotateY(), pos);
                    worldIn.setBlockState(pos, state);
                    
                    if(canPlace)
                    {
                        worldIn.destroyBlock(pos, true);
                        SpaceSaltUtils.placeAlchemiter(worldIn, placePos, mchnFacing.rotateYCCW());
                    }
                    else return EnumActionResult.FAIL;
                    
                break;
                case CRUXTRUDER:
                    placePos = pos.offset(mchnFacing.rotateYCCW());
                    canPlace = SpaceSaltUtils.canPlaceCruxtruder(heldStack, player, worldIn, pos.offset(mchnFacing.rotateY()).offset(mchnFacing, 2), mchnFacing, pos);
        
                    if(canPlace)
                    {
                        worldIn.destroyBlock(pos, true);
                        SpaceSaltUtils.placeCruxtruder(player, worldIn, placePos, mchnFacing.getOpposite(), false);
                    }
                    else return EnumActionResult.FAIL;
        
                break;
                case TOTEM_LATHE:
                    placePos = pos.offset(mchnFacing.rotateY());
    
                    if(mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
                            || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
                        placePos = placePos.offset(mchnFacing.rotateY());
                    canPlace = SpaceSaltUtils.canPlaceTotemLathe(heldStack, player, worldIn, placePos, mchnFacing, pos);
        
                    if(canPlace)
                    {
                        worldIn.destroyBlock(pos, true);
                        SpaceSaltUtils.placeTotemLathe(worldIn, placePos, mchnFacing);
                    }
                    else return EnumActionResult.FAIL;
        
                    break;
                case PUNCH_DESIGNIX:
                    
                    if (mchnFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || mchnFacing.getFrontOffsetX() < 0 && hitZ < 0.5F || mchnFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || mchnFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F) {
                        placePos = pos.offset(mchnFacing.rotateY());
                    }
                    canPlace = SpaceSaltUtils.canPlacePunchDesignix(heldStack, player, worldIn, placePos, mchnFacing, pos);
                    
                    
                    if(canPlace)
                    {
                        worldIn.destroyBlock(pos, true);
                        SpaceSaltUtils.placePunchDesignix(worldIn, placePos, mchnFacing);
                    }
                    else return EnumActionResult.FAIL;
        
                    break;
            }
    
            heldStack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        
        return EnumActionResult.FAIL;
    }
}
