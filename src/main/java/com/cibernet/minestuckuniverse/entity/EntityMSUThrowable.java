package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.prjctilekind.IPropertyProjectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityMSUThrowable extends EntityThrowable
{
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.ITEM_STACK);

    private NBTTagCompound projectileData = new NBTTagCompound();

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
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        ItemStack stack = getStack();
        List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
        for (WeaponProperty p : propertyList)
            if (p instanceof IPropertyProjectile)
                ((IPropertyProjectile) p).onProjectileUpdate(this);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            boolean destroy = true;
            boolean drop = true;
            boolean isEntity = result.entityHit != null;

            ItemStack stack = getStack();

            if(stack.getItem() instanceof IPropertyWeapon)
            {
                List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
                for (WeaponProperty p : propertyList)
                    if (p instanceof IPropertyProjectile)
                    {
                        if(!(isEntity ? ((IPropertyProjectile) p).onEntityImpact(this, result) : ((IPropertyProjectile) p).onBlockImpact(this, result)))
                            destroy = false;
                        if(!((IPropertyProjectile) p).dropOnImpact(this, result))
                            drop = false;
                    }
            }

            if(destroy)
            {
                if(drop)
                    spawnItem(posX, posY + (EnumFacing.DOWN.equals(result.sideHit) ? -0.3 : 0), posZ, 5);
                this.setDead();
            }
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

    public NBTTagCompound getProjectileData() {
        return projectileData;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if(compound.hasKey("ProjectileData"))
            projectileData = compound.getCompoundTag("ProjectileData");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if(!projectileData.hasNoTags())
            compound.setTag("ProjectileData", projectileData);
    }
}
