package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
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
	protected List<EnumEnchantmentType> enchantmentTypes = new ArrayList<>();
	protected List<String> baseTool = new ArrayList<>();
	public List<MSUToolClass> parents = new ArrayList<>();
	protected boolean disablesShield = false;
	public final String name;

	public MSUToolClass(String name)
	{
		this.name = name;
	}

	public MSUToolClass(String name, String... baseTools)
	{
		this(name);
		for(String baseTool : baseTools)
			this.baseTool.add(baseTool);
	}
	
	public MSUToolClass(String name, Material... materials)
	{
		this(name);
		for(Material mat : materials)
			harvestMaterials.add(mat);
	}
	
	public MSUToolClass(String name, MSUToolClass... classCombo)
	{
		this(name);
		for(MSUToolClass cls : classCombo)
		{
			harvestMaterials.addAll(cls.harvestMaterials);
			enchantments.addAll(cls.enchantments);
			enchantmentTypes.addAll(cls.enchantmentTypes);
			baseTool.addAll(cls.baseTool);
			parents.add(cls);
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
		for(EnumEnchantmentType ench : enchantmentTypes)
			this.enchantmentTypes.add(ench);
		return this;
	}

	public boolean canEnchantWith(Enchantment enchantment)
	{
		return getEnchantments().contains(enchantment) || getEnchantmentTypes().contains(enchantment.type);
	}

	public List<Material> getHarvestMaterials() {return harvestMaterials;}
	public List<Enchantment> getEnchantments() {return enchantments;}
	public List<EnumEnchantmentType> getEnchantmentTypes() {return enchantmentTypes;}
	public List<String> getBaseTools() {return baseTool;}

	public List<Enchantment> getAllEnchantments()
	{
		ArrayList<Enchantment> result = new ArrayList<>(getEnchantments());

		 Enchantment.REGISTRY.forEach(ench ->
		 {
		 	if(!result.contains(ench) && getEnchantmentTypes().contains(ench.type))
		 		result.add(ench);
		 });
		 return result;
	}

	public boolean isCompatibleWith(MSUToolClass other)
	{
		if(other == null)
			return false;

		for(MSUToolClass parent : other.parents)
			if(parent.isCompatibleWith(this))
				return true;
		return equals(other);
	}

	public MSUToolClass setDisablesShield()
	{
		disablesShield = true;
		return this;
	}

	public boolean disablesShield()
	{
		return disablesShield;
	}

	@Override
	public String toString() {
		return name;
	}
}
