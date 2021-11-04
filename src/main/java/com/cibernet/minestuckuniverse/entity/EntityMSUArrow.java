package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.bowkind.IPropertyArrow;
import com.cibernet.minestuckuniverse.items.weapons.MSUBowBase;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EntityMSUArrow extends EntityArrow
{

	private static final DataParameter<ItemStack> BOW_STACK = EntityDataManager.createKey(EntityMSUArrow.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<ItemStack> ARROW_STACK = EntityDataManager.createKey(EntityMSUArrow.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Optional<UUID>> SHOOTER_UUID = EntityDataManager.createKey(EntityMSUArrow.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private NBTTagCompound projectileData = new NBTTagCompound();

	public EntityMSUArrow(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(BOW_STACK, new ItemStack(Items.BOW));
		dataManager.register(ARROW_STACK, new ItemStack(Items.ARROW));
		dataManager.register(SHOOTER_UUID, Optional.absent());
	}

	public EntityMSUArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityMSUArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityMSUArrow(World worldIn, EntityLivingBase shooter, ItemStack arrow, ItemStack bow)
	{
		super(worldIn, shooter);
		setArrowStack(arrow);
		setBowStack(bow);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		updateShootingEntity();

		if(shootingEntity != null)
			setOwnerUniqueId(shootingEntity.getUniqueID());

		ItemStack stack = getBowStack();
		if(stack.getItem() instanceof IPropertyWeapon)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for (WeaponProperty p : propertyList)
				if (p instanceof IPropertyArrow)
					((IPropertyArrow) p).onProjectileUpdate(this);
		}

		ItemStack arrow = getArrowStack();

		if(arrow.getItem() instanceof ItemTippedArrow)
		{
			if (this.world.isRemote)
			{
				if (this.inGround)
				{
					if (this.timeInGround % 5 == 0)
						this.spawnPotionParticles(1);
				}
				else this.spawnPotionParticles(2);
			}
			else if (this.inGround && this.timeInGround != 0 && this.timeInGround >= 600)
			{
				this.world.setEntityState(this, (byte)0);
				setArrowStack(new ItemStack(Items.ARROW, arrow.getCount()));
			}
		}
		else if(arrow.getItem() instanceof ItemSpectralArrow)
		{
			if (this.world.isRemote)
			{
				if (this.inGround)
				{
					if (this.timeInGround % 5 == 0)
						this.spawnGlowingParticles(1);
				}
				else this.spawnGlowingParticles(2);
			}
		}
	}

	public void spawnPotionParticles(int count)
	{
		ItemStack arrowStack = getArrowStack();

		if (arrowStack.getItem() instanceof ItemTippedArrow)
		{
			int i = PotionUtils.getColor(arrowStack);
			double r = (double)(i >> 16 & 255) / 255.0D;
			double g = (double)(i >> 8 & 255) / 255.0D;
			double b = (double)(i >> 0 & 255) / 255.0D;

			for (int j = 0; j < count; ++j)
				this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, r, g, b);
		}
	}

	public void spawnGlowingParticles(int count)
	{
		for (int j = 0; j < count; ++j)
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0, 0, 0);
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 0)
		{
			ItemStack arrowStack = getArrowStack();

			if (arrowStack.getItem() instanceof ItemTippedArrow)
				spawnPotionParticles(20);
		}
		else
		{
			super.handleStatusUpdate(id);ItemStack stack = getBowStack();
			if(stack.getItem() instanceof IPropertyWeapon)
			{
				List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
				for (WeaponProperty p : propertyList)
					if (p instanceof IPropertyArrow)
						((IPropertyArrow) p).onStatusUpdate(this, id);
			}
		}
	}

	@Override
	public void onHit(RayTraceResult raytraceResultIn)
	{
		ItemStack stack = getBowStack();
		if(stack.getItem() instanceof IPropertyWeapon)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for (WeaponProperty p : propertyList)
				if (p instanceof IPropertyArrow)
				{
					if(!(raytraceResultIn.entityHit != null ? ((IPropertyArrow) p).onEntityImpact(this, raytraceResultIn) : ((IPropertyArrow) p).onBlockImpact(this, raytraceResultIn)))
						return;
				}
		}

		super.onHit(raytraceResultIn);
	}

	@Override
	protected void arrowHit(EntityLivingBase living)
	{
		ItemStack bowStack = getBowStack();
		if(bowStack.getItem() instanceof IPropertyWeapon)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) bowStack.getItem()).getProperties(bowStack);
			for (WeaponProperty p : propertyList)
				if (p instanceof IPropertyArrow)
					((IPropertyArrow) p).onEntityImpactPost(this, living);
		}

		super.arrowHit(living);

		ItemStack arrowStack = getArrowStack();



		if(arrowStack.getItem() instanceof ItemTippedArrow)
		{
			PotionType potion = PotionUtils.getPotionFromItem(arrowStack);
			Collection<PotionEffect> customPotionEffects = PotionUtils.getFullEffectsFromItem(arrowStack);

			for (PotionEffect potioneffect : potion.getEffects())
			{
				living.addPotionEffect(new PotionEffect(potioneffect.getPotion(), Math.max(potioneffect.getDuration() / 8, 1), potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
			}

			if (!customPotionEffects.isEmpty())
			{
				for (PotionEffect potioneffect1 : customPotionEffects)
				{
					living.addPotionEffect(potioneffect1);
				}
			}
		}
		else if(arrowStack.getItem() instanceof ItemSpectralArrow)
		{
			PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, 200, 0);
			living.addPotionEffect(potioneffect);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		super.onCollideWithPlayer(entityIn);
		if (getArrowStack().isEmpty() && !this.world.isRemote && this.inGround && this.arrowShake <= 0)
		{
			if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode)
			{
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	@Override
	public boolean hasNoGravity()
	{
		ItemStack stack = getBowStack();
		if(stack.getItem() instanceof IPropertyWeapon)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for (WeaponProperty p : propertyList)
				if (p instanceof IPropertyArrow)
				{
					if(!((IPropertyArrow) p).hasGravity(this))
						return true;
				}
		}
		return super.hasNoGravity();
	}

	@Override
	public boolean canRenderOnFire()
	{
		return !getBowStack().getItem().equals(MinestuckUniverseItems.bowOfLight) && super.canRenderOnFire();
	}

	@Nullable
	protected UUID getOwnerUniqueId()
	{
		return (UUID)((Optional)this.dataManager.get(SHOOTER_UUID)).orNull();
	}

	protected void setOwnerUniqueId(@Nullable UUID uniqueId)
	{
		this.dataManager.set(SHOOTER_UUID, Optional.fromNullable(uniqueId));
	}

	public ItemStack getBowStack()
	{
		return dataManager.get(BOW_STACK);
	}

	public void setBowStack(ItemStack stack)
	{
		dataManager.set(BOW_STACK, stack);
	}

	@Override
	public ItemStack getArrowStack() {
		return dataManager.get(ARROW_STACK);
	}

	public void setArrowStack(ItemStack stack)
	{
		dataManager.set(ARROW_STACK, stack);
	}

	public NBTTagCompound getProjectileData() {
		return projectileData;
	}

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/projectiles/arrow.png");
	public ResourceLocation getArrowTexture()
	{
		ItemStack stack = getBowStack();
		if(stack.getItem() instanceof MSUBowBase)
			return ((MSUBowBase) stack.getItem()).getArrowTexture();
		return TEXTURE;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if(compound.hasKey("ProjectileData"))
			projectileData = compound.getCompoundTag("ProjectileData");
		if(compound.hasKey("Bow"))
			setBowStack(new ItemStack(compound.getCompoundTag("Bow")));
		if(compound.hasKey("Item"))
			setArrowStack(new ItemStack(compound.getCompoundTag("Item")));

		this.shootingEntity = null;
		if(compound.hasUniqueId("Owner"))
			setOwnerUniqueId(compound.getUniqueId("Owner"));

		shootingEntity = this.updateShootingEntity();
	}

	public Entity updateShootingEntity()
	{
		if (this.shootingEntity == null && getOwnerUniqueId() != null)
		{
			try
			{
				shootingEntity = world.getPlayerEntityByUUID(getOwnerUniqueId());

			}
			catch (Throwable var2)
			{
				this.shootingEntity = null;
			}
		}

		return this.shootingEntity;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		if(!projectileData.hasNoTags())
			compound.setTag("ProjectileData", getProjectileData());

		compound.setTag("Bow", getBowStack().writeToNBT(new NBTTagCompound()));
		compound.setTag("Item", getArrowStack().writeToNBT(new NBTTagCompound()));

		if(getOwnerUniqueId() != null)
			compound.setUniqueId("ownerName", getOwnerUniqueId());
		else if(shootingEntity != null)
			compound.setUniqueId("ownerName", shootingEntity.getUniqueID());
	}

	public boolean isInGround() {
		return inGround;
	}
}
