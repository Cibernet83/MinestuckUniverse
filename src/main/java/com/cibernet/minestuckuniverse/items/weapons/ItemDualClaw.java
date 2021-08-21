package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.items.properties.PropertySweep;
import com.cibernet.minestuckuniverse.items.properties.clawkind.IPropertyClaw;
import com.cibernet.minestuckuniverse.items.properties.PropertyDualWield;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemDualClaw extends MSUWeaponBase
{
    public double damage;
    public double damageSheathed;
    public double attackSpeed;
    public double attackSpeedSheathed;

    protected final ArrayList<WeaponProperty> sheathedProperties = new ArrayList<>();

    public ItemDualClaw(int maxUses, double damageVsEntity, double damagedVsEntityWhileShiethed, double weaponSpeed, double weaponSpeedWhileShiethed, int enchantability, String name, String regName) {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, regName, name);
        this.damage = damageVsEntity;
        this.damageSheathed = damagedVsEntityWhileShiethed;
        this.attackSpeed = weaponSpeed;
        this.attackSpeedSheathed = weaponSpeedWhileShiethed;

        this.addProperties(new PropertySweep(), new PropertyDualWield());
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
        double dmg = isDrawn(stack) ? this.damage : this.damageSheathed;

        for (WeaponProperty p : properties)
            dmg = p.getAttackDamage(stack, dmg);

        return dmg;
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
        	((ItemDualClaw) stack.getItem()).getProperties(stack).forEach(p ->
	        {
	        	if(p instanceof IPropertyClaw)
	        		((IPropertyClaw) p).onStateChange(player, stack, draw);
	        });
        }
    }

    @Override
    public MSUWeaponBase addProperties(WeaponProperty... properties)
    {
        List<WeaponProperty> propertiesList = sheathedProperties;
        for(WeaponProperty p : properties)
        {
            for (WeaponProperty p1 : propertiesList)
                if(!p.compatibleWith(p1))
                    throw new IllegalArgumentException("Property " + p1 + " is not compatible with " + p);

            propertiesList.add(p);
        }
        return super.addProperties(properties);
    }

    public MSUWeaponBase addProperties(boolean drawn, WeaponProperty... properties)
    {
        List<WeaponProperty> propertiesList = (drawn ? getProperties() : sheathedProperties);
        for(WeaponProperty p : properties)
        {
            for (WeaponProperty p1 : propertiesList)
                if(!p.compatibleWith(p1))
                    throw new IllegalArgumentException("Property " + p1 + " is not compatible with " + p);

            propertiesList.add(p);
        }

        return this;
    }

    @Override
    public List<WeaponProperty> getProperties(ItemStack stack) {
        return isDrawn(stack) ? super.getProperties() : sheathedProperties;
    }
}
