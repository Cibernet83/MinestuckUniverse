package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.captchalogue.OperandiModus;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;

public class OperandiArmorItem extends ItemArmor implements IRegistryItem
{
	public static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("OPERANDI", MinestuckUniverse.MODID+":operandi", 1, new int[] {1, 1, 1, 1}, -1, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0);

	final String name;

	public OperandiArmorItem(String name, EntityEquipmentSlot equipmentSlotIn)
	{
		super(MATERIAL, 5, equipmentSlotIn);
		
		setCreativeTab(TabMinestuck.instance);
		
		setUnlocalizedName(name);
		this.name = name;
		OperandiModus.itemPool.add(this);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}
	
	@Override
	public boolean isRepairable()
	{
		return false;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		if(damage > getMaxDamage(stack))
			super.setDamage(stack, damage);
	}

	@Override
	public void setRegistryName() {
		setRegistryName(name);
	}
}
