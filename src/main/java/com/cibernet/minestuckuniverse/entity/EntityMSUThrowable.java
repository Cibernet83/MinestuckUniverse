package com.cibernet.minestuckuniverse.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMSUThrowable extends EntityThrowable
{
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.ITEM_STACK);

    public EntityMSUThrowable(World worldIn) {
        super(worldIn);
    }

    public EntityMSUThrowable(World worldIn, double x, double y, double z, ItemStack stack)
    {
        super(worldIn, x, y, z);
        setStack(stack);
    }

    public EntityMSUThrowable(World worldIn, EntityLivingBase throwerIn, ItemStack stack)
    {
        super(worldIn, throwerIn);
        setStack(stack);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STACK, ItemStack.EMPTY);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            spawnItem(posX, posY + (EnumFacing.DOWN.equals(result.sideHit) ? -0.3 : 0), posZ, 5);
            this.setDead();
        }
    }

    protected EntityItem spawnItem(double x, double y, double z, int pickupDelay)
    {
        EntityItem item = new EntityItem(world, x, y, z, getStack());
        item.motionY = (rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D)/2.0;
        item.setPickupDelay(pickupDelay);

        world.spawnEntity(item);
        return item;
    }

    public ItemStack getStack()
    {
        return dataManager.get(STACK);
    }

    public void setStack(ItemStack stack)
    {
        dataManager.set(STACK, stack);
    }
}
