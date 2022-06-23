package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechRageOutburst extends TechHeroAspect
{
	public TechRageOutburst(String name) {
		super(name, EnumAspect.RAGE, EnumRole.PASSIVE, EnumAspect.MIND);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE || time >= 25)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 12)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time > 20)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.RAGE, 10);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);

		if(time >= 24)
		{
			float dmg = Math.max(8, Math.abs(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTempKarma()));

			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(16), (entity) -> entity != player && (entity instanceof EntityPlayer || entity instanceof IMob)))
				if(!player.isOnSameTeam(target)) target.attackEntityFrom(new EntityCritDamageSource("vengefulOutburst", player).setCrit().setDamageBypassesArmor(), dmg);
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 12);
		}

		return true;
	}
}