package com.cibernet.minestuckuniverse.powers;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerWindSpeed extends MSUPowerBase
{
	public PowerWindSpeed()
	{
		super(EnumClass.HEIR, EnumAspect.BREATH);
	}
	
	@Override
	public boolean use(World worldIn, EntityPlayer player, boolean isNative)
	{
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 6, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 3, true, true));
		return true;
	}
	
	@Override
	public boolean useOnBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, float hitX, float hitY, float hitZ, boolean isNative)
	{
		return false;
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
