package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.tileentity.TileEntityAutoCaptcha;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAutoCaptcha extends BlockContainer implements IRegistryItem
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	protected BlockAutoCaptcha()
	{
		super(Material.IRON);
		setUnlocalizedName("autoCaptcha");
		setHarvestLevel("pickaxe", 0);
		setHardness(3.0F);
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
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityAutoCaptcha();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote)
			playerIn.openGui(MinestuckUniverse.instance, MSUUtils.AUTO_CAPTCHA_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityAutoCaptcha te = (TileEntityAutoCaptcha) worldIn.getTileEntity(pos);
		if(te != null)
			InventoryHelper.dropInventoryItems(worldIn, pos, te);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return false;
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
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntityAutoCaptcha te = (TileEntityAutoCaptcha) worldIn.getTileEntity(pos);
		if(pos == null)
			return super.getComparatorInputOverride(blockState, worldIn, pos);
		int strength = 0;
		ItemStack stack = te.getStackInSlot(0);
		if(!stack.isEmpty())
			strength++;
		if(!te.getStackInSlot(1).isEmpty())
			strength++;
		if(stack.getCount() == stack.getMaxStackSize())
			strength++;
		return strength;
	}

	@Override
	public void setRegistryName() {
		setRegistryName("auto_captcha");
	}
}
