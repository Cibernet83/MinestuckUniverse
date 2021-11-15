package com.cibernet.minestuckuniverse.capabilities.strife;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.FakePlayer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StrifeData implements IStrifeData
{
	public static final int PORTFOLIO_SIZE = 10;

	protected EntityLivingBase owner;
	protected final StrifeSpecibus[] portfolio = new StrifeSpecibus[PORTFOLIO_SIZE];
	protected int selWeapon = -1;
	protected int selSpecibus = -1;
	protected boolean isArmed = false;

	protected int droppedCards = 0;
	protected int prevSelSlot = 0;

	protected boolean abstrataSwitcherUnlocked = false;
	protected boolean strifeEnabled = false;

	@Override
	public void setOwner(EntityLivingBase owner) {
		this.owner = owner;
	}

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		if(selWeapon < 0)
		{
			clearPortfolio();
			portfolio[0] = StrifeSpecibus.empty();
			selWeapon = 0;
		}

		writePortfolio(nbt);
		writeSelectedIndexes(nbt);
		writeDroppedCards(nbt);
		nbt.setBoolean("AbstrataSwitcherUnlocked", abstrataSwitcherUnlocked);

		return nbt;
	}

	@Override
	public NBTTagCompound writeSelectedIndexes(NBTTagCompound nbt)
	{
		nbt.setInteger("SelectedSpecibus", getSelectedSpecibusIndex());
		nbt.setInteger("SelectedWeapon", getSelectedWeaponIndex());
		nbt.setBoolean("Armed", isArmed());
		return nbt;
	}

	@Override
	public NBTTagCompound writePortfolio(NBTTagCompound nbt, int... indexes)
	{
		if(indexes.length > 0)
			nbt.setBoolean("KeepPortfolio", true);

		NBTTagList portfolioList = new NBTTagList();
		for(int i = 0; i < portfolio.length; i++)
		{
			if((indexes.length <= 0 || ArrayUtils.contains(indexes, i)) && portfolio[i] != null)
			{
				NBTTagCompound spNbt = new NBTTagCompound();
				spNbt.setInteger("Slot", i);
				spNbt.setTag("Specibus", portfolio[i].writeToNBT(new NBTTagCompound()));

				portfolioList.appendTag(spNbt);
			}
		}

		nbt.setTag("Portfolio", portfolioList);
		return nbt;
	}

	@Override
	public NBTTagCompound writeDroppedCards(NBTTagCompound nbt)
	{
		nbt.setInteger("DroppedCards", droppedCards);
		return nbt;
	}

	@Override
	public NBTTagCompound writeConfig(NBTTagCompound nbt)
	{
		nbt.setBoolean("AbstrataSwitcherUnlocked", abstrataSwitcherUnlocked);
		nbt.setBoolean("CanStrife", strifeEnabled);
		return nbt;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Portfolio") && !nbt.getBoolean("KeepPortfolio"))
			clearPortfolio();

		if(nbt.hasKey("SelectedSpecibus"))
			setSelectedSpecibusIndex(nbt.getInteger("SelectedSpecibus"));
		if(nbt.hasKey("SelectedWeapon"))
			setSelectedWeaponIndex(nbt.getInteger("SelectedWeapon"));
		if(nbt.hasKey("Armed"))
			setArmed(nbt.getBoolean("Armed"));

		if(nbt.hasKey("CanStrife"))
			strifeEnabled = nbt.getBoolean("CanStrife");
		if(nbt.hasKey("AbstrataSwitcherUnlocked"))
			abstrataSwitcherUnlocked = nbt.getBoolean("AbstrataSwitcherUnlocked");
		if(nbt.hasKey("DroppedCards"))
			droppedCards = nbt.getInteger("DroppedCards");

		NBTTagList portfolioList = nbt.getTagList("Portfolio", 10);

		for(int i = 0; i < portfolioList.tagCount(); i++)
		{
			NBTTagCompound spNbt = portfolioList.getCompoundTagAt(i);
			int slot = spNbt.getInteger("Slot");
			if(slot >= 0 && slot < portfolio.length)
				portfolio[slot] = new StrifeSpecibus(spNbt.getCompoundTag("Specibus"));
		}
	}

	@Override
	public StrifeSpecibus[] getPortfolio() {
		return portfolio;
	}

	@Override
	public boolean isPortfolioFull()
	{
		for(StrifeSpecibus sp : portfolio)
			if(sp == null)
				return false;
		return true;
	}

	@Override
	public boolean isPortfolioEmpty()
	{
		for(StrifeSpecibus sp : portfolio)
			if(sp != null)
				return false;
		return false;
	}

	@Override
	public boolean portfolioHasAbstratus(KindAbstratus abstratus)
	{
		for(StrifeSpecibus sp : portfolio)
			if(sp != null && sp.getKindAbstratus() == abstratus)
				return true;
		return false;
	}

	@Override
	public boolean addSpecibus(StrifeSpecibus specibus)
	{
		if(isPortfolioFull() || (specibus.isAssigned() && portfolioHasAbstratus(specibus.getKindAbstratus())))
			return false;

		for(int i = 0; i < portfolio.length; i++)
			if(portfolio[i] == null)
			{
				portfolio[i] = specibus;
				break;
			}
		return true;
	}

	@Override
	public StrifeSpecibus removeSpecibus(int index)
	{
		if(index < 0 || index > portfolio.length || portfolio[index] == null)
			return null;
		StrifeSpecibus result = portfolio[index];
		portfolio[index] = null;

		if(getSelectedSpecibusIndex() == index)
		{
			setSelectedSpecibusIndex(-1);
			setArmed(false);
		}

		return result;
	}

	@Override
	public void setSpecibus(StrifeSpecibus specibus, int index)
	{
		portfolio[index] = specibus;
	}

	@Override
	public void clearPortfolio() {
		for(int i = 0; i < portfolio.length; i++)
			portfolio[i] = null;
	}

	@Override
	public boolean canStrife() {
		return strifeEnabled;
	}

	@Override
	public void setStrifeEnabled(boolean canStrife) {
		strifeEnabled = canStrife;
	}

	@Override
	public boolean abstrataSwitcherUnlocked() {
		return abstrataSwitcherUnlocked;
	}

	@Override
	public void unlockAbstrataSwitcher(boolean unlocked) {
		abstrataSwitcherUnlocked = unlocked;
	}

	@Override
	public int getDroppedCards() {
		return droppedCards;
	}

	@Override
	public void setDroppedCards(int v) {
		droppedCards = v;
	}

	@Override
	public boolean canDropCards()
	{
		if(!(owner instanceof EntityPlayer) || owner instanceof FakePlayer)
			return false;
		return droppedCards < Math.max(MSUConfig.strifeCardMobDrops, MinestuckPlayerData.getData((EntityPlayer) owner).echeladder.getRung()/6);
	}

	@Override
	public int getSelectedSpecibusIndex() {
		return selSpecibus;
	}

	@Override
	public int getSelectedWeaponIndex() {
		return selWeapon;
	}

	@Override
	public void setSelectedSpecibusIndex(int index)
	{
		if(selSpecibus != index)
		{
			selWeapon = 0;
			selSpecibus = index;
		}
	}

	@Override
	public void setSelectedWeaponIndex(int index)
	{
		selWeapon = index;
	}

	@Override
	public boolean isArmed() {
		return isArmed;
	}

	@Override
	public void setArmed(boolean armed)
	{
		isArmed = armed;
	}

	@Override
	public int getPrevSelSlot() {
		return prevSelSlot;
	}

	@Override
	public void setPrevSelSlot(int slot)
	{
		prevSelSlot = slot;
	}

	@Override
	public StrifeSpecibus[] getNonEmptyPortfolio()
	{
		int size = 0;
		for(StrifeSpecibus specibus : portfolio)
			if(specibus != null && specibus.isAssigned() && (specibus.getKindAbstratus().isFist() || !specibus.getContents().isEmpty()))
				size++;
		StrifeSpecibus[] result = new StrifeSpecibus[size];
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
			if(specibus != null && specibus.isAssigned() && (specibus.getKindAbstratus().isFist() || !specibus.getContents().isEmpty()))
				result[i++] = specibus;

		return result;
	}

	@Override
	public int getSpecibusIndex(StrifeSpecibus specibus)
	{
		for(int i = 0; i < portfolio.length; i++)
			if(portfolio[i] == specibus)
				return i;
		return -1;
	}
}
