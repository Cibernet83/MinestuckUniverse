package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCruxiteSlime extends EntityTameable
{
	
	private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(EntityCruxiteSlime.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> SLIME_COLOR = EntityDataManager.createKey(EntityCruxiteSlime.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> CRUXITE_TYPE = EntityDataManager.createKey(EntityCruxiteSlime.class, DataSerializers.VARINT);
	private static final DataParameter<ItemStack> STORED_ITEM = EntityDataManager.createKey(EntityCruxiteSlime.class, DataSerializers.ITEM_STACK);
	public float squishAmount;
	public int eatCooldown;
	public float squishFactor;
	public float prevSquishFactor;
	private boolean wasOnGround;
	
	
	public EntityCruxiteSlime(World worldIn)
	{
		super(worldIn);
		moveHelper = new SlimeMoveHelper(this);
		setTamed(false);
	}
	
	@Nullable
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}
	
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new AISlimeFloat(this));
		//this.tasks.addTask(2, new AISlimeAttack(this));
		this.tasks.addTask(3, new AISlimeFace(this));
		this.tasks.addTask(5, new AISlimeHop(this));
		//this.targetTasks.addTask(1, new AISlimeTargetNearestEdible(	this));
		//this.targetTasks.addTask(6, new AISlimeFollowOwner(	this));
		
		//this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		//this.targetTasks.addTask(1, new AISlimeOwnerHurtByTarget(this));
		//this.targetTasks.addTask(2, new AISlimeOwnerHurtTarget(this));
		//this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
	}
	
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SLIME_SIZE, Integer.valueOf(1));
		this.dataManager.register(CRUXITE_TYPE, Integer.valueOf(1));
		this.dataManager.register(SLIME_COLOR, Integer.valueOf(0x99D9EA));
		this.dataManager.register(STORED_ITEM, ItemStack.EMPTY);
	}
	
	public void setSlimeSize(int size, boolean resetHealth)
	{
		this.dataManager.set(SLIME_SIZE, Integer.valueOf(size));
		this.setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
		this.setHitboxSize(0.51000005F * (float)size, 0.51000005F * (float)size);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue((double)(size * 16));
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));
		
		if (resetHealth)
		{
			this.setHealth(this.getMaxHealth());
		}
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player)
	{
		return 0;
	}
	
	protected void setHitboxSize(float width, float height)
	{
		if (width != this.width || height != this.height)
		{
			float f = this.width;
			this.width = width;
			this.height = height;
			
			if (this.width < f)
			{
				double d0 = (double)width / 2.0D;
				this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double)this.height, this.posZ + d0));
				return;
			}
			
			AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
			this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.width, axisalignedbb.minY + (double)this.height, axisalignedbb.minZ + (double)this.width));
			
			if (this.width > f && !this.firstUpdate && !this.world.isRemote)
			{
				this.move(MoverType.SELF, (double)(f - this.width), 0.0D, (double)(f - this.width));
			}
		}
	}
	
	/**
	 * Returns the size of the slime.
	 */
	public int getSlimeSize()
	{
		return (this.dataManager.get(SLIME_SIZE)).intValue();
	}
	
	public int getSlimeColor()
	{
		return dataManager.get(SLIME_COLOR).intValue();
	}
	
	public void setSlimeColor(int v)
	{
		dataManager.set(SLIME_COLOR, v);
	}
	
	public int getCruxiteType()
	{
		return dataManager.get(CRUXITE_TYPE).intValue();
	}
	
	public void setCruxiteType(int v)
	{
		dataManager.set(CRUXITE_TYPE, v);
	}
	
	public ItemStack getStoredItem()
	{
		return dataManager.get(STORED_ITEM);
	}
	
	public void setStoredItem(ItemStack stack)
	{
		dataManager.set(STORED_ITEM, stack);
	}
	
	public static void registerFixesSlime(DataFixer fixer)
	{
		EntityLiving.registerFixesMob(fixer, EntityCruxiteSlime.class);
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Size", this.getSlimeSize() - 1);
		compound.setInteger("EatingCooldown", eatCooldown);
		compound.setBoolean("wasOnGround", this.wasOnGround);
		compound.setInteger("Color", getSlimeColor());
		compound.setInteger("CruxiteType", getCruxiteType());
		NBTTagCompound itemNBT = new NBTTagCompound();
		getStoredItem().writeToNBT(itemNBT);
		compound.setTag("StoredItem", itemNBT);
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		int i = compound.getInteger("Size");
		
		if (i < 0)
		{
			i = 0;
		}
		
		this.setSlimeSize(i + 1, false);
		this.wasOnGround = compound.getBoolean("wasOnGround");
		eatCooldown = compound.getInteger("EatingCooldown");
		
		setSlimeColor(compound.getInteger("Color"));
		setCruxiteType(compound.getInteger("CruxiteType"));
		if(compound.getTag("StoredItem") instanceof NBTTagCompound)
			setStoredItem(new ItemStack((NBTTagCompound) compound.getTag("StoredItem")));
	}
	
	public boolean isSmallSlime()
	{
		return this.getSlimeSize() <= 1;
	}
	
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.SLIME;
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
		this.prevSquishFactor = this.squishFactor;
		super.onUpdate();
		
		if (this.onGround && !this.wasOnGround)
		{
			int i = this.getSlimeSize();
			if (spawnCustomParticles()) { i = 0; } // don't spawn particles if it's handled by the implementation itself
			for (int j = 0; j < i * 8; ++j)
			{
				float f = this.rand.nextFloat() * ((float)Math.PI * 2F);
				float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
				float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
				float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
				World world = this.world;
				EnumParticleTypes enumparticletypes = this.getParticleType();
				double d0 = this.posX + (double)f2;
				double d1 = this.posZ + (double)f3;
				world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
			}
			
			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.squishAmount = -0.5F;
		}
		else if (!this.onGround && this.wasOnGround)
		{
			this.squishAmount = 1.0F;
		}
		
		this.wasOnGround = this.onGround;
		this.alterSquishAmount();
		
		if(eatCooldown <= 0)
		{
			double bbScale = (getSlimeSize());
			
			AxisAlignedBB itemBB = new AxisAlignedBB(Math.round(posX-bbScale/2.0), Math.floor(posY), Math.round(posZ-bbScale/2.0), Math.round(posX+bbScale/2.0), Math.floor(posY)+bbScale, Math.round(posZ+bbScale/2.0));
			for(EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, itemBB))
			{
				ItemStack stack = item.getItem();
				EntityPlayer thrower = null;
				
				if(item.getThrower() != null)
					thrower = world.getPlayerEntityByName(item.getThrower());
				
				if(isSlimeFood(stack))
				{
					heal(1);
					if(!isTamed() && thrower != null)
					{
						if (this.rand.nextInt(2) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, thrower))
						{
							setTamedBy(thrower);
							if(!world.isRemote)
								InventoryHelper.spawnItemStack(world, posX, posY, posZ, getStoredItem());
							setStoredItem(ItemStack.EMPTY);
							playTameEffect(true);
							this.world.setEntityState(this, (byte) 7);
							eatCooldown = 60;
						}
						else
						{
							playTameEffect(false);
							this.world.setEntityState(this, (byte) 6);
							eatCooldown = 20;
						}
					}
					else
					{
						if(!getStoredItem().isEmpty() && this.rand.nextInt(2) == 0)
						{
							if(!world.isRemote)
								InventoryHelper.spawnItemStack(world, posX, posY, posZ, getStoredItem());
							setStoredItem(ItemStack.EMPTY);
							playTameEffect(true);
							this.world.setEntityState(this, (byte) 7);
						}
						else if (this.rand.nextInt(8) == 0)
						{
							if(!world.isRemote)
								InventoryHelper.spawnItemStack(world, posX, posY, posZ, new ItemStack(MinestuckUniverseItems.cruxiteGel, 1, getCruxiteType()));
						}
						playEatingEffect(stack);
						eatCooldown = 20;
					}
					this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					item.getItem().shrink(1);
					break;
					
				}
				else if(stack.getItem() == MinestuckItems.cruxiteDowel)
				{
					if(stack.getMetadata() == 0)
						setSlimeColor(0x99D9EA);
					else setSlimeColor(ColorCollector.getColor(stack.getMetadata()-1));
					setCruxiteType(stack.getMetadata());
					playEatingEffect(stack);
					eatCooldown = 60;
					this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					item.getItem().shrink(1);
					break;
				}
				else if(getSlimeSize() < 5 && stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 1)
				{
					setSlimeSize(getSlimeSize()+1, false);
					heal(2);
					playUpgradeEffect();
					this.world.setEntityState(this, (byte) 5);
					eatCooldown = 600;
					this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					item.getItem().shrink(1);
					break;
				}
			}
		}
		else eatCooldown--;
	}
	
	public static boolean isSlimeFood(ItemStack stack)
	{
		return stack.getItem().equals(Items.APPLE) || stack.getItem().equals(Items.CARROT) || stack.getItem().equals(Items.COOKIE)  || stack.getItem().equals(Items.CHICKEN)
				|| stack.getItem().equals(MinestuckItems.sporeo);
	}
	
	public boolean isEdible(ItemStack stack)
	{
		return stack.getItem().equals(MinestuckItems.cruxiteDowel) || (getSlimeSize() < 5 && stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 1) || isSlimeFood(stack);
	}
	
	@Override
	public void handleStatusUpdate(byte id)
	{
		if(id == 5)
			playUpgradeEffect();
		else super.handleStatusUpdate(id);
	}
	
	protected void playUpgradeEffect()
	{
		for (int i = 0; i < 7; ++i)
		{
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
		}
	}
	
	protected void playEatingEffect(ItemStack stack)
	{
		for (int i = 0; i < 5; ++i)
		{
			Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
			vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
			double d0 = (double)(-this.rand.nextFloat()) * 0.6D - 0.3D;
			Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
			vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
			vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
			vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
			if (this.world instanceof WorldServer) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
				((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, 0,  vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
			else //Fix the fact that spawning ItemCrack uses TWO arguments.
				this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
			
		}
	}
	
	
	
	protected void alterSquishAmount()
	{
		this.squishAmount *= 0.6F;
	}
	
	/**
	 * Gets the amount of time the slime needs to wait between jumps.
	 */
	protected int getJumpDelay()
	{
		return this.rand.nextInt(20) + 10;
	}
	
	protected EntityCruxiteSlime createInstance()
	{
		return new EntityCruxiteSlime(this.world);
	}
	
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if (SLIME_SIZE.equals(key))
		{
			int i = this.getSlimeSize();
			this.setSize(0.51000005F * (float)i, 0.51000005F * (float)i);
			this.rotationYaw = this.rotationYawHead;
			this.renderYawOffset = this.rotationYawHead;
			
			if (this.isInWater() && this.rand.nextInt(20) == 0)
			{
				this.doWaterSplashEffect();
			}
		}
		
		super.notifyDataManagerChange(key);
	}
	
	/**
	 * Will get destroyed next tick.
	 */
	public void setDead()
	{
		int i = this.getSlimeSize();
		
		if(!world.isRemote)
			InventoryHelper.spawnItemStack(world, posX, posY, posZ, getStoredItem());
		setStoredItem(ItemStack.EMPTY);
		
		if (!this.world.isRemote && i > 1 && this.getHealth() <= 0.0F)
		{
			int j = 2 + this.rand.nextInt(3);
			
			for (int k = 0; k < j; ++k)
			{
				float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
				float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
				EntityCruxiteSlime entityslime = this.createInstance();
				
				if (this.hasCustomName())
				{
					entityslime.setCustomNameTag(this.getCustomNameTag());
				}
				
				if (this.isNoDespawnRequired())
				{
					entityslime.enablePersistence();
				}
				
				entityslime.setSlimeSize(i / 2, true);
				entityslime.setSlimeColor(getSlimeColor());
				entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
				this.world.spawnEntity(entityslime);
			}
			
		}
		
		super.setDead();
	}
	
	/**
	 * Applies a velocity to the entities, to push them away from eachother.
	 */
	public void applyEntityCollision(Entity entityIn)
	{
		super.applyEntityCollision(entityIn);
		
		if (!isTamed() && entityIn instanceof EntityIronGolem && this.canDamage((EntityLivingBase) entityIn))
		{
			this.dealDamage((EntityLivingBase)entityIn);
		}
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (this.canDamage(entityIn))
		{
			this.dealDamage(entityIn);
		}
	}
	
	protected void dealDamage(EntityLivingBase entityIn)
	{
		int i = this.getSlimeSize();
		
		if (this.canEntityBeSeen(entityIn) && this.getDistanceSq(entityIn) < 0.6D * (double)i * 0.6D * (double)i && attackEntityAsMob(entityIn))
		{
			this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.applyEnchantments(this, entityIn);
		}
	}
	
	public float getEyeHeight()
	{
		return 0.625F * this.height;
	}
	
	/**
	 * Indicates weather the slime is able to damage the player
	 */
	protected boolean canDamage(EntityLivingBase entity)
	{
		return !this.isSmallSlime() || !(isTamed() && getOwner().equals(entity));
	}
	
	@Override
	public ItemStack getHeldItemMainhand()
	{
		return getStoredItem();
	}
	
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
	}
	
	protected SoundEvent getSquishSound()
	{
		return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
	}
	
	protected Item getDropItem()
	{
		return this.getSlimeSize() == 1 ? Items.SLIME_BALL : null;
	}
	
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return LootTableList.EMPTY;
	}
	
	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere()
	{
		return false;
	}
	
	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.4F * (float)this.getSlimeSize();
	}
	
	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	public int getVerticalFaceSpeed()
	{
		return 0;
	}
	
	/**
	 * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
	 */
	protected boolean makesSoundOnJump()
	{
		return this.getSlimeSize() > 0;
	}
	
	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	protected void jump()
	{
		this.motionY = 0.41999998688697815D;
		this.isAirBorne = true;
	}
	
	protected SoundEvent getJumpSound()
	{
		return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
	}
	
	/* ======================================== FORGE START =====================================*/
	/**
	 * Called when the slime spawns particles on landing, see onUpdate.
	 * Return true to prevent the spawning of the default particles.
	 */
	protected boolean spawnCustomParticles() { return false; }
	/* ======================================== FORGE END   =====================================*/
	
	static class AISlimeAttack extends EntityAIBase
	{
		private final EntityCruxiteSlime slime;
		private int growTieredTimer;
		
		public AISlimeAttack(EntityCruxiteSlime slimeIn)
		{
			this.slime = slimeIn;
			this.setMutexBits(2);
		}
		
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
			
			if (entitylivingbase == null)
			{
				return false;
			}
			else if (!entitylivingbase.isEntityAlive())
			{
				return false;
			}
			else if(!slime.isTamed())
				return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage;
			else return false;
		}
		
		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting()
		{
			this.growTieredTimer = 300;
			super.startExecuting();
		}
		
		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting()
		{
			EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
			
			if (entitylivingbase == null)
			{
				return false;
			}
			else if (!entitylivingbase.isEntityAlive())
			{
				return false;
			}
			else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
			{
				return !slime.isTamed();
			}
			else
			{
				return --this.growTieredTimer > 0;
			}
		}
		
		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
			((SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamage(slime.getAttackTarget()));
		}
	}
	
	static class AISlimeFace extends EntityAIBase
	{
		private final EntityCruxiteSlime slime;
		private float chosenDegrees;
		private int nextRandomizeTime;
		
		private Entity target;
		
		public AISlimeFace(EntityCruxiteSlime slimeIn)
		{
			this.slime = slimeIn;
			this.setMutexBits(2);
		}
		
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.isPotionActive(MobEffects.LEVITATION));
		}
		
		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			double d0 = this.getFollowRange();
			List<EntityItem> edibles = this.slime.world.getEntitiesWithinAABB(EntityItem.class, this.slime.getEntityBoundingBox().grow(d0*1.5, 4.0D, d0*1.5), (entity) -> slime.isEdible(entity.getItem()));
			List<EntityLivingBase> ownerList = this.slime.world.getEntitiesWithinAABB(EntityLivingBase.class, this.slime.getEntityBoundingBox().grow(d0, 4.0D, d0), (entity) -> entity.equals(slime.getOwner()) && slime.getDistanceSq(entity.getPosition()) > 16);
			List<EntityLivingBase> consorts = this.slime.world.getEntitiesWithinAABB(EntityConsort.class, this.slime.getEntityBoundingBox().grow(d0*0.75, 4.0D, d0*0.75));
			List<EntitySlime> slimes = this.slime.world.getEntitiesWithinAABB(EntitySlime.class, this.slime.getEntityBoundingBox().grow(d0, 4.0D, d0));
			boolean ignoreOwner = false;
			
			if(!edibles.isEmpty())
				target = edibles.get(0);
			else if(!consorts.isEmpty())
			{
				ignoreOwner = true;
				if(slime.getDistanceSq(consorts.get(0).getPosition()) > 8)
					target = consorts.get(0);
			}
			else if(!ignoreOwner && !ownerList.isEmpty())
				target = ownerList.get(0);
			else if (--this.nextRandomizeTime <= 0)
			{
				this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
				
				if(!slime.isTamed() && !slimes.isEmpty())
					target = slimes.get(0);
				else
				{
					target = null;
					this.chosenDegrees = (float)this.slime.getRNG().nextInt(360);
				}
			}
			
			if(target != null)
			{
				double ax = slime.posX - target.posX;
				double az = slime.posZ - target.posZ;
				chosenDegrees = (float) ((Math.atan2(az, ax)) * 180 / Math.PI) + 90;
			}
			
			
			((SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
		}
		protected double getFollowRange()
		{
			IAttributeInstance iattributeinstance = this.slime.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
		}
	}
	
	static class AISlimeFloat extends EntityAIBase
	{
		private final EntityCruxiteSlime slime;
		
		public AISlimeFloat(EntityCruxiteSlime slimeIn)
		{
			this.slime = slimeIn;
			this.setMutexBits(5);
			((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
		}
		
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return this.slime.isInWater() || this.slime.isInLava();
		}
		
		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			if (this.slime.getRNG().nextFloat() < 0.8F)
			{
				this.slime.getJumpHelper().setJumping();
			}
			
			((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
		}
	}
	
	static class AISlimeHop extends EntityAIBase
	{
		private final EntityCruxiteSlime slime;
		
		public AISlimeHop(EntityCruxiteSlime slimeIn)
		{
			this.slime = slimeIn;
			this.setMutexBits(5);
		}
		
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return true;
		}
		
		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
		}
	}
	
	static class SlimeMoveHelper extends EntityMoveHelper
	{
		private float yRot;
		private int jumpDelay;
		private final EntityCruxiteSlime slime;
		private boolean isAggressive;
		
		public SlimeMoveHelper(EntityCruxiteSlime slimeIn)
		{
			super(slimeIn);
			this.slime = slimeIn;
			this.yRot = 180.0F * slimeIn.rotationYaw / (float)Math.PI;
		}
		
		public void setDirection(float p_179920_1_, boolean p_179920_2_)
		{
			this.yRot = p_179920_1_;
			this.isAggressive = p_179920_2_;
		}
		
		public void setSpeed(double speedIn)
		{
			this.speed = speedIn;
			this.action = EntityMoveHelper.Action.MOVE_TO;
		}
		
		public void onUpdateMoveHelper()
		{
			this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.yRot, 90.0F);
			this.entity.rotationYawHead = this.entity.rotationYaw;
			this.entity.renderYawOffset = this.entity.rotationYaw;
			
			if (this.action != EntityMoveHelper.Action.MOVE_TO)
			{
				this.entity.setMoveForward(0.0F);
			}
			else
			{
				this.action = EntityMoveHelper.Action.WAIT;
				
				if (this.entity.onGround)
				{
					this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
					
					if (this.jumpDelay-- <= 0)
					{
						this.jumpDelay = this.slime.getJumpDelay();
						
						if (this.isAggressive)
						{
							this.jumpDelay /= 3;
						}
						
						this.slime.getJumpHelper().setJumping();
						
						if (this.slime.makesSoundOnJump())
						{
							this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
						}
					}
					else
					{
						this.slime.moveStrafing = 0.0F;
						this.slime.moveForward = 0.0F;
						this.entity.setAIMoveSpeed(0.0F);
					}
				}
				else
				{
					this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
				}
			}
		}
	}
}
