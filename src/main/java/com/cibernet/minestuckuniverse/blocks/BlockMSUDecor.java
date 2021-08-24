package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.mraof.minestuck.block.BlockDecor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMSUDecor extends BlockDecor implements IRegistryItem
{
    private final String registryName;

    protected BlockMSUDecor(String unlocalizedName, String regName)
    {
        super(unlocalizedName);
        registryName = regName;
        setCreativeTab(TabMinestuckUniverse.main);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        switch(getBBFromName())
        {
            case CHESSBOARD:
                return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
            case BLENDER: case FROG_STATUE: default:
            return BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing facing = state.getValue(FACING);

        EnumBB boundingBox = getBB();

        return boundingBox.BOUNDING_BOX[facing.getHorizontalIndex()];
    }

    public AxisAlignedBB modifyAABBForDirection(EnumFacing facing, AxisAlignedBB bb)
    {
        AxisAlignedBB out = null;
        switch(facing.ordinal())
        {
            case 2:	//North
                out = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
                break;
            case 3:	//South
                out = new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ);
                break;
            case 4:	//West
                out = new AxisAlignedBB(bb.minZ, bb.minY, 1-bb.maxX, bb.maxZ, bb.maxY, 1-bb.minX);
                break;
            case 5:	//East
                out = new AxisAlignedBB(1-bb.maxZ, bb.minY, bb.minX, 1-bb.minZ, bb.maxY, bb.maxX);
                break;
        }
        return out;
    }

    public EnumBB getBB()
    {
        return EnumBB.WIZARD;
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }

    public enum EnumBB implements IStringSerializable
    {
        WIZARD	(new AxisAlignedBB(3/16D, 0.0D, 3/16D, 13/16D, 1D, 12/16D))
        ;

        private final AxisAlignedBB[] BOUNDING_BOX;

        EnumBB(AxisAlignedBB bb)
        {
            BOUNDING_BOX = new AxisAlignedBB[4];
            BOUNDING_BOX[0] = bb;
            BOUNDING_BOX[1] = new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
            BOUNDING_BOX[2] = new AxisAlignedBB(1 - bb.maxX, bb.minY, 1- bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
            BOUNDING_BOX[3] = new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);

        }

        @Override
        public String toString()
        {
            return getName();
        }

        @Override
        public String getName()
        {
            return name().toLowerCase();
        }
    }
}
