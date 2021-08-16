package com.cibernet.minestuckuniverse.items;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MSUToolClass
{
	protected List<Material> harvestMaterials = new ArrayList<>();
	protected List<Enchantment> enchantments = new ArrayList<>();
	protected List<String> baseTool = new ArrayList<>();
	
	public MSUToolClass()
	{
	}

	public MSUToolClass(String... baseTools)
	{
		for(String baseTool : baseTools)
			this.baseTool.add(baseTool);
	}
	
	public MSUToolClass(Material... materials)
	{
		for(Material mat : materials)
			harvestMaterials.add(mat);
	}
	
	public MSUToolClass(MSUToolClass... classCombo)
	{
		for(MSUToolClass cls : classCombo)
		{
			harvestMaterials.addAll(cls.harvestMaterials);
			enchantments.addAll(cls.enchantments);
			baseTool.addAll(cls.baseTool);
		}
	}
	
	public boolean canHarvest(IBlockState state)
	{
		if(harvestMaterials.contains(state.getMaterial()))
			return true;
		AtomicBoolean effective = new AtomicBoolean(false);
		baseTool.forEach(s -> {if(state.getBlock().isToolEffective(s, state)) effective.set(true);});
		return effective.get();
	}
	
	public MSUToolClass addBaseTool(String... name)
	{
		for(String baseTool : name)
			this.baseTool.add(baseTool);
		return this;
	}
	
	public MSUToolClass addEnchantments(Enchantment... enchantments)
	{
		for(Enchantment ench : enchantments)
			this.enchantments.add(ench);
		return this;
	}
	
	
	
	public MSUToolClass addEnchantments(List<Enchantment> enchantments)
	{
		this.enchantments.addAll(enchantments);
		return this;
	}
	
	public MSUToolClass addEnchantments(EnumEnchantmentType... enchantmentTypes)
	{
		for(EnumEnchantmentType type : enchantmentTypes)
		{
			Enchantment.REGISTRY.forEach(enchantment ->
			{if(enchantment.type.equals(type)) addEnchantments(enchantment);});
		}
		
		return this;
	}
	
	public List<Material> getHarvestMaterials() {return harvestMaterials;}
	public List<Enchantment> getEnchantments() {return enchantments;}
}
