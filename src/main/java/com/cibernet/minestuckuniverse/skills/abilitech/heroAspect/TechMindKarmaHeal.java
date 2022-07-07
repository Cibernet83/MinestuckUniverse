package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

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
	public TechMindKarmaHeal(String name) {
		super(name, EnumAspect.MIND, EnumTechType.UTILITY);
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
}
