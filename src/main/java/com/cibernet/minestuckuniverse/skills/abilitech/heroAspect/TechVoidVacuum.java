package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechVoidVacuum extends TechHeroAspect
{
    public TechVoidVacuum(String name)
    {
        super(name, EnumAspect.VOID, EnumTechType.DEFENSE, EnumAspect.BREATH);
    }

    protected static final int RADIUS = 16;

    @Override
    public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
    {
        if(state == SkillKeyStates.KeyState.NONE)
            return false;

        if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
        {
            player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
            return false;
        }

        if(time > 15)
            badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.VOID, 20);
        else
            badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.VOID, 10);

        if (time % 3 == 0 && !player.isCreative())
            player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);

        float strength = Math.min(Math.max(0, time-10)/40f , 2);
        for(Entity target : world.getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().grow(RADIUS), (entity) -> entity != player))
        {
            Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize();

            target.velocityChanged = true;
            target.isAirBorne = true;
            float f = MathHelper.sqrt(vec.x * vec.x + vec.z * vec.z + vec.y * vec.y);
            target.motionX /= 2.0D;
            target.motionZ /= 2.0D;
            target.motionX += vec.x / (double)f * (double)strength;
            target.motionY += vec.y / (double)f * (double)strength;
            target.motionZ += vec.z / (double)f * (double)strength;

            /*
            if (target.onGround)
            {
                target.motionY /= 2.0D;
                target.motionY += (double)strength;
                if (target.motionY > 0.4000000059604645D)
                    target.motionY = 0.4000000059604645D;
            }
            */

        }

        return true;
    }
}
