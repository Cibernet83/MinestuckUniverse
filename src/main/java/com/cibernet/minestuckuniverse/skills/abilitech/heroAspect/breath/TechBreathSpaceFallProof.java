package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TechBreathSpaceFallProof extends TechHeroAspect
{
	public TechBreathSpaceFallProof(String name, long cost)
	{
		super(name, EnumAspect.BREATH, cost, EnumTechType.PASSIVE, EnumTechType.UTILITY);
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		return title != null && (title.getHeroAspect() == EnumAspect.SPACE || title.getHeroAspect() == EnumAspect.BREATH);
	}

	@Override
	public List<String> getTags()
	{
		List<String> list = super.getTags();
		list.add(0, "@SPACE@");

		return list;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingAttack(LivingAttackEvent event)
	{
		if((event.getSource().equals(DamageSource.FALL) || event.getSource().equals(DamageSource.FLY_INTO_WALL)) &&
				event.getEntityLiving().hasCapability(MSUCapabilities.GOD_TIER_DATA, null) &&
				event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.BREATH_SPACE_VERTIGO_BLOCK))
			event.setCanceled(true);
	}
}
