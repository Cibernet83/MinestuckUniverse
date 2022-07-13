package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityMagicMissile;
import com.cibernet.minestuckuniverse.events.handlers.BadgeEventHandler;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechAspectralBolt extends TechBoondollarCost
{
	public TechAspectralBolt(String name, long cost)
	{
		super(name, cost);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));

		if(title == null)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}



		EntityMagicMissile missile = new EntityMagicMissile(world, player);
		missile.shoot(player, player.rotationPitch, player.rotationYawHead, 0, 0.5f, 0.2f);

		missile.setColor(BadgeEffects.getAspectParticleColors(title.getHeroAspect())[0]);

		missile.damage = player.isSneaking() ? 0 : 4;

		if(player.isSneaking())
			missile.effects.addAll(GTEventHandler.getAspectEffects(player).values());
		else
		{
			PotionEffect effect = BadgeEventHandler.NEGATIVE_EFFECTS.get(title.getHeroAspect());
			missile.effects.add(new PotionEffect(effect.getPotion(), effect.getAmplifier(), effect.getDuration()/2));
		}

		world.spawnEntity(missile);

		if(!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-2);

		return true;
	}

	@Override
	public boolean canUse(World world, EntityPlayer player)
	{
		return !(player.isPotionActive(MSUPotions.GOD_TIER_LOCK) && player.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() >= 1);
	}
}
