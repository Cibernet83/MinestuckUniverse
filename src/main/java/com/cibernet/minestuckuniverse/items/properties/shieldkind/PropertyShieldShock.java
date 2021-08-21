package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import com.cibernet.minestuckuniverse.items.properties.PropertyShock;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PropertyShieldShock extends WeaponProperty implements IPropertyShield
{
	int stunTime;
	float stunDamage;
	float chance;
	float chanceParry;
	int stunTimeParry;
	float stunDamageParry;

	public PropertyShieldShock(int stunTime, float stunDamage, float chance, boolean onParry)
	{
		this(stunTime, stunDamage, onParry ? 0 : chance, stunTime, stunDamage, onParry ? chance : 0);
	}

	public PropertyShieldShock(int stunTime, float stunDamage, float chance, int stunTimeParry, float stunDamageParry, float chanceParry)
	{
		this.stunTime = stunTime;
		this.stunTimeParry = stunTimeParry;
		this.stunDamage = stunDamage;
		this.stunDamageParry = stunDamageParry;
		this.chance = chance;
		this.chanceParry = chanceParry;
	}

	@Override
	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player)
	{
		if(chanceParry > 0)
			return stack.getItem() instanceof MSUShieldBase && ((MSUShieldBase) stack.getItem()).isParrying(stack);

		return chance > 0 && stack.equals(player.getActiveItemStack());
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{
		if(blocked && source.getImmediateSource() instanceof EntityLivingBase && (player.world.rand.nextFloat() < chance))
			PropertyShock.shockTarget(player, (EntityLivingBase) source.getImmediateSource(), stunTime, stunDamage);
	}

	@Override
	public boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		if(source.getImmediateSource() instanceof EntityLivingBase && (player.world.rand.nextFloat() < chanceParry))
			PropertyShock.shockTarget(player, (EntityLivingBase) source.getImmediateSource(), stunTimeParry, stunDamageParry);
		return true;
	}
}
