package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

public class TechSylph extends TechHeroClass
{
	public TechSylph(String name, long cost)
	{
		super(name, EnumClass.SYLPH, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.RELEASED)
			badgeEffects.clearTether(techSlot);
		if(!state.equals(SkillKeyStates.KeyState.HELD))
			return false;
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		EntityLivingBase target = badgeEffects.getTether(techSlot) instanceof EntityLivingBase ? (EntityLivingBase) badgeEffects.getTether(techSlot) : null;
		if(target == null)
		{
			target = MSUUtils.getTargetEntity(player);
			badgeEffects.setTether(target, techSlot);
		}

		if(target == null || !(target.getHealth() < target.getMaxHealth() || (target instanceof EntityPlayer && ((EntityPlayer) target).getFoodStats().needFood())))
			return false;

		if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, true)))
			return false;

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.SYLPH, 5);

		if((time % 10) == 0)
		{
			target.heal(2);
			if(target instanceof EntityPlayer && ((EntityPlayer) target).getFoodStats().needFood())
				((EntityPlayer) target).getFoodStats().addStats(1, 1);

			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 2);
			((WorldServer)world).spawnParticle(EnumParticleTypes.HEART, target.posX + ((Math.random()-0.5)/2), target.posY+1.5, target.posZ + ((Math.random()-0.5)/2), 1, 1, 0, 0.5, 0);
		}

			
		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}