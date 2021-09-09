package com.cibernet.minestuckuniverse.capabilities.beam;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.UUID;

public interface IBeamData extends IMSUCapabilityBase<World>
{
	ArrayList<Beam> getBeams();
	Beam getBeam(UUID beamId);
	void addBeam(Beam beam);
	void tickBeams();
}
