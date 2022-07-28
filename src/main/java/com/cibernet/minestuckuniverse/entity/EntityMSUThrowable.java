package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.client.render.RenderThrowable;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.MSUThrowableBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.throwkind.IPropertyThrowable;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class EntityMSUThrowable extends EntityThrowable
{
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> PLAIN = EntityDataManager.createKey(EntityMSUThrowable.class, DataSerializers.BOOLEAN);
    
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
        dataManager.register(PLAIN, false);
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

        if(id == 4)
            for (int i = 0; i < 8; ++i)
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ,
                        ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D,
                        Item.getIdFromItem(getStack().getItem()));
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
        	if(this.ticksExisted <= 4 && result.entityHit != null && result.entityHit.equals(this.thrower))
        		return;
        	
            boolean destroy = true;
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
                    }
            }

            if(destroy)
            {
                if(stack.isItemStackDamageable())
                    stack.damageItem(1, thrower);
                spawnItem(posX, posY + (EnumFacing.DOWN.equals(result.sideHit) ? -0.3 : 0), posZ, 5);
                this.setDead();
            }
            
            if(getPlainHit() && isEntity)
            {
            	Multimap<String, AttributeModifier> att = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            	float dmg = 1;
            	if(att == null || att.size() <= 0)
            		dmg *= (float) Math.min(5, (stack.getCount()/stack.getMaxStackSize() + 1) * Math.min(2.5, 1 + (motionX * motionX + motionY * motionY + motionZ * motionZ)));
            	else
            	{
            		double attkSpeed = 4;
            		for(String key : att.keySet())
            		{
            			double baseAdd = 0;
            			double baseMult = 1;
            			double finalMult = 1;
            			for(AttributeModifier mod : att.get(key))
            			{
            				switch(mod.getOperation())
            				{
            				default:
            					baseAdd += mod.getAmount();
            					break;
            				case 1:
            					baseMult *= mod.getAmount();
            					break;
            				case 2:
            					finalMult *= mod.getAmount();
            					break;
            				}
            			}
            			if(key.toLowerCase().contains("speed"))
            				attkSpeed = ((attkSpeed * baseMult) + baseAdd) * finalMult;
            			if(key.toLowerCase().contains("attack"))
            				dmg = (float) (((dmg * baseMult) + baseAdd) * finalMult);
            		}
            		
            		dmg *= Math.min(1, ((ticksExisted/20)%(1/attkSpeed) + .05)/(1/attkSpeed));
            	}
            	result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), dmg);
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

    protected List<EntityItem> spawnItem(double x, double y, double z, int pickupDelay)
    {
        ArrayList<ItemStack> items = new ArrayList<>();
        ArrayList<EntityItem> entities = new ArrayList<>();

        if(!(getThrower() instanceof EntityPlayer && ((EntityPlayer) getThrower()).isCreative()))
            items.add(getStack());

        if(getStack().getItem() instanceof IPropertyWeapon)
        {
	        List<WeaponProperty> propertyList = ((IPropertyWeapon) getStack().getItem()).getProperties(getStack());
	        for (WeaponProperty p : propertyList)
	            if (p instanceof IPropertyThrowable)
	                ((IPropertyThrowable) p).getDroppedItems(this, items);
        }

        if(!items.contains(getStack()))
            world.setEntityState(this, (byte) 4);

        for(ItemStack i : items)
        {
            if(i == null)
                continue;

            EntityItem item = new EntityItem(world, x, y, z, i);
            item.motionY = (rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D)/2.0;
            item.setPickupDelay(pickupDelay);
            world.spawnEntity(item);
            entities.add(item);
        }
        return entities;
    }

    public ItemStack getStack()
    {
        return dataManager.get(STACK);
    }

    public void setStack(ItemStack stack)
    {
        dataManager.set(STACK, stack);
    }
    
    public boolean getPlainHit()
    {
    	return dataManager.get(PLAIN);
    }
    
    public void setPlainHit(boolean plain)
    {
        dataManager.set(PLAIN, plain);
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
        if(compound.hasKey("Plain"))
        	setPlainHit(compound.getBoolean("Plain"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if(!projectileData.hasNoTags())
            compound.setTag("ProjectileData", getProjectileData());
        compound.setFloat("Size", getProjectileSize());
        compound.setBoolean("Plain", getPlainHit());

        NBTTagCompound stackNbt = getStack().writeToNBT(new NBTTagCompound());
        compound.setTag("Item", stackNbt);
    }
}
