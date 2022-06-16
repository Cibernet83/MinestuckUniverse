package com.cibernet.minestuckuniverse.capabilities.godTier;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.badges.Badge;
import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.badges.MasterBadge;
import com.cibernet.minestuckuniverse.badges.heroAspect.BadgeHeroAspect;
import com.cibernet.minestuckuniverse.badges.heroAspectUtil.BadgeHeroAspectUtil;
import com.cibernet.minestuckuniverse.badges.heroClass.BadgeHeroClass;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.util.EnumLunarSway;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class GodTierData implements IGodTierData
{
	private int staticKarma = 0;
	private float tempKarma = 0;

	private final HashMap<SkillType, SkillData> godTierXp = new HashMap<SkillType, SkillData>()
	{{
		put(SkillType.GENERAL, new SkillData(SkillType.GENERAL, 1));
		put(SkillType.ATTACK, new SkillData(SkillType.ATTACK, 0.02));
		put(SkillType.DEFENSE, new SkillData(SkillType.DEFENSE, 0.4));
		put(SkillType.SPEED, new SkillData(SkillType.SPEED, 0.05));
		put(SkillType.LUCK, new SkillData(SkillType.LUCK, 0.5));
	}};

	private final TreeMap<Badge, Boolean> badges = new TreeMap<>();
	private MasterBadge masterBadge = null;
	private boolean isMasterBadgeEnabled = true;
	private int maxBadges = MSUConfig.godTierBadgeSlots;
	private boolean masterControl = MSUConfig.godTierMasterControl;
	private boolean canGodTier = true;
	private boolean climbedTheSpire = false;

	private EnumLunarSway lunarSway;
	private EnumConsort consortType;
	private GristType gristHoard = GristType.Build;

	private boolean reset = false;

	private EntityPlayer owner;

	@Override
	public boolean addBadge(Badge badge, boolean sendUpdate)
	{
		if(badge instanceof MasterBadge)
		{
			if(masterBadge != null)
				return false;
			masterBadge = (MasterBadge) badge;
			if(sendUpdate)
				update();
			badge.onBadgeUnlocked(owner.world, owner);
			return true;
		}

		if(badge == null || badges.containsKey(badge) || getBadgesLeft() <= 0)
			return false;

		badges.put(badge, true);
		if(sendUpdate)
			update();
		badge.onBadgeUnlocked(owner.world, owner);
		return true;
	}

	@Override
	public boolean hasBadge(Badge badge)
	{
		if(badge instanceof MasterBadge)
			return badge == masterBadge || (isBadgeEnabled(MSUBadges.BADGE_OVERLORD));
		return badges.containsKey(badge);
	}

	@Override
	public boolean isBadgeActive(Badge badge)
	{
		if(badge instanceof MasterBadge && isBadgeActive(MSUBadges.BADGE_OVERLORD))
			return isBadgeEnabled(badge);
		return !owner.isSpectator() && hasBadge(badge) && (!badge.canDisable() || (isBadgeEnabled(badge) && badge.canUse(owner.world, owner)));
	}

	@Override
	public boolean isBadgeEnabled(Badge badge)
	{
		return hasBadge(badge) && (badge instanceof MasterBadge ? isMasterBadgeEnabled : badges.get(badge));
	}

	@Override
	public void setBadgeEnabled(Badge badge, boolean enabled)
	{
		if(!hasBadge(badge) || !badge.canDisable())
			return;
		if(badge instanceof MasterBadge)
			isMasterBadgeEnabled = enabled;
		else badges.put(badge, enabled);
	}

	@Override
	public List<Badge> getAllBadges()
	{
		List<Badge> result = new ArrayList<>();
		for (Badge badge : badges.keySet())
			if (badge != null)
				result.add(badge);
		if (masterBadge != null)
			result.add(masterBadge);
		return result;
	}

	@Override
	public void resetBadges()
	{
		masterBadge = null;
		badges.clear();
	}

	@Override
	public void resetTitleBadges(boolean sendUpdate)
	{
		badges.keySet().removeIf((key) -> key instanceof BadgeHeroClass || key instanceof BadgeHeroAspect || key instanceof BadgeHeroAspectUtil);
		if(sendUpdate) update();
	}

	@Override
	public int getBadgesLeft()
	{
		return maxBadges-badges.size();
	}

	@Override
	public int getMaxBadges()
	{
		return maxBadges;
	}

	@Override
	public void setMaxBadges(int maxBadges)
	{
		this.maxBadges = maxBadges;
	}

	@Override
	public MasterBadge getMasterBadge()
	{
		return masterBadge;
	}

	@Override
	public boolean isGodTier()
	{
		return godTierXp.get(SkillType.GENERAL).level > 0;
	}

	@Override
	public boolean canGodTier(World world)
	{
		return canGodTier && SburbHandler.getConnectionForDimension(world.provider.getDimension()) != null && owner.equals(SburbHandler.getConnectionForDimension(world.provider.getDimension()).getClientIdentifier().getPlayer());
	}

	@Override
	public void setToBaseGodTier(boolean sendUpdate)
	{
		resetSkills(sendUpdate);
		godTierXp.get(SkillType.GENERAL).level = 1;
		if(sendUpdate) update();
	}

	@Override
	public String getGodTierTitle(Title title)
	{
		if(I18n.hasKey("godTierTitle."+title.getHeroClass().toString()+"."+title.getHeroAspect().toString()+"."+getSkillLevel(SkillType.GENERAL)))
			return I18n.format("godTierTitle."+title.getHeroClass().toString()+"."+title.getHeroAspect().toString()+"."+getSkillLevel(SkillType.GENERAL));

		else if(I18n.hasKey("godTierTitle."+title.getHeroClass().toString()+"."+getSkillLevel(SkillType.GENERAL)))
			return I18n.format("godTierTitle."+title.getHeroClass().toString()+"."+getSkillLevel(SkillType.GENERAL), title.getHeroAspect().getDisplayName());

		else if(I18n.hasKey("godTierTitle."+title.getHeroAspect().toString()+"."+getSkillLevel(SkillType.GENERAL)))
			return I18n.format("godTierTitle."+title.getHeroAspect().toString()+"."+getSkillLevel(SkillType.GENERAL), title.getHeroClass().getDisplayName());

		else if(I18n.hasKey("godTierTitle."+getSkillLevel(SkillType.GENERAL)))
			return I18n.format("godTierTitle."+getSkillLevel(SkillType.GENERAL), title.getTitleName());

		else if(getSkillLevel(SkillType.GENERAL) >= 8 && I18n.hasKey("godTierTitle."+title.getHeroAspect().toString()+".max"))
			return I18n.format("godTierTitle."+title.getHeroAspect().toString()+".max", title.getHeroClass().getDisplayName());

		else return I18n.format("godTierTitle.default", title.getTitleName());
	}

	@Override
	public boolean climbedTheSpire() {
		return climbedTheSpire;
	}

	@Override
	public void setClimbedTheSpire(boolean v) {
		climbedTheSpire = v;
	}

	@Override
	public int getSkillLevel(SkillType type)
	{
		return godTierXp.get(type).level;
	}

	@Override
	public float getSkillXp(SkillType type)
	{
		return godTierXp.get(type).xp;
	}

	@Override
	public void increaseXp(SkillType type, float value)
	{
		godTierXp.get(type).addXp(value, getXpToNextLevel(type));
		if(type != SkillType.GENERAL)
			godTierXp.get(SkillType.GENERAL).addXp(value, getXpToNextLevel(type));

		if(!hasBadge(MSUBadges.GIFT_OF_GAB) && getSkillLevel(SkillType.GENERAL) > 1)
			this.badges.put(MSUBadges.GIFT_OF_GAB, true);

		update();
	}

	@Override
	public void resetSkill(SkillType type, boolean sendUpdate)
	{
		godTierXp.get(type).reset();
		if(sendUpdate) update();
	}

	@Override
	public void resetSkills(boolean sendUpdate)
	{
		for(SkillType type : SkillType.values())
			resetSkill(type, false);
		if(sendUpdate) update();
	}

	@Override
	public double getSkillAttributeLevel(SkillType type)
	{
		return Math.pow(godTierXp.get(type).level, 0.765) * godTierXp.get(type).attributeMod * (isBadgeActive(MSUBadges.BADGE_PAGE) ? 2 : 1) * (isBadgeActive(MSUBadges.BADGE_OVERLORD) ? 3 : 1);
	}

	@Override
	public int getXpToNextLevel(SkillType type)
	{
		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(owner));
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			title = MinestuckPlayerData.title;

		if(title == null)
			return 50;

		int x = getSkillLevel(type)+1;

		switch (title.getHeroClass())
		{
			case KNIGHT: case PRINCE: return (int) (Math.exp((x-1)*0.04)*50); //Exponential
			case HEIR: case MAID: return (int) Math.min(2500, Math.pow(x-1, 2)+50); //Parabolic
			case MUSE: case SEER: case MAGE: return (int) (4*Math.sin(x-1)+2*x)*5 + 50; //Fluctuating
			case BARD: case WITCH: case SYLPH: return (int)(x * 12 + Math.sin(2*(x-1))*10 + 25); //Erratic
			case THIEF: case ROGUE: return 10 * Math.min(x + (x%10), x + ((10-x)%10)) + 50; //Step
			case PAGE: return 14*x + 50;
			//case BARD: return (int) ((150*Math.sin(x)+180)+x*0.5);
			case LORD: return 50*x;
			//case MUSE: return Math.max(50, (int)(-x*0.8 + 250));
		}

		return 50;
	}

	@Override
	public int getSkillAttributeOperationType(SkillType skill)
	{
		return skill == SkillType.LUCK || skill == SkillType.DEFENSE ? 0 : 2;
	}

	@Override
	public float getTempKarma()
	{
		return tempKarma;
	}

	@Override
	public int getStaticKarma()
	{
		return staticKarma;
	}

	@Override
	public void setTempKarma(float tempKarma)
	{
		this.tempKarma = tempKarma;
	}

	@Override
	public void setStaticKarma(int staticKarma)
	{
		this.staticKarma = staticKarma;
	}

	@Override
	public int getTotalKarma()
	{
		return (int) (tempKarma + staticKarma);
	}

	@Override
	public void markForReset()
	{
		reset = true;
	}

	@Override
	public void update()
	{
		if(owner.isUser())
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_DATA_CLIENT));
		else
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_DATA_SERVER, owner), owner);
	}

	@Override
	public boolean hasMasterControl()
	{
		return masterControl;
	}

	@Override
	public void setMasterControl(boolean masterControl)
	{
		this.masterControl = masterControl;
	}

	@Override
	public GristType getGristHoard()
	{
		return gristHoard;
	}

	@Override
	public void setGristHoard(GristType gristHoard)
	{
		this.gristHoard = gristHoard;
	}

	@Override
	public EnumConsort getConsortType()
	{
		return consortType;
	}

	@Override
	public void setConsortType(EnumConsort consortType)
	{
		this.consortType = consortType;
	}

	@Override
	public EnumLunarSway getLunarSway() {
		return lunarSway;
	}

	@Override
	public void setLunarSway(EnumLunarSway lunarSway) {
		this.lunarSway = lunarSway;
	}

	@Override
	public void setOwner(EntityPlayer owner)
	{
		this.owner = owner;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.staticKarma = nbt.getInteger("StaticKarma");
		this.tempKarma = nbt.getFloat("TempKarma");

		for(SkillData data : godTierXp.values())
		{
			NBTTagCompound skill = nbt.getCompoundTag(data.type.getName());
			if(skill == null)
				continue;

			data.level = skill.getInteger("Level");
			data.xp = skill.getFloat("Xp");
		}

		resetBadges();

		if(nbt.hasKey("AllBadges"))
			masterControl = nbt.getBoolean("AllBadges");
		if(nbt.hasKey("MaxBadges"))
			maxBadges = nbt.getInteger("MaxBadges");

		NBTTagCompound badges = nbt.getCompoundTag("Badges");
		for(int i = 0; i < maxBadges && badges.hasKey(String.valueOf(i)); i++)
		{
			NBTTagCompound badgeData = badges.getCompoundTag(String.valueOf(i));
			Badge badge = MSUBadges.REGISTRY.getValue(new ResourceLocation(badgeData.getString("ID")));
			if(badge != null)
				this.badges.put(badge, badgeData.getBoolean("Enabled") || !badge.canDisable());
		}
		if(nbt.hasKey("MasterBadge"))
		{
			Badge badge = MSUBadges.REGISTRY.getValue(new ResourceLocation(nbt.getString("MasterBadge")));
			if(badge instanceof MasterBadge)
				masterBadge = (MasterBadge) badge;
		}
		if(nbt.hasKey("IsMasterEnabled"))
			isMasterBadgeEnabled = nbt.getBoolean("IsMasterEnabled") || !masterBadge.canDisable();
		if(nbt.hasKey("CanGodTier"))
			canGodTier = nbt.getBoolean("CanGodTier");
		if(nbt.hasKey("ClimbedTheSpire"))
			climbedTheSpire = nbt.getBoolean("ClimbedTheSpire");

		if(nbt.hasKey("GristHoardType"))
			gristHoard = GristType.getTypeFromString(nbt.getString("GristHoardType"));

		if(nbt.getInteger("ConsortType") != -1)
			consortType = EnumConsort.values()[nbt.getInteger("ConsortType")];
		if(nbt.getInteger("LunarSway") != -1)
			lunarSway = EnumLunarSway.values()[nbt.getInteger("LunarSway")];

		if(!hasBadge(MSUBadges.GIFT_OF_GAB) && getSkillLevel(SkillType.GENERAL) > 1)
			this.badges.put(MSUBadges.GIFT_OF_GAB, true);
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setInteger("StaticKarma", staticKarma);
		nbt.setFloat("TempKarma", tempKarma);

		for(SkillData data : godTierXp.values())
		{
			NBTTagCompound skill = new NBTTagCompound();
			skill.setInteger("Level", data.level);
			skill.setFloat("Xp", data.xp);
			nbt.setTag(data.type.getName(), skill);
		}

		Iterator<Map.Entry<Badge, Boolean>> iter = badges.entrySet().iterator();
		NBTTagCompound badges = new NBTTagCompound();
		for(int i = 0; iter.hasNext(); i++)
		{
			Map.Entry<Badge, Boolean> entry = iter.next();
			NBTTagCompound badgeData = new NBTTagCompound();
			badgeData.setString("ID", entry.getKey().getRegistryName().toString());
			badgeData.setBoolean("Enabled", entry.getValue());
			badges.setTag(String.valueOf(i), badgeData);
		}
		nbt.setTag("Badges", badges);
		if(masterBadge != null)
		{
			nbt.setString("MasterBadge", masterBadge.getRegistryName().toString());
			nbt.setBoolean("IsMasterEnabled", isMasterBadgeEnabled);
		}

		nbt.setInteger("ConsortType", consortType == null ? -1 : consortType.ordinal());
		nbt.setInteger("LunarSway", lunarSway == null ? (int)Math.floor(Math.random()*EnumLunarSway.values().length) : lunarSway.ordinal());

		nbt.setString("GristHoardType", gristHoard.getRegistryName().toString());

		nbt.setBoolean("AllBadges", masterControl);
		nbt.setBoolean("CanGodTier", canGodTier);
		nbt.setBoolean("ClimbedTheSpire", climbedTheSpire);
		nbt.setInteger("MaxBadges", maxBadges);

		if(reset)
			nbt.setBoolean("Reset", true);

		return nbt;
	}

	private static class SkillData
	{
		int level = 0;
		float xp = 0;
		double attributeMod;
		final SkillType type;

		public SkillData(SkillType type, double attributeMod)
		{
			this.type = type;
			this.attributeMod = attributeMod;
		}

		public void addXp(float v, float maxXp)
		{
			if(xp+v >= maxXp)
			{
				xp = xp+v-maxXp;
				level++;
			}
			else xp += v;
		}

		public void reset()
		{
			level = 0;
			xp = 0;
		}
	}

	public enum SkillType implements IStringSerializable
	{
		GENERAL("General"),
		DEFENSE("Defense"),
		ATTACK("Attack"),
		LUCK("Luck"),
		SPEED("Speed");

		private final String name;

		SkillType(String name)
		{
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event)
	{
		event.getEntity().getCapability(MSUCapabilities.GOD_TIER_DATA, null).readFromNBT(event.getOriginal().getCapability(MSUCapabilities.GOD_TIER_DATA, null).writeToNBT());
	}
}