package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.space;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.util.SpaceSaltUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechSpaceResize extends TechHeroAspect
{
	public TechSpaceResize(String name, long cost)
	{
		super(name, EnumAspect.SPACE, cost, EnumTechType.UTILITY);
		requiredStacks.add(new ItemStack(MinestuckUniverseItems.spaceSalt, 1));
	}


	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(!state.equals(SkillKeyStates.KeyState.PRESS))
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 4)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		RayTraceResult trace = MSUUtils.getMouseOver(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

		if(trace != null)
		{
			if(trace.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				if(SpaceSaltUtils.onSpaceSaltUse(player.world, player, EnumHand.MAIN_HAND, trace.getBlockPos(), trace.sideHit, trace))
				{
					badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 4);
					return true;
				}
			}
			else if(trace.typeOfHit == RayTraceResult.Type.ENTITY)
			{
				EntityLivingBase target = (EntityLivingBase) trace.entityHit;
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
					return false;
				NBTTagCompound nbt = target.writeToNBT(new NBTTagCompound());
				if(nbt.hasKey("Size"))
				{
					float size = nbt.getFloat("Size");

					nbt.setFloat("Size", Math.min(10, size+0.2f*(player.isSneaking() ? -1 : 1)));
					target.readFromNBT(nbt);
					nbt = target.writeToNBT(new NBTTagCompound());
					if(nbt.getFloat("Size") == size)
					{
						size = Math.min(10, size+(player.isSneaking() ? -1 : 1));
						nbt.setFloat("Size", size);
						if(size <= 0)
							target.setDead();
						target.readFromNBT(nbt);
					}

					badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 4);
					target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 10);
					return true;
				}
			}
		}

		return false;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 4 && super.isUsableExternally(world, player);
	}
}
