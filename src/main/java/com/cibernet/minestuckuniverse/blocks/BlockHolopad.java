package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHolopad extends BlockContainer implements IRegistryItem
{

	public BlockHolopad()
	{
		super(Material.ROCK, MapColor.SNOW);
		setUnlocalizedName("holopad");
		setCreativeTab(TabMinestuckUniverse.main);
		this.setHardness(2.0F);
		this.setHarvestLevel("pickaxe", 0);
	}
	
	protected static final AxisAlignedBB HOLOPAD_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.06875D, 0.875D, 0.375D, 0.81875D);
	protected static final AxisAlignedBB HOLOPAD_TOP_AABB = new AxisAlignedBB(0.1875D, 0.375D, 0.1625D, 0.8125D, 0.4375D, 0.7875D);
	protected static final AxisAlignedBB HOLOPAD_CARDSLOT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.8625D, 0.75D, 0.63125D, 0.99625D);
	public static final PropertyDirection FACING;
	public static final PropertyBool CARD;
	
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityHolopad();
	}
	
	public boolean hasTileEntity() {
		return true;
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		} else {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TileEntityHolopad) {
				((TileEntityHolopad)te).onRightClick(playerIn);
			}
			
			return true;
		}
	}
	
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntityHolopad te = (TileEntityHolopad)worldIn.getTileEntity(pos);
		if (te != null && !worldIn.isRemote) {
			te.dropItem(true, worldIn, pos, te.getCard());
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withProperty(FACING, mirrorIn.mirror((EnumFacing)state.getValue(FACING)));
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		iblockstate = iblockstate.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
		return iblockstate;
	}
	
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{FACING, CARD});
	}
	
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntityHolopad tileEntity = (TileEntityHolopad)worldIn.getTileEntity(pos);
		return tileEntity != null && tileEntity.hasCard() ? state.withProperty(CARD, true) : state.withProperty(CARD, false);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = (EnumFacing)state.getValue(FACING);
		return this.modifyAABBForDirection(facing, HOLOPAD_AABB);
	}
	
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
		EnumFacing facing = (EnumFacing)state.getValue(FACING);
		AxisAlignedBB top = this.modifyAABBForDirection(facing, HOLOPAD_TOP_AABB);
		AxisAlignedBB cardSlot = this.modifyAABBForDirection(facing, HOLOPAD_CARDSLOT_AABB);
		if (entityBox.intersects(top)) {
			collidingBoxes.add(top);
		}
		
		if (entityBox.intersects(cardSlot)) {
			collidingBoxes.add(cardSlot);
		}
		
	}
	
	public AxisAlignedBB modifyAABBForDirection(EnumFacing facing, AxisAlignedBB bb) {
		AxisAlignedBB out = null;
		switch(facing.ordinal()) {
			case 2:
				out = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
				break;
			case 3:
				out = new AxisAlignedBB(1.0D - bb.maxX, bb.minY, 1.0D - bb.maxZ, 1.0D - bb.minX, bb.maxY, 1.0D - bb.minZ);
				break;
			case 4:
				out = new AxisAlignedBB(bb.minZ, bb.minY, 1.0D - bb.maxX, bb.maxZ, bb.maxY, 1.0D - bb.minX);
				break;
			case 5:
				out = new AxisAlignedBB(1.0D - bb.maxZ, bb.minY, bb.minX, 1.0D - bb.minZ, bb.maxY, bb.maxX);
		}
		
		return out;
	}
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	static {
		FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
		CARD = PropertyBool.create("card");
	}

	@Override
	public void setRegistryName() {
		setRegistryName("holopad");
	}
}
