package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechBloodBubble extends TechHeroAspect
{
	public TechBloodBubble(String name)
	{
		super(name, EnumAspect.BLOOD, 10000, EnumTechType.DEFENSE);
	}

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

		EntityBubble bubble = badgeEffects.getTether(techSlot) instanceof EntityLivingBase ? (EntityBubble) badgeEffects.getTether(techSlot) : null;

		if(bubble != null && bubble.isDead)
			bubble = null;

		if(bubble == null)
		{
			bubble = new EntityBubble(world, 3, 0xB71015, 25, false, true, false);
			bubble.setPosition(player.posX, player.posY-0.05, player.posZ);
			world.spawnEntity(bubble);
			badgeEffects.setTether(bubble, techSlot);
		}

		bubble.setLifespan(bubble.getLifespan()+1);

		if(!player.isCreative() && time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BLOOD,2);

		return true;
	}

}
