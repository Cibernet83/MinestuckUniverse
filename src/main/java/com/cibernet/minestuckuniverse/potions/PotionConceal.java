package com.cibernet.minestuckuniverse.potions;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionConceal extends MSUPotionBase
{
	protected PotionConceal(boolean isBadEffectIn, int liquidColorIn, String name)
	{
		super(isBadEffectIn, liquidColorIn, name);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		if(entity.getActivePotionEffect(MobEffects.GLOWING) != null)
			entity.removePotionEffect(this);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLivingRender(RenderLivingEvent.Pre event)
	{
		if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isConcealed())
		{
			event.getEntity().setInvisible(true);
			event.getEntity().setGlowing(false);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerVisibility(PlayerEvent.Visibility event)
	{
		if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isConcealed())
			event.modifyVisibility(0);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		entityLivingBaseIn.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setConcealed(true);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		entityLivingBaseIn.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setConcealed(false);
		((WorldServer) entityLivingBaseIn.world).getEntityTracker().sendToTrackingAndSelf(entityLivingBaseIn, new SPacketEntityMetadata(entityLivingBaseIn.getEntityId(), entityLivingBaseIn.getDataManager(), true));
	}
}
