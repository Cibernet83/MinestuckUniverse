package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemKnittingNeedles extends ItemPluralWeapon
{
    public ItemKnittingNeedles(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
        setMaxStackSize(2);

        this.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID,"plural"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return stack.getCount() >= 2 ? 1 : 0;
            }
        });
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return stack.getCount() >= 2 ? I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + "Plural.name").trim() : super.getItemStackDisplayName(stack);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        return stack.getCount() >= 2 ? 0 : super.getAttackDamage(stack);
    }
}
