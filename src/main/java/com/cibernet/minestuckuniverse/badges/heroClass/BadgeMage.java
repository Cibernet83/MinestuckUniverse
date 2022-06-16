package com.cibernet.minestuckuniverse.badges.heroClass;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BadgeMage extends BadgeHeroClass
{
	public BadgeMage()
	{
		super(EnumClass.MAGE);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative())
		{
			if(player.getFoodStats().getFoodLevel() <= 0)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}
			if((time % 30) == 0)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);
		}

		int karma = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma();

		int alignmentColor = 0x00FF15;
		int minKarma = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.KARMA) ? 40 : 20;
		if(karma >= minKarma)
			alignmentColor = 0xFFD800;
		else if(karma <= -minKarma)
			alignmentColor = 0xB200FF;

		if (time > 50)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, alignmentColor, 5, player), player);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.MAGE, 1);

		if (time > 100)
			player.sendStatusMessage(new TextComponentTranslation("status.alignmentPrediction." + (karma >= minKarma ? "heroic" : karma <= -minKarma ? "just" : "neutral") + "Self")
					                         .setStyle(new Style().setColor(karma >= minKarma ? TextFormatting.GOLD : karma <= -minKarma ? TextFormatting.DARK_PURPLE : TextFormatting.GREEN)), true);

		return true;
	}
}
