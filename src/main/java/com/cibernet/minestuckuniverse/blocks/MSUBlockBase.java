package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class MSUBlockBase extends Block implements IRegistryItem
{
    final String registryName;

    public MSUBlockBase(Material blockMaterialIn, MapColor blockMapColorIn, String registryName)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setCreativeTab(TabMinestuckUniverse.main);
        this.registryName = registryName;
    }

    @SideOnly(Side.CLIENT)
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
        this(blockMaterialIn, blockMapColorIn, registryName);
        this.setUnlocalizedName(unlocalizedName);
    }

    public MSUBlockBase(Material blockMaterialIn, String registryName, String unlocalizedName)
    {
        this(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), registryName, unlocalizedName);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }
}
