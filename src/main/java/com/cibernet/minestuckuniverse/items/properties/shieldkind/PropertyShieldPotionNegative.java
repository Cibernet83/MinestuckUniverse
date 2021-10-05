package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PropertyShieldPotionNegative extends WeaponProperty implements IPropertyShield
{
	public PotionEffect[] effects;

	public PropertyShieldPotionNegative(PotionEffect... effects)
	{
		this.effects = effects;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(stack.hasTagCompound() && entityIn instanceof EntityLivingBase)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(!nbt.getBoolean("Parried") && nbt.getInteger("ParryTime") == 1)
			{
				PotionEffect effect = effects[entityIn.world.rand.nextInt(effects.length)];
				((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
			}
		}
	}
}
