package com.cibernet.minestuckuniverse.items;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.block.BlockLargeMachine;
import com.mraof.minestuck.block.BlockReturnNode;
import com.mraof.minestuck.item.ItemCaptcharoidCamera;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemCaptcharoidOverride extends ItemCaptcharoidCamera
{

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        //pos.offset(facing).offset(facing.rotateY()).up(), pos.offset(facing.getOpposite()).offset(facing.rotateYCCW()).down()
        if(!worldIn.isRemote)
        {

            AxisAlignedBB bb = new AxisAlignedBB(pos.offset(facing));
            List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, bb);

            if(!list.isEmpty())
            {
                ItemStack item = list.get(0).getDisplayedItem();
                if(item.isEmpty()) item = new ItemStack(Items.ITEM_FRAME);

                player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(item));
                player.getHeldItem(hand).damageItem(1, player);
            }
            else
            {
                IBlockState state = worldIn.getBlockState(pos);
                ItemStack block = new ItemStack(Item.getItemFromBlock(state.getBlock()));
                int meta = state.getBlock().damageDropped(state);
                block.setItemDamage(meta);

                if(ItemGhostBlock.containsKey(state.getBlock()))
                    block = new ItemStack(ItemGhostBlock.get(state.getBlock()));
                else if(worldIn.getBlockState(pos).getBlock() instanceof BlockLargeMachine)
                    block = new ItemStack(((BlockLargeMachine) worldIn.getBlockState(pos).getBlock()).getItemFromMachine());

                player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(block));
                player.getHeldItem(hand).damageItem(1, player);
            }
            return EnumActionResult.PASS;
        }

        return EnumActionResult.SUCCESS;
    }
}
