package com.cibernet.minestuckuniverse.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;

import java.util.UUID;

public class PotionBerserk extends MSUPotionBase
{
    public static final UUID SPEED_UUID = UUID.randomUUID();
    public static final UUID ATTACK_SPEED_UUID = UUID.randomUUID();
    public static final UUID ATTACK_UUID = UUID.randomUUID();

    protected PotionBerserk(boolean isBadEffectIn, int liquidColorIn, String name)
    {
        super(isBadEffectIn, liquidColorIn, name);
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, SPEED_UUID.toString(), 0.1D, 2);
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_UUID.toString(), 0.1D, 2);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);


        IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null)
        {
            AttributeModifier attributemodifier = new AttributeModifier(ATTACK_UUID, this.getName(), (amplifier+1)*Math.max(0.2, 1-entityLivingBaseIn.getHealth()/entityLivingBaseIn.getMaxHealth())*4, 0);
            iattributeinstance.removeModifier(attributemodifier);
            iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), this.getName() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);

        IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null)
            iattributeinstance.removeModifier(ATTACK_UUID);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        if(duration <= 5)
            return false;
        int i = 20 >> amplifier;
        if (i > 0)
            return duration % i == 0;
        else  return true;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if(entityLivingBaseIn instanceof EntityPlayer)
        {
            FoodStats stats = ((EntityPlayer) entityLivingBaseIn).getFoodStats();

            if(!((EntityPlayer) entityLivingBaseIn).isCreative())
                stats.addExhaustion(1);

            if(stats.getFoodLevel() <= 0 )
            {
                entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600));
                entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 2));
                entityLivingBaseIn.removeActivePotionEffect(MSUPotions.RAGE_BERSERK);
                removeAttributesModifiersFromEntity(entityLivingBaseIn, entityLivingBaseIn.getAttributeMap(), amplifier);
            }
        }
    }
}
