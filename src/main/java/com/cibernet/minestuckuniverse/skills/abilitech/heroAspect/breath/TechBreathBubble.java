package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechBreathBubble extends TechHeroAspect
{
	public static final DamageSource DAMAGE_SOURCE = new DamageSource(MinestuckUniverse.MODID+".suffocate").setDamageBypassesArmor();

	public TechBreathBubble(String name, long cost)
	{
		super(name, EnumAspect.BREATH, cost, EnumTechType.OFFENSE);
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

		RayTraceResult trace = MSUUtils.getMouseOver(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

		if(trace.getBlockPos() == null && trace.entityHit == null)
			return false;

		EntityBubble bubble = badgeEffects.getTether(techSlot) instanceof EntityBubble ? (EntityBubble) badgeEffects.getTether(techSlot) : null;

		if(bubble != null && bubble.isDead)
			bubble = null;

		if(state == SkillKeyStates.KeyState.PRESS)
		{
			if(bubble != null)
				bubble.setDead();
			bubble = new EntityBubble(world, trace.entityHit == null ? 1 : Math.max(trace.entityHit.width, trace.entityHit.height) * 1.1f, 0x47E2FA, 25, false, false, false);
			bubble.setSuffocates(true);

			if(trace.entityHit != null)
				bubble.setPosition(trace.entityHit.posX, trace.entityHit.posY-0.05, trace.entityHit.posZ);
			else bubble.setPosition(trace.getBlockPos().getX(), trace.getBlockPos().getY()-0.05, trace.getBlockPos().getZ());
			world.spawnEntity(bubble);
			badgeEffects.setTether(bubble, techSlot);
		}
		if(bubble != null && bubble.isEntityAlive())
			bubble.setLifespan(bubble.getLifespan()+1);

		if(!player.isCreative() && time % 10 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BREATH,2);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}
