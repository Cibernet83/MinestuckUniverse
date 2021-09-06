package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.IEnchantableProperty;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class PropertyHookshot extends WeaponProperty implements IPropertyArrow, IEnchantableProperty
{
	boolean inGround;
	boolean invert;
	boolean global;
	float strength;
	float radius;

	public PropertyHookshot(float strength, float radius)
	{
		this(strength, radius, true, false, false);
	}
	public PropertyHookshot(float strength, float radius, boolean inGround, boolean invert, boolean global)
	{
		this.strength = strength;
		this.radius = radius;
		this.inGround = inGround;
		this.invert = invert;
		this.global = global;
	}

	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		if(!inGround)
			return true;

		if(global)
		{
			for(Entity target : arrow.world.getEntitiesWithinAABBExcludingEntity(arrow, arrow.getEntityBoundingBox().grow(radius)))
			{
				target.fallDistance = 0;
				if(invert)
					moveAwayFrom(target, arrow, strength*2f);
				else moveTowards(target, arrow, strength*2f);
			}
		}
		else if(arrow.shootingEntity instanceof EntityLivingBase && arrow.getDistance(arrow.shootingEntity) <= radius)
		{
			EntityLivingBase shooter = (EntityLivingBase) arrow.shootingEntity;

			if(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemMainhand()) ||
					(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemOffhand())))
			{
				shooter.fallDistance = 0;
				if(invert)
					moveAwayFrom(shooter, arrow, strength*2f);
				else moveTowards(shooter, arrow, strength*2f);
			}
		}

		return true;
	}

	@Override
	public void onProjectileUpdate(EntityMSUArrow arrow)
	{
		if(arrow.isInGround() != inGround)
			return;

		if(global)
		{
			for(Entity target : arrow.world.getEntitiesWithinAABBExcludingEntity(arrow, arrow.getEntityBoundingBox().grow(radius)))
			{
				target.fallDistance = 0;
				if(invert)
					moveAwayFrom(target, arrow, strength*2f);
				else moveTowards(target, arrow, strength*2f);
			}
		}
		else if(arrow.shootingEntity instanceof EntityLivingBase && arrow.getDistance(arrow.shootingEntity) <= radius)
		{
			EntityLivingBase shooter = (EntityLivingBase) arrow.shootingEntity;

			if(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemMainhand()) ||
					(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemOffhand())))
			{
				double xDist = shooter.posX - arrow.posX;
				double zDist = shooter.posZ - arrow.posZ;

				float yaw = getYawTo(shooter.posX, shooter.posZ, arrow.posX, arrow.posZ);
				float pitch = (float) Math.atan2((shooter.posY - arrow.posY), Math.sqrt((xDist*xDist) + (zDist*zDist))) * (180F / (float)Math.PI);

				arrow.rotationPitch = (pitch + 540) % 360;
				arrow.rotationYaw = yaw;

				shooter.fallDistance = 0;
				if(invert)
					moveAwayFrom(shooter, arrow, strength);
				else moveTowards(shooter, arrow, strength);
			}
		}
	}

	public static void moveAwayFrom(Entity target, Entity source, float strength)
	{
		Vec3d vec = new Vec3d(target.posX-source.posX, target.posY-source.posY + (target.onGround ? 0.4 : 0), target.posZ-source.posZ).normalize();
		moveTowards(target, strength, vec.x, vec.y, vec.z);
	}

	public static void moveTowards(Entity target, Entity source, float strength)
	{
		Vec3d vec = new Vec3d(source.posX-target.posX, source.posY-target.posY, source.posZ-target.posZ).normalize();
		moveTowards(target, strength, vec.x, vec.y, vec.z);
	}

	public static void moveTowards(Entity target, float strength, double xRatio, double yRatio, double zRatio)
	{
		target.velocityChanged = true;
		target.isAirBorne = true;
		float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio + yRatio * yRatio);
		target.motionX /= 2.0D;
		target.motionY /= 2.0D;
		target.motionZ /= 2.0D;
		target.motionX += xRatio / (double)f * (double)strength;
		target.motionY += yRatio / (double)f * (double)strength;
		target.motionZ += zRatio / (double)f * (double)strength;

	}

	protected static float getYawTo(double x1, double z1, double x2, double z2) {
		double d0 = x2 - x1;
		double d1 = z2 - z1;
		return (float)-(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
	}

	@Override
	public Boolean canEnchantWith(ItemStack stack, Enchantment enchantment)
	{
		if(enchantment == Enchantments.INFINITY)
			return false;
		return null;
	}
}
