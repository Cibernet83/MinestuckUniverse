package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.items.ItemAbilitechnosyth;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.block.BlockLargeMachine;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BlockAbilitechnosynth extends BlockLargeMachine implements IRegistryItem
{
	final String name;
	final int index;

	public static final PropertyInteger PART = PropertyInteger.create("part", 0, 3);

	public BlockAbilitechnosynth(int index)
	{
		this(index == 0 ? "abilitechnosynth" : "abilitechnosynth_"+index, index);
		setCreativeTab(TabMinestuckUniverse.main);
	}

	public BlockAbilitechnosynth(String name, int index)
	{
		super();

		this.index = index;
		this.name = name;
		setUnlocalizedName("abilitechnosynth");

		setDefaultState(getDefaultState().withProperty(DIRECTION, EnumFacing.SOUTH).withProperty(PART, 0));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PART, DIRECTION);
	}

	public static IBlockState getState(int index, EnumFacing facing)
	{
		return MinestuckUniverseBlocks.abilitechnosynth[index/4].getDefaultState().withProperty(DIRECTION, facing).withProperty(PART, index%4);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DIRECTION, EnumFacing.getHorizontal(meta/4)).withProperty(PART, meta%4);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIRECTION).getHorizontalIndex()*4 + state.getValue(PART);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		int part = Arrays.asList(MinestuckUniverseBlocks.abilitechnosynth).indexOf(state.getBlock())*4 + state.getValue(BlockAbilitechnosynth.PART);
		if(!playerIn.isSneaking() && (facing.equals(state.getValue(DIRECTION)) || (part < 12 && part >= 6 && facing.equals(EnumFacing.UP))))
		{
			if(worldIn.isRemote && ItemAbilitechnosyth.isValid(state, worldIn, pos))
				playerIn.openGui(MinestuckUniverse.instance, MSUUtils.FRAYMACHINE_UI, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;

	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(MinestuckUniverseBlocks.abilitechnosynth[0]);
	}

	@Override
	public Item getItemFromMachine()
	{
		return Item.getItemFromBlock(MinestuckUniverseBlocks.abilitechnosynth[0]);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return null;
	}

	@Override
	public void setRegistryName()
	{
		setRegistryName(name);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int part = Arrays.asList(MinestuckUniverseBlocks.abilitechnosynth).indexOf(state.getBlock())*4 + state.getValue(BlockAbilitechnosynth.PART);
		if(AABBS[part] == null)
		{
			AxisAlignedBB aabb = COLLISION_AABBS[part][0];

			for(AxisAlignedBB c : COLLISION_AABBS[part])
				aabb = new AxisAlignedBB(Math.min(aabb.minX, c.minX), Math.min(aabb.minY, c.minY), Math.min(aabb.minZ, c.minZ), Math.max(aabb.maxX, c.maxX), Math.max(aabb.maxY, c.maxY), Math.max(aabb.maxZ, c.maxZ));

			AABBS[part] = aabb;
		}

		return BlockHolopad.modifyAABBForDirection(state.getValue(DIRECTION), AABBS[part]);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		int part = Arrays.asList(MinestuckUniverseBlocks.abilitechnosynth).indexOf(state.getBlock())*4 + state.getValue(BlockAbilitechnosynth.PART);
		for(AxisAlignedBB aabb : COLLISION_AABBS[part])
		{
			aabb = BlockHolopad.modifyAABBForDirection(state.getValue(DIRECTION), aabb).offset(pos);
			if(entityBox.intersects(aabb))
				collidingBoxes.add(aabb);
		}
	}

	static final AxisAlignedBB[][] COLLISION_AABBS = new AxisAlignedBB[][]
			{
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,0,13/16d,5/15d,1), new AxisAlignedBB(7/16d,5/16d,2/16d,11/16d,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,0,1,5/15d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(3/16d,0,0,1,5/15d,1), new AxisAlignedBB(5/16d,5/16d,2/16d,9/16d,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,0,13/16d,5/15d,1), new AxisAlignedBB(0,5/16d,0,12/16d,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,0,1,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(3/16d,0,0,1,5/15d,1), new AxisAlignedBB(1/16d,5/16d,0,1,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(7/16d,0,0,11/16d,7.5/16d,1), new AxisAlignedBB(0,0,12.1/16d,7/16d,2/16d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,12.1/16d,1,2/16d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(5/16d,0,0,9/16d,7.5/16d,1), new AxisAlignedBB(9/16d, 0, 12.1/16d, 1, 2/16d, 1)},
				new AxisAlignedBB[]{new AxisAlignedBB(6.99/16d, 0,0, 11.02/16d,7.5/16d,12/16d), new AxisAlignedBB(0,0,0,7/16d,4.5/16d,.5d), new AxisAlignedBB(0,0,0.5d,.5d,1,.5d),
					new AxisAlignedBB(.5d, 0, 12.01/16d,4/16d,14/16d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,0,1,4.5/16d,.5d), new AxisAlignedBB(0,0,0.5d,1,1,.5d)},
				new AxisAlignedBB[]{new AxisAlignedBB(9/16d,0,0,7/16d,4.5/16d,.5d), new AxisAlignedBB(4.99/16d,0,0,9.01/16d,7.5/16d,12/16d), new AxisAlignedBB(.5d, 0, .5d, 1, 1, 1),
					new AxisAlignedBB(4/16d,0,12.01/16d,.5d,14/16d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,12/16d,4/16d,14/16d,1), new AxisAlignedBB(4/16d,0,12/16d,.5d,7/16d,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(0,0,.5d,4/16d,6/16d,12/16d), new AxisAlignedBB(4/16d,0,.5d,12/16d,9/16d,12/16d), new AxisAlignedBB(12/16d,0,.5d,1,6/16d,12/16d),
					new AxisAlignedBB(0,0,12/16d,1,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(.5d,0,12/16d,12/16d,7/16d,1), new AxisAlignedBB(12/16d,0,12/16d,1,1,1)},
				new AxisAlignedBB[]{new AxisAlignedBB(12/16d,0,12/16d,1,3/16d,1), new AxisAlignedBB(4/16d,0,12/16d,12/16d,7/16d,1), new AxisAlignedBB(0,0,12/16d,4/16d,3/16d,1)},
			};
	static AxisAlignedBB[] AABBS = new AxisAlignedBB[16];
}
