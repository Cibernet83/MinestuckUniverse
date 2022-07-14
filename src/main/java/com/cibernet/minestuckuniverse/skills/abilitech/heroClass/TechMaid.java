package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechMaid extends TechHeroClass
{
	public TechMaid(String name, long cost)
	{
		super(name, EnumClass.MAID, cost, EnumTechType.DEFENSE);
	}

    @Override
    public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
    {
	    if(state == SkillKeyStates.KeyState.NONE || time > 40)
		    return false;

	    if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
	    {
		    player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
		    return false;
	    }

        EnumAspect aspect = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player)).getHeroAspect();

		if(time == 39)
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 8)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}

			for(EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(5,1,5), (entity) -> entity != player))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
					continue;

				if(!(target instanceof EntityPlayer))
				{
					target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, aspect, 10);
					target.addPotionEffect(new PotionEffect(GTEventHandler.aspectEffects[aspect.ordinal()], 2400, 3));
					continue;
				}
				EnumAspect targetAspect = MinestuckPlayerData.getTitle(IdentifierHandler.encode((EntityPlayer) target)).getHeroAspect();

				if(targetAspect == EnumAspect.HOPE || targetAspect == EnumAspect.MIND || targetAspect == EnumAspect.VOID)
				{
					target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, aspect, 10);
					target.addPotionEffect(new PotionEffect(MSUPotions.GOD_TIER_COMEBACK, 1200, 0));
					continue;
				}

				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MAID, 10);
				target.addPotionEffect(new PotionEffect(GTEventHandler.aspectEffects[targetAspect.ordinal()], 1500, (int) ((MinestuckPlayerData.getData(player).echeladder.getRung() * GTEventHandler.aspectStrength[targetAspect.ordinal()]) + 3)));
			}
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 8);
		}


		EntityLivingBase target = MSUUtils.getTargetEntity(player);
		if(time <= 36)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.MAID, target == null ? 1 : 5);
		else
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumClass.MAID, 20);
		if(state != SkillKeyStates.KeyState.PRESS)
			return true;

		if(target != null)
		{
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 2);
			if(!(target instanceof EntityPlayer))
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, aspect, 10);
				target.addPotionEffect(new PotionEffect(GTEventHandler.aspectEffects[aspect.ordinal()], 2400, 3));
				return true;
			}
			EnumAspect targetAspect = MinestuckPlayerData.getTitle(IdentifierHandler.encode((EntityPlayer) target)).getHeroAspect();
			if(targetAspect == EnumAspect.HOPE || targetAspect == EnumAspect.MIND || targetAspect == EnumAspect.VOID)
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, aspect, 10);
				target.addPotionEffect(new PotionEffect(MSUPotions.GOD_TIER_COMEBACK, 1200, 0));
				return true;
			}
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MAID, 10);
			target.addPotionEffect(new PotionEffect(GTEventHandler.aspectEffects[targetAspect.ordinal()], 1500, (int) ((MinestuckPlayerData.getData(player).echeladder.getRung() * GTEventHandler.aspectStrength[targetAspect.ordinal()]) + 3)));
		}
		return true;
	}
    
    @Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 2 && super.isUsableExternally(world, player);
	}
}