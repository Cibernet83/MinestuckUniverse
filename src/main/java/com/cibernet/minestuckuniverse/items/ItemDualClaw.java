package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.items.properties.PropertyAbstractClaw;
import com.cibernet.minestuckuniverse.items.properties.PropertyPlural;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDualClaw extends MSUWeaponBaseSweep
{
    public double damage;
    public double damageSheathed;
    public double attackSpeed;
    public double attackSpeedSheathed;

    public ItemDualClaw(int maxUses, double damageVsEntity, double damagedVsEntityWhileShiethed, double weaponSpeed, double weaponSpeedWhileShiethed, int enchantability, String name, String regName) {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, regName, name);
        this.damage = damageVsEntity;
        this.damageSheathed = damagedVsEntityWhileShiethed;
        this.attackSpeed = weaponSpeed;
        this.attackSpeedSheathed = weaponSpeedWhileShiethed;

        this.addProperties(new PropertyPlural());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!playerIn.isSneaking())
            return new ActionResult(EnumActionResult.PASS, stack);

        EnumHand otherHand = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack otherStack = playerIn.getHeldItem(otherHand);

        if(otherStack.getItem() instanceof ItemDualClaw)
            draw(playerIn, otherStack, !isDrawn(otherStack));
        draw(playerIn, stack, !isDrawn(stack));
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    public static boolean isDrawn(ItemStack itemStack) {
        return checkTagCompound(itemStack).getBoolean("IsDrawn");
    }

    private static NBTTagCompound checkTagCompound(ItemStack stack) {
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

    public double getAttackDamage(ItemStack stack) {
        return this.isDrawn(stack) ? this.damage : this.damageSheathed;
    }

    public double getAttackSpeed(ItemStack stack) {
        return this.isDrawn(stack) ? this.attackSpeed : this.attackSpeedSheathed;
    }

    public static void draw(EntityPlayer player, ItemStack stack, boolean draw)
    {
        NBTTagCompound tagCompound = checkTagCompound(stack);
        tagCompound.setBoolean("IsDrawn", draw);

        if(stack.getItem() instanceof ItemDualClaw)
        {
        	((ItemDualClaw) stack.getItem()).getProperties().forEach(p ->
	        {
	        	if(p instanceof PropertyAbstractClaw)
	        		((PropertyAbstractClaw) p).onStateChange(player, stack, draw);
	        });
        }
    }
}
