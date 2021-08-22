package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class PropertyExplode extends WeaponProperty
{
	boolean onCrit;
	float chance;
	float strength;

	public PropertyExplode(float strength, float chance, boolean onCrit)
	{
		this.strength = strength;
		this.chance = chance;
		this.onCrit = onCrit;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
		{
			target.hurtResistantTime = 0;
			target.world.createExplosion(null, target.posX, target.posY, target.posZ, strength, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.world, player));
		}
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		if(onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
		{
			target.hurtResistantTime = 0;
			target.world.createExplosion(null, target.posX, target.posY, target.posZ, strength, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.world, player));
		}
		return super.onCrit(stack, player, target, damageModifier);
	}


}
