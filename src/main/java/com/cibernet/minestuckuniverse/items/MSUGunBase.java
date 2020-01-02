package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MSUGunBase extends Item
{
    public MSUGunBase() {
        this.maxStackSize = 1;
        this.setCreativeTab(TabMinestuckUniverse.instance);
        
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
         ItemStack heldItem=playerIn.getHeldItem(handIn);
         return new ActionResult<>(EnumActionResult.SUCCESS,heldItem);
    }
}
