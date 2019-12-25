package com.cibernet.minestuckuniverse.blocks;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockParadoxTransportalizer extends MSUBlockBase
{
	protected static final AxisAlignedBB TRANSPORTALIZER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	public BlockParadoxTransportalizer()
	{
		super(Material.IRON, MapColor.IRON);
		setUnlocalizedName("paradoxTransportalizer");
		setRegistryName("paradox_transportalizer");
		this.setHardness(3.5F);
		this.setHarvestLevel("pickaxe", 0);
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
			WorldServer spawnWorld = entity.getServer().getWorld(0);
			BlockPos spawnPos = spawnWorld.getTopSolidOrLiquidBlock(spawnWorld.getSpawnPoint());
			
			if(world.isBlockPowered(pos))
			{
				if (entity instanceof EntityPlayerMP)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.transportalizerDisabled", new Object[0]));
				return;
			}
			
			if(world.getBlockState(pos.up()).getMaterial().blocksMovement() && world.getBlockState(pos.up()).getMaterial().blocksMovement())
			{
				if (entity instanceof EntityPlayerMP)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.blocked", new Object[0]));
				return;
			}
			
			int[] bannedDims = MinestuckConfig.forbiddenDimensionsTpz;
			for(int dim : bannedDims)
				if(dim == world.provider.getDimension())
				{
					if (entity instanceof EntityPlayerMP)
						entity.sendMessage(new TextComponentTranslation("message.transportalizer.forbidden", new Object[0]));
					return;
				}
			
			Teleport.teleportEntity(entity, 0, null, spawnPos);
		}
	}
}
