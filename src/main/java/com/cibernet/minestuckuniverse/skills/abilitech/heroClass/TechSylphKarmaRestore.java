package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechSylphKarmaRestore extends TechHeroClass
{
	public TechSylphKarmaRestore(String name, long cost)
	{
		super(name, EnumClass.SYLPH, cost, EnumTechType.DEFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.RELEASED)
			badgeEffects.clearTether(techSlot);

		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		EntityPlayer target = badgeEffects.getTether(techSlot) instanceof EntityPlayer ? (EntityPlayer) badgeEffects.getTether(techSlot) : null;
		if(target == null && MSUUtils.getTargetEntity(player) instanceof EntityPlayer &&
				!MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, true)))
		{
			target = (EntityPlayer) MSUUtils.getTargetEntity(player);
			badgeEffects.setTether(target, techSlot);
		}

		if (target != null)
		{
			IGodTierData targetData = target.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			int tickMod = (int)(4 + 0.4f * player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTempKarma());
			if((targetData.getStaticKarma() != 0 || targetData.getTempKarma() != 0) && time % tickMod == 0)
			{
				if (targetData.getStaticKarma() != 0)
					targetData.setStaticKarma(targetData.getStaticKarma() + (targetData.getStaticKarma() > 0 ? -1 : 1));
				else
					targetData.setTempKarma(targetData.getTempKarma() + (targetData.getTempKarma() > 0 ? -1 : 1));

				if (time % (tickMod * 2) == 0)
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
				
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.BURST, EnumClass.SYLPH, 4);
			}
		}
		

		
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.SYLPH, 2);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
	
	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}
	
	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		return player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}
}
