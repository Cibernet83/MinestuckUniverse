package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import java.util.HashMap;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechMaidServe extends TechHeroClass
{

	public TechMaidServe(String name, long cost)
	{
		super(name, EnumClass.MAID, cost, EnumTechType.PASSIVE, EnumTechType.DEFENSE);
	}
	
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		boolean serving = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this);
		if (state == SkillKeyStates.KeyState.PRESS)
        {
            player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setSkillPassiveEnabled(this, !serving);
            player.sendStatusMessage(new TextComponentTranslation(!serving ? "status.badgeEnabled" : "status.badgeDisabled", getDisplayComponent()), true);
        }
        return true;
	}

	@Override
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot)
	{
		if(!player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this))
			return false;
		for(Entity entity : player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(3)))
			if(entity instanceof EntityLivingBase && (entity instanceof EntityAnimal || entity instanceof EntityPlayer &&
					!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, entity, this, techSlot, true))))
			{
				HashMap<Potion, PotionEffect> effectsToBeAdded = GTEventHandler.getAspectEffects(player);
				for(Potion pot : effectsToBeAdded.keySet())
				{
					PotionEffect currentPotionEffect = ((EntityLivingBase) entity).getActivePotionEffect(pot);
					if ((currentPotionEffect == null && (!GTEventHandler.REFRESH_POTIONS.contains(pot) || entity.ticksExisted%600 == 0)) || (currentPotionEffect != null && currentPotionEffect.getDuration() <= (pot.equals(MobEffects.NIGHT_VISION) ? 200 : 20) && currentPotionEffect.getAmplifier() <= effectsToBeAdded.get(pot).getAmplifier() && !GTEventHandler.REFRESH_POTIONS.contains(pot)))
						player.addPotionEffect(new PotionEffect(pot, (pot.equals(MobEffects.NIGHT_VISION) ? 300 : 100), effectsToBeAdded.get(pot).getAmplifier(), true, true));
				}
			}
		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}
	
}
