package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechKnightHalt extends TechHeroClass {
	public TechKnightHalt(String name, long cost) {
		super(name, EnumClass.KNIGHT, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 3)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(state == SkillKeyStates.KeyState.NONE || time >= 60)
			return false;


		if(time % 20 == 0)
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 3);

		for(Entity target : world.getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().grow(5,1,5), (entity) -> !(entity instanceof EntityLivingBase)))
		{
			target.motionX = 0;
			target.motionY = 0;
			target.motionZ = 0;
			target.velocityChanged = true;

			if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
				continue;
		}

		badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.KNIGHT, 20);

		return true;
	}
}
