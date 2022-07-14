package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.life;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechLifeBreed extends TechHeroAspect
{
	public TechLifeBreed(String name, long cost) {
		super(name, EnumAspect.LIFE, cost, EnumTechType.UTILITY);
	}

	protected static final int RADIUS = 8;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state.equals(SkillKeyStates.KeyState.NONE) || time >= 16)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 10)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.LIFE, 20);
        else
        	badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 6);

		if(time >= 15)
		{
			for(EntityAnimal target : world.getEntitiesWithinAABB(EntityAnimal.class, player.getEntityBoundingBox().grow(RADIUS)))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
					continue;
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.LIFE, 3);

				target.setInLove(player);
			}
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 4);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 4 && super.isUsableExternally(world, player);
	}
}
