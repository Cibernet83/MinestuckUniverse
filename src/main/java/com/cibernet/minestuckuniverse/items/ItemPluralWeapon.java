package com.cibernet.minestuckuniverse.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPluralWeapon extends MSUWeaponBase
{
    public ItemPluralWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
    }

    public ItemPluralWeapon(double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
        super(damageVsEntity, weaponSpeed, enchantability, name, unlocName);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        try {
            return getClientsideDisplayName(stack);
        }
        catch(NoSuchMethodError e)
        {}
        return super.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    public String getClientsideDisplayName(ItemStack stack)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if(player != null)
        {
            EnumHand mainHand = player.getHeldItem(EnumHand.MAIN_HAND).equals(stack) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            EnumHand offHand = mainHand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

            if(player.getHeldItem(mainHand).equals(stack) && player.getHeldItem(offHand).isItemEqual(stack))
                return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + "Plural.name").trim();
        }

        return super.getItemStackDisplayName(stack);
    }
}
