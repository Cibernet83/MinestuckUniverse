package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MSUUtils;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMachineChasis extends MSUBlockBase implements ITileEntityProvider
{


    public BlockMachineChasis()
    {
        super(Material.IRON, MapColor.IRON);
        setUnlocalizedName("machineChasis");
        setRegistryName("machine_chasis");
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
}
