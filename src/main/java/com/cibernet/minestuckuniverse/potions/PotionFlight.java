package com.cibernet.minestuckuniverse.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionFlight extends MSUPotionBase
{
    protected PotionFlight(boolean isBadEffectIn, int liquidColorIn, String name)
    {
        super(isBadEffectIn, liquidColorIn, name);
        if(!isBadEffectIn)
            setBeneficial();
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if(!(entityLivingBaseIn instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) entityLivingBaseIn;

        if(!player.isSpectator())
        {
            player.capabilities.allowFlying = !isBadEffect();
            if(isBadEffect())
                player.capabilities.isFlying = false;
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {

        return (duration % 5) == 0;
    }
}
