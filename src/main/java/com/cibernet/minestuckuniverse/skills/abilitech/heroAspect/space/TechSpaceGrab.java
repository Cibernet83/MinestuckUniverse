package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.space;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechSpaceGrab extends TechHeroAspect
{

	public TechSpaceGrab(String name, long cost)
	{
		super(name, EnumAspect.SPACE, cost, EnumTechType.UTILITY);
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

		Entity target = null;
		int soulValue = 0;

		if(state == SkillKeyStates.KeyState.PRESS)
		{
			RayTraceResult result = MSUUtils.getMouseOver(world, player, player.getEntityAttribute(EntityPlayerMP.REACH_DISTANCE).getAttributeValue(), true);
			if(result.entityHit == null || result.entityHit instanceof EntityPlayer /*|| result.entityHit instanceof EntityLiving*/)
				badgeEffects.clearTether(techSlot);
			else target = result.entityHit;

			/* maybe make this an edit mode thing instead
			if(target == null && player.capabilities.allowEdit)
			{
				BlockPos targetBlock = MSUUtils.getTargetBlock(player);

				if(targetBlock != null)
				{
					IBlockState targetState = world.getBlockState(targetBlock);
					float hardness = targetState.getBlockHardness(world, targetBlock);
					if(!targetState.getBlock().isAir(targetState, world, targetBlock) && hardness >= 0 && hardness < 5)
					{
						EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)targetBlock.getX() + 0.5D, (double)targetBlock.getY(), (double)targetBlock.getZ() + 0.5D, targetState);
						entityfallingblock.setHurtEntities(true);
						world.spawnEntity(entityfallingblock);
						target = entityfallingblock;
						target.onGround = false;
					}
				}
			}
			*/
			badgeEffects.setTether(target, techSlot);

			badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 1);
		}
		else target = badgeEffects.getTether(techSlot);

		if(target == null || MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
			return false;

		if(target instanceof EntityLiving)
			soulValue += ((EntityLiving) target).getMaxHealth() * (target instanceof EntityUnderling ? 0.5f : 1f);

		for(Entity entity : target.getPassengers())
			if(entity instanceof EntityPlayer)
				soulValue += 100000000;
			else if(entity instanceof EntityLiving)
				soulValue += ((EntityLiving) entity).getMaxHealth();


		if(soulValue > 20)
		{
			badgeEffects.clearTether(techSlot);
			target = null;
		}

		if(target == null)
			return false;

		RayTraceResult result = MSUUtils.rayTraceBlocks(player, player.getEntityAttribute(EntityPlayerMP.REACH_DISTANCE).getAttributeValue());

		target.motionX = 0;
		target.motionY = 0;
		target.motionZ = 0;
		target.velocityChanged = true;
		target.isAirBorne = true;
		target.onGround = false;
		target.fallDistance = 0;

		if(state == SkillKeyStates.KeyState.RELEASED)
		{
			target.motionX = (result.hitVec.x - target.posX);
			target.motionY = (result.hitVec.y - target.posY);
			target.motionZ = (result.hitVec.z - target.posZ);
		}
		else target.move(MoverType.PLAYER, result.hitVec.x - target.posX, result.hitVec.y - target.posY, result.hitVec.z - target.posZ);

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 1);

		if(!player.isCreative() && time % (50 - 40*soulValue/20f) == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player) {
		return !isSuper && super.canUnlock(world, player);
	}
}
