package com.cibernet.minestuckuniverse.items.properties.beams;

import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public interface IPropertyBeam
{
	default void onBeamTick(ItemStack stack, Beam beam){}
	default DamageSource onEntityImpact(ItemStack stack, Beam beam, Entity entity, DamageSource damageSource){return damageSource;}
	default String beamDamageName(Beam beam, ItemStack stack, String name) {return name;}
	default void onBlockImpact(ItemStack stack, Beam beam, BlockPos pos){}
	default float getBeamRadius(ItemStack stack, Beam beam, float radius) {return radius;}
}
