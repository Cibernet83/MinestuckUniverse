package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.mraof.minestuck.item.weapon.ItemWeapon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MSUWeaponBase extends ItemWeapon
{
    public MSUWeaponBase(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, unlocName);
        this.setRegistryName(name);
        this.setCreativeTab(TabMinestuckUniverse.instance);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    //TODO
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
