package com.cibernet.minestuckuniverse.items;

import com.mraof.minestuck.client.util.MinestuckModelManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBeamBlade extends MSUWeaponBase
{
    public boolean dyeable = false;
    public ItemBeamBlade(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
    }

    public ItemBeamBlade setDyeable() {dyeable = true; return this;}
    public boolean isDyeable() {return dyeable;}
    public boolean isDrawn(ItemStack itemStack) {
        return this.checkTagCompound(itemStack).getBoolean("IsDrawn");
    }

    @Override
    public boolean getHasSubtypes() {
        return super.getHasSubtypes() || dyeable;
    }

    @Override
    public void setDamage(ItemStack stack, int damage)
    {
        if(!isDrawn(stack))
            return;
        if(damage < 1) {
            changeState(stack, false);
            damage = 1;
        }
        super.setDamage(stack, damage);
    }

    private NBTTagCompound checkTagCompound(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }

        if (!tagCompound.hasKey("IsDrawn")) {
            tagCompound.setBoolean("IsDrawn", true);
        }

        return tagCompound;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        changeState(stack, isDrawn(stack));
        return new ActionResult(EnumActionResult.SUCCESS, stack);


    }

    public double getAttackDamage(ItemStack stack) {
        return this.isDrawn(stack) ? super.getAttackDamage(stack) : 0;
    }

    public double getAttackSpeed(ItemStack stack) {
        return this.isDrawn(stack) ? super.getAttackSpeed(stack) : 0;
    }

    public void changeState(ItemStack stack, boolean drawn) {
        NBTTagCompound tagCompound = this.checkTagCompound(stack);
        tagCompound.setBoolean("IsDrawn", drawn);
    }
}
