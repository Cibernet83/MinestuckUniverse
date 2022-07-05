package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class TechVoidDoomBubble extends TechHeroAspect
{
	public TechVoidDoomBubble(String name)
	{
		super(name, EnumAspect.VOID, 10000, EnumTechType.DEFENSE);
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

		RayTraceResult trace = MSUUtils.getMouseOver(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

		if(trace.getBlockPos() == null && trace.entityHit == null)
			return false;

		EntityBubble bubble = badgeEffects.getActiveBubble();

		if(bubble != null && bubble.isDead)
			bubble = null;

		if(bubble == null)
		{
			bubble = new EntityBubble(world, trace.entityHit == null ? 4 : Math.max(trace.entityHit.width, trace.entityHit.height)+1, 0x181633, 25, true, false);

			if(trace.entityHit != null)
				bubble.setPosition(trace.entityHit.posX, trace.entityHit.posY-0.05, trace.entityHit.posZ);
			else bubble.setPosition(trace.getBlockPos().getX(), trace.getBlockPos().getY()-0.05, trace.getBlockPos().getZ());
			world.spawnEntity(bubble);
			badgeEffects.setActiveBubble(bubble);
		}

		bubble.setLifespan(bubble.getLifespan()+1);

		if(time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA,2, 0x001856, 0x1C1C1C);

		return super.onUseTick(world, player, badgeEffects, state, time);
	}


	@Override
	public List<String> getTags()
	{
		List<String> list = super.getTags();
		list.add(0, "@DOOM@");

		return list;
	}


}
