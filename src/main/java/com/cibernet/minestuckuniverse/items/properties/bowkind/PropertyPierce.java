package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;

public class PropertyPierce extends WeaponProperty implements IPropertyArrow
{
	float damageReduction;

	public PropertyPierce(float damageReduction)
	{
		this.damageReduction = damageReduction;
	}

	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		Entity entity = result.entityHit;

		if(arrow.getDamage() <= 0)
			return true;

		NBTTagCompound data = arrow.getProjectileData();

		if (entity != null)
		{
			NBTTagList pierced = data.getTagList("PiercedEntities", 10);

			for(int i = 0; i < pierced.tagCount(); i++)
			{
				NBTTagCompound entry = pierced.getCompoundTagAt(i);

				if(entry.getInteger("Time") <= 1)
				{
					pierced.removeTag(i);
					i--;
					continue;
				}

				entry.setInteger("Time", entry.getInteger("Time")-1);
				if(entity.getUniqueID().equals(entry.getUniqueId("Entity")))
					return false;
			}

			float f = MathHelper.sqrt(arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ);
			int i = MathHelper.ceil((double) f * arrow.getDamage());

			if (arrow.getIsCritical())
				i += arrow.world.rand.nextInt(i / 2 + 2);

			DamageSource damagesource;

			if (arrow.shootingEntity == null)
				damagesource = DamageSource.causeArrowDamage(arrow, arrow);
			else damagesource = DamageSource.causeArrowDamage(arrow, arrow.shootingEntity);

			if (arrow.isBurning() && !(entity instanceof EntityEnderman))
				entity.setFire(5);

			if (entity.attackEntityFrom(damagesource, (float) i))
			{
				if (entity instanceof EntityLivingBase)
				{
					EntityLivingBase entitylivingbase = (EntityLivingBase) entity;

					if (arrow.shootingEntity instanceof EntityLivingBase) {
						EnchantmentHelper.applyThornEnchantments(entitylivingbase, arrow.shootingEntity);
						EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) arrow.shootingEntity, entitylivingbase);
					}

					if (arrow.shootingEntity != null && entitylivingbase != arrow.shootingEntity && entitylivingbase instanceof EntityPlayer && arrow.shootingEntity instanceof EntityPlayerMP)
						((EntityPlayerMP) arrow.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
				}

				arrow.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (arrow.world.rand.nextFloat() * 0.2F + 0.9F));

				arrow.setDamage(arrow.getDamage()*damageReduction);

				if(!data.hasKey("PiercedEntities", 10))
					data.setTag("PiercedEntities", new NBTTagList());

				NBTTagCompound pierceEntry = new NBTTagCompound();
				pierceEntry.setInteger("Time", 20);
				pierceEntry.setUniqueId("Entity", entity.getUniqueID());
				data.getTagList("PiercedEntities", 10).appendTag(pierceEntry);

			}
		}
		return false;
	}
}
