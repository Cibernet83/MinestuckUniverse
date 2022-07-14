package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.doom;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechDoomBind extends TechHeroAspect
{
	public TechDoomBind(String name, long cost)
	{
		super(name, EnumAspect.DOOM, cost, EnumTechType.PASSIVE, EnumTechType.DEFENSE);
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving().world.isRemote || !(GTEventHandler.BLOCKABLE_UNBLOCKABLES.contains(event.getSource()) || event.getSource().isMagicDamage() || !event.getSource().isUnblockable()))
			return;

		IGodTierData data = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		if(data != null && data.isTechPassiveEnabled(MSUSkills.DOOM_SURVIVORS_BIND) &&
			event.getEntityLiving().getHealth() > 5 && event.getAmount() >= event.getEntityLiving().getHealth())
		{
			event.setAmount(event.getEntityLiving().getHealth()-1);
			event.getEntityLiving().hurtResistantTime = event.getEntityLiving().maxHurtResistantTime*2;
			event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.DOOM, 15);
		}
	}
}
