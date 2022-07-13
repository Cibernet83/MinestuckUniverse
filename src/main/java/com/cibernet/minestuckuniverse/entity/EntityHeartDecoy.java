package com.cibernet.minestuckuniverse.entity;

import java.util.UUID;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.util.Teleport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ThreadDownloadImageData;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHeartDecoy extends EntityDecoy
{
	private static final DataParameter<String> UUSERNAME = EntityDataManager.createKey(EntityHeartDecoy.class, DataSerializers.STRING);
	private static final DataParameter<String> PPLAYER_UUID = EntityDataManager.createKey(EntityHeartDecoy.class, DataSerializers.STRING);
	
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
		dataManager.set(UUSERNAME, username);
		dataManager.set(PPLAYER_UUID, uuid.toString());
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(UUSERNAME, "");
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
		username = dataManager.get(UUSERNAME);
		if(username == null || username.isEmpty())
			return;
		
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
	
	/*
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		System.out.println("justwriteNBt");
		super.writeToNBT(compound);
		compound.setString("heldUsername", username);
		compound.setUniqueId("heldUUID", uuid);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		System.out.println("justreadNBt");
		super.readFromNBT(nbt);
		username = nbt.getString("heldUsername");
		uuid = nbt.getUniqueId("heldUUID");
		if(world.isRemote)
			setupCustomSkin();
		
	}
	*/
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		System.out.println("writeentityNBt");
		super.writeEntityToNBT(compound);
		compound.setString("heldUsername", dataManager.get(UUSERNAME));
		compound.setUniqueId("heldUUID", UUID.fromString(dataManager.get(PPLAYER_UUID)));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		System.out.println("readentityNBt");
		super.readEntityFromNBT(compound);
		dataManager.set(UUSERNAME, compound.getString("heldUsername"));
		dataManager.set(PPLAYER_UUID, compound.getUniqueId("heldUUID").toString());
		if(world.isRemote)
			setupCustomSkin();
	}
}
