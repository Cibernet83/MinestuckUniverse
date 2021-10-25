package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class PropertyBreakableItem extends WeaponProperty
{
	public static boolean isBroken(Item item, ItemStack stack)
	{
		return item instanceof IPropertyWeapon && ((IPropertyWeapon)item).hasProperty(PropertyBreakableItem.class, stack) && stack.isItemStackDamageable() && stack.getItemDamage() >= stack.getMaxDamage();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack, String name)
	{
		return stack.getItemDamage() >= stack.getMaxDamage() ?
				I18n.translateToLocalFormatted("property.item.broken", super.getItemStackDisplayName(stack, name)) : name;
	}

	@Override
	public int onDurabilityChanged(ItemStack stack, int damage)
	{
		return Math.min(stack.getMaxDamage(), super.onDurabilityChanged(stack, damage));
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(stack.getItemDamage() == stack.getMaxDamage()-1)
			player.renderBrokenItemStack(stack);
	}

	@Override
	public void onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if(stack.getItemDamage() == stack.getMaxDamage()-1)
			entityLiving.renderBrokenItemStack(stack);
	}

	@Override
	public double getAttackDamage(ItemStack stack, double dmg) {
		return stack.getItemDamage() >= stack.getMaxDamage() ? 0 : super.getAttackDamage(stack, dmg);
	}

	@Override
	public double getAttackSpeed(ItemStack stack, double speed) {
		return stack.getItemDamage() >= stack.getMaxDamage() ? 0 : super.getAttackSpeed(stack, speed);
	}

	public static ItemStack getBrokenStack(Item item)
	{
		return new ItemStack(item, 1, item.isDamageable() ? item.getMaxDamage() : 0);
	}

	public static IItemPropertyGetter getPropertyOverride()
	{
		return ((stack, worldIn, entityIn) -> stack.getItemDamage() >= stack.getMaxDamage() ? 1 : 0);
	}
}
