package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Collection;

public class SoulData
{
	double posX, posY, posZ;
	float rotationYaw, rotationPitch;
	double motionX, motionY, motionZ;
	float health;
	Collection<PotionEffect> potionEffects;
	int decayTime;
	int currentItem;

	public SoulData(EntityLivingBase player)
	{
		this.potionEffects = new ArrayList<>();
		for(PotionEffect effect : player.getActivePotionEffects())
			this.potionEffects.add(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));

		this.posX = player.posX;
		this.posY = player.posY;
		this.posZ = player.posZ;
		this.rotationYaw = player.rotationYaw;
		this.rotationPitch = player.rotationPitch;
		Vec3d motion = getEntityMotion(player);
		this.motionX = motion.x;
		this.motionY = motion.y;
		this.motionZ = motion.z;
		this.health = player.getHealth();
		this.decayTime = player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getDecayTime();

		if(player instanceof EntityPlayer)
			this.currentItem = ((EntityPlayer) player).inventory.currentItem;
	}

	public void apply(EntityLivingBase player)
	{
		player.rotationYaw = this.rotationYaw;
		player.rotationPitch = this.rotationPitch;
		player.setRotationYawHead(player.rotationYaw);
		Teleport.localTeleport(player, null, this.posX, this.posY, this.posZ);
		player.motionX = this.motionX;
		player.motionY = this.motionY;
		player.motionZ = this.motionZ;
		player.velocityChanged = true;
		player.setHealth(this.health);

		player.clearActivePotions();
		for(PotionEffect effect : potionEffects)
			player.addPotionEffect(effect);

		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setDecayTime(decayTime);

		if(player instanceof EntityPlayer)
		{
			((EntityPlayer) player).inventory.currentItem = this.currentItem;
			if(FMLCommonHandler.instance().getSide() == Side.SERVER)
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.SET_CURRENT_ITEM, this.currentItem), (EntityPlayer) player);
		}

	}

	public Vec3d getEntityMotion(EntityLivingBase entity)
	{
		Vec3d prevPos = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getPrevPos();
		Vec3d pos = new Vec3d(entity.posX, entity.posY, entity.posZ);

		if(prevPos == null)
			return new Vec3d(0,0,0);

		return pos.subtract(prevPos);
	}
}
