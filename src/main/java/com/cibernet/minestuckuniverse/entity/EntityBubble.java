package com.cibernet.minestuckuniverse.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityBubble extends Entity
{
	private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityBubble.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityBubble.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> LIFESPAN = EntityDataManager.createKey(EntityBubble.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> CAN_ENTER = EntityDataManager.createKey(EntityBubble.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CAN_EXIT = EntityDataManager.createKey(EntityBubble.class, DataSerializers.BOOLEAN);

	public EntityBubble(World worldIn)
	{
		super(worldIn);
		setSize(3, 3);
	}

	public EntityBubble(World world, float size, int color, int lifespan, boolean canEnter, boolean canExit)
	{
		this(world);

		setBubbleSize(size);
		setColor(color);
		setLifespan(lifespan);
		setCanEnter(canEnter);
		setCanExit(canEnter);
	}

	//Dragon Bubble: 0x00FF7F
	//Hardlight Bubble:
	//Abyss Bubble:
	//Blood Bubble:
	//Rose Quartz Bubble: 0xFDE3FD

	@Override
	protected void entityInit()
	{
		dataManager.register(SIZE, 3f);
		dataManager.register(COLOR, 0xFDB1E8);
		dataManager.register(LIFESPAN, 20);
		dataManager.register(CAN_EXIT, false);
		dataManager.register(CAN_ENTER, true);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if(getLifespan() > 0)
			setLifespan(getLifespan()-1);

		if(getLifespan() == 0)
			setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("Size"))
			setBubbleSize(compound.getFloat("Size"));
		if(compound.hasKey("Color"))
			setColor(compound.getInteger("Color"));
		if(compound.hasKey("Lifespan"))
			setLifespan(compound.getInteger("Lifespan"));
		if(compound.hasKey("CanEnter"))
			setCanEnter(compound.getBoolean("CanEnter"));
		if(compound.hasKey("CanExit"))
			setCanEnter(compound.getBoolean("CanExit"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("Size", getBubbleSize());
		compound.setInteger("Color", getColor());
		compound.setInteger("Lifespan", getLifespan());
		compound.setBoolean("CanEnter", canEnter());
		compound.setBoolean("CanExit", canExit());
	}

	@Override
	public boolean shouldRenderInPass(int pass)
	{
		return pass == 1;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return canEnter() ? null : getEntityBoundingBox();
	}

	public float getBubbleSize()
	{
		return dataManager.get(SIZE);
	}

	public void setBubbleSize(float v)
	{
		dataManager.set(SIZE, v);

		this.setSize(v, v);
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public boolean canEnter()
	{
		return dataManager.get(CAN_ENTER);
	}

	public void setCanEnter(boolean v)
	{
		dataManager.set(CAN_ENTER, v);
	}

	public boolean canExit()
	{
		return dataManager.get(CAN_EXIT);
	}

	public void setCanExit(boolean v)
	{
		dataManager.set(CAN_EXIT, v);
	}

	public int getColor() {
		return dataManager.get(COLOR);
	}

	public void setColor(int v)
	{
		dataManager.set(COLOR, v);
	}

	public int getLifespan()
	{
		return dataManager.get(LIFESPAN);
	}

	public void setLifespan(int v)
	{
		dataManager.set(LIFESPAN, v);
	}

	@Override
	public boolean isRidingSameEntity(Entity entityIn)
	{
		return super.isRidingSameEntity(entityIn);
	}

	@Override
	public boolean hitByEntity(Entity entityIn) {
		return super.hitByEntity(entityIn);
	}

	/*
	@SubscribeEvent
	public static void onGetCollisionBoxes(GetCollisionBoxesEvent event)
	{
		Entity entity = event.getEntity();
		if(entity != null && !(entity instanceof EntityBubble) && entity.world != null && event.getAabb() != null )
			for(EntityBubble bubble : entity.world.getEntitiesWithinAABB(EntityBubble.class, event.getAabb()))
			{
				AxisAlignedBB aabb = bubble.getEntityBoundingBox();

				if(!bubble.canExit())
				{

					//System.out.println(entity.posY + " " + aabb.minY);

					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY, aabb.maxZ));
					//if(entity.posY > aabb.minY)
					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ));
					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.minZ));
					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
					//event.getCollisionBoxesList().add(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.maxY, aabb.maxZ));
				}
			}
	}
	*/

}
