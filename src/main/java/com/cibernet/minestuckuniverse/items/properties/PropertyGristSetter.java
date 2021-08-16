package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.blocks.BlockGrist;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;

public class PropertyGristSetter extends WeaponProperty
{
	public GristType gristType;

	public PropertyGristSetter(GristType type)
	{
		this.gristType = type;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		int frogType = -1;
		if(gristType == GristType.Gold)
			frogType = 5;
		if(gristType == GristType.Ruby)
			frogType = 2;
		if(gristType == GristType.Artifact)
			frogType = target.world.rand.nextInt(100) == 0 ? 6 : 4;

		if(frogType != -1 && target instanceof EntityFrog && ((EntityFrog) target).getType() != frogType && ((EntityFrog) target).getType() != 4)
		{
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + target.height/2f, target.posZ, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height/2f, target.posZ, 5, 0.2D, 0.2D, 0.2D, 0.5D,
					Block.getStateId(BlockGrist.BLOCKS.get(gristType).getDefaultState()));
			target.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.7f, ((target.world.rand.nextFloat() - target.world.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);

			NBTTagCompound nbt = target.writeToNBT(new NBTTagCompound());
			nbt.setInteger("Type", frogType);
			target.readFromNBT(nbt);
		}
		if(target instanceof EntityUnderling && getUnderlingGrist((EntityUnderling) target) != gristType)
		{
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + target.height/2f, target.posZ, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height/2f, target.posZ, 5, target.width/2f, target.height/2f, target.width/2f, 0.5D,
					Block.getStateId(BlockGrist.BLOCKS.get(gristType).getDefaultState()));
			target.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.7f, ((target.world.rand.nextFloat() - target.world.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);

			setUnderlingGrist((EntityUnderling) target, gristType);
		}
	}

	public static GristType getUnderlingGrist(EntityUnderling target)
	{
		return ObfuscationReflectionHelper.getPrivateValue(EntityUnderling.class, target, "type");
	}

	public static void setUnderlingGrist(EntityUnderling target, GristType type)
	{
		try {
			ObfuscationReflectionHelper.findMethod(EntityUnderling.class, "applyGristType", void.class, GristType.class, boolean.class).invoke(target, type, false);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
