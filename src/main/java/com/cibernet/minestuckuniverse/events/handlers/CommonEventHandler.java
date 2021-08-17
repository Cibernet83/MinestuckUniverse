package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.properties.PropertyRandomDamage;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

public class CommonEventHandler
{
	@SubscribeEvent
	public static void onEntityHurt(LivingDamageEvent event)
	{
		if(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(MinestuckUniverseItems.spikedHelmet))
		{
			if(event.getEntityLiving().world.rand.nextInt(2) == 0 && event.getSource().getImmediateSource() != null)
				event.getSource().getImmediateSource().attackEntityFrom(DamageSource.causeThornsDamage(event.getEntityLiving()), 4);
		}
		if(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(MinestuckUniverseItems.wizardHat) && event.getSource().isMagicDamage())
		{
			event.setAmount(event.getAmount()*0.5f);
			event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem(1, event.getEntityLiving());
		}
		if(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(MinestuckUniverseItems.archmageHat) && event.getSource().isMagicDamage())
		{
			event.setAmount(event.getAmount()*0.2f);
			event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem(1, event.getEntityLiving());
		}
	}

	public static final IAttribute COOLED_ATTACK_STRENGTH = new RangedAttribute(null, MinestuckUniverse.MODID+".cooledAttackStrength", 0, 0, 1).setDescription("Cooled Attack Strength");

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.player.isSpectator())
		{
			event.player.capabilities.isFlying = true;
			event.player.capabilities.allowFlying = true;
		}

		if(event.player.getAttributeMap().getAttributeInstance(COOLED_ATTACK_STRENGTH) == null)
			event.player.getAttributeMap().registerAttribute(COOLED_ATTACK_STRENGTH);

		if(event.phase == TickEvent.Phase.START)
		{
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
			{
				ItemStack stack = event.player.getItemStackFromSlot(slot);
				AbstractAttributeMap attrMap = event.player.getAttributeMap();

				for (Map.Entry<String, AttributeModifier> attr : stack.getAttributeModifiers(slot).entries()) {
					IAttributeInstance attrInstance = attrMap.getAttributeInstanceByName(attr.getKey());

					if (attrInstance != null && attrInstance.hasModifier(attr.getValue()) && attrInstance.getModifier(attr.getValue().getID()).getAmount() != attr.getValue().getAmount()) {
						attrInstance.removeModifier(attr.getValue().getID());
						attrInstance.applyModifier(attr.getValue());
					}

				}
			}
		}
		else if(event.phase == TickEvent.Phase.END)
		{
			double str = getCooledAttackStrength(event.player);
			double currStr = event.player.getCooledAttackStrength(0.5f);

			if(str != currStr)
				event.player.getAttributeMap().getAttributeInstance(COOLED_ATTACK_STRENGTH).setBaseValue(currStr);
		}
	}

	public static float getCooledAttackStrength(EntityPlayer player)
	{
		return (float) player.getAttributeMap().getAttributeInstance(COOLED_ATTACK_STRENGTH).getAttributeValue();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().player == null)
			return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		if((player.isPotionActive(MSUPotions.SKYHBOUND) && player.getActivePotionEffect(MSUPotions.SKYHBOUND).getDuration() >= 5)
				|| (player.isCreative() && player.isPotionActive(MSUPotions.EARTHBOUND) && player.getActivePotionEffect(MSUPotions.EARTHBOUND).getDuration() < 5))
			player.capabilities.allowFlying = true;
		if((player.isPotionActive(MSUPotions.EARTHBOUND) && player.getActivePotionEffect(MSUPotions.EARTHBOUND).getDuration() >= 5)
				|| (!player.isCreative() && player.isPotionActive(MSUPotions.SKYHBOUND) && player.getActivePotionEffect(MSUPotions.SKYHBOUND).getDuration() < 5))
		{
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		}

		if(!player.isCreative() && player.isPotionActive(MSUPotions.CREATIVE_SHOCK))
		{
			int duration = player.getActivePotionEffect(MSUPotions.CREATIVE_SHOCK).getDuration();
			if(duration >= 5)
				player.capabilities.allowEdit = false;
			else player.capabilities.allowEdit = !MSUUtils.getPlayerGameType(player).hasLimitedInteractions();
		}


		if(player.isSpectator())
		{
			player.capabilities.isFlying = true;
			player.capabilities.allowFlying = true;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		boolean isRandom = false;
		float randValue = 0;

		if(stack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon) stack.getItem()).hasProperty(PropertyRandomDamage.class))
		{
			PropertyRandomDamage prop = (PropertyRandomDamage) ((IPropertyWeapon) stack.getItem()).getProperty(PropertyRandomDamage.class);
			randValue = prop.getMax() * prop.getMulitiplier();
			isRandom = true;
		}
		else if(stack.getItem() instanceof com.mraof.minestuck.item.weapon.ItemRandomWeapon)
		{
			randValue = 64;
			isRandom = true;
		}

		for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
		{
			Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(entityequipmentslot);

			if (!multimap.isEmpty())
			{
				for (Map.Entry<String, AttributeModifier> entry : multimap.entries())
				{
					AttributeModifier attributemodifier = entry.getValue();
					double d0 = attributemodifier.getAmount();

					if (event.getEntityPlayer() != null)
					{
						if (isRandom && attributemodifier.getID().equals(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF")))
						{
							d0 = d0 + event.getEntityPlayer().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
							d0 = d0 + (double) EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
							double d1;

							if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2)
								d1 = d0;
							else d1 = d0 * 100.0D;

							String attackString = (" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey())));
							String newAttackString = (" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), (ItemStack.DECIMALFORMAT.format(d1) + "-" + ItemStack.DECIMALFORMAT.format(d1+randValue)), I18n.translateToLocal("attribute.name." + (String)entry.getKey())));

							if(event.getToolTip().contains(attackString))
								event.getToolTip().set(event.getToolTip().indexOf(attackString), newAttackString);
							else event.getToolTip().add(newAttackString);
						}
					}

				}
			}
		}
	}

	@SubscribeEvent
	public static void onPotionRemove(PotionEvent.PotionRemoveEvent event)
	{
		onPotionEnd(event.getEntityLiving(), event.getPotion());
	}

	@SubscribeEvent
	public static void onPotionExpire(PotionEvent.PotionExpiryEvent expiryEvent)
	{
		onPotionEnd(expiryEvent.getEntityLiving(), expiryEvent.getPotionEffect().getPotion());
	}

	private static void onPotionEnd(EntityLivingBase entityLiving, Potion potion)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;

			if(potion == MSUPotions.EARTHBOUND && player.isCreative())
			{
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.FLIGHT_EFFECT, potion == MSUPotions.EARTHBOUND), player);
				player.capabilities.allowFlying = true;
			}
			if(potion == MSUPotions.SKYHBOUND && !player.isCreative())
			{
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.FLIGHT_EFFECT, potion == MSUPotions.EARTHBOUND), player);
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
			}
			if(!player.isCreative() && potion == MSUPotions.CREATIVE_SHOCK)
			{
				player.capabilities.allowEdit = !MSUUtils.getPlayerGameType(player).hasLimitedInteractions();
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.BUILD_INHIBIT_EFFECT), player);
			}
		}
	}


	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(event.getEntityPlayer().isPotionActive(MSUPotions.CREATIVE_SHOCK))
			event.setNewSpeed(0);
	}

	@SubscribeEvent
	public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
	{
		if(event.getEntityPlayer().isPotionActive(MSUPotions.CREATIVE_SHOCK))
			event.setCanHarvest(false);
	}

	@SubscribeEvent
	public static void onKnockback(LivingKnockBackEvent event)
	{
		if(event.getAttacker() instanceof EntityPlayer)
			event.setStrength(event.getStrength() + Math.max(0, 0.5f*(EnchantmentHelper.getMaxEnchantmentLevel(MSUEnchantments.SUPERPUNCH, (EntityLivingBase) event.getAttacker())-1)));
	}
}
