package com.cibernet.minestuckuniverse.capabilities.mediumData;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public interface IMediumData extends IMSUCapabilityBase<World>
{
	ChunkPos getQuestBedChunk();
	BlockPos getQuestBedPos();
}
