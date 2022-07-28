package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.BadgeEventHandler;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechLord extends TechHeroClass
{
	public TechLord(String name, long cost)
	{
		super(name, EnumClass.LORD, cost, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		boolean isOverlord = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.BADGE_OVERLORD);

		int chargeTime = isOverlord ? 9 : 18;
		int energy = isOverlord ? 6 : 12;

		if(title == null || state == SkillKeyStates.KeyState.NONE || time > chargeTime)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < energy)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		badgeEffects.startPowerParticles(getClass(), time > chargeTime-3 ? MSUParticles.ParticleType.BURST : MSUParticles.ParticleType.AURA, EnumClass.LORD, 20);

		if(time >= chargeTime)
		{
			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(48), (entity) -> entity != player))
			{
				if(target instanceof EntityPlayer && target.getDistance(player) >= 6)
				{
					int targetKarma = target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma();
					if(targetKarma != 0 && Math.signum(targetKarma) == Math.signum(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()))
						continue;
				}
				else if(!(target instanceof IMob)) continue;

				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, false)))
					continue;

				PotionEffect effect = BadgeEventHandler.NEGATIVE_EFFECTS.get(title.getHeroAspect());

				if(isOverlord)
				{
					switch(title.getHeroAspect())
					{
						case SPACE:
							target.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 800, 200));
							break;
						case LIGHT:
							target.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 1200, 2));
							break;
						case MIND:
							target.addPotionEffect(new PotionEffect(MSUPotions.MIND_CONFUSION, 300, 0));
							break;
						case TIME:
							target.addPotionEffect(new PotionEffect(MSUPotions.TIME_STOP, 600, 0));
							break;
						case DOOM:
							target.addPotionEffect(new PotionEffect(MSUPotions.DECAY, 115, 2));
							break;
						case RAGE:
							target.addPotionEffect(new PotionEffect(MSUPotions.RAGE_BERSERK, 300, 10));
							break;
						case VOID:
							target.attackEntityFrom(DamageSource.OUT_OF_WORLD, 18);
							break;
						case HOPE:
							target.attackEntityFrom(DamageSource.ON_FIRE, 30);
							break;
						case BREATH:
							target.attackEntityFrom(DamageSource.DROWN, 18);
							break;
						case LIFE:
						case HEART:
						case BLOOD:
							target.attackEntityFrom(DamageSource.MAGIC, 40);
							break;
					}
				}

				target.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration()*2 * (isOverlord ? 2 : 1), (int) ((effect.getAmplifier() + 1)*1.5f * (isOverlord ? 2 : 1)) - 1));
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.LORD, 10);
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - energy);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= (player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.BADGE_OVERLORD) ? 6 : 12) && super.isUsableExternally(world, player);
	}
}