package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIAttackRageShifted;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
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
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class TechRageManagement extends TechHeroAspect
{
	protected static final int RADIUS = 16;

	public static final AttributeModifier ATTACK_MOD = new AttributeModifier(UUID.fromString("a10e3486-c1dd-4a74-acfc-9be5d8bcaecc"), "rage_util_boost", 4, 0).setSaved(true);

	public TechRageManagement(String name) {
		super(name, EnumAspect.RAGE, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 3)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(time == 1)
		{
			EntityLivingBase target = MSUUtils.getTargetEntity(player);

			if(!(target instanceof EntityCreature))
				return false;

			toggleRageShift((EntityCreature) target);
			target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);
			if(!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-3);
		}
		else if(time == 40)
		{
			int count = 0;
			List<EntityCreature> list = world.getEntitiesWithinAABB(EntityCreature.class, player.getEntityBoundingBox().grow(RADIUS));

			for(EntityLivingBase target : list)
			{
				if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 3)
					break;

				toggleRageShift((EntityCreature) target);
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.RAGE, 10);
				count++;

				if(!player.isCreative())
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-3);
			}

			if(count > 0 || list.isEmpty())
				badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumAspect.RAGE, list.isEmpty() ? 1 : 4);
			else
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				return false;
			}
		}

		return true;
	}

	public static void toggleRageShift(EntityCreature entity)
	{
		IBadgeEffects badgeEffects = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null);

		if(!badgeEffects.isRageShifted())
			enableRageShift(entity);
		else
			disableRageShift(entity);

		badgeEffects.setRageShifted(!badgeEffects.isRageShifted());
	}

	public static void enableRageShift(EntityCreature entity)
	{
		boolean hasHostileTask = false;
		boolean hasAttackAI = false;

		for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(entity.targetTasks.taskEntries))
		{
			if(entry.action instanceof EntityAINearestAttackableTarget || entry.action instanceof EntityAINearestAttackableTargetWithHeight)
			{
				entity.targetTasks.removeTask(entry.action);
				hasHostileTask = true;
			}
		}
		for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(entity.tasks.taskEntries))
		{
			if(!hasAttackAI && (entry.action instanceof EntityAIAttackRanged || entry.action instanceof EntityAIAttackMelee))
				hasAttackAI = true;

			if(entry.action instanceof EntityAICreeperSwell)
			{
				entity.targetTasks.removeTask(entry.action);
				hasHostileTask = true;
			}
		}

		if(!hasHostileTask)
		{
			entity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityPlayer.class, true));
			entity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityIronGolem.class, true));

			if(!hasAttackAI)
				entity.tasks.addTask(2, new EntityAIAttackRageShifted(entity,1.5D,false));
		}
		else if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null &&
				!entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(ATTACK_MOD))
			entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(ATTACK_MOD);

		entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).cleanRageShift();
	}

	@SuppressWarnings("deprecation")
	public static void disableRageShift(EntityCreature entity)
	{
		for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(entity.targetTasks.taskEntries))
			entity.targetTasks.removeTask(entry.action);
		for(EntityAITasks.EntityAITaskEntry entry : new LinkedHashSet<>(entity.tasks.taskEntries))
			entity.tasks.removeTask(entry.action);

		try
		{
			try
			{
				ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184651_r", void.class).invoke(entity);
			}
			catch (ReflectionHelper.UnableToFindMethodException e)
			{
				ObfuscationReflectionHelper.findMethod(EntityLiving.class, "initEntityAI", void.class).invoke(entity);
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}

		entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).cleanRageShift();
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityCreature))
			return;

		if (event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isRageShiftDirty())
			TechRageManagement.enableRageShift((EntityCreature) event.getEntityLiving());
	}
}
