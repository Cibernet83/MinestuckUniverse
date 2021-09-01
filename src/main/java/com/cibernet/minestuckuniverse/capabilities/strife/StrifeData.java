package com.cibernet.minestuckuniverse.capabilities.strife;

import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;

public class StrifeData implements IStrifeData
{
	public static final int PORTFOLIO_SIZE = 10;
	public static int abstrataSwitcherRung = -1; //TODO client sync

	protected EntityLivingBase owner;
	protected final StrifeSpecibus[] portfolio = new StrifeSpecibus[PORTFOLIO_SIZE];
	protected int selWeapon = -1;
	protected int selSpecibus = -1;
	protected boolean isArmed = false;

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
		NBTTagList portfolioList = new NBTTagList();
		for(int i = 0; i < portfolio.length; i++)
			if((indexes.length <= 0 || Arrays.asList(indexes).contains(i)) && portfolio[i] != null)
			{
				NBTTagCompound spNbt = new NBTTagCompound();
				spNbt.setInteger("Slot", i);
				spNbt.setTag("Specibus", portfolio[i].writeToNBT(new NBTTagCompound()));

				portfolioList.appendTag(spNbt);
			}

		nbt.setTag("Portfolio", portfolioList);
		return nbt;
	}



	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		clearPortfolio();

		if(nbt.hasKey("SelectedSpecibus"))
			setSelectedSpecibusIndex(nbt.getInteger("SelectedSpecibus"));
		if(nbt.hasKey("SelectedWeapon"))
			setSelectedWeaponIndex(nbt.getInteger("SelectedWeapon"));
		if(nbt.hasKey("Armed"))
			setArmed(nbt.getBoolean("Armed"));

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
		return false;
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
		setSelectedSpecibusIndex(-1);
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
		selWeapon = 0;
		selSpecibus = index;
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
