package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class ItemDragonCharge extends MSUItemBase
{
	public ItemDragonCharge(String name, String unlocName)
	{
		super(name, unlocName);
		setMaxStackSize(16);
		setCreativeTab(TabMinestuckUniverse.weapons);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem()
		{
			/**
			 * Dispense the specified stack, play the dispense sound and spawn particles.
			 */
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
				IPosition iposition = BlockDispenser.getDispensePosition(source);
				double d0 = iposition.getX() + (double)((float)enumfacing.getFrontOffsetX() * 0.3F);
				double d1 = iposition.getY() + (double)((float)enumfacing.getFrontOffsetY() * 0.3F);
				double d2 = iposition.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 0.3F);
				World world = source.getWorld();
				Random random = world.rand;
				double d3 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetX();
				double d4 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetY();
				double d5 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetZ();
				world.spawnEntity(new EntityDragonFireball(world, d0, d1, d2, d3, d4, d5));
				stack.shrink(1);
				return stack;
			}
			/**
			 * Play the dispense sound from the specified block.
			 */
			protected void playDispenseSound(IBlockSource source)
			{
				source.getWorld().playEvent(1017, source.getBlockPos(), 0);
			}
		});
	}



	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (!playerIn.capabilities.isCreativeMode)
		{
			itemstack.shrink(1);
		}

		playerIn.getCooldownTracker().setCooldown(this, 40);
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote)
		{
			float inaccuracy = 0.2f;
			float velocity = 0.5f;

			float motionX = (float) ((-MathHelper.sin(playerIn.rotationYaw * 0.017453292F) * MathHelper.cos(playerIn.rotationPitch * 0.017453292F) + worldIn.rand.nextGaussian() * 0.007499999832361937D * inaccuracy)*velocity);
			float motionY = (float) ((-MathHelper.sin((playerIn.rotationPitch) * 0.017453292F) + worldIn.rand.nextGaussian() * 0.007499999832361937F * inaccuracy)*velocity);
			float motionZ = (float) ((MathHelper.cos(playerIn.rotationYaw * 0.017453292F) * MathHelper.cos(playerIn.rotationPitch * 0.017453292F) + worldIn.rand.nextGaussian() * 0.007499999832361937D * inaccuracy)*velocity);

			EntityDragonFireball dragonFireball = new EntityDragonFireball(worldIn, playerIn, 0, 0, 0);

			dragonFireball.setLocationAndAngles(dragonFireball.posX, dragonFireball.posY + playerIn.getEyeHeight(), dragonFireball.posZ, dragonFireball.rotationYaw, dragonFireball.rotationPitch);
			double d0 = (double)MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
			dragonFireball.accelerationX = motionX / d0 * 0.1D;
			dragonFireball.accelerationY = motionY / d0 * 0.1D;
			dragonFireball.accelerationZ = motionZ / d0 * 0.1D;

			worldIn.spawnEntity(dragonFireball);
		}



		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}
}
