package com.cibernet.minestuckuniverse.badges.heroClass;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;

public class BadgeMuse extends BadgeHeroClass
{
	public BadgeMuse() {
		super(EnumClass.MUSE, 7, 80);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time) {
		return false;
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayer) || !event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.BADGE_MUSE))
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();

		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		if(title == null)
			return;

		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.MUSE, 30);

		for(EntityPlayer target : player.world.getMinecraftServer().getPlayerList().getPlayers())
		{
			if(!player.isOnSameTeam(target) && Math.signum(target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()) != Math.signum(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()))
				continue;

			Collection<PotionEffect> effects = title == null ? new ArrayList<PotionEffect>(){{add(new PotionEffect(MobEffects.STRENGTH, 300, 4));}} :
					GTEventHandler.getAspectEffects(player).values();

			for(PotionEffect p : effects)
				target.addPotionEffect(new PotionEffect(p.getPotion(), 1200, 9));

			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MUSE, 10);
		}
	}
}
