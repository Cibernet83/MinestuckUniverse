package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechLightBubble extends TechHeroAspect
{
	public TechLightBubble(String name)
	{
		super(name, EnumAspect.LIGHT, 10000, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityBubble bubble = badgeEffects.getActiveBubble();

		if(bubble != null && bubble.isDead)
			bubble = null;

		if(bubble == null)
		{
			bubble = new EntityBubble(world, 3, player.getName().equals("Cibernet") ? 0x66FFBA : 0xF4ECB7, 100, false, false);
			bubble.setPosition(player.posX, player.posY-0.05, player.posZ);
			world.spawnEntity(bubble);
			badgeEffects.setActiveBubble(bubble);
		}

		bubble.setLifespan(bubble.getLifespan()+1);

		if(time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		if(player.getName().equals("Cibernet"))
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA,2, 0x00FF7F);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.LIGHT,2);

		return super.onUseTick(world, player, badgeEffects, state, time);
	}

}
