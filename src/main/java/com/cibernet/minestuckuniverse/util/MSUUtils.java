package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

import java.util.Random;

public class MSUUtils
{
    public static final int MACHINE_CHASIS_GUI = 0;
    public static final int AUTO_CAPTCHA_GUI = 1;
    public static final int PORKHOLLOW_ATM_GUI = 2;
    
    public static void registerDeployList()
    {
        DeployList.registerItem("holopad", new ItemStack(MinestuckUniverseBlocks.holopad), new GristSet(GristType.Build, MinestuckUniverse.isArsenalLoaded ? 10000 : 1000), 2);
    }
    
    public static boolean compareCards(ItemStack card1, ItemStack card2, boolean ignoreStacksize)
    {
        ItemStack stack1 = AlchemyRecipes.getDecodedItem(card1);
        ItemStack stack2 = AlchemyRecipes.getDecodedItem(card2);
        if(!card1.isItemEqual(card2))
            return false;
        if(!card1.hasTagCompound() || !card2.hasTagCompound())
            return true;
        if(card1.getTagCompound().getBoolean("punched") != card2.getTagCompound().getBoolean("punched"))
            return false;
        if(!ignoreStacksize && stack1.getCount() != stack2.getCount())
            return false;
        if(stack1.hasTagCompound() != stack2.hasTagCompound())
            return false;
        if(stack1.hasTagCompound() && stack2.hasTagCompound())
            return stack1.getTagCompound().equals(stack2.getTagCompound());
        else return true;
    }
    
    public static void giveBoonItem(EntityPlayer reciever, int value)
    {
        ItemStack stack = ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), value);
        if(!reciever.addItemStackToInventory(stack))
        {
            EntityItem entity = reciever.dropItem(stack, false);
            if(entity != null)
                entity.setNoPickupDelay();
        } else reciever.inventoryContainer.detectAndSendChanges();
    }
    
    public static void spawnEntityParticles(Entity entityIn, EnumParticleTypes particleTypes)
    {
        Random rand = new Random();
        if (entityIn.world.isRemote)
        {
            for (int i = 0; i < 20; ++i)
            {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                double d3 = 10.0D;
                entityIn.world.spawnParticle(particleTypes, entityIn.posX + (double)(rand.nextFloat() * entityIn.width * 2.0F) - (double)entityIn.width - d0 * 10.0D,
                        entityIn.posY + (double)(rand.nextFloat() * entityIn.height) - d1 * 10.0D,
                        entityIn.posZ + (double)(rand.nextFloat() * entityIn.width * 2.0F) - (double)entityIn.width - d2 * 10.0D, d0, d1, d2);
            }
        }
        else
        {
            entityIn.world.setEntityState(entityIn, (byte)20);
        }
    }
}
