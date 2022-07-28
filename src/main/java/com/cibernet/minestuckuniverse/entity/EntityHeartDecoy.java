package com.cibernet.minestuckuniverse.entity;

import java.util.ArrayList;
import java.util.UUID;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.world.LandAspectBedrock;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHeartDecoy extends EntityDecoy
{
	public static final DataParameter<String> USERNAME = EntityDataManager.createKey(EntityHeartDecoy.class, DataSerializers.STRING);
	public static final DataParameter<String> PPLAYER_UUID = EntityDataManager.createKey(EntityHeartDecoy.class, DataSerializers.STRING);
	public static final ArrayList<EntityDecoy> DECOYS_ACTIVE = new ArrayList<>();
	
	public int slim = -1;
	public ResourceLocation skinLoc;
	public ResourceLocation capeLoc;
	
	public EntityHeartDecoy(World world)
	{
		super(world);
	}
	
	public EntityHeartDecoy(WorldServer world, EntityPlayerMP player) 
	{
		super(world, player);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		dataManager.set(USERNAME, username);
		dataManager.set(PPLAYER_UUID, uuid.toString());
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		DECOYS_ACTIVE.add(this);
		dataManager.register(USERNAME, "");
		dataManager.register(PPLAYER_UUID, "");
	}
	
	@Override
	public void onUpdate() 
	{
		if(!world.isRemote && 
		((isPotionActive(MSUPotions.GOD_TIER_LOCK) && getActivePotionEffect(MSUPotions.GOD_TIER_LOCK).getAmplifier() > 0) || 
				locationChanged()
		))
			returnToSender(null, 0);
		try
		{
			super.onUpdate();
		}
		catch(Exception e)
		{
			returnToSender(null, -1);
		}
	}
	
	@Override
	public boolean locationChanged() 
	{
		return (int)prevPosX != (int)posX || (int)prevPosY != (int)posY || 
				(int)prevPosZ != (int)posZ;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void setupCustomSkin()
	{
		uuid = UUID.fromString(dataManager.get(PPLAYER_UUID));
		
		NetworkPlayerInfo info = Minecraft.getMinecraft().getConnection().getPlayerInfo(uuid);
		if(info.hasLocationSkin())
		{
			slim = info.getSkinType().contentEquals("slim") ? 1 : 0;
			skinLoc = info.getLocationSkin();
			capeLoc = info.getLocationCape();
		}
	}
	
	@Override
	public ResourceLocation getLocationSkin() {
		if(skinLoc == null)
			setupCustomSkin();
		return skinLoc;
	}

	@Override
	public ResourceLocation getLocationCape() {
		return capeLoc;
	}
	
	public int isSlim()
	{
		if(slim == -1)
			setupCustomSkin();
		return slim;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (!world.isRemote && (!gameType.equals(GameType.CREATIVE) || damageSource.canHarmInCreative()))
			returnToSender(damageSource, par2);
		return true;
	}
	
	public void returnToSender(DamageSource damageSource, float damage)
	{
		DECOYS_ACTIVE.remove(this);
		username = dataManager.get(USERNAME);
		if(username == null || username.isEmpty())
		{
			this.setDead();
			return;
		}
		EntityPlayer player = world.getMinecraftServer() == null ? null : world.getMinecraftServer().getPlayerList().getPlayerByUsername(username);

		if(damage == -1 || player == null)
		{
			this.setDead();
			return;
		}
		
		Teleport.teleportEntity(player, dimension, null, posX, posY, posZ);
		player.setGameType(gameType);
		player.rotationYawHead = rotationYawHead;
		player.rotationPitch = rotationPitch;
		player.capabilities.readCapabilitiesFromNBT(capabilities);
		player.sendPlayerAbilities();
		player.fallDistance = 0;
		player.setHealth(getHealth());
		player.getFoodStats().readNBT(getFoodStatsNBT() != null ? getFoodStatsNBT() : new NBTTagCompound());
		player.inventory.copyInventory(inventory);
		
		
		markedForDespawn = true;
		
		if(damageSource != null)
			player.attackEntityFrom(damageSource, damage);
		
		IBadgeEffects BE = player.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		for(int i = 0; i < SkillKeyStates.Key.values().length; i++)
			if(BE.getTether(i) instanceof EntityHeartDecoy)
				BE.setTether(null, i);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setString("heldUsername", dataManager.get(USERNAME));
		compound.setUniqueId("heldUUID", UUID.fromString(dataManager.get(PPLAYER_UUID)));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		dataManager.set(USERNAME, compound.getString("heldUsername"));
		dataManager.set(PPLAYER_UUID, compound.getUniqueId("heldUUID").toString());
		if(world.isRemote)
			setupCustomSkin();
	}
	
	/*
	@SubscribeEvent
	public static void removeDecoyOnLogOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if(ServerEditHandler.getData(event.player) != null)
			ServerEditHandler.reset(ServerEditHandler.getData(event.player));
		for(EntityDecoy decoy : EntityHeartDecoy.DECOYS_ACTIVE)
			if(decoy instanceof EntityHeartDecoy && decoy.getDataManager().get(EntityHeartDecoy.PPLAYER_UUID).contentEquals(event.player.getUniqueID().toString()))
				((EntityHeartDecoy) decoy).returnToSender(null, 0);
	}
	
	
	@SubscribeEvent
	public static void onSpawnDecoy(EntityJoinWorldEvent event)
	{
		if(!(event.getEntity() instanceof EntityDecoy))
			return;
		EntityDecoy decoy = ((EntityDecoy) event.getEntity());
		NBTTagCompound tag = decoy.getEntityData();
		
		if((decoy.username != null && !decoy.username.isEmpty()) && (decoy.uuid != null && !decoy.uuid.toString().isEmpty()))
		{
			tag.setString("decoyUsername", decoy.username);
			tag.setUniqueId("decoyUUID", decoy.uuid);
		}
		else if(tag.hasKey("decoyUsername") && tag.hasKey("decoyUUID"))
		{
			decoy.username = tag.getString("decoyUsername");
			decoy.uuid = tag.getUniqueId("decoyUUID");
		}
	}
	
	*/
}
