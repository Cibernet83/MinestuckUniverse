package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechThiefDash extends TechHeroClass
{
	public TechThiefDash(String name, long cost)
	{
		super(name, EnumClass.THIEF, cost, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.PERFORM_DASH, 3f), player);
		badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.THIEF, 20);

		if(!player.isCreative())
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-4);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 4 && super.isUsableExternally(world, player);
	}
}
