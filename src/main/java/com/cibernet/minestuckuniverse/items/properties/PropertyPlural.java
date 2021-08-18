package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PropertyPlural extends WeaponProperty
{
    @Override
    public String getItemStackDisplayName(ItemStack stack, String name)
    {
        if(FMLCommonHandler.instance().getSide().equals(Side.CLIENT))
            return getClientsideDisplayName(stack, name);

        return super.getItemStackDisplayName(stack, name);
    }

    @SideOnly(Side.CLIENT)
    public String getClientsideDisplayName(ItemStack stack, String name)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if(player != null)
        {
            EnumHand mainHand = player.getHeldItem(EnumHand.MAIN_HAND).equals(stack) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            EnumHand offHand = mainHand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

            if(player.getHeldItem(mainHand).equals(stack) && player.getHeldItem(offHand).isItemEqual(stack))
                return I18n.translateToLocal(stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name.plural").trim();
        }

        return name;
    }
}
