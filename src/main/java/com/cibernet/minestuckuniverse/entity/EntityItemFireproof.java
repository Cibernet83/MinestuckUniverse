package com.cibernet.minestuckuniverse.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemFireproof extends EntityItem
{
    public EntityItemFireproof(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityItemFireproof(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    public EntityItemFireproof(World worldIn) {
        super(worldIn);
    }

    @Override
    public void setFire(int seconds)
    {

    }
}
