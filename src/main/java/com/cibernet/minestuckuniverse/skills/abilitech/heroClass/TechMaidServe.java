package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.mraof.minestuck.util.EnumClass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechMaidServe extends TechHeroClass
{

	public TechMaidServe(String name)
	{
		super(name, EnumClass.MAID);
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
	
	//TODO: make this only work on allied players
	@Override
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot)
	{
		if(!player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this))
			return false;
		for(Entity entity : player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(3)))
			if(entity instanceof EntityLivingBase && (entity instanceof EntityAnimal || entity instanceof EntityPlayer))
				for(Potion pot : GTEventHandler.getAspectEffects(player).keySet())
					if(!(((EntityLivingBase) entity).isPotionActive(pot) && ((EntityLivingBase) entity).getActivePotionEffect(pot).getDuration() <= 20))
						((EntityLivingBase) entity).addPotionEffect(new PotionEffect(pot, 100, GTEventHandler.getAspectEffects(player).get(pot).getAmplifier(), true, true));
		return true;
	}
	
}
