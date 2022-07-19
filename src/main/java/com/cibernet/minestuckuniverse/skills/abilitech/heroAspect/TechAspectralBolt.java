package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityMagicMissile;
import com.cibernet.minestuckuniverse.events.handlers.BadgeEventHandler;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechAspectralBolt extends TechBoondollarCost
{
	public TechAspectralBolt(String name, long cost)
	{
		super(name, cost, EnumTechType.OFFENSE);
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

		missile.setColor(BadgeEffects.getAspectParticleColors(title.getHeroAspect())[Math.min(player.isSneaking() ? 1 : 0, BadgeEffects.getAspectParticleColors(title.getHeroAspect()).length-1)] );

		missile.damage = player.isSneaking() ? 0 : 4;

		if(player.isSneaking())
			missile.effects.addAll(GTEventHandler.getAspectEffects(player).values());
		else
		{
			PotionEffect effect = BadgeEventHandler.NEGATIVE_EFFECTS.get(title.getHeroAspect());
			missile.effects.add(new PotionEffect(effect.getPotion(), effect.getDuration()/2, effect.getAmplifier()/2));
		}

		missile.shootingEntity = player;
		world.spawnEntity(missile);
		player.swingArm(EnumHand.MAIN_HAND);

		if(!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-2);

		return true;
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return (world.isRemote ? MinestuckPlayerData.title : MinestuckPlayerData.getData(player).title) != null;
	}

	@Override
	public boolean canUse(World world, EntityPlayer player)
	{
		return !(player.isPotionActive(MSUPotions.GOD_TIER_LOCK) && player.getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() >= 1);
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return super.isUsableExternally(world, player) && (player.isCreative() || player.getFoodStats().getFoodLevel() >= 2);
	}
}
