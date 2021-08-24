package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.tileentity.TileEntityParadoxTransportalizer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockParadoxTransportalizer extends BlockContainer implements IRegistryItem
{
	protected static final AxisAlignedBB TRANSPORTALIZER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	public BlockParadoxTransportalizer()
	{
		super(Material.IRON, MapColor.IRON);
		setUnlocalizedName("paradoxTransportalizer");
		this.setHardness(3.5F);
		this.setHarvestLevel("pickaxe", 0);
		setCreativeTab(TabMinestuckUniverse.main);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		String key = getUnlocalizedName()+".tooltip";
		if(!I18n.translateToLocal(key).equals(key))
			tooltip.add(I18n.translateToLocal(key));
		super.addInformation(stack, player, tooltip, advanced);
	}


	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return TRANSPORTALIZER_AABB;
	}
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!world.isRemote && entity.getRidingEntity() == null && entity.getPassengers().isEmpty())
		{
			TileEntityParadoxTransportalizer te = (TileEntityParadoxTransportalizer)world.getTileEntity(pos);
			if(te != null)
				if (entity.timeUntilPortal == 0) {
					te.teleport(world, pos, entity);
				} else {
					entity.timeUntilPortal = entity.getPortalCooldown();
				}
		}
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityParadoxTransportalizer();
	}

	@Override
	public void setRegistryName() {
		setRegistryName("paradox_transportalizer");
	}
}
