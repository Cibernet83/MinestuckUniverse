package com.cibernet.minestuckuniverse.badges.heroAspectUtil;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIFollowReformer;
import com.cibernet.minestuckuniverse.entity.ai.EntityAINearestNonReformer;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class BadgeUtilBlood extends BadgeHeroAspectUtil
{
	public BadgeUtilBlood()
	{
		super(EnumAspect.BLOOD, new ItemStack(MinestuckItems.minestuckBucket,16, 1));
	}

	@Override
	public void onBadgeUnlocked(World world, EntityPlayer player) {
		super.onBadgeUnlocked(world, player);
		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setReforming(true);
	}

	@Override
	public boolean onBadgeTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		badgeEffects.setReforming(!badgeEffects.isReforming());
		player.sendStatusMessage(new TextComponentTranslation(badgeEffects.isReforming() ? "status.badgeEnabled" : "status.badgeDisabled", getDisplayComponent()), true);
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.BLOOD, 6);

		if (badgeEffects.isReforming())
			for (EntityCreature entity : world.getEntities(EntityCreature.class, (e) -> player.equals(e.getAttackTarget())))
				entity.setAttackTarget(null);

		return true;
	}

	@SubscribeEvent
	public static void canTargetPlayer(PlayerEvent.Visibility event)
	{
		if(event.getEntityPlayer().getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null &&
			event.getEntityPlayer().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.BADGE_UTIL_BLOOD))
			event.modifyVisibility(0);
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityAnimal && ((EntityAnimal)event.getEntity()).getNavigator() instanceof PathNavigateGround)
			((EntityAnimal)event.getEntity()).targetTasks.addTask(3, new EntityAIFollowReformer((EntityCreature) event.getEntity(), 1.1D));
		else if(event.getEntity() instanceof EntityUnderling)
		{
			EntityUnderling underling = (EntityUnderling) event.getEntity();
			EntityListFilter attackEntitySelector = new EntityListFilter(new ArrayList());
			attackEntitySelector.entityList.add(EntityPlayerMP.class);
			for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(underling.targetTasks.taskEntries))
				underling.targetTasks.removeTask(entry.action);
			underling.targetTasks.addTask(2, new EntityAINearestNonReformer(underling, EntityLivingBase.class, 128.0F, 2, true, false, attackEntitySelector));
		}
	}
}
