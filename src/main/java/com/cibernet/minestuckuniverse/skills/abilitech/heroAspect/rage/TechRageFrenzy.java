package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffect;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.ai.AIRageFrenzyTarget;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIAttackRageShifted;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import static com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.rage.TechRageManagement.resetAI;

public class TechRageFrenzy extends TechHeroAspect
{
	protected static final int RADIUS = 16;

	public static final AttributeModifier ATTACK_MOD = new AttributeModifier(UUID.fromString("a10e3486-c1dd-4a74-acfc-9be5d8bcaecc"), "rage_util_boost", 4, 0).setSaved(true);

	public TechRageFrenzy(String name, long cost) {
		super(name, EnumAspect.RAGE, cost, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE || time > 20)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 5)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 2);

		if(time == 20)
		{
			List<EntityCreature> list = world.getEntitiesWithinAABB(EntityCreature.class, player.getEntityBoundingBox().grow(RADIUS));

			for(EntityLivingBase target : list)
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
					continue;
				if(!target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isRageShifted())
					enableRageFrenzy((EntityCreature) target);
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);
			}

			if(!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-5);

			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.RAGE, list.isEmpty() ? 1 : 4);
		}

		return true;
	}

	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 5 && super.isUsableExternally(world, player);
	}

	public static void enableRageFrenzy(EntityCreature entity)
	{
		boolean hasAttackAI = false;

		for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(entity.tasks.taskEntries))
		{
			if(!hasAttackAI && (entry.action instanceof EntityAIAttackRanged || entry.action instanceof EntityAIAttackMelee))
			{
				hasAttackAI = true;
				break;
			}
		}

		if(entity instanceof EntityUnderling)
			for(EntityAITasks.EntityAITaskEntry task : entity.targetTasks.taskEntries)
				entity.targetTasks.removeTask(task.action);

		entity.targetTasks.addTask(1, new AIRageFrenzyTarget(entity));

		if(!hasAttackAI)
			entity.tasks.addTask(2, new EntityAIAttackRageShifted(entity,1.5D,false));

		else if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null &&
				!entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(ATTACK_MOD))
			entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(ATTACK_MOD);

		IBadgeEffects badgeEffects = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		badgeEffects.setRageShifted(true);

		resetAI(entity);
	}


	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityCreature))
			return;

		if (event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isRageShifted())
			TechRageFrenzy.enableRageFrenzy((EntityCreature) event.getEntityLiving());
	}
}
