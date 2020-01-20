package com.cibernet.minestuckuniverse.powers;

import com.cibernet.minestuckuniverse.entity.ai.EntityAIAttackShifted;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerRageShift extends MSUPowerBase
{
	public PowerRageShift()
	{
		super(EnumClass.WITCH, EnumAspect.RAGE);
	}
	
	@Override
	public boolean use(World worldIn, EntityPlayer player, boolean isNative)
	{
		return false;
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
		boolean isHostile = false;
		
		if(isNative)
		{
			if(!(entityIn instanceof EntityCreature))
				return false;
			for(Object a : entityIn.targetTasks.taskEntries.toArray())
			{
				EntityAIBase ai = ((EntityAITasks.EntityAITaskEntry) a).action;
				if(ai instanceof EntityAINearestAttackableTarget)
					
					System.out.println(ai);
				entityIn.targetTasks.removeTask(ai);
				isHostile = true;
				
			}
			for(Object a : entityIn.tasks.taskEntries.toArray())
			{
				EntityAIBase ai = ((EntityAITasks.EntityAITaskEntry) a).action;
				if(ai instanceof EntityAICreeperSwell)
				{
					entityIn.tasks.removeTask(ai);
					isHostile = true;
				}
			}
		}
		
		System.out.println(isHostile);
		if(!isHostile)
		{
			entityIn.targetTasks.addTask(3, new EntityAINearestAttackableTarget((EntityCreature) entityIn, EntityPlayer.class, true));
			entityIn.targetTasks.addTask(3, new EntityAINearestAttackableTarget((EntityCreature) entityIn, EntityIronGolem.class, true));
			entityIn.tasks.addTask(2, new EntityAIAttackShifted(entityIn,1.0D,false));
			
			if(entityIn instanceof EntityCreeper)
				entityIn.tasks.addTask(2, new EntityAICreeperSwell((EntityCreeper) entityIn));
		}
		
		
		MSUUtils.spawnEntityParticles(entityIn, EnumParticleTypes.DRAGON_BREATH);
		return true;
	}
}
