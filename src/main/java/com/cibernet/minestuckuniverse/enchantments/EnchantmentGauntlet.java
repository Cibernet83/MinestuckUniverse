package com.cibernet.minestuckuniverse.enchantments;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IClassedTool;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentGauntlet extends EnchantmentKnockback
{
	protected EnchantmentGauntlet(Rarity rarityIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, slots);
		setName(MinestuckUniverse.MODID+".superpunch");
	}

	public boolean canApplyTogether(Enchantment ench)
	{
		return !(ench instanceof EnchantmentDamage);
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof IClassedTool && ((IClassedTool) stack.getItem()).getToolClass() != null && ((IClassedTool) stack.getItem()).getToolClass().canEnchantWith(this);
	}

	/**
	 * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
	 * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
	 */
	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
	{
		return 1.0F + (float)Math.max(0, getMaxLevel() - level) * 0.5F;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}

