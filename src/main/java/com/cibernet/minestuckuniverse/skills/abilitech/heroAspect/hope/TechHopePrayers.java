package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class TechHopePrayers extends TechHeroAspect
{
	public TechHopePrayers(String name, long cost) {
		super(name, EnumAspect.HOPE, cost, EnumTechType.DEFENSE); //, EnumAspect.LIGHT);
	}

	protected static final int RADIUS = 20;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time) {

		if (state == SkillKeyStates.KeyState.NONE || time > 40)
			return false;
		
		if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 8) {
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time == 20)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.HOPE, 10);

			List<Potion> potionPoolPos = new ArrayList<>();
			Potion.REGISTRY.forEach(potion ->
			{
				if (!potion.isBadEffect())
					potionPoolPos.add(potion);
			});

			Potion potion = potionPoolPos.get(world.rand.nextInt(potionPoolPos.size()));

			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(RADIUS), target -> target != player && !(target instanceof IMob) && !target.isSpectator()))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, true)))
					continue;
				
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HOPE, 10);

				target.addPotionEffect(new PotionEffect(potion, potion.isInstant() ? 0 : 300, 2));

				if(target instanceof EntityHopeGolem && ((EntityHopeGolem) target).getOwner().equals(player))
					((EntityHopeGolem) target).setHopeTicks(((EntityHopeGolem) target).getHopeTicks() + 100);
			}

			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);

			if (state == SkillKeyStates.KeyState.NONE || time >= 19)
				return false;

			if (!player.isCreative() && player.getFoodStats().getFoodLevel() < 6) {
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}
		}
		if(time < 40)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HOPE, time < 20 ? 2 : 6);
		else
		{
			List<Potion> potionPoolPos = new ArrayList<>();
			Potion.REGISTRY.forEach(potion ->
			{
				if(!potion.isBadEffect())
					potionPoolPos.add(potion);
			});

			Potion potion = potionPoolPos.get(world.rand.nextInt(potionPoolPos.size()));
			player.addPotionEffect(new PotionEffect(potion, potion.isInstant() ? 0 : 300, 2));
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 6);
		}

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 8 && super.isUsableExternally(world, player);
	}
}
