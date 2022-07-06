package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechPrinceWrath extends TechHeroClass
{
	public TechPrinceWrath(String name)
	{
		super(name, EnumClass.PRINCE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 9)
		{
			if(state.equals(SkillKeyStates.KeyState.HELD))
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		if(state.equals(SkillKeyStates.KeyState.RELEASED))
		{
			EntityLivingBase target = MSUUtils.getTargetEntity(player);
			float dmg = 10 * Math.min(3.0f, Math.max(1.0f, time/40f));
			if(target != null)
			{
				target.getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumClass.PRINCE, 20);
				target.attackEntityFrom(new EntityCritDamageSource("princeDmg", player).setCrit(), dmg);
				if (!player.isCreative())
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 9);
			}

		}
		else if((int) (time % (120f / Math.max(time, 1f))) == 0)
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumClass.PRINCE, 2);

		return true;
	}
}
