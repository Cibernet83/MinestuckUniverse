package com.cibernet.minestuckuniverse.powers;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerDaytimeShift extends MSUPowerBase
{
	public PowerDaytimeShift()
	{
		super(EnumClass.WITCH, EnumAspect.TIME);
	}
	
	@Override
	public boolean use(World worldIn, EntityPlayer player, boolean isNative)
	{
		if(isNative) return false;
		return onHeld(worldIn, player, isNative);
	}
	
	@Override
	public boolean useOnBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, float hitX, float hitY, float hitZ, boolean isNative)
	{
		return false;
	}
	
	@Override
	public boolean onHeld(World worldIn, EntityPlayer playerIn, boolean isNative)
	{
		int val = 50;
		if(isNative)
		{
			if(playerIn.isSneaking())
				val = -50;
		} else val = 100;
		
		worldIn.setWorldTime(worldIn.getWorldTime()+val);
		return true;
	}
	
	@Override
	public boolean useOnEntity(World worldIn, EntityPlayer playerIn, EntityLiving entityIn, boolean isNative)
	{
		return false;
	}
}
