package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.powers.MSUPowerBase;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemClasspectPower extends MSUItemBase
{
	private final MSUPowerBase power;
	
	public ItemClasspectPower(MSUPowerBase power, String name, String unlocName)
	{
		super(name, unlocName);
		this.power = power;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		boolean used = power.useOnBlock(worldIn, player, pos, hitX, hitY, hitZ, false);
		if(used)
		{
			if(isDamageable())
				player.getHeldItem(hand).damageItem(1,player);
			else player.getHeldItem(hand).shrink(1);
		}
		return used ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		boolean used = power.use(worldIn, playerIn, false);
		if(used)
		{
			if(isDamageable())
				stack.damageItem(1,playerIn);
			else stack.shrink(1);
		}
		return used ? new ActionResult<>(EnumActionResult.SUCCESS, stack) : new ActionResult<>(EnumActionResult.FAIL, stack);
	}
}
