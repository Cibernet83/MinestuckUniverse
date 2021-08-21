package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemYarnBall extends MSUThrowableBase
{
    public ItemYarnBall(String regName, String unlocName)
    {
        super(regName, unlocName);
        setHasSubtypes(true);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (int i = 0; i < 16; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);

        if(worldIn.getBlockState(pos).getBlock().equals(MinestuckBlocks.transportalizer))
        {
            NBTTagCompound nbt = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
            TileEntity te = MinestuckBlocks.transportalizer.createTileEntity(worldIn, MinestuckBlocks.transportalizer.getDefaultState());
            te.readFromNBT(nbt);
            worldIn.setBlockState(pos, MinestuckUniverseBlocks.sleevedTransportalizers.get(EnumDyeColor.byDyeDamage(stack.getItemDamage())).getDefaultState());
            worldIn.setTileEntity(pos, te);
            if(!player.isCreative())
                stack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 0.5f, 1);

            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
