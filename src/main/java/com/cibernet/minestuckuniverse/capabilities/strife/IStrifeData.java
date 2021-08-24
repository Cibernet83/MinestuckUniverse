package com.cibernet.minestuckuniverse.capabilities.strife;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import net.minecraft.entity.EntityLivingBase;

public interface IStrifeData extends IMSUCapabilityBase<EntityLivingBase>
{
	StrifeSpecibus[] getPortfolio();

	boolean isPortfolioFull();
	boolean isPortfolioEmpty();
	boolean portfolioHasAbstratus(KindAbstratus abstratus);

	boolean addSpecibus(StrifeSpecibus specibus);
	void setSpecibus(StrifeSpecibus specibus, int index);

	int getSelectedSpecibusIndex();
	int getSelectedWeaponIndex();
	void setSelectedSpecibusIndex(int index);
	void setSelectedWeaponIndex(int index);
}
