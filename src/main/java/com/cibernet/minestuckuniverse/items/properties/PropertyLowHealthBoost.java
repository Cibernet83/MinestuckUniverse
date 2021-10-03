package com.cibernet.minestuckuniverse.items.properties;

import com.google.common.collect.Multimap;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class PropertyLowHealthBoost extends WeaponProperty
{
	IAttribute attribute;
	String name;
	double maxAmount;
	double minAmount;
	float healthThreshold;
	int operation;
	public final UUID attrUUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());

	public PropertyLowHealthBoost(IAttribute attribute, String name, double max, double min, float health, int operation)
	{
		this.attribute = attribute;
		this.name = name;
		this.maxAmount = max;
		this.minAmount = min;
		this.healthThreshold = health;
		this.operation = operation;
	}

	@Override
	public void getAttributeModifiers(EntityLivingBase player, ItemStack stack, Multimap<String, AttributeModifier> multimap)
	{
		multimap.put(attribute.getName(), new AttributeModifier(attrUUID, name,(player.getHealth()/player.getMaxHealth() <= healthThreshold) ?
				minAmount + (maxAmount-minAmount)*Math.max(0, 1-((player.getHealth())/(player.getMaxHealth()*healthThreshold))) : 0, operation));
	}

	public IItemPropertyGetter getPropertyOverride()
	{
		return ((stack, worldIn, entityIn) -> (entityIn != null && entityIn.getHealth()/entityIn.getMaxHealth() <= healthThreshold) ? (float)
				(minAmount + (maxAmount-minAmount)*Math.max(0, 1-((entityIn.getHealth())/(entityIn.getMaxHealth()*healthThreshold)))) : 0);
	}
}
