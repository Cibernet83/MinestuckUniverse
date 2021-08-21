package com.cibernet.minestuckuniverse.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDiverHelmet extends MSUArmorBase
{
    public ItemDiverHelmet(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocName, String registryName) {
        super(materialIn, renderIndexIn, equipmentSlotIn, unlocName, registryName);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getItemStackFromSlot(EntityEquipmentSlot.HEAD) == stack && entityIn.isInWater())
            ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 0, 0, true, false));
    }
}
