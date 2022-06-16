package com.cibernet.minestuckuniverse.world.gen;

import com.cibernet.minestuckuniverse.world.gen.structure.StructureQuestBed;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenHandler implements IWorldGenerator
{

	public static final StructureQuestBed QUEST_BED = new StructureQuestBed();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(chunkGenerator instanceof ChunkProviderLands)
			QUEST_BED.generate(world, random, new BlockPos(chunkX*16, 80, chunkZ*16));
	}
}
