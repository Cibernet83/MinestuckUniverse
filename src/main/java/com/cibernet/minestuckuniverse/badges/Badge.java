package com.cibernet.minestuckuniverse.badges;

import com.cibernet.minestuckuniverse.potions.MSUPotions;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Badge extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Badge> implements Comparable<Badge>
{
    String unlocalizedName;
    int sortIndex;
    static int sort = 0;

    public Badge()
    {
        this.sortIndex = sort++;
    }

    public String getUnlocalizedName()
    {
        return "badge." + unlocalizedName;
    }

    public String getDisplayName()
    {
        return getDisplayComponent().getFormattedText();
    }

    public TextComponentTranslation getDisplayComponent()
    {
        return new TextComponentTranslation(getUnlocalizedName()+".name");
    }

    @SideOnly(Side.CLIENT)
    public String getDisplayTooltip()
    {
        return I18n.format(getUnlocalizedName() + ".tooltip");
    }

    public String getReadRequirements()
    {
        return new TextComponentTranslation(getUnlocalizedName()+".read").getFormattedText();
    }

    public String getUnlockRequirements()
    {
        return new TextComponentTranslation(getUnlocalizedName()+".unlock").getFormattedText();
    }

    public Badge setUnlocalizedName(String unlocalizedName)
    {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public boolean canAppearOnList(World world, EntityPlayer player)
    {
        return true;
    }

    public boolean isReadable(World world, EntityPlayer player)
    {
        return true;
    }

    public boolean canUnlock(World world, EntityPlayer player)
    {
        return true;
    }

    public boolean canUse(World world, EntityPlayer player) { return !(player.isPotionActive(MSUPotions.GOD_TIER_LOCK) && player.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() >= 2); }

    public boolean canDisable() { return true; }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getTextureLocation()
    {
        return new ResourceLocation(getRegistryName().getResourceDomain(), "textures/gui/badges/"+getRegistryName().getResourcePath()+".png");
    }

    public int getSortIndex()
    {
        return sortIndex;
    }

    public static boolean findItem(EntityPlayer player, ItemStack stack, boolean decr)
    {
        return findItem(player, stack, decr, true);
    }

    public static boolean findItem(EntityPlayer player, ItemStack stack, boolean decr, boolean ignoreMeta)
    {
        stack = stack.copy();
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack invStack = player.inventory.getStackInSlot(i);
            if(!decr)
                invStack = invStack.copy();

            if(ignoreMeta ? invStack.getItem() == stack.getItem() : invStack.isItemEqual(stack))
            {
                if(stack.getCount() > invStack.getCount())
                {
                    stack.setCount(stack.getCount() - invStack.getCount());
                    invStack.setCount(0);
                }
                else
                {
                    invStack.setCount(invStack.getCount()-stack.getCount());
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public int compareTo(Badge o) {
        return this.sortIndex - o.sortIndex;
    }

    public void onBadgeUnlocked(World world, EntityPlayer player)
    {
    }
}
