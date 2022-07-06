package com.cibernet.minestuckuniverse.skills.abilitech.heroClass;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechPrinceSlash extends TechHeroClass
{
	public TechPrinceSlash(String name)
	{
		super(name, EnumClass.PRINCE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time) {
		return false;
	}

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer) || !(event.getSource().getImmediateSource() instanceof EntityPlayer))
			return;

		EntityPlayer target = (EntityPlayer) event.getEntity();
		EntityPlayer source = (EntityPlayer) event.getSource().getImmediateSource();

		Title targetTitle = MinestuckPlayerData.getTitle(IdentifierHandler.encode(target));
		Title sourceTitle = MinestuckPlayerData.getTitle(IdentifierHandler.encode(source));

		if(targetTitle != null && sourceTitle != null && targetTitle.getHeroAspect() == sourceTitle.getHeroAspect() &&
				source.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(MSUSkills.RULING_SLASH))
			event.setAmount(event.getAmount()*3);
	}
}
