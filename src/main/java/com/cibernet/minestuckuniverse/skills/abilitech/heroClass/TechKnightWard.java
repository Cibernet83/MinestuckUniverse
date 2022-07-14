package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechKnightWard extends TechHeroClass
{
	public TechKnightWard(String name, long cost)
	{
		super(name, EnumClass.KNIGHT, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE || time >= 45)
			return false;


		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time == 40)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 6)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(5,1,5), (entity) -> true))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
					continue;
				target.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2400, 2));
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 6);
		}

		if(time > 36)
			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.KNIGHT, 20);

		EntityLivingBase target = MSUUtils.getTargetEntity(player);
		if(time <= 40)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.KNIGHT, target == null ? 1 : 5);

		if(state == SkillKeyStates.KeyState.PRESS && target != null &&
				!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
		{
			target.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2400, 2));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2400, 2));
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 2);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 2 && super.isUsableExternally(world, player);
	}
}