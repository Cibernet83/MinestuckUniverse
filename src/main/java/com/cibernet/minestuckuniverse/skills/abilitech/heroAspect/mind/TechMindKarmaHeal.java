package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechMindKarmaHeal extends TechHeroAspect
{
	public TechMindKarmaHeal(String name, long cost) {
		super(name, EnumAspect.MIND, cost, EnumTechType.DEFENSE, EnumTechType.PASSIVE);
	}

	@Override
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot)
	{
		IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		if(data != null && data.getStaticKarma() != 0 && world.getTotalWorldTime() % 1200 == 0)
		{
			data.setStaticKarma((int) (data.getStaticKarma() - Math.signum(data.getTempKarma())));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
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
