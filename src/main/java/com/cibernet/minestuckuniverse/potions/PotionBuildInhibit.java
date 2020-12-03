package com.cibernet.minestuckuniverse.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionBuildInhibit extends MSUPotionBase
{

    protected PotionBuildInhibit(int liquidColorIn, String name)
    {
        super(true, liquidColorIn, name);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if(!(entityLivingBaseIn instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) entityLivingBaseIn;

        if(!player.isCreative())
        {
            player.capabilities.allowEdit = false;
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return (duration % 5) == 0;
    }
}
