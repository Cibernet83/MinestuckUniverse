package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class PropertyBloodBound extends WeaponProperty implements IEnchantableProperty
{
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack, int duration)
	{
		return Math.max(72000, super.getMaxItemUseDuration(stack, duration));
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		playerIn.setActiveHand(handIn);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
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
	public Boolean canEnchantWith(ItemStack stack, Enchantment enchantment)
	{
		return (enchantment.type == EnumEnchantmentType.BREAKABLE) ? false : null;
	}
}
