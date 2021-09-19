package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.blocks.BlockGrist;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		if(target instanceof EntityUnderling && ((EntityUnderling) target).getGristType() != gristType)
		{
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + target.height/2f, target.posZ, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
			((WorldServer)target.world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height/2f, target.posZ, 5, target.width/2f, target.height/2f, target.width/2f, 0.5D,
					Block.getStateId(BlockGrist.BLOCKS.get(gristType).getDefaultState()));
			target.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.7f, ((target.world.rand.nextFloat() - target.world.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);

			((EntityUnderling) target).applyGristType(gristType, false);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if(event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase source = ((EntityLivingBase) event.getSource().getTrueSource());
			ItemStack stack = source.getHeldItemMainhand();

			//On frog initial hit
			if(stack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon) stack.getItem()).hasProperty(PropertyGristSetter.class, stack))
			{
				GristType gristType = ((PropertyGristSetter) ((IPropertyWeapon) stack.getItem()).getProperty(PropertyGristSetter.class, stack)).gristType;
				int frogType = -1;
				if(gristType == GristType.Gold)
					frogType = 5;
				if(gristType == GristType.Ruby)
					frogType = 2;
				if(gristType == GristType.Artifact)
					frogType = 6;

				if(event.getEntityLiving() instanceof EntityFrog && (((EntityFrog) event.getEntityLiving()).getType() != frogType || ((EntityFrog) event.getEntityLiving()).getType() == 4))
					event.setAmount(0);
			}

		}
	}
}
