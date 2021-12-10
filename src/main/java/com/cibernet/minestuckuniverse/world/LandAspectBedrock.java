package com.cibernet.minestuckuniverse.world;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LandAspectBedrock extends TerrainLandAspect
{
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{

		registry.setBlockState("surface", Blocks.AIR.getDefaultState());
		registry.setBlockState("upper", Blocks.AIR.getDefaultState());
		registry.setBlockState("ground", Blocks.AIR.getDefaultState());

		registry.setBlockState("structure_primary", Blocks.BEDROCK.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.BEDROCK.getDefaultState());
		registry.setBlockState("structure_secondary", Blocks.BEDROCK.getDefaultState());
		registry.setBlockState("structure_primary_stairs", MinestuckUniverseBlocks.bedrockStairs.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MinestuckUniverseBlocks.bedrockStairs.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.BEDROCK.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.BEDROCK.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
		registry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN));

		registry.setBlockState("river", Blocks.AIR.getDefaultState());
		registry.setBlockState("ocean", Blocks.AIR.getDefaultState());
		registry.setBlockState("fall_fluid", Blocks.AIR.getDefaultState());

		registry.setBlockState("light_block", Blocks.AIR.getDefaultState());
		registry.setBlockState("bucket1", Blocks.AIR.getDefaultState());
		//registry.setBlockState("bush", Blocks.AIR.getDefaultState().withProperty(BlockTallGrass.TYPE, net.minecraft.block.BlockTallGrass.EnumType.FERN));
	}

	@Override
	public List<ILandDecorator> getDecorators()
	{
		return new ArrayList<>();
	}

	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0,0,0);
	}

	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.SALAMANDER;
	}

	@Override
	public String getPrimaryName()
	{
		return "null";
	}

	@Override
	public String[] getNames()
	{
		return new String[] {"null"};
	}
}
