package com.cibernet.minestuckuniverse.capabilities;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.BadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffect;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.beam.BeamData;
import com.cibernet.minestuckuniverse.capabilities.beam.IBeamData;
import com.cibernet.minestuckuniverse.capabilities.consortCosmetics.ConsortHatsData;
import com.cibernet.minestuckuniverse.capabilities.consortCosmetics.IConsortHatsData;
import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import com.cibernet.minestuckuniverse.capabilities.game.IGameData;
import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.ISkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.mediumData.IMediumData;
import com.cibernet.minestuckuniverse.capabilities.mediumData.MediumData;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MSUCapabilities
{
	@CapabilityInject(IStrifeData.class)
	public static final Capability<IStrifeData> STRIFE_DATA = null;
	@CapabilityInject(IBeamData.class)
	public static final Capability<IBeamData> BEAM_DATA = null;
	@CapabilityInject(IGameData.class)
	public static final Capability<IGameData> GAME_DATA = null;
	@CapabilityInject(IConsortHatsData.class)
	public static final Capability<IConsortHatsData> CONSORT_HATS_DATA = null;
	@CapabilityInject(IBadgeEffects.class)
	public static final Capability<IBadgeEffects> BADGE_EFFECTS = null;
	@CapabilityInject(ISkillKeyStates.class)
	public static final Capability<ISkillKeyStates> SKILL_KEY_STATES = null;
	@CapabilityInject(IGodTierData.class)
	public static final Capability<IGodTierData> GOD_TIER_DATA = null;
	@CapabilityInject(IMediumData.class)
	public static final Capability<IMediumData> MEDIUM_DATA = null;

	public static void registerCapabilities()
	{
		MinecraftForge.EVENT_BUS.register(MSUCapabilities.class);
		MinecraftForge.EVENT_BUS.register(BadgeEffects.class);
		MinecraftForge.EVENT_BUS.register(SkillKeyStates.class);
		MinecraftForge.EVENT_BUS.register(GodTierData.class);


		CapabilityManager.INSTANCE.register(IStrifeData.class, new MSUCapabilityProvider.Storage<>(), StrifeData::new);
		CapabilityManager.INSTANCE.register(IBeamData.class, new MSUCapabilityProvider.Storage<>(), BeamData::new);
		CapabilityManager.INSTANCE.register(IGameData.class, new MSUCapabilityProvider.Storage<>(), GameData::new);
		CapabilityManager.INSTANCE.register(IConsortHatsData.class, new MSUCapabilityProvider.Storage<>(), ConsortHatsData::new);

		CapabilityManager.INSTANCE.register(IBadgeEffects.class, new MSUCapabilityProvider.Storage<>(), BadgeEffects::new);
		CapabilityManager.INSTANCE.register(ISkillKeyStates.class, new MSUCapabilityProvider.Storage<>(), SkillKeyStates::new);
		CapabilityManager.INSTANCE.register(IGodTierData.class, new MSUCapabilityProvider.Storage<>(), GodTierData::new);
		CapabilityManager.INSTANCE.register(IMediumData.class, new MSUCapabilityProvider.Storage<>(), MediumData::new);
	}

	@SubscribeEvent
	public static void attachWorldCap(AttachCapabilitiesEvent<World> event)
	{
		event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "beam_data"), new MSUCapabilityProvider<>(BEAM_DATA, event.getObject()));

		if(MinestuckDimensionHandler.isLandDimension(event.getObject().provider.getDimension()))
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "medium_data"), new MSUCapabilityProvider<>(MEDIUM_DATA, event.getObject()));

		if (event.getObject().provider.getDimension() == 0)
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "game_data"), new MSUCapabilityProvider<>(GAME_DATA, event.getObject()));
	}

	@SubscribeEvent
	public static void attachEntityCap(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "strife_data"), new MSUCapabilityProvider<>(STRIFE_DATA, (EntityLivingBase) event.getObject()));

		if(event.getObject() instanceof EntityConsort || event.getObject() instanceof EntityFrog)
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "cosmetics"), new MSUCapabilityProvider<>(CONSORT_HATS_DATA, (EntityLivingBase) event.getObject()));

		if (event.getObject() instanceof EntityLivingBase)
		{
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "badge_effects"), new MSUCapabilityProvider<>(BADGE_EFFECTS, (EntityLivingBase) event.getObject()));
		}

		if (event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "god_key_states"), new MSUCapabilityProvider<>(SKILL_KEY_STATES, (EntityPlayer) event.getObject()));
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "god_tier_data"), new MSUCapabilityProvider<>(GOD_TIER_DATA, (EntityPlayer) event.getObject()));
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayerMP)
		{
			IStrifeData cap = event.getEntity().getCapability(STRIFE_DATA, null);
			cap.setStrifeEnabled(MSUConfig.combatOverhaul);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, event.getEntity(), UpdateStrifeDataPacket.UpdateType.ALL), ((EntityPlayer)event.getEntity()));
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_BEAMS, event.getWorld()), ((EntityPlayer)event.getEntity()));
		}
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		event.world.getCapability(MSUCapabilities.BEAM_DATA, null).tickBeams();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().isGamePaused())
			Minecraft.getMinecraft().player.world.getCapability(MSUCapabilities.BEAM_DATA, null).tickBeams();
	}
}