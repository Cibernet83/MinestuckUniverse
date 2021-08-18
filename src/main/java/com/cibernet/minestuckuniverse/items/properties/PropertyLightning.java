package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class PropertyLightning extends WeaponProperty
{
	boolean onCrit;
	boolean causeFire;
	float damage;
	float chance;

	public PropertyLightning(float damage, float chance, boolean onCrit, boolean causeFire)
	{
		this.causeFire = causeFire;
		this.chance = chance;
		this.damage = damage;
		this.onCrit = onCrit;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
			doLightning(player.world, player, target.posX, target.posY, target.posZ, damage, causeFire);
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{

		if(onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(player) >= 1))
			doLightning(player.world, player, target.posX, target.posY, target.posZ, damage, causeFire);

		return super.onCrit(stack, player, target, damageModifier);
	}

	public static void doLightning(World world, EntityLivingBase source, double x, double y, double z, float damage, boolean causeFire)
	{
		EntityLightningBolt lightning = new EntityLightningBolt(world, x, y, z, true);
		world.spawnEntity(lightning);
		world.addWeatherEffect(lightning);

		List<Entity> list = lightning.world.getEntitiesWithinAABBExcludingEntity(lightning, new AxisAlignedBB(lightning.posX - 3.0D, lightning.posY - 3.0D, lightning.posZ - 3.0D,
				lightning.posX + 3.0D, lightning.posY + 6.0D + 3.0D, lightning.posZ + 3.0D));

		if (causeFire && !world.isRemote)
		{
			lightning.boltVertex = world.rand.nextLong();
			BlockPos blockpos = new BlockPos(lightning);

			if (world.getGameRules().getBoolean("doFireTick") && world.isAreaLoaded(blockpos, 10) && world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, blockpos))
				world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
		}

		for (Entity entity : list)
			if (!entity.equals(source) && !ForgeEventFactory.onEntityStruckByLightning(entity, lightning))
			{
				entity.attackEntityFrom(source == null ? DamageSource.LIGHTNING_BOLT : new EntityDamageSource("lightningBolt", source), damage);
				entity.onStruckByLightning(lightning);
				entity.hurtResistantTime = 0;
			}
	}
}
