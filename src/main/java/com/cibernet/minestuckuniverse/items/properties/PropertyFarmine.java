package com.cibernet.minestuckuniverse.items.properties;

import com.mraof.minestuck.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PropertyFarmine extends WeaponProperty
{
	int terminus;
	int radius;

	public PropertyFarmine(int radius, int terminus)
	{
		this.radius = radius;
		this.terminus = terminus;
	}

	@Override
	public void onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn)
	{
		if (!worldIn.isRemote)
		{
			Comparator<Pair> comparator = new PairedIntComparator();
			PriorityQueue<Pair> candidates = new PriorityQueue(comparator);
			Block block = blockState.getBlock();
			int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			Item drop = block.getItemDropped(blockState, new Random(0L), fortuneLevel);
			int damageDrop = block.damageDropped(blockState);

			if (stack.getItem().canHarvestBlock(blockState, stack) && playerIn instanceof EntityPlayer && !playerIn.isSneaking() && this.terminus != 1 && this.radius != 0 &&
					(blockState.getBlockHardness(worldIn, pos) == 0 || (double)Math.abs(blockState.getBlockHardness(worldIn, pos)) >= 1.0E-9D))
			{
				if (stack.getItem().getDestroySpeed(stack, blockState) >= 0)
					candidates.add(new Pair(pos, this.radius));
					else candidates.add(new Pair(pos, 1));

				HashSet<BlockPos> blocksToBreak = new HashSet();
				boolean passedBreakLimit = false;

				int rad;
				while(!candidates.isEmpty()) {
					BlockPos curr = (BlockPos) candidates.peek().object1;
					rad = (Integer) candidates.poll().object2;
					if (!blocksToBreak.contains(curr)) {
						blocksToBreak.add(curr);
						if (rad != 0) {
							for(int i = -1; i < 2; ++i) {
								for(int j = -1; j < 2; ++j) {
									for(int k = -1; k < 2; ++k) {
										if (i != 0 || j != 0 || k != 0) {
											BlockPos newBlockPos = new BlockPos(curr.getX() + i, curr.getY() + j, curr.getZ() + k);
											IBlockState newState = worldIn.getBlockState(newBlockPos);
											Block newBlock = newState.getBlock();
											if (newBlock.equals(block) && newBlock.getItemDropped(newState, new Random(0L), fortuneLevel) == drop && newBlock.damageDropped(newState) == damageDrop) {
												candidates.add(new Pair(newBlockPos, rad - 1));
											}
										}
									}
								}
							}
						}
					}

					if (blocksToBreak.size() + 1 > stack.getMaxDamage() - stack.getItemDamage() || blocksToBreak.size() + 1 > this.terminus) {
						passedBreakLimit = true;
						break;
					}
				}

				if (passedBreakLimit) {
					blocksToBreak.clear();

					for(int i = -1; i < 2; ++i) {
						for(rad = -1; rad < 2; ++rad) {
							for(i = -1; i < 2; ++i) {
								BlockPos newBlockPos = new BlockPos(pos.getX() + i, pos.getY() + rad, pos.getZ() + i);
								IBlockState newState = worldIn.getBlockState(newBlockPos);
								Block newBlock = newState.getBlock();
								if (newBlock.equals(block) && newBlock.getItemDropped(newState, new Random(0L), fortuneLevel) == drop && newBlock.damageDropped(newState) == damageDrop && blocksToBreak.size() + 1 < stack.getMaxDamage() - stack.getItemDamage()) {
									blocksToBreak.add(newBlockPos);
								}
							}
						}
					}
				}

				Iterator var24 = blocksToBreak.iterator();

				while(var24.hasNext()) {
					BlockPos blockToBreak = (BlockPos)var24.next();
					IBlockState state = worldIn.getBlockState(blockToBreak);
					harvestBlock(worldIn, state.getBlock(), blockToBreak, state, (EntityPlayer)playerIn, stack);
				}

				if (stack.getItem().isDamageable()) {
					stack.damageItem(blocksToBreak.size() + 1, playerIn);
				}

				return;
			}
		}
	}

	private static boolean harvestBlock(World world, Block block, BlockPos pos, IBlockState state, EntityLivingBase playerIn, ItemStack stack) {
		EntityPlayer player = (EntityPlayer)playerIn;
		TileEntity te = world.getTileEntity(pos);
		if (block.removedByPlayer(state, world, pos, player, true)) {
			block.onBlockDestroyedByPlayer(world, pos, state);
			block.harvestBlock(world, player, pos, state, te, stack);
			return true;
		} else {
			return false;
		}
	}

	private class PairedIntComparator implements Comparator<Pair> {
		private PairedIntComparator() {
		}

		public int compare(Pair x, Pair y) {
			return x != null && y != null && x.object2 != null && y.object2 != null ? (Integer)y.object2 - (Integer)x.object2 : 0;
		}
	}
}
