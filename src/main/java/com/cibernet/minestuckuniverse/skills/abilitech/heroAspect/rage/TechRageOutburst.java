package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class TechRageOutburst extends TechHeroAspect
{
	public TechRageOutburst(String name, long cost) {
		super(name, EnumAspect.RAGE, cost, EnumTechType.OFFENSE);//, EnumAspect.MIND);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		float dmg = Math.max(20, Math.abs(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTempKarma())/2f);

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < dmg/2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}


		if(time >= 10)
		{
			if(state == SkillKeyStates.KeyState.RELEASED)
			{
				for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(16), (entity) -> entity != player && (entity instanceof EntityPlayer || entity instanceof IMob)))
				{
					if(!player.isOnSameTeam(target) && !MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, false)))
						target.attackEntityFrom(new EntityCritDamageSource("vengefulOutburst", player).setCrit().setDamageBypassesArmor(), dmg);
				}
				if (!player.isCreative() && !world.isRemote)
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - (int)(dmg*0.75f));
			}
			else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);

		} else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.RAGE, 5);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= Math.max(40, Math.abs(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTempKarma()))*.375f && super.isUsableExternally(world, player);
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return super.canAppearOnList(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		return super.canUnlock(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}
}