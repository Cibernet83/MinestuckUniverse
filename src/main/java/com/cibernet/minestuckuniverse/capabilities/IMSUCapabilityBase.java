package com.cibernet.minestuckuniverse.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface IMSUCapabilityBase<O>
{
	NBTTagCompound writeToNBT();
	void readFromNBT(NBTTagCompound nbt);
	default void setOwner(O owner) {}
}
