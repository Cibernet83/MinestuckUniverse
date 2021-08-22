package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PropertyShieldFire extends WeaponProperty implements IPropertyShield
{
	int fireTicks;
	int fireTicksParry;
	float chance;
	float chanceParry;
	boolean lightOnSneak;

	public PropertyShieldFire(int fireTicks, int fireTicksParry, float chance, float chanceParry, boolean lightOnSneak)
	{
		this.fireTicks = fireTicks;
		this.fireTicksParry = fireTicksParry;
		this.chance = chance;
		this.chanceParry = chanceParry;
		this.lightOnSneak = lightOnSneak;
	}

	@Override
	public void onEntityItemUpdate(EntityItem entityItem)
	{
		super.onEntityItemUpdate(entityItem);

		if(entityItem.getItem().hasTagCompound())
			entityItem.getItem().getTagCompound().removeTag("Lit");
	}

	public boolean isLit(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			return false;
		if(chanceParry > 0 && stack.getItem() instanceof MSUShieldBase && ((MSUShieldBase) stack.getItem()).isParrying(stack))
			return true;
		return stack.getTagCompound().getBoolean("Lit");
	}


	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(chance > 0 && entityIn instanceof EntityLivingBase && stack.equals(((EntityLivingBase) entityIn).getActiveItemStack()))
		{
			if(lightOnSneak)
			{
				boolean isLit = stack.hasTagCompound() && stack.getTagCompound().getBoolean("Lit");
				if(isLit != entityIn.isSneaking())
					entityIn.playSound(isLit ? SoundEvents.BLOCK_FIRE_EXTINGUISH : SoundEvents.ITEM_FLINTANDSTEEL_USE, 1, worldIn.rand.nextFloat() * 0.4F + 0.8F);
			}

			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("Lit", !lightOnSneak || entityIn.isSneaking());
		} else if(stack.hasTagCompound())
			stack.getTagCompound().removeTag("Lit");
	}

	@Override
	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player)
	{
		if(chance > 0 && isLit(stack) && stack.equals(player.getActiveItemStack()))
			return true;

		return chanceParry > 0 && stack.getItem() instanceof MSUShieldBase && ((MSUShieldBase) stack.getItem()).isParrying(stack);
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{
		if(isLit(stack) && blocked && source.getImmediateSource() != null && (player.world.rand.nextFloat() < chance))
			source.getImmediateSource().setFire(fireTicks/20);
	}

	@Override
	public boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		if(source.getImmediateSource() != null && (player.world.rand.nextFloat() < chanceParry))
			source.getImmediateSource().setFire(fireTicksParry);
		return true;
	}
}
