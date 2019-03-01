package com.cibernet.minestuckuniverse.items;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.item.ItemCaptcharoidCamera;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.block.ItemAlchemiter;
import com.mraof.minestuck.item.block.ItemCruxtruder;
import com.mraof.minestuck.item.block.ItemPunchDesignix;
import com.mraof.minestuck.item.block.ItemTotemLathe;
import com.mraof.minestuck.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;

public class ItemSpaceSalt extends MSUItemBase {

    public ItemSpaceSalt() {
        super("salis_spatius", "spaceSalt");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockPos mainPos;
        EnumFacing mchnFacing;
        ItemStack heldStack = player.getHeldItem(hand);
        Object a = (block.getClass());
        System.out.println(a);

        if(block instanceof BlockLargeMachine)
        {
            //TODO fix if broken crash
            //TODO fix breaks
            //TODO fix world is remote items thing

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

                for(int x = 0; x < 4; x++)
                    for(int z = 0; z < 4; z++)
                        worldIn.destroyBlock(mainPos.offset(mchnFacing, x).offset(mchnFacing.rotateY(), z), true);
                for(int y = 0; y < 4; y++)
                    worldIn.destroyBlock(mainPos.up(y), true);

                if(!worldIn.isRemote)
                InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(MinestuckBlocks.sburbMachine, 1, 3));
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

                for(int x = 0; x < 4; x++)
                    for(int y = 0; y < 3; y++)
                        worldIn.destroyBlock(mainPos.up(y).offset(mchnFacing.rotateYCCW(), x), true);

                if(!worldIn.isRemote)
                InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(MinestuckBlocks.sburbMachine, 1, 2));
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }else if(block instanceof BlockPunchDesignix)
            {
                mainPos = ((BlockPunchDesignix)block).getMainPos(state, pos);
                //TODO figure out a broken check
                mchnFacing = (EnumFacing)state.getValue(BlockPunchDesignix.DIRECTION);

                for(int x = 0; x < 2; x++)
                    for(int y = 0; y < 2; y++)
                        worldIn.destroyBlock(mainPos.down(y).offset(mchnFacing.rotateYCCW(), x), true);

                if(!worldIn.isRemote)
                InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(MinestuckBlocks.sburbMachine, 1, 1));
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }else if(block instanceof BlockCruxtruder)
            {
                mainPos = ((BlockCruxtruder)block).getMainPos(state, pos);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityCruxtruder))
                    return EnumActionResult.FAIL;
                else if(((TileEntityCruxtruder)te).isBroken())
                    return EnumActionResult.FAIL;
                for(int x = 0; x < 3; x++)
                    for(int z = 0; z < 3; z++)
                        for(int y = 1; y < 2; y++)
                            worldIn.destroyBlock(mainPos.north(x-1).east(z-1).down(y), true);

                worldIn.destroyBlock(mainPos, true);

                if(!worldIn.isRemote)
                    InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(MinestuckBlocks.sburbMachine, 1, 0));
                heldStack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
