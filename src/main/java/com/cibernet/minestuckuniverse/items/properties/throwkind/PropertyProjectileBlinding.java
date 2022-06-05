package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class PropertyProjectileBlinding extends WeaponProperty implements IPropertyThrowable
{
	int inkColor;

	public PropertyProjectileBlinding(int color)
	{
		inkColor = color;
	}

	@Override
	public boolean onEntityImpact(EntityMSUThrowable projectile, RayTraceResult result)
	{
		onImpact(projectile, result);
		return true;
	}

	@Override
	public boolean onBlockImpact(EntityMSUThrowable projectile, RayTraceResult result)
	{
		onImpact(projectile, result);
		return true;
	}

	public void onImpact(EntityMSUThrowable proj, RayTraceResult result)
	{
		World world = proj.world;
		Random rand = world.rand;

		if(!world.isRemote)
		{
			float f3 = 1.5F;
			double k1 = MathHelper.floor(proj.posX - (double)f3 - 1.0D);
			double l1 = MathHelper.floor(proj.posX + (double)f3 + 1.0D);
			double i2 = MathHelper.floor(proj.posY - (double)f3 - 1.0D);
			double i1 = MathHelper.floor(proj.posY + (double)f3 + 1.0D);
			double j2 = MathHelper.floor(proj.posZ - (double)f3 - 1.0D);
			double j1 = MathHelper.floor(proj.posZ + (double)f3 + 1.0D);


			for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(k1, i2, j2, l1, i1, j1)))
			{
				if(rand.nextInt(3) == 0)
				{
					entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, false, false));
					if(entity instanceof EntityPlayer)
						((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.eightBallBlind"), true);
				}
			}

			world.setEntityState(proj, (byte) 3);
		}
	}

	@Override
	public void onStatusUpdate(EntityMSUThrowable projectile, byte id)
	{
		if (id == 3)
		{
			for (int i = 0; i < 24; ++i)
				MSUParticles.spawnInkParticle(projectile.posX, projectile.posY, projectile.posZ, 0, 0, 0, inkColor, 2);
		}
	}
}
