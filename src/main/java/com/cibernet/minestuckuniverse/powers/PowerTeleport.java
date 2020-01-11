package com.cibernet.minestuckuniverse.powers;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerTeleport extends MSUPowerBase
{
	public PowerTeleport()
	{
		super(EnumClass.HEIR, EnumAspect.SPACE);
	}
	
	@Override
	public boolean use(World worldIn, EntityPlayer player, boolean isNative)
	{
		return false;
	}
	
	@Override
	public boolean useOnBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, float hitX, float hitY, float hitZ, boolean isNative)
	{
		return playerIn.attemptTeleport(pos.getX(),pos.getY()+1,pos.getZ());
	}
	
	@Override
	public boolean onHeld(World worldIn, EntityPlayer playerIn, boolean isNative)
	{
		return false;
	}
	
	@Override
	public boolean useOnEntity(World worldIn, EntityPlayer playerIn, EntityLiving entityIn, boolean isNative)
	{
		return false;
	}
}
