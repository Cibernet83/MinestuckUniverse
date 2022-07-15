package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.blocks.BlockAbilitechnosynth;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockLargeMachine;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Arrays;

public class ItemAbilitechnosyth extends MSUItemBlock
{
	public ItemAbilitechnosyth() {
		super(MinestuckUniverseBlocks.abilitechnosynth[0], "abilitechnosynth");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			Block block = worldIn.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag) {
				pos = pos.up();
			}

			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			ItemStack itemstack = player.getHeldItem(hand);

			if (!itemstack.isEmpty()) {
				if (!canPlaceAt(itemstack, player, worldIn, pos, placedFacing)) {
					return EnumActionResult.FAIL;
				} else {
					IBlockState state = this.block.getDefaultState().withProperty(BlockAlchemiter.DIRECTION, placedFacing);
					this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state);
					if (!player.isCreative()) {
						itemstack.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
		for(int x = -1; x < 1; x++) {
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack)) {
				return false;
			}

			for(int y = 0; y < 4; ++y) {
				for(int z = 0; z < 2; ++z) {
					if (!world.mayPlace(MinestuckUniverseBlocks.abilitechnosynth[0], pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		EnumFacing facing = player.getHorizontalFacing().getOpposite();
		if (!world.isRemote)
		{
			int i = 0;
			for(int y = 0; y < 2; y++) for(int z = 0; z >= -1 ; z--) for(int x = -1; x <= 1; x++)
			{
				world.setBlockState(pos.offset(facing, z).offset(facing.rotateYCCW(), x).up(y), BlockAbilitechnosynth.getState(i, facing));
				i++;
			}

			for(int x = -1; x <= 1; x++)
			{
				world.setBlockState(pos.offset(facing, -1).offset(facing.rotateYCCW(), x).up(2), BlockAbilitechnosynth.getState(i, facing));
				i++;
			}

			world.setBlockState(pos.offset(facing, -1).up(3), BlockAbilitechnosynth.getState(i, facing));

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
			}
		}

		return true;
	}

	public static boolean isValid(IBlockState state, World world, BlockPos pos)
	{
		if(!(state.getBlock() instanceof BlockAbilitechnosynth))
			return false;

		EnumFacing facing = state.getValue(BlockLargeMachine.DIRECTION);
		int part = Arrays.asList(MinestuckUniverseBlocks.abilitechnosynth).indexOf(state.getBlock())*4 + state.getValue(BlockAbilitechnosynth.PART);

		if(part < 12)
			pos = pos.offset(facing.rotateY(), (part%3)-1).down(part/6).offset(facing, (part/3) % 2);
		else if(part < 15)
			pos = pos.offset(facing.rotateY(), (part%3)-1).down(2).offset(facing, 1);
		else pos = pos.down(3).offset(facing, 1);

		System.out.println(pos);

		int i = 0;
		for(int y = 0; y < 2; y++) for(int z = 0; z >= -1 ; z--) for(int x = -1; x <= 1; x++)
		{
			if(!world.getBlockState(pos.offset(facing, z).offset(facing.rotateYCCW(), x).up(y)).equals(BlockAbilitechnosynth.getState(i, facing)))
				return false;
			i++;
		}

		for(int x = -1; x <= 1; x++)
		{
			if(!world.getBlockState(pos.offset(facing, -1).offset(facing.rotateYCCW(), x).up(2)).equals(BlockAbilitechnosynth.getState(i, facing)))
				return false;
			i++;
		}

		return world.getBlockState(pos.offset(facing, -1).up(3)).equals(BlockAbilitechnosynth.getState(i, facing));
	}

	public static boolean isAvailable(SburbConnection sburbConnection)
	{
		if(sburbConnection.getClientIdentifier() == null || sburbConnection.getServerIdentifier() == null || sburbConnection.getServerIdentifier().getPlayer() == null)
			return false;
		EntityPlayer player = MSUUtils.getOfflinePlayer((WorldServer) sburbConnection.getServerIdentifier().getPlayer().world, sburbConnection.getClientIdentifier());
		return player != null && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getAllAbilitechs().size() > 0;
	}
}
