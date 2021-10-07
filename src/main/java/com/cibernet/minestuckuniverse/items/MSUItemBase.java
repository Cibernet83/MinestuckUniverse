package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MSUItemBase extends Item implements IRegistryItem
{

    final String registryName;
    boolean isSecret = false;

    public static final ArrayList<String> DEDICATED_TOOLTIPS = new ArrayList<String>()
    {{
        add("Cibernet");
        add("Ishumire");
        add("Badadamadaba");
        add("ThatLameOverlord");
        add("Nhezak");
        add("Fishwreck");
        add("Akisephila");
        add("ZeroCraftsman");
        add("_draconix");
        add("Owo_XxX_owO");
        add("carefreeDesigner");
    }};

    public MSUItemBase(String name, String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(TabMinestuckUniverse.main);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        String playerName = Minecraft.getMinecraft().player == null ? "" : Minecraft.getMinecraft().player.getName();

        if(DEDICATED_TOOLTIPS.contains(playerName) && net.minecraft.client.resources.I18n.hasKey(key+"."+playerName))
                tooltip.add(I18n.translateToLocal(key+"."+playerName));
        else if(net.minecraft.client.resources.I18n.hasKey(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }
}
