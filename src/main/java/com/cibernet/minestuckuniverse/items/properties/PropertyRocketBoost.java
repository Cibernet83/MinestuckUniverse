package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.IPropertyShield;
import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PropertyRocketBoost extends WeaponProperty
{

	float power;

	public PropertyRocketBoost(float power)
	{
		this.power = power;
	}


	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return (stack.getItem() instanceof MSUShieldBase) ? null : EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack, int duration) {
		return Math.max(duration, 72000);
	}

	@Override
	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player)
	{
		return stack.hasTagCompound() && stack.getTagCompound().getInteger("RocketTime") > 0;
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.setActiveHand(handIn);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onEntityItemUpdate(EntityItem entityItem) {
		super.onEntityItemUpdate(entityItem);

		if(entityItem.getItem().hasTagCompound() && entityItem.getItem().getTagCompound().getInteger("RocketTime") > 0)
			entityItem.getItem().getTagCompound().setInteger("RocketTime", 0);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(entityIn instanceof EntityLivingBase && !entityIn.isSneaking() && stack.equals(((EntityLivingBase) entityIn).getActiveItemStack()))
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());

			int rocketTime = stack.getTagCompound().getInteger("RocketTime");
			stack.getTagCompound().setInteger("RocketTime", rocketTime+1);

			entityIn.moveRelative(0, (float)Math.cos((entityIn.rotationPitch+90)*Math.PI/180f), (float)Math.sin((entityIn.rotationPitch+90)*Math.PI/180f), power);

			if(!worldIn.isRemote)
				((WorldServer)worldIn).spawnParticle(EnumParticleTypes.FLAME, entityIn.posX, entityIn.posY + entityIn.height/2f, entityIn.posZ, 1, 0.1D, 0.1D, 0.1D, 0.0D);

			if(rocketTime % 5 == 0)
				stack.damageItem(1, (EntityLivingBase) entityIn);
		}
		else if(stack.hasTagCompound() && stack.getTagCompound().getInteger("RocketTime") > 0)
			stack.getTagCompound().setInteger("RocketTime", 0);
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
