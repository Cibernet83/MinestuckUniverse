package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.light;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechLightBubble extends TechHeroAspect
{
	public TechLightBubble(String name, long cost)
	{
		super(name, EnumAspect.LIGHT, cost, EnumTechType.DEFENSE);
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

		EntityBubble bubble = badgeEffects.getTether(techSlot) instanceof EntityBubble ? (EntityBubble) badgeEffects.getTether(techSlot) : null;

		if(bubble != null && bubble.isDead)
			bubble = null;

		if(state == SkillKeyStates.KeyState.PRESS)
		{
			if(bubble != null)
				bubble.setDead();
			bubble = new EntityBubble(world, 3, player.getName().equals("Cibernet") ? 0x66FFBA : 0xF4ECB7, 100, false, false, false);
			bubble.setPosition(player.posX, player.posY-0.05, player.posZ);
			world.spawnEntity(bubble);
			badgeEffects.setTether(bubble, techSlot);
		}
		
		if(bubble == null)
			return false;
		
		if(bubble != null && bubble.isEntityAlive())
			bubble.setLifespan(bubble.getLifespan()+1);

		if(!player.isCreative() && time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		if(player.getName().equals("Cibernet"))
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA,2, 0x00FF7F);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT,2);

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}
