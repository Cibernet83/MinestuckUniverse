package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import com.cibernet.minestuckuniverse.items.properties.PropertyGristSetter;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class PropertyEventHandler
{
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(PropertyEventHandler.class);
		MinecraftForge.EVENT_BUS.register(PropertyGristSetter.class);
	}

	@SubscribeEvent
	public static void onCrit(CriticalHitEvent event)
	{
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();

		if(!event.getTarget().world.isRemote && event.isVanillaCritical() && stack.getItem() instanceof IPropertyWeapon && event.getTarget() instanceof EntityLivingBase)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for(WeaponProperty p : propertyList)
				event.setDamageModifier(p.onCrit(event.getEntityPlayer().getHeldItemMainhand(), event.getEntityPlayer(), (EntityLivingBase) event.getTarget(), event.getDamageModifier()));
		}
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event)
	{
		if(!event.getEntityLiving().isEntityInvulnerable(event.getSource()) && !event.getEntity().world.isRemote)
		{
			ItemStack stack = event.getEntityLiving().getActiveItemStack();

			if(stack.getItem() instanceof MSUShieldBase && (!(event.getEntityLiving() instanceof EntityPlayer) || !((EntityPlayer) event.getEntityLiving()).getCooldownTracker().hasCooldown(stack.getItem())))
				((MSUShieldBase) stack.getItem()).onHitWhileShielding(stack, event.getEntityLiving(), event.getSource(), event.getAmount(), MSUShieldBase.canBlockDamageSource(event.getEntityLiving(), event.getSource()));

			stack = event.getEntityLiving().getHeldItemMainhand().getItem() instanceof MSUShieldBase ? event.getEntityLiving().getHeldItemMainhand()
					: event.getEntityLiving().getHeldItemOffhand().getItem() instanceof MSUShieldBase ? event.getEntityLiving().getHeldItemOffhand() : ItemStack.EMPTY;


			boolean isStunned = false;
			if(event.getEntityLiving() instanceof EntityPlayer)
				isStunned = ((EntityPlayer) event.getEntityLiving()).getCooldownTracker().hasCooldown(stack.getItem());

			if(!stack.isEmpty() && !isStunned && ((MSUShieldBase)stack.getItem()).isParrying(stack) && ((MSUShieldBase)stack.getItem()).onShieldParry(stack, event.getEntityLiving(), event.getSource(), event.getAmount()))
			{
				if (!event.getSource().isProjectile())
				{
					Entity entity = event.getSource().getImmediateSource();
					damageShield(event.getEntityLiving(), stack, 1);

					if (entity instanceof EntityLivingBase)
						((EntityLivingBase)entity).knockBack(event.getEntityLiving(), 0.5F, event.getEntityLiving().posX - entity.posX, event.getEntityLiving().posZ - entity.posZ);
				}

				event.getEntityLiving().playSound(MSUSoundHandler.shieldParry, 1.0F, 0.8F + event.getEntity().world.rand.nextFloat() * 0.4F);
			}
		}
	}

	protected static void damageShield(EntityLivingBase entity, ItemStack stack, float damage)
	{
		if (entity instanceof EntityPlayer && damage >= 3.0F && stack.getItem().isShield(stack, entity))
		{
			ItemStack copyBeforeUse = stack.copy();
			int i = 1 + MathHelper.floor(damage);
			stack.damageItem(i, entity);

			if (stack.isEmpty())
			{
				EnumHand enumhand = entity.getHeldItemMainhand().equals(stack) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) entity, copyBeforeUse, enumhand);
				entity.setItemStackToSlot(enumhand ==  EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);

				entity.resetActiveHand();
				entity.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);
			}
		}
	}
}
