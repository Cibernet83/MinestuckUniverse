package com.cibernet.minestuckuniverse.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MSUCapabilityProvider<H extends IMSUCapabilityBase<O>, O> implements ICapabilitySerializable<NBTTagCompound>
{
	private final Capability<H> capability;
	private final H instance;

	public MSUCapabilityProvider(Capability<H> capability, O owner)
	{
		this.capability = capability;
		this.instance = getCapability().getDefaultInstance();
		this.instance.setOwner(owner);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == getCapability();
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == getCapability() ? getCapability().cast(instance) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound) getCapability().writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		getCapability().readNBT(instance, null, nbt);
	}

	public Capability<H> getCapability()
	{
		return capability;
	}

	public static class Storage<H extends IMSUCapabilityBase> implements Capability.IStorage<H>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<H> capability, H instance, EnumFacing side)
		{
			return instance.writeToNBT();
		}

		@Override
		public void readNBT(Capability<H> capability, H instance, EnumFacing side, NBTBase nbt)
		{
			instance.readFromNBT((NBTTagCompound) nbt);
		}
	}
}
