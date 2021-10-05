package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class PropertyUseOnCooled extends WeaponProperty
{
	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return (CommonEventHandler.getCooledAttackStrength(playerIn) != 1) ? EnumActionResult.FAIL : super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void onStopUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if(entityLiving instanceof EntityPlayer)
		((EntityPlayer)entityLiving).resetCooldown();
	}
}
