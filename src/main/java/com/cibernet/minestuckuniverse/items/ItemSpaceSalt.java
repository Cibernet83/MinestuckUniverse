package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.util.SpaceSaltUtils;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        if(SpaceSaltUtils.onSpaceSaltUse(worldIn, player, hand, pos, facing, hitX, hitY, hitZ))
        {
            ItemStack heldStack = player.getHeldItem(hand);
            heldStack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        NBTTagCompound nbt = target.writeToNBT(new NBTTagCompound());

        if(nbt.hasKey("Size"))
        {
            float size = nbt.getFloat("Size");

            nbt.setFloat("Size", Math.min(10, size+0.2f*(playerIn.isSneaking() ? -1 : 1)));
            target.readFromNBT(nbt);
            nbt = target.writeToNBT(new NBTTagCompound());
            if(nbt.getFloat("Size") == size)
            {
                size = Math.min(10, size+(playerIn.isSneaking() ? -1 : 1));
                nbt.setFloat("Size", size);
                if(size <= 0)
                    target.setDead();
                target.readFromNBT(nbt);
            }

            stack.shrink(1);
            return true;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
