package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.tileentity.TileEntityPlatinumTransportalizer;
import com.cibernet.minestuckuniverse.tileentity.TileEntityRedTransportalizer;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockPlatinumTransportalizer extends BlockCustomTransportalizer
{
    public BlockPlatinumTransportalizer() {
        super(MapColor.WHITE_STAINED_HARDENED_CLAY);
        this.setUnlocalizedName("platinumTransportalizer");
        setBlockUnbreakable();
        setResistance(6000000.0F);
        disableStats();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileEntityPlatinumTransportalizer();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!playerIn.isCreative())
        {
            if (playerIn instanceof EntityPlayerMP)
                playerIn.sendStatusMessage(new TextComponentTranslation("message.transportalizer.notOpped"), true);
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void setRegistryName() {
        this.setRegistryName("platinum_transportalizer");
    }
}
