package com.cibernet.minestuckuniverse.world.decorators;

import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class AirDecoratorVein implements ILandDecorator
{

	int amount;
	IBlockState block;
	int size;
	int highestY;

	public AirDecoratorVein(IBlockState block, int amount, int size, int highestY)
	{
		this.amount = amount;
		this.block = block;
		this.size = size;
		this.highestY = highestY;
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int minY = 0;
		int maxY = highestY;
		int diffBtwnMinMaxY = maxY - minY;
		for(int x = 0; x < amount; x++)
		{
			int posX = chunkX * 16 + random.nextInt(16);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = chunkZ * 16 + random.nextInt(16);
			(new WorldGenMinable(block, size, new OreHandler.BlockStatePredicate(Blocks.AIR.getDefaultState()))).generate(world, random, new BlockPos(posX, posY, posZ));
		}
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.2F;
	}
	
}
