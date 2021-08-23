package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MSUItemBase extends Item implements IRegistryItem
{

    final String registryName;
    boolean isSecret = false;

    public MSUItemBase(String name, String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(TabMinestuckUniverse.instance);
        registryName = name;
    }

    public MSUItemBase setSecret()
    {
        isSecret = true;
        return this;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(!isSecret)
            super.getSubItems(tab, items);
    }

    public MSUItemBase(String name)
    {
        this(name, name);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }
}
