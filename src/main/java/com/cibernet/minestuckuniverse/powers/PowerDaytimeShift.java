package com.cibernet.minestuckuniverse.powers;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
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
		worldIn.setWorldTime(worldIn.getWorldTime()+100);
		return true;
	}
	
	@Override
	public boolean useOnBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, float hitX, float hitY, float hitZ, boolean isNative)
	{
		return false;
	}
}
