package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TechSeerDodge extends TechHeroClass
{
	public TechSeerDodge(String name, long cost)
	{
		super(name, EnumClass.SEER, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		boolean canDodge = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this);
		if (state == SkillKeyStates.KeyState.PRESS)
        {
            player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setSkillPassiveEnabled(this, !canDodge);
            player.sendStatusMessage(new TextComponentTranslation(!canDodge ? "status.badgeEnabled" : "status.badgeDisabled", getDisplayComponent()), true);
        }
        return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingAttack(LivingAttackEvent event)
	{
		if(event.getEntityLiving().world.isRemote)
			return;
		
		IGodTierData data = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		IBadgeEffects effects = event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null);

		if((GTEventHandler.BLOCKABLE_UNBLOCKABLES.contains(event.getSource()) || event.getSource().isMagicDamage() || !event.getSource().isUnblockable())
				&& data != null && data.isTechPassiveEnabled(MSUSkills.FORESIGHT_DODGE) && effects != null && Math.abs(event.getEntityLiving().ticksExisted - effects.getLastSeerDodge()) >= 1200)
		{
			EntityLivingBase entity = event.getEntityLiving();

			effects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.SEER, 20);
			effects.setLastSeerDodge((int) event.getEntityLiving().ticksExisted);
			
			event.setCanceled(true);

			entity.moveRelative(0, (float)Math.cos((entity.rotationPitch+90)*Math.PI/180f)/(MSUUtils.isTrulyOnGround(entity) ? 2.5F : 1.25F), (float)Math.sin((entity.rotationPitch+90)*Math.PI/180f), MSUUtils.isTrulyOnGround(entity) ? 3F : 1.5F);
			entity.velocityChanged = true;
		}
	}
}