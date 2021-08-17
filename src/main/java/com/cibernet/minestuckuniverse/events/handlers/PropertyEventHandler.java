package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.properties.PropertyGristSetter;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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

		if(event.isVanillaCritical() && stack.getItem() instanceof IPropertyWeapon && event.getTarget() instanceof EntityLivingBase)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for(WeaponProperty p : propertyList)
				event.setDamageModifier(p.onCrit(event.getEntityPlayer().getHeldItemMainhand(), event.getEntityPlayer(), (EntityLivingBase) event.getTarget(), event.getDamageModifier()));
		}
	}
}
