package com.cibernet.minestuckuniverse.items;

import com.mraof.minestuck.item.weapon.ItemDualWeapon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDualClaw extends ItemDualWeapon
{
    public ItemDualClaw(int maxUses, double damageVsEntity, double damagedVsEntityWhileShiethed, double weaponSpeed, double weaponSpeedWhileShiethed, int enchantability, String name) {
        super(maxUses, damageVsEntity, damagedVsEntityWhileShiethed, weaponSpeed, weaponSpeedWhileShiethed, enchantability, name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        EnumHand otherHand = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack otherStack = playerIn.getHeldItem(otherHand);

        if(otherStack.getItem() instanceof ItemDualClaw)
            super.onItemRightClick(worldIn, playerIn, otherHand);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
