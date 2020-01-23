package com.cibernet.minestuckuniverse.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ItemLightningWeapon extends MSUWeaponBase
{
    public ItemLightningWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
    {
        target.world.addWeatherEffect(new EntityLightningBolt(target.world, target.posX, target.posY, target.posZ, false));
        return super.hitEntity(stack, target, player);
    }
}
