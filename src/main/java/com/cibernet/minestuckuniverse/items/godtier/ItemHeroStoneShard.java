package com.cibernet.minestuckuniverse.items.godtier;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class ItemHeroStoneShard extends MSUItemBase
{
    public final EnumAspect aspect;

    public ItemHeroStoneShard(EnumAspect aspect)
    {
        super(("hero_stone_shard" + (aspect == null ? "" : ("_" + aspect.toString()))), "heroStoneShard");

        this.aspect = aspect;
        setCreativeTab(TabMinestuckUniverse.godTier);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {

        String heroAspect = TextFormatting.OBFUSCATED + "Thing" + TextFormatting.RESET;

        if(aspect != null)
            heroAspect = I18n.translateToLocal("title." + aspect.toString());

        return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", heroAspect).trim();
    }
}
