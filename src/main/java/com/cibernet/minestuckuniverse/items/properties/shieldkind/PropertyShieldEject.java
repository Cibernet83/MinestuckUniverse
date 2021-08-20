package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class PropertyShieldEject extends WeaponProperty implements IPropertyShield
{
	double power;
	int cooldown;

	public PropertyShieldEject(double power, int cooldown)
	{
		this.power = power;
		this.cooldown = cooldown;
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{
		if(player.isSneaking())
			return;

		if(source.getDamageLocation() == null)
			player.moveRelative(0, 1, -0.2f, (float)power*0.35f);
		else
		{
			Vec3d motionVec = source.getDamageLocation().subtract(player.posX, player.posY, player.posZ).normalize().scale(power);

			if(!player.isAirBorne)
				motionVec.scale(5);

			player.motionX = -motionVec.x;
			player.motionY = -motionVec.y;
			player.motionZ = -motionVec.z;
			player.isAirBorne = true;
			player.velocityChanged = true;
		}

		if(!player.world.isRemote)
			((WorldServer)player.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, player.posX, player.posY + player.height/2f, player.posZ, 1, 0.0D, 0.0D, 0.0D, 0.05D);
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1.05f);
		if(player instanceof EntityPlayer)
			((EntityPlayer) player).getCooldownTracker().setCooldown(stack.getItem(), cooldown);

	}
}
