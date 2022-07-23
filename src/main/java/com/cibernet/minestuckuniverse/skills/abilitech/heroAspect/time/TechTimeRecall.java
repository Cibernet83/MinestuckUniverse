package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.SoulData;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;

public class TechTimeRecall extends TechHeroAspect
{
	private static final int ENERGY_USE = 5;
	private static final int RECALL_TICKS = 5 * 20;

	public TechTimeRecall(String name, long cost) {
		super(name, EnumAspect.TIME, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		Queue<SoulData> soulData = badgeEffects.getTimeSoulData();

		soulData.add(new SoulData(player));
		while (soulData.size() > RECALL_TICKS)
			soulData.remove();

		if(state != SkillKeyStates.KeyState.HELD || time > 20)
			return false;

		if (time < 20)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, 2);
			return true;
		}

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < ENERGY_USE)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.TIME, 4);

		soulData.element().apply(player);
		soulData.clear();

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME,4);

		if (!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - ENERGY_USE);

		return true;
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.getEntity().hasCapability(MSUCapabilities.BADGE_EFFECTS, null))
			event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).getTimeSoulData().clear();
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player) {
		return super.isUsableExternally(world, player) && player.getFoodStats().getFoodLevel() >= ENERGY_USE;
	}
}
