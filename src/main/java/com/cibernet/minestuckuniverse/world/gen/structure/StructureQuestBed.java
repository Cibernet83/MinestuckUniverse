package com.cibernet.minestuckuniverse.world.gen.structure;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class StructureQuestBed extends WorldGenerator
{

	public static final int bottom = 64;
	public static final int top = 154;
	public static final int radius = 32;

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		ChunkPos chunk = getQuestBedChunk(worldIn);

		if(worldIn.provider.createChunkGenerator() instanceof ChunkProviderLands && position.getX()/16 == chunk.x + radius/16 && position.getZ()/16 == chunk.z  + radius/16)
		{
			position = position.add(-2 - radius, 0, -1 - radius);

			ChunkProviderLands landGenerator = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			Title title = MinestuckPlayerData.getData(SburbHandler.getConnectionForDimension(worldIn.provider.getDimension()).getClientIdentifier()).title;
			EnumAspect aspect = title == null ? null : title.getHeroAspect();

			IBlockState wall = (aspect == null ? MinestuckUniverseBlocks.wildcardHeroStoneWall : MinestuckUniverseBlocks.heroStoneWalls.get(aspect)).getDefaultState();
			IBlockState light = MinestuckUniverseBlocks.glowingHeroStone.getDefaultState();
			IBlockState ground = getMountainMaterial(landGenerator);

			double j = 1;
			for(double i = 1; i < top-bottom; i = i > 32 ? Math.floor(i+1) : i + Math.min(1, i/32d))
			{
				int blockSize = Math.max(3, 16 - (int)(i*10d/(top-bottom)));

				int x = (int) (position.getX() + Math.cos(j/8d *(Math.PI*2d)) * Math.max(5, radius-i*0.3d));
				int z = (int) (position.getZ() + Math.sin(j/8d *(Math.PI*2d)) * Math.max(5, radius-i*0.3d));
				int y = bottom + (int)i;

				for(int w = 0; w < blockSize; w++) for (int d = 0; d < blockSize; d++) for (int h = 0; h < Math.min(32, 9+i); h++)
					if(ground.isOpaqueCube() || (!ground.isOpaqueCube() && !worldIn.getBlockState(new BlockPos(x+w, y-h, z+d)).getMaterial().blocksMovement()))
						setBlockAndNotifyAdequately(worldIn, new BlockPos(x+w, y-h, z+d), ground);

				j += j/48d;
			}

			for(int yOff = 1; yOff <= 16; yOff++)
				for(int xOff = -7; xOff <= 7; xOff++)
				{
					int z2 = (int)Math.sqrt(Math.pow(7, 2) - Math.pow(xOff, 2));
					for(int zOff = -z2; zOff < z2; zOff++)
						setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+xOff+2, top-yOff, position.getZ()+zOff+2), ground);
				}

			position = position.add(2, 0, 1);

			for(int xOff = -1; xOff <= 1; xOff++)
				for(int zOff = -1; zOff <= 2; zOff++)
					setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + xOff, top, position.getZ() + zOff), (
							xOff == 0 && zOff == 0 ?
									(aspect == null ? MinestuckUniverseBlocks.wildcardChiseledHeroStone : MinestuckUniverseBlocks.chiseledHeroStones.get(aspect)) :
									(aspect == null ? MinestuckUniverseBlocks.wildcardHeroStone : MinestuckUniverseBlocks.heroStones.get(aspect))
							).getDefaultState());


			for(int yOff = 0; yOff < 2; yOff++)
				for(int xOff = -1; xOff <= 1; xOff++)
					setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+xOff, top+yOff, position.getZ()-2), wall);

			setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX(), top+2, position.getZ()-2), wall);

			for(int yOff = 0; yOff < 7; yOff++)
			{
				setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()-2, top+yOff, position.getZ()+3), wall);
				setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+2, top+yOff, position.getZ()+3), wall);
				setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+2, top+yOff, position.getZ()-2), wall);
				setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()-2, top+yOff, position.getZ()-2), wall);
			}
			setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()-2, top+7, position.getZ()+3), light);
			setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+2, top+7, position.getZ()+3), light);
			setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()+2, top+7, position.getZ()-2), light);
			setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX()-2, top+7, position.getZ()-2), light);

			return true;
		}

		return false;
	}

	public static IBlockState getMountainMaterial(ChunkProviderLands generator)
	{
		TerrainLandAspect terrain = generator.aspect1;

		if(terrain instanceof LandAspectSandstone || terrain instanceof LandAspectWood)
			return generator.getUpperBlock();

		if(terrain instanceof LandAspectShade)
			return Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(11);
		if(terrain instanceof LandAspectRainbow)
			return Blocks.STAINED_GLASS.getStateFromMeta(0);
		if(terrain instanceof LandAspectFrost)
			return Blocks.PACKED_ICE.getDefaultState();

		return generator.getGroundBlock();
	}

	public static BlockPos getQuestBedPos(World world)
	{
		if(world.hasCapability(MSUCapabilities.MEDIUM_DATA, null))
			return world.getCapability(MSUCapabilities.MEDIUM_DATA, null).getQuestBedPos();
		else return null;
	}

	public static ChunkPos getQuestBedChunk(World world)
	{
		if(world.hasCapability(MSUCapabilities.MEDIUM_DATA, null))
			return world.getCapability(MSUCapabilities.MEDIUM_DATA, null).getQuestBedChunk();
		else return null;
	}
}
