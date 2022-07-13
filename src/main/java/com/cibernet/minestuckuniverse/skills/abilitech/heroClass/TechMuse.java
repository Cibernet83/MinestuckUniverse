package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;

public class TechMuse extends TechHeroClass
{
	public TechMuse(String name, long cost)
	{
		super(name, EnumClass.MUSE, cost, EnumTechType.PASSIVE, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time) {
		return false;
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayer) || !event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.MUSE_REQUIEM))
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();

		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		if(title == null)
			return;

		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.MUSE, 30);

		for(EntityPlayer target : player.world.getMinecraftServer().getPlayerList().getPlayers())
		{
			if(Math.signum(target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()) != Math.signum(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma()) &&
					!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player.world, target, MSUSkills.MUSE_REQUIEM, -1, false)))
				continue;

			Collection<PotionEffect> effects = title == null ? new ArrayList<PotionEffect>(){{add(new PotionEffect(MobEffects.STRENGTH, 300, 4));}} :
					GTEventHandler.getAspectEffects(player).values();

			for(PotionEffect p : effects)
				target.addPotionEffect(new PotionEffect(p.getPotion(), 1200, 9));

			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.MUSE, 10);
		}
	}
}
