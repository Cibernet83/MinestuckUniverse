package com.cibernet.minestuckuniverse.skills.badges;

import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.Skill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class Badge extends Skill
{
    public static final List<Badge> BADGES = new ArrayList<>();

    public Badge()
    {
        super();
        BADGES.add(this);
    }

    public String getUnlocalizedName()
    {
        return "badge." + unlocalizedName;
    }

    @Override
    public boolean canDisable() { return true; }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getTextureLocation()
    {
        return new ResourceLocation(getRegistryName().getResourceDomain(), "textures/gui/badges/"+getRegistryName().getResourcePath()+".png");
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

    public void onBadgeUnlocked(World world, EntityPlayer player)
    {
    }
}
