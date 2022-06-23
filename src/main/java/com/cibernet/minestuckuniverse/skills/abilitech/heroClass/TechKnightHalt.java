package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechKnightHalt extends TechHeroClass {
	public TechKnightHalt(String name) {
		super(name, EnumClass.KNIGHT);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 5)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(state == SkillKeyStates.KeyState.NONE || time >= 25)
			return false;

		if(time >= 20)
		{
			if(time == 20)
				if (!player.isCreative())
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 5);

			for(Entity target : world.getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().grow(5,1,5), (entity) -> !(entity instanceof EntityLivingBase)))
			{
				target.motionX = 0;
				target.motionY = 0;
				target.motionZ = 0;
			}

			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.KNIGHT, 20);
		}
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.KNIGHT, 3);
		return true;
	}
}
