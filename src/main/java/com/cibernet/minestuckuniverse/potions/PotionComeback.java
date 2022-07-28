package com.cibernet.minestuckuniverse.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class PotionComeback extends MSUPotionBase
{

    public static final UUID STRENGTH_UUID = UUID.randomUUID();

    protected PotionComeback(boolean isBadEffectIn, int liquidColorIn, String name)
    {
        super(isBadEffectIn, liquidColorIn, name);
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, STRENGTH_UUID.toString(), 3, 0);
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return 3 * (double)(amplifier + 1);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
            entityLivingBaseIn.heal(1.0F);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        if(duration < 60)
            return false;
        int i = 20 >> amplifier;
        if (i > 0)
            return duration % i == 0;
        return true;
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.isPotionActive(MSUPotions.GOD_TIER_COMEBACK) && event.getSource() != DamageSource.OUT_OF_WORLD)
            event.setAmount(event.getAmount() * (float)(25-((entity.getActivePotionEffect(MSUPotions.GOD_TIER_COMEBACK).getAmplifier() + 1) * 5)) / 25f);
    }
}
