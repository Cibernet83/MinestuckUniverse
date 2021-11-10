package com.cibernet.minestuckuniverse.capabilities.strife;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IStrifeData extends IMSUCapabilityBase<EntityLivingBase>
{
	StrifeSpecibus[] getPortfolio();

	boolean isPortfolioFull();
	boolean isPortfolioEmpty();
	boolean portfolioHasAbstratus(KindAbstratus abstratus);

	boolean addSpecibus(StrifeSpecibus specibus);
	StrifeSpecibus removeSpecibus(int index);
	void setSpecibus(StrifeSpecibus specibus, int index);
	void clearPortfolio();

	boolean canStrife();
	void setStrifeEnabled(boolean canStrife);
	boolean abstrataSwitcherUnlocked();
	void unlockAbstrataSwitcher(boolean unlocked);
	int getDroppedCards();
	void setDroppedCards(int v);
	boolean canDropCards();

	int getSelectedSpecibusIndex();
	int getSelectedWeaponIndex();
	void setSelectedSpecibusIndex(int index);
	void setSelectedWeaponIndex(int index);
	boolean isArmed();
	void setArmed(boolean armed);

	int getPrevSelSlot();
	void setPrevSelSlot(int slot);

	StrifeSpecibus[] getNonEmptyPortfolio();
	int getSpecibusIndex(StrifeSpecibus specibus);

	NBTTagCompound writeSelectedIndexes(NBTTagCompound nbt);
	NBTTagCompound writePortfolio(NBTTagCompound nbt, int... indexes);
	NBTTagCompound writeDroppedCards(NBTTagCompound nbt);
	NBTTagCompound writeConfig(NBTTagCompound nbt);
}
