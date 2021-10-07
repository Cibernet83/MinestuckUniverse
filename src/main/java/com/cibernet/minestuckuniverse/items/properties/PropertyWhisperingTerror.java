package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

public class PropertyWhisperingTerror extends WeaponProperty
{
	float chance;

	public PropertyWhisperingTerror(float chance)
	{
		this.chance = chance;
	}

	@Override
	public void onEntityHit(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker instanceof EntityPlayer && (double)attacker.getRNG().nextFloat() < chance)
		{
			if (!attacker.world.isRemote)
			{
				String[] options = new String[]{"item.clawOfNrubyiglith.message.machinations", "item.clawOfNrubyiglith.message.stir", "item.clawOfNrubyiglith.message.suffering", "item.clawOfNrubyiglith.message.will", "item.clawOfNrubyiglith.message.done", "item.clawOfNrubyiglith.message.conspiracies"};
				Random rand = new Random();
				int num = rand.nextInt(options.length);
				ITextComponent message = new TextComponentTranslation(options[num], new Object[0]);
				message.getStyle().setColor(TextFormatting.DARK_PURPLE);
				attacker.sendMessage(message);
			}

			attacker.addPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 2));
		}
	}
}
