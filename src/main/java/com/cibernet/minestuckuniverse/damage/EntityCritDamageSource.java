package com.cibernet.minestuckuniverse.damage;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class EntityCritDamageSource extends EntityDamageSource implements IGodTierDamage
{

    protected boolean isCrit = false;
    protected boolean godproof = false;

    public EntityCritDamageSource(String damageTypeIn, @Nullable Entity damageSourceEntityIn)
    {
        super(MinestuckUniverse.MODID+"."+damageTypeIn, damageSourceEntityIn);
    }

    @Override
    public boolean isDifficultyScaled() {
        return false;
    }

    @Override
    public EntityCritDamageSource setCrit()
    {
        isCrit = true;
        return this;
    }

    @Override
    public boolean isCrit() {
        return isCrit;
    }

    @Override
    public EntityCritDamageSource setGodproof()
    {
        godproof = true;
        return this;
    }

    @Override
    public boolean isGodproof() {
        return godproof;
    }
}
