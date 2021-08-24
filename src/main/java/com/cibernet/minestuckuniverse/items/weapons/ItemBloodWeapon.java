package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.weapons.MSUWeaponBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBloodWeapon extends MSUWeaponBase
{
	public ItemBloodWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		int result = 72000;
		for(WeaponProperty p : getProperties(stack))
			result = p.getMaxItemUseDuration(stack, result);
		return result;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
		if(result.getType() != EnumActionResult.PASS)
			return result;

		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(entityIn instanceof EntityLivingBase && stack.equals(((EntityLivingBase) entityIn).getActiveItemStack()))
		{
			int useTime = 72000-((EntityLivingBase) entityIn).getItemInUseCount();

			if(useTime >= 40 && (useTime % 20) == 0)
			{
				if(entityIn.isSneaking())
				{
					if(stack.isItemDamaged())
					{
						entityIn.attackEntityFrom(new DamageSource(MinestuckUniverse.MODID+".bloodSword").setDamageIsAbsolute(), 1);
					 	stack.setItemDamage(Math.max(0, stack.getItemDamage()-5));
					}
				}
				else if(((EntityLivingBase) entityIn).getHealth() < ((EntityLivingBase) entityIn).getMaxHealth())
				{
					stack.damageItem(5, (EntityLivingBase) entityIn);
					((EntityLivingBase) entityIn).heal(1);
				}
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(enchantment.type == EnumEnchantmentType.BREAKABLE)
			return false;
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
}
