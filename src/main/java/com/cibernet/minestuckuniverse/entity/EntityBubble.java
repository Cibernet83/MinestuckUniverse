package com.cibernet.minestuckuniverse.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class EntityBubble extends Entity
{
	private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityBubble.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityBubble.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> CAN_ENTER = EntityDataManager.createKey(EntityBubble.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CAN_EXIT = EntityDataManager.createKey(EntityBubble.class, DataSerializers.BOOLEAN);

	public EntityBubble(World worldIn)
	{
		super(worldIn);
		setSize(3, 3);
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
		dataManager.register(CAN_EXIT, false);
		dataManager.register(CAN_ENTER, true);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

	}

	/*
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return canEnter() ? null : getEntityBoundingBox();
	}
	*/
	public float getSize()
	{
		return dataManager.get(SIZE);
	}

	public boolean canEnter()
	{
		return dataManager.get(CAN_ENTER);
	}

	public boolean canExit()
	{
		return dataManager.get(CAN_EXIT);
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

	public int getColor() {
		return dataManager.get(COLOR);
	}
}
