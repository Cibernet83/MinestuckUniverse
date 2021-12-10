package com.cibernet.minestuckuniverse.capabilities;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.beam.BeamData;
import com.cibernet.minestuckuniverse.capabilities.beam.IBeamData;
import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import com.cibernet.minestuckuniverse.capabilities.game.IGameData;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
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
import net.minecraftforge.common.util.FakePlayer;
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

	public static void registerCapabilities()
	{
		MinecraftForge.EVENT_BUS.register(MSUCapabilities.class);

		CapabilityManager.INSTANCE.register(IStrifeData.class, new MSUCapabilityProvider.Storage<>(), StrifeData::new);
		CapabilityManager.INSTANCE.register(IBeamData.class, new MSUCapabilityProvider.Storage<>(), BeamData::new);
		CapabilityManager.INSTANCE.register(IGameData.class, new MSUCapabilityProvider.Storage<>(), GameData::new);
	}

	@SubscribeEvent
	public static void attachWorldCap(AttachCapabilitiesEvent<World> event)
	{
		event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "beam_data"), new MSUCapabilityProvider<>(BEAM_DATA, event.getObject()));

		if (event.getObject().provider.getDimension() == 0)
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "game_data"), new MSUCapabilityProvider<>(GAME_DATA, event.getObject()));
	}

	@SubscribeEvent
	public static void attachEntityCap(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
			event.addCapability(new ResourceLocation(MinestuckUniverse.MODID, "strife_data"), new MSUCapabilityProvider<>(STRIFE_DATA, (EntityLivingBase) event.getObject()));
	}

	@SubscribeEvent
	public static void onPlayerJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayerMP && !(event.getEntity() instanceof FakePlayer))
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
