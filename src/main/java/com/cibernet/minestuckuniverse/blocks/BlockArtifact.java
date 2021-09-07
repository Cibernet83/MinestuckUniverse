package com.cibernet.minestuckuniverse.blocks;

import com.mraof.minestuck.block.BlockLargeMachine;
import com.mraof.minestuck.block.BlockSburbMachine;
import com.mraof.minestuck.event.AlchemizeItemAlchemiterEvent;
import com.mraof.minestuck.event.AlchemizeItemEvent;
import com.mraof.minestuck.event.AlchemizeItemMinichemiterEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockArtifact extends MSUBlockBase
{
	public BlockArtifact(Material fire, MapColor purple, String artifact, String artifact1) {
		super(fire, purple, artifact, artifact1);

		setHardness(0);
		setResistance(0);
		disableStats();
		setTickRandomly(true);
	}

	@Override
	public int tickRate(World worldIn) {
		return 10;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (!worldIn.isAreaLoaded(pos, 2)) return;

		for(EnumFacing face : EnumFacing.values())
		{
			BlockPos targetPos = pos.offset(face);
			float chance = getBlockEncouragement(worldIn, targetPos);
			if(chance >= 1 || (chance > 0 && worldIn.rand.nextFloat() < chance))
				worldIn.setBlockState(targetPos, getDefaultState(), 3);
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);

		if(world instanceof World)
		for(EnumFacing face : EnumFacing.values())
		{
			BlockPos targetPos = pos.offset(face);
			float chance = getBlockEncouragement((World) world, targetPos);
			if(chance >= 1 || (chance > 0 && ((World)world).rand.nextFloat() < chance))
				((World)world).setBlockState(targetPos, getDefaultState(), 3);
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}


	private float getBlockEncouragement(World worldIn, BlockPos pos)
	{
		if (worldIn.isAirBlock(pos))
			return 0;
		IBlockState state = worldIn.getBlockState(pos);
		float hardness =  state.getBlockHardness(worldIn, pos);

		if(state.getBlock() instanceof BlockSburbMachine || state.getBlock() instanceof BlockLargeMachine || hardness == 0)
			return 1;
		if(state.getBlock() instanceof BlockArtifact || hardness < 0)
			return 0;

		return Math.min(0.2f, 0.25f/state.getBlockHardness(worldIn, pos));
	}

	@SubscribeEvent
	public static void onAlchemize(AlchemizeItemEvent event)
	{
		if(event.getResultItem().getItem() instanceof ItemBlock && ((ItemBlock) event.getResultItem().getItem()).getBlock() instanceof BlockArtifact)
		{
			BlockPos pos = null;

			if(event instanceof AlchemizeItemAlchemiterEvent)
				pos = ((AlchemizeItemAlchemiterEvent) event).getItemPos().down();
			else if(event instanceof AlchemizeItemMinichemiterEvent)
				pos = ((AlchemizeItemMinichemiterEvent) event).getAlchemiter().getPos();

			if(pos != null)
			{
				event.getWorld().setBlockState(pos, ((ItemBlock) event.getResultItem().getItem()).getBlock().getDefaultState(), 3);
				event.setCanceled(true);
			}
		}

	}
}
