package com.cibernet.minestuckuniverse.world;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.BlockBlobDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import thaumcraft.api.blocks.BlocksTC;

import java.util.ArrayList;
import java.util.List;

public class LandAspectBlobs extends TerrainLandAspect
{
    static Vec3d fogColor = new Vec3d(0.5D, 0.5D, 0.5D);
    static Vec3d skyColor = new Vec3d(0.75D, 0.75D, 0.75D);

    @Override
    public void registerBlocks(StructureBlockRegistry registry)
    {
        registry.setBlockState("surface", Blocks.AIR.getDefaultState());
        registry.setBlockState("upper", Blocks.AIR.getDefaultState());
        registry.setBlockState("ground", Blocks.AIR.getDefaultState());
        registry.setBlockState("ocean", Blocks.AIR.getDefaultState());
        registry.setBlockState("structure_primary", Blocks.END_BRICKS.getDefaultState());
        registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        registry.setBlockState("structure_primary_stairs", Blocks.PURPUR_STAIRS.getDefaultState());
        registry.setBlockState("structure_secondary", Blocks.PURPUR_BLOCK.getDefaultState());
        registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
        registry.setBlockState("fall_fluid", MinestuckBlocks.blockEnder.getDefaultState());
        registry.setBlockState("structure_planks", Blocks.BRICK_BLOCK.getDefaultState());
        registry.setBlockState("structure_planks_slab", Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.BRICK));
        registry.setBlockState("village_path", MinestuckBlocks.coarseEndStone.getDefaultState());
        registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.getDefaultState());
        registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
        registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE));
    }

    @Override
    public List<ILandDecorator> getDecorators()
    {
        ArrayList<ILandDecorator> list = new ArrayList();
        list.add(new BlockBlobDecorator(Blocks.STONE.getDefaultState(), 8, 5, new Biome[]{BiomeMinestuck.mediumRough}));
        list.add(new BlockBlobDecorator(Blocks.CLAY.getDefaultState(), 8, 5, new Biome[]{BiomeMinestuck.mediumNormal}));
        return list;
    }

    @Override
    public Vec3d getFogColor() {
        return fogColor;
    }

    @Override
    public EnumConsort getConsortType() {
        return EnumConsort.SALAMANDER;
    }

    @Override
    public String getPrimaryName() {
        return "blobs";
    }

    @Override
    public String[] getNames() {
        return new String[]{"blobs","floaty_things"};
    }
}
