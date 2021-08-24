package com.cibernet.minestuckuniverse.capabilities.strife;

import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class StrifeData implements IStrifeData
{
	public static final int PORTFOLIO_SIZE = 10;

	protected EntityLivingBase owner;
	protected StrifeSpecibus[] portfolio = new StrifeSpecibus[PORTFOLIO_SIZE];
	protected int selWeapon = 0;
	protected int selSpecibus = 0;

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList portfolioList = new NBTTagList();

		for(int i = 0; i < portfolio.length; i++)
			if(portfolio[i] != null)
			{
				NBTTagCompound spNbt = new NBTTagCompound();
				spNbt.setInteger("Slot", i);
				spNbt.setTag("Specibus", portfolio[i].writeToNBT(new NBTTagCompound()));

				portfolioList.appendTag(spNbt);
			}

		nbt.setTag("Portfolio", portfolioList);
		nbt.setInteger("SelectedSpecibus", getSelectedSpecibusIndex());
		nbt.setInteger("SelectedWeapon", getSelectedWeaponIndex());

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList portfolioList = nbt.getTagList("Portfolio", 10);

		for(int i = 0; i < portfolioList.tagCount(); i++)
		{
			NBTTagCompound spNbt = portfolioList.getCompoundTagAt(i);
			int slot = spNbt.getInteger("Slot");
			if(slot >= 0 && slot < portfolio.length)
				portfolio[slot] = new StrifeSpecibus(spNbt.getCompoundTag("Specibus"));
		}

		setSelectedSpecibusIndex(nbt.getInteger("SelectedSpecibus"));
		setSelectedWeaponIndex(nbt.getInteger("SelectedWeapon"));
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
		System.out.println(portfolio);
		for(StrifeSpecibus sp : portfolio)
			if(sp != null && sp.getKindAbstratus() == abstratus)
				return true;
		return false;
	}

	@Override
	public boolean addSpecibus(StrifeSpecibus specibus)
	{
		if(isPortfolioFull() || portfolioHasAbstratus(specibus.getKindAbstratus()))
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
	public void setSpecibus(StrifeSpecibus specibus, int index)
	{
		portfolio[index] = specibus;
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
}
