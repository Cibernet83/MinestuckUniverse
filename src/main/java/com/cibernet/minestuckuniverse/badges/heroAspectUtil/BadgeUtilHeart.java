package com.cibernet.minestuckuniverse.badges.heroAspectUtil;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.util.SoulData;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Collections;

public class BadgeUtilHeart extends BadgeHeroAspectUtil
{
	public BadgeUtilHeart()
	{
		super(EnumAspect.HEART, new ItemStack(Items.ENDER_EYE, 200));
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if (state != SkillKeyStates.KeyState.HELD)
			return false;

		if (time < 60)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 2);
			return true;
		}

		EntityLivingBase target = MSUUtils.getTargetEntity(player);
		if (time != 60 || !(target instanceof EntityPlayer))
			return false;

		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		SoulData targetSoulData = new SoulData(target);
		SoulData playerSoulData = new SoulData(player);

		targetSoulData.apply(player);
		playerSoulData.apply(target);

		PotionEffect effect = new PotionEffect(MSUPotions.GOD_TIER_LOCK, 100);
		effect.setCurativeItems(Collections.emptyList());
		target.addPotionEffect(effect);

		if (!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 4);
		target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, 2);

		return true;
	}
}
