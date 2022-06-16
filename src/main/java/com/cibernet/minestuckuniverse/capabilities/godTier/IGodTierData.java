package com.cibernet.minestuckuniverse.capabilities.godTier;

import com.cibernet.minestuckuniverse.badges.Badge;
import com.cibernet.minestuckuniverse.badges.MasterBadge;
import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import com.cibernet.minestuckuniverse.util.EnumLunarSway;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;

public interface IGodTierData extends IMSUCapabilityBase<EntityPlayer>
{
	boolean addBadge(Badge badge, boolean sendUpdate);
	boolean hasBadge(Badge badge);
	boolean isBadgeActive(Badge badge);
	boolean isBadgeEnabled(Badge badge);
	void setBadgeEnabled(Badge badge, boolean enabled);
	List<Badge> getAllBadges();
	void resetBadges();
	void resetTitleBadges(boolean sendUpdate);
	int getBadgesLeft();
	int getMaxBadges();
	void setMaxBadges(int maxBadges);
	MasterBadge getMasterBadge();

	boolean isGodTier();
	boolean canGodTier(World world);
	void setToBaseGodTier(boolean sendUpdate);
	String getGodTierTitle(Title title);

	boolean climbedTheSpire();
	void setClimbedTheSpire(boolean v);

	int getSkillLevel(GodTierData.SkillType type);
	float getSkillXp(GodTierData.SkillType type);
	void increaseXp(GodTierData.SkillType type, float value);
	void resetSkill(GodTierData.SkillType type, boolean sendUpdate);
	void resetSkills(boolean sendUpdate);
	double getSkillAttributeLevel(GodTierData.SkillType type);
	int getXpToNextLevel(GodTierData.SkillType type);
	int getSkillAttributeOperationType(GodTierData.SkillType skill);

	float getTempKarma();
	int getStaticKarma();
	void setTempKarma(float tempKarma);
	void setStaticKarma(int staticKarma);
	int getTotalKarma();

	void markForReset();
	void update();
	boolean hasMasterControl();
	void setMasterControl(boolean masterControl);
	GristType getGristHoard();
	void setGristHoard(GristType gristHoard);
	EnumConsort getConsortType();
	void setConsortType(EnumConsort consortType);
	EnumLunarSway getLunarSway();
	void setLunarSway(EnumLunarSway lunarSway);
}
