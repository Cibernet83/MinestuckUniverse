package com.cibernet.minestuckuniverse.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIMindflayerTarget extends EntityAIBase
{
	private EntityCreature entity;
	private float speed;
	private float moveStrafe, moveForward;

	public EntityAIMindflayerTarget(EntityCreature entity, float speed)
	{
		this.entity = entity;
		this.speed = speed;
		setMutexBits(255);
	}

	public void setMove(float moveStrafe, float moveForward)
	{
		this.moveStrafe = moveStrafe;
		this.moveForward = moveForward;
	}

	@Override
	public boolean shouldExecute()
	{
		return true;
	}

	@Override
	public void updateTask()
	{
		Vec3d target = entity.getPositionVector().addVector(moveStrafe, 0, moveForward);
		entity.getNavigator().tryMoveToXYZ(target.x, target.y, target.z, speed);
	}
}
