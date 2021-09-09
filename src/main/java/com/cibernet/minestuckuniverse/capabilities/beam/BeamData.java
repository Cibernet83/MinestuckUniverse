package com.cibernet.minestuckuniverse.capabilities.beam;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class BeamData implements IBeamData
{
	private World owner;
	private final LinkedHashMap<UUID, Beam> beams = new LinkedHashMap<>();

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList beamList = new NBTTagList();

		for(Beam beam : getBeams())
			beamList.appendTag(beam.writeToNBT());

		nbt.setTag("Beams", beamList);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		beams.clear();
		NBTTagList beamList = nbt.getTagList("Beams", 10);

		for(int i = 0; i < beamList.tagCount(); i++)
		{
			Beam beam = new Beam(owner, nbt.hasUniqueId("UUID") ? nbt.getUniqueId("UUID"): UUID.randomUUID());
			beam.readFromNBT(beamList.getCompoundTagAt(i));
			addBeam(beam);
		}
	}

	@Override
	public void setOwner(World owner)
	{
		this.owner = owner;
	}

	@Override
	public ArrayList<Beam> getBeams() {
		return new ArrayList<>(beams.values());
	}

	@Override
	public Beam getBeam(UUID beamId) {
		return beams.get(beamId);
	}

	@Override
	public void addBeam(Beam beam)
	{
		beams.put(beam.getUniqueID(), beam);
	}

	@Override
	public void tickBeams()
	{
		for(Beam beam : getBeams())
		{
			beam.onUpdate();
			if(beam.isDead())
				beams.values().remove(beam);
		}
	}
}
