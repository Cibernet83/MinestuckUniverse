package com.cibernet.minestuckuniverse.skills.badges;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterBadge extends BadgeLevel
{
	private final int requiredXp;
	private final float baseStat;
	private final float limit;

	public MasterBadge(String unlocName, int gtLevel, int requiredXp, float baseStat, float limit)
	{
		super(unlocName, gtLevel);
		this.requiredXp = requiredXp;
		this.baseStat = baseStat;
		this.limit = limit;
	}

	public float getStatNumber(EntityPlayer player)
	{
		return Math.max(0, Math.min(limit + (player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.BADGE_OVERLORD) ? 20 : 0), (player.getLuck()+1)*baseStat));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getDisplayTooltip() {
		return I18n.format(getUnlocalizedName() + ".tooltip", getStatNumber(Minecraft.getMinecraft().player), (MSUSkills.MASTER_BADGE_WISE.getStatNumber(Minecraft.getMinecraft().player)/2));
	}

	@Override
	public String getUnlockRequirements() {
		return I18n.format("badge.class.unlock", requiredXp);
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		if(player.experienceLevel >= requiredXp)
		{
			player.experienceLevel -= requiredLevel;
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.ADD_PLAYER_XP, requiredLevel), player);
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTextureLocation()
	{
		return new ResourceLocation(getRegistryName().getResourceDomain(), "textures/gui/skills/"+getRegistryName().getResourcePath() +
																		   (Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.BADGE_OVERLORD) ? "_overlord" : "") +".png");
	}
}
