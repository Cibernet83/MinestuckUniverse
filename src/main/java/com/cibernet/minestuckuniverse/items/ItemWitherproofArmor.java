package com.cibernet.minestuckuniverse.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWitherproofArmor extends MSUArmorBase
{
    public ItemWitherproofArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocName, String registryName) {
        super(materialIn, renderIndexIn, equipmentSlotIn, unlocName, registryName);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTick(world, player, itemStack);
        if(player.isPotionActive(MobEffects.WITHER))
            player.removeActivePotionEffect(MobEffects.WITHER);
    }
}
