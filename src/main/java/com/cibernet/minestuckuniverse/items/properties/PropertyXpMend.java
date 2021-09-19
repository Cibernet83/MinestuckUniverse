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

public class PropertyXpMend extends WeaponProperty implements IEnchantableProperty
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
		if(entityIn instanceof EntityPlayer && stack.equals(((EntityLivingBase) entityIn).getActiveItemStack()))
		{
			EntityPlayer player = (EntityPlayer) entityIn;
			int useTime = 72000-player.getItemInUseCount();

			if(useTime >= 40)
			{
				if(player.isSneaking())
				{
					if(useTime % 4 == 0)
					{
						stack.damageItem(1, player);
						player.addExperience(1);
					}
				}
				else if(stack.isItemDamaged() && useTime % 2 == 0)
				{
					player.experience -= 1/(float)player.xpBarCap();
					player.experienceTotal --;

					if(player.experience < 0)
					{
						player.experience += 1;
						player.addExperienceLevel(-1);
					}

					if(useTime % 10 == 0)
						stack.setItemDamage(Math.max(0, stack.getItemDamage()-4));
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
