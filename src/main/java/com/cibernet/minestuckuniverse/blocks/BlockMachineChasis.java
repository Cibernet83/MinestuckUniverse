package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMachineChasis extends MSUBlockBase implements ITileEntityProvider
{


    public BlockMachineChasis()
    {
        super(Material.IRON, MapColor.IRON, "machine_chassis");
        setUnlocalizedName("machineChassis");
        setHarvestLevel("pickaxe", 0);
        setHardness(3.0F);
    }

    @Override
    public boolean isTopSolid(IBlockState state) { return true; }
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMachineChasis();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
            playerIn.openGui(MinestuckUniverse.instance, MSUUtils.MACHINE_CHASIS_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityMachineChasis te = (TileEntityMachineChasis)worldIn.getTileEntity(pos);
        if(te != null)
            InventoryHelper.dropInventoryItems(worldIn, pos, te);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        checkPowered(worldIn, pos);
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        checkPowered(worldIn, pos);

    }

    private void checkPowered(World worldIn, BlockPos pos)
    {
        if(worldIn.isBlockPowered(pos) && worldIn.getTileEntity(pos) instanceof TileEntityMachineChasis)
        {
            TileEntityMachineChasis te = (TileEntityMachineChasis) worldIn.getTileEntity(pos);
            te.assemble();
        }
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return true;
    }
}
