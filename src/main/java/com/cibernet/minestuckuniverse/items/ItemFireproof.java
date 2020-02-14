package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.entity.classes.EntityItemFireproof;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemFireproof extends MSUItemBase
{
	public ItemFireproof(String name, String unlocName)
	{
		super(name, unlocName);
	}
	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}
	
	@Nullable
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		EntityItem item = new EntityItemFireproof(world, location.posX, location.posY, location.posZ, itemstack);
		item.setPickupDelay(40);
		item.setVelocity(location.motionX, location.motionY, location.motionZ);
		return item;
	}
	
}
