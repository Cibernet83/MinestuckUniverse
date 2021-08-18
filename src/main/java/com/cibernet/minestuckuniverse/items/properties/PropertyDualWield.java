package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import com.cibernet.minestuckuniverse.items.IClassedTool;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertyDualWield extends PropertyPlural
{
	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
	{
		ItemStack offhandStack = player.getHeldItemOffhand();
		if(player instanceof EntityPlayer && offhandStack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon) offhandStack.getItem()).hasProperty(this.getClass(), offhandStack))
		{
			List<WeaponProperty> propertyList = new ArrayList<>(((IPropertyWeapon) offhandStack.getItem()).getProperties(offhandStack));
			propertyList.removeIf(p -> this.getClass().isInstance(p));
			for(WeaponProperty p : propertyList)
				p.onItemRightClick(worldIn, player, handIn);
		}
		return super.onItemRightClick(worldIn, player, handIn);
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.onEntityHit(stack, target, player);

		ItemStack offhandStack = player.getHeldItemOffhand();

		if(player instanceof EntityPlayer && offhandStack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon) offhandStack.getItem()).hasProperty(this.getClass(), offhandStack))
		{
			List<WeaponProperty> propertyList = new ArrayList<>(((IPropertyWeapon) offhandStack.getItem()).getProperties(offhandStack));
			propertyList.removeIf(p -> this.getClass().isInstance(p));

			target.hurtResistantTime = 0;
			attackTargetEntityWithOffhandItem((EntityPlayer) player, target);

			for(WeaponProperty p : propertyList)
				p.onEntityHit(stack, target, player);
		}
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		ItemStack offhandStack = player.getHeldItemOffhand();
		if(player instanceof EntityPlayer && offhandStack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon) offhandStack.getItem()).hasProperty(this.getClass(), offhandStack))
		{
			List<WeaponProperty> propertyList = new ArrayList<>(((IPropertyWeapon) offhandStack.getItem()).getProperties(offhandStack));
			propertyList.removeIf(p -> this.getClass().isInstance(p));
			for(WeaponProperty p : propertyList)
				p.onCrit(stack, player, target, damageModifier);
		}
		return super.onCrit(stack, player, target, damageModifier);
	}

	public void attackTargetEntityWithOffhandItem(EntityPlayer player, Entity targetEntity)
	{
		if (targetEntity.canBeAttackedWithItem())
		{
			if (!targetEntity.hitByEntity(player))
			{

				AbstractAttributeMap attrMap = player.getAttributeMap();

				for (Map.Entry<String, AttributeModifier> attr : player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getAttributeModifiers(EntityEquipmentSlot.MAINHAND).entries()) {
					IAttributeInstance attrInstance = attrMap.getAttributeInstanceByName(attr.getKey());

					if (attrInstance != null && attrInstance.hasModifier(attr.getValue()) && attrInstance.getModifier(attr.getValue().getID()).getAmount() != attr.getValue().getAmount())
						attrInstance.removeModifier(attr.getValue().getID());

					player.getAttributeMap().applyAttributeModifiers(player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
				}

				float f = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float f1;

				for (Map.Entry<String, AttributeModifier> attr : player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getAttributeModifiers(EntityEquipmentSlot.MAINHAND).entries()) {
					IAttributeInstance attrInstance = attrMap.getAttributeInstanceByName(attr.getKey());

					if (attrInstance != null && attrInstance.hasModifier(attr.getValue()) && attrInstance.getModifier(attr.getValue().getID()).getAmount() != attr.getValue().getAmount()) {
						attrInstance.removeModifier(attr.getValue().getID());
						attrInstance.applyModifier(attr.getValue());
					}

				}

				if (targetEntity instanceof EntityLivingBase)
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemOffhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
				else f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemOffhand(), EnumCreatureAttribute.UNDEFINED);

				f *= 0.75f;

				if(player.getHeldItemMainhand().getItem() instanceof IClassedTool && player.getHeldItemOffhand().getItem() instanceof IClassedTool && (((IClassedTool) player.getHeldItemOffhand().getItem()).getToolClass() == null ||
						!((IClassedTool) player.getHeldItemOffhand().getItem()).getToolClass().equals(((IClassedTool) player.getHeldItemMainhand().getItem()).getToolClass())))
					f *= 0.4f;

				float f2 = CommonEventHandler.getCooledAttackStrength(player);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetCooldown();

				if (f > 0.0F || f1 > 0.0F)
				{
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;

					if (player.isSprinting() && flag)
					{
						player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}


					f = f + f1;
					boolean flag3 = false;
					double d0 = (double)(player.distanceWalkedModified - player.prevDistanceWalkedModified);

					if (flag && !flag1 && player.onGround && d0 < (double)player.getAIMoveSpeed())
					{
						ItemStack itemstack = player.getHeldItem(EnumHand.OFF_HAND);

						if (itemstack.getItem() instanceof ItemSword)
						{
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;

					double d1 = targetEntity.motionX;
					double d2 = targetEntity.motionY;
					double d3 = targetEntity.motionZ;
					boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);

					if (flag5)
					{
						if (i > 0)
						{
							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (flag3)
						{
							float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;

							for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D)))
							{
								if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D)
								{
									entitylivingbase.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
								}
							}

							player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
							player.spawnSweepParticles();
						}

						if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged)
						{
							((EntityPlayerMP)targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
							targetEntity.velocityChanged = false;
							targetEntity.motionX = d1;
							targetEntity.motionY = d2;
							targetEntity.motionZ = d3;
						}

						if (!flag3)
						{
							if (flag)
							{
								player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
							}
							else
							{
								player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F)
						{
							player.onEnchantmentCritical(targetEntity);
						}

						player.setLastAttackedEntity(targetEntity);

						ItemStack itemstack1 = player.getHeldItemOffhand() ;
						Entity entity = targetEntity;

						if (targetEntity instanceof MultiPartEntityPart)
						{
							IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)targetEntity).parent;

							if (ientitymultipart instanceof EntityLivingBase)
							{
								entity = (EntityLivingBase)ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase)
						{
							ItemStack beforeHitCopy = itemstack1.copy();

							if (itemstack1.getItem().isDamageable())
								itemstack1.damageItem(1, player);

							player.addStat(StatList.getObjectUseStats(itemstack1.getItem()));

							if (itemstack1.isEmpty())
							{
								net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.OFF_HAND);
								player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
							}
						}

						if (targetEntity instanceof EntityLivingBase)
						{
							float f5 = f4 - ((EntityLivingBase)targetEntity).getHealth();
							player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (player.world instanceof WorldServer && f5 > 2.0F)
							{
								int k = (int)((double)f5 * 0.5D);
							}
						}

						player.addExhaustion(0.1F);
					}
				}
			}
		}
	}
}
