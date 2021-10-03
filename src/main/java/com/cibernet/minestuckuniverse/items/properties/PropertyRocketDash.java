package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PropertyRocketDash extends WeaponProperty
{
	int duration;
	int cooldownTime;
	float power;
	float dashDmgBonus;

	public PropertyRocketDash(int duration, int cooldownTime, float power, float dashDmgBonus)
	{
		this.duration = duration;
		this.cooldownTime = cooldownTime;
		this.power = power;
		this.dashDmgBonus = dashDmgBonus;
	}

	@Override
	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player)
	{
		return stack.hasTagCompound() && stack.getTagCompound().getInteger("RocketTime") > 0;
	}

	@Override
	public double getAttackDamage(ItemStack stack, double dmg)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getInteger("RocketTime") > 0)
			return dmg * dashDmgBonus;
		return super.getAttackDamage(stack, dmg);
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(playerIn.getCooldownTracker().hasCooldown(stack.getItem()) || stack.getTagCompound().getInteger("RocketTime") > 0)
			return super.onItemRightClick(worldIn, playerIn, handIn);

		stack.getTagCompound().setInteger("RocketTime", duration);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onEntityItemUpdate(EntityItem entityItem)
	{
		super.onEntityItemUpdate(entityItem);

		ItemStack stack = entityItem.getItem();
		if(stack.hasTagCompound() && stack.getTagCompound().getInteger("RocketTime") > 0)
		{
			int rocketTime = stack.getTagCompound().getInteger("RocketTime");
			stack.getTagCompound().setInteger("RocketTime", rocketTime-1);

			Vec3d motionVec = new Vec3d(entityItem.motionX, entityItem.motionY, entityItem.motionZ).normalize();

			if(!entityItem.world.isRemote)
				((WorldServer)entityItem.world).spawnParticle(EnumParticleTypes.FLAME, entityItem.posX, entityItem.posY + entityItem.height/2f, entityItem.posZ, 1, 0.1D, 0.1D, 0.1D, 0.05D);

			entityItem.motionX = motionVec.x * power*2;
			entityItem.motionY = motionVec.y * power*2;
			entityItem.motionZ = motionVec.z * power*2;
			entityItem.isAirBorne = true;
			entityItem.setNoGravity(rocketTime > 1);

			if(rocketTime % 2 == 0 && stack.attemptDamageItem(1, entityItem.world.rand, null))
				stack.shrink(1);

			if(stack.isEmpty())
			{
				if(!entityItem.world.isRemote)
					((WorldServer)entityItem.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entityItem.posX, entityItem.posY + entityItem.height/2f, entityItem.posZ, 1, 0.0D, 0.0D, 0.0D, 0.05D);
				entityItem.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.6f, 1.05f);

				for(EntityLivingBase target : entityItem.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityItem.posX-0.5, entityItem.posY, entityItem.posZ-0.5, entityItem.posX+0.5, entityItem.posY+1, entityItem.posZ+0.5).grow(3)))
					target.attackEntityFrom(DamageSource.causeExplosionDamage((EntityLivingBase) null), dashDmgBonus);
				return;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(stack.hasTagCompound())
		{
			int rocketTime = stack.getTagCompound().getInteger("RocketTime");

			if(rocketTime > 0)
			{
				if(isSelected || (entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getHeldItemOffhand().equals(stack)))
				{
					if(!(entityIn instanceof EntityPlayer))
						entityIn.moveRelative(0, (float)Math.cos((entityIn.rotationPitch+90)*Math.PI/180f), (float)Math.sin((entityIn.rotationPitch+90)*Math.PI/180f), power);
					else if(worldIn.isRemote && entityIn instanceof EntityPlayerSP)
					{
						MovementInput input = ((EntityPlayerSP)entityIn).movementInput;

						float magnitude = (float) Math.sqrt(Math.pow(input.moveForward, 2) + Math.pow(input.moveStrafe, 2));


						entityIn.moveRelative(input.moveStrafe*magnitude, (float)Math.cos((entityIn.rotationPitch+90)*Math.PI/180f), input.moveForward == 0 && input.moveStrafe == 0 ? (float)Math.sin((entityIn.rotationPitch+90)*Math.PI/180f)
								: (float)Math.sin((entityIn.rotationPitch+90)*Math.PI/180f * input.moveForward * magnitude), power);
					}
				}

				if(rocketTime % 2 == 0 && entityIn instanceof EntityLivingBase)
					stack.damageItem(1, (EntityLivingBase) entityIn);

				if(!worldIn.isRemote)
					((WorldServer)worldIn).spawnParticle(EnumParticleTypes.FLAME, entityIn.posX, entityIn.posY + entityIn.height/2f, entityIn.posZ, 1, 0.1D, 0.1D, 0.1D, 0.0D);

				if(stack.isEmpty())
				{
					if(!worldIn.isRemote)
						((WorldServer)worldIn).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entityIn.posX, entityIn.posY + entityIn.height/2f, entityIn.posZ, 1, 0.0D, 0.0D, 0.0D, 0.05D);
					entityIn.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.6f, 1.05f);
					for(EntityLivingBase target : worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityIn.posX-0.5, entityIn.posY+entityIn.height/2f -0.5, entityIn.posZ-0.5, entityIn.posX+0.5, entityIn.posY+entityIn.height/2f+0.5, entityIn.posZ+0.5).grow(3)))
						target.attackEntityFrom(DamageSource.causeExplosionDamage((EntityLivingBase) null), dashDmgBonus);
					return;
				}

				stack.getTagCompound().setInteger("RocketTime", stack.getTagCompound().getInteger("RocketTime")-1);
				if(rocketTime == 1 && entityIn instanceof EntityPlayer)
					((EntityPlayer) entityIn).getCooldownTracker().setCooldown(stack.getItem(), cooldownTime);
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(!stackA.hasTagCompound())
			stackA.setTagCompound(new NBTTagCompound());
		if(!stackB.hasTagCompound())
			stackB.setTagCompound(new NBTTagCompound());

		stackA.getTagCompound().setInteger("RocketTime", 0);
		stackB.getTagCompound().setInteger("RocketTime", 0);

		return !ItemStack.areItemStacksEqual(stackA, stackB);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(!stackA.hasTagCompound())
			stackA.setTagCompound(new NBTTagCompound());
		if(!stackB.hasTagCompound())
			stackB.setTagCompound(new NBTTagCompound());

		stackA.getTagCompound().setInteger("RocketTime", 0);
		stackB.getTagCompound().setInteger("RocketTime", 0);

		return super.shouldCauseBlockBreakReset(stackA, stackB);
	}
}
