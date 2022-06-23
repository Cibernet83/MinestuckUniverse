package com.cibernet.minestuckuniverse.capabilities.godTier;

import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.skills.badges.MasterBadge;
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
	boolean addSkill(Skill badge, boolean sendUpdate);
	boolean hasSkill(Skill badge);

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

	Abilitech getTech(int slot);
	int getTechSlots();
	boolean isTechEquipped(Abilitech tech);
	boolean isTechPassiveEnabled(Abilitech tech);
	void equipTech(Abilitech tech, int slot);
	void unequipTech(int slot);
	Abilitech getSelectedTech();
	void setSelectedTech(Abilitech tech);
	void resetSelectedTech();

	boolean isGodTier();
	boolean canGodTier(World world);
	void setToBaseGodTier(boolean sendUpdate);
	String getGodTierTitle(Title title);

	boolean climbedTheSpire();
	void setClimbedTheSpire(boolean v);

	int getSkillLevel(GodTierData.StatType type);
	float getSkillXp(GodTierData.StatType type);
	void increaseXp(GodTierData.StatType type, float value);
	void resetSkill(GodTierData.StatType type, boolean sendUpdate);
	void resetSkills(boolean sendUpdate);
	double getSkillAttributeLevel(GodTierData.StatType type);
	int getXpToNextLevel(GodTierData.StatType type);
	int getSkillAttributeOperationType(GodTierData.StatType skill);

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
