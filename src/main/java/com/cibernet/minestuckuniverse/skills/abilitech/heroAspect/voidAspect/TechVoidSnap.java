package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.voidAspect;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Arrays;

public class TechVoidSnap extends TechHeroAspect
{
	public TechVoidSnap(String name, long cost) {
		super(name, EnumAspect.VOID, cost, EnumTechType.UTILITY);
	}

	protected static final int ENERGY_USE = 2;

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < ENERGY_USE)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		Entity target = MSUUtils.getMouseOver(world, player, player.getEntityAttribute(EntityPlayerMP.REACH_DISTANCE).getAttributeValue(), true).entityHit;

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.VOID, 5);

		if(target != null && !(target instanceof EntityPlayer) && !(target instanceof EntityDecoy)&& !Arrays.asList(MSUConfig.protectedEntities).contains(EntityRegistry.getEntry(target.getClass()).getRegistryName().toString()) &&
				!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, false)))
		{
			target.setDead();
			if (!player.isCreative() && !world.isRemote)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);
			if(target instanceof EntityLivingBase)
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.VOID, 10);
			else
				MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, BadgeEffects.getAspectParticleColors(EnumAspect.VOID)[0], 10, target), player);
		}

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return super.isUsableExternally(world, player) && player.getFoodStats().getFoodLevel() >= ENERGY_USE;
	}
}
