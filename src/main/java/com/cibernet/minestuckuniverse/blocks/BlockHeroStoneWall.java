package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.BlockWall;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHeroStoneWall extends BlockWall implements IGodTierBlock, IRegistryItem
{
    protected final EnumAspect aspect;
    private final String name;

    public BlockHeroStoneWall(EnumAspect aspect)
    {
        super(aspect == null ? MinestuckUniverseBlocks.wildcardHeroStone : MinestuckUniverseBlocks.heroStones.get(aspect));
        this.aspect = aspect;

        setHarvestLevel("pickaxe", 3);
        setHardness(20.0F);
        setBlockUnbreakable();
        setCreativeTab(TabMinestuckUniverse.godTier);

        name = ("hero_stone_wall" + (aspect == null ? "" : ("_" + aspect.toString())));
        setUnlocalizedName("heroStoneWall");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this));
    }

    @Override
    public boolean canGodTier() {
        return false;
    }

    @Override
    public EnumAspect getAspect()
    {
        return aspect;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        String heroAspect = TextFormatting.OBFUSCATED + "Thing" + TextFormatting.RESET;

        if(getAspect() != null)
            heroAspect = getAspect().getDisplayName();


        tooltip.add(heroAspect);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(name);
    }
}
