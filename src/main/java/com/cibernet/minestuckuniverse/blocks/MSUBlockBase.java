package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MSUBlockBase extends Block
{
    public MSUBlockBase(Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setCreativeTab(TabMinestuckUniverse.instance);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, player, tooltip, advanced);
    }
    
    public MSUBlockBase(Material blockMaterialIn, MapColor blockMapColorIn, String registryName, String unlocalizedName)
    {
        this(blockMaterialIn, blockMapColorIn);
        this.setRegistryName(registryName);
        this.setUnlocalizedName(unlocalizedName);
    }

    public MSUBlockBase(Material blockMaterialIn, String registryName, String unlocalizedName)
    {
        this(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), registryName, unlocalizedName);
    }
}
