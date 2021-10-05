package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.client.render.RenderThrowable;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.MSUThrowableBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.throwkind.IPropertyThrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityMSUThrowable extends EntityThrowable
{
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.FLOAT);

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

    @SideOnly(Side.CLIENT)
    public RenderThrowable.IRenderProperties getRenderProperties()
    {
        if(getStack().getItem() instanceof MSUThrowableBase)
            return ((MSUThrowableBase) getStack().getItem()).getRenderProperties();
        return null;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(STACK, new ItemStack(MinestuckUniverseItems.wizardbeardYarn));
        dataManager.register(SIZE, 1f);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if(SIZE.equals(key))
            setSize(getProjectileSize()*0.25f, getProjectileSize()*0.25f);
        super.notifyDataManagerChange(key);
    }

    public float getProjectileSize() {
        return dataManager.get(SIZE);
    }

    public void setProjectileSize(float size)
    {
        dataManager.set(SIZE, size);
        setSize(size*0.25f, size*0.25f);
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        ItemStack stack = getStack();
        if(stack.getItem() instanceof IPropertyWeapon)
        {
            List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
            for (WeaponProperty p : propertyList)
                if (p instanceof IPropertyThrowable)
                    ((IPropertyThrowable) p).onProjectileUpdate(this);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        ItemStack stack = getStack();
        if(stack.getItem() instanceof IPropertyWeapon)
        {
            List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
            for (WeaponProperty p : propertyList)
                if (p instanceof IPropertyThrowable)
                    ((IPropertyThrowable) p).onStatusUpdate(this, id);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            boolean destroy = true;
            boolean drop = !(getThrower() instanceof EntityPlayer && ((EntityPlayer) getThrower()).isCreative());
            boolean isEntity = result.entityHit != null;

            ItemStack stack = getStack();

            if(stack.getItem() instanceof IPropertyWeapon)
            {
                List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
                for (WeaponProperty p : propertyList)
                    if (p instanceof IPropertyThrowable)
                    {
                        if(!(isEntity ? ((IPropertyThrowable) p).onEntityImpact(this, result) : ((IPropertyThrowable) p).onBlockImpact(this, result)))
                            destroy = false;
                        if(!((IPropertyThrowable) p).dropOnImpact(this, result))
                            drop = false;
                    }
            }

            if(destroy)
            {
                if(stack.isItemStackDamageable())
                    stack.damageItem(1, thrower);
                if(drop)
                    spawnItem(posX, posY + (EnumFacing.DOWN.equals(result.sideHit) ? -0.3 : 0), posZ, 5);
                this.setDead();
            }
        }
    }

    @Override
    protected float getGravityVelocity()
    {
        float grav = super.getGravityVelocity();
        ItemStack stack = getStack();
        if(stack.getItem() instanceof IPropertyWeapon)
        {
            List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
            for (WeaponProperty p : propertyList)
                if (p instanceof IPropertyThrowable)
                    grav = ((IPropertyThrowable) p).getGravity(this, grav);
        }
        return grav;
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
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if(compound.hasKey("ProjectileData"))
            projectileData = compound.getCompoundTag("ProjectileData");
        if(compound.hasKey("Size"))
            setProjectileSize(compound.getFloat("Size"));
        if(compound.hasKey("Item"))
            setStack(new ItemStack(compound.getCompoundTag("Item")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if(!projectileData.hasNoTags())
            compound.setTag("ProjectileData", getProjectileData());
        compound.setFloat("Size", getProjectileSize());

        NBTTagCompound stackNbt = getStack().writeToNBT(new NBTTagCompound());
        compound.setTag("Item", stackNbt);
    }
}
