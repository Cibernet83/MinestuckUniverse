package com.cibernet.minestuckuniverse.capabilities.mediumData;

import com.cibernet.minestuckuniverse.MSUConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class MediumData implements IMediumData
{
	ChunkPos questBedChunk;
	World world;

	@Override
	public void setOwner(World owner) {
		this.world = owner;
	}

	@Override
	public ChunkPos getQuestBedChunk()
	{
		if(questBedChunk == null)
		{
			world.rand.setSeed(world.getSeed() * world.provider.getDimension());

			int r = (world.rand.nextInt(MSUConfig.questBedSpawnArea) + MSUConfig.questBedSpawnDistance)/16;
			double a = world.rand.nextDouble() * Math.PI * 2d;

			questBedChunk = new ChunkPos((int)(Math.cos(a)*r), (int)(Math.sin(a)*r));
		}

		return questBedChunk;
	}

	@Override
	public BlockPos getQuestBedPos()
	{
		ChunkPos chunkPos = getQuestBedChunk();
		return new BlockPos(chunkPos.x*16, 128, chunkPos.z*16);
	}

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		if(questBedChunk != null)
		{
			nbt.setInteger("BedChunkX", questBedChunk.x);
			nbt.setInteger("BedChunkZ", questBedChunk.z);
		}

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("BedChunkX") && nbt.hasKey("BedChunkZ"))
		{
			questBedChunk = new ChunkPos(nbt.getInteger("BedChunkX"), nbt.getInteger("BedChunkZ"));
		}
	}
}
