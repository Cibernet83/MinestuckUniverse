package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import java.util.HashMap;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.events.handlers.BadgeEventHandler;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechWitchTrap extends TechHeroClass
{
	
	public TechWitchTrap(String name, long cost)
	{
		super(name, EnumClass.WITCH, cost, EnumTechType.OFFENSE);
	}
	
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		RayTraceResult trace = MSUUtils.getMouseOver(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

		if(trace.getBlockPos() == null && trace.entityHit == null)
			return false;

		EntityAreaEffectCloud cloud = badgeEffects.getTether(techSlot) instanceof EntityAreaEffectCloud ? (EntityAreaEffectCloud) badgeEffects.getTether(techSlot) : null;

		if(cloud != null && cloud.isDead)
			cloud = null;
		
		if(cloud == null)
		{
			IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(player);

			Title title = MinestuckPlayerData.getTitle(identifier);
			if(title == null)
				return false;
			EnumAspect aspect = title.getHeroAspect();
			
			cloud = new EntityAreaEffectCloud(world);
			
			PotionEffect pot = BadgeEventHandler.NEGATIVE_EFFECTS.get(aspect);
			pot = new PotionEffect(pot.getPotion(), 30, pot.getAmplifier(), false, false);
			cloud.addEffect(pot);
			cloud.setRadius(3.5F);
			cloud.setOwner(player);
			cloud.setDuration(30);
			cloud.setRadiusOnUse(0);
			int[] colors = BadgeEffects.getAspectParticleColors(aspect);
			if(colors.length > 0)
				cloud.setColor(colors[0]);

			if(trace.entityHit != null)
				cloud.setPosition(trace.entityHit.posX, trace.entityHit.posY-0.05, trace.entityHit.posZ);
			else
				cloud.setPosition(trace.getBlockPos().getX(), trace.getBlockPos().getY() + (trace.sideHit == EnumFacing.UP ? 1 : trace.sideHit == EnumFacing.DOWN ? -1 : 0), trace.getBlockPos().getZ());
			world.spawnEntity(cloud);
			badgeEffects.setTether(cloud, techSlot);
		}

		cloud.setDuration(cloud.getDuration() + 1);

		if(!player.isCreative() && time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.WITCH, 2);

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
}
