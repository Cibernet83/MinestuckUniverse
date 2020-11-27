package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDungeonDoor extends MSUBlockBase
{
    private static final int BLOCK_LIMIT = 100;
    private static int blockCount = 0;

    public BlockDungeonDoor(String registryName, String unlocalizedName)
    {
        super(Material.ROCK, registryName, unlocalizedName);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        disableStats();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);
        if(stack.getItem() == MinestuckUniverseItems.dungeonKey)
        {
            activateDoor(worldIn, pos);
            if(!playerIn.isCreative())
            stack.shrink(1);
            blockCount = 0;
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    private static void activateDoor(World world, BlockPos pos)
    {
        world.destroyBlock(pos, false);

        if(blockCount >= BLOCK_LIMIT)
            return;
        for(EnumFacing direction : EnumFacing.values())
        {
            IBlockState state = world.getBlockState(pos.offset(direction));
            if(state.getBlock() instanceof BlockDungeonDoor)
            {
                blockCount++;
                activateDoor(world, pos.offset(direction));
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return super.getItemDropped(state, rand, fortune);
    }
}
