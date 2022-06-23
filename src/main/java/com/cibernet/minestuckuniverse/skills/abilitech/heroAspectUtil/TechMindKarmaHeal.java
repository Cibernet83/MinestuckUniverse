package com.cibernet.minestuckuniverse.skills.abilitech.heroAspectUtil;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumRole;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechMindKarmaHeal extends TechHeroAspect
{
	public TechMindKarmaHeal(String name) {
		super(name, EnumAspect.MIND, EnumRole.NEUTRAL);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);

		if (target instanceof EntityPlayer)
		{
			IGodTierData targetData = target.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			int tickMod = (int)(4 + 0.4f * player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTempKarma());
			if ((targetData.getStaticKarma() != 0 || targetData.getTempKarma() != 0) && time % tickMod == 0)
			{
				if (targetData.getStaticKarma() != 0)
					targetData.setStaticKarma(targetData.getStaticKarma() + (targetData.getStaticKarma() > 0 ? -1 : 1));
				else
					targetData.setTempKarma(targetData.getTempKarma() + (targetData.getTempKarma() > 0 ? -1 : 1));

				if (time % (tickMod * 2) == 0)
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);

				badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.MIND, 4);
			}

		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 2);

		return true;
	}
}
