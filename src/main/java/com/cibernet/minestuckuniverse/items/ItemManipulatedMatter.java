package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.blocks.IGodTierBlock;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemManipulatedMatter extends MSUItemBase
{

	public ItemManipulatedMatter(String unlocName, String name)
	{
		super(name, unlocName);
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format("item.manipulatedMatter.tooltip"));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (player.capabilities.allowEdit)
		{
			spawnStructure(player.getHeldItem(hand), player, world, getPlacementPos(player, world, pos.offset(facing), player.getHeldItem(hand)));
			return EnumActionResult.SUCCESS;
		}
		else
		{
			player.sendStatusMessage(new TextComponentTranslation("item.manipulatedMatter.cantEdit"), true);
			return EnumActionResult.FAIL;
		}
	}

	public static BlockPos getPlacementPos(EntityPlayer player, World world, BlockPos pos, ItemStack stack)
	{
		int w = 0, d = 0;

		if(stack.hasTagCompound())
		{
			w = stack.getTagCompound().getInteger("width");
			d = stack.getTagCompound().getInteger("depth");
		}

		return getPlacementPos(player, world, pos, w, d);
	}

	public static BlockPos getPlacementPos(EntityPlayer player, World world, BlockPos pos, int width, int depth)
	{
		switch (player.getHorizontalFacing())
		{
			case NORTH: return pos.offset(EnumFacing.NORTH, width+1);
			case WEST: return pos.offset(EnumFacing.WEST, depth-1);
			default: return pos;
		}
	}

	public static void spawnStructure(ItemStack itemStack, EntityPlayer player, World world, BlockPos cornerPos)
	{
		if (world.isRemote || !itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey("blockList"))
			return;

		NBTTagCompound nbt = itemStack.getTagCompound();

		BlockPos pos1 = cornerPos;
		BlockPos pos2 = pos1.add(nbt.getInteger("width"), nbt.getInteger("height"), nbt.getInteger("depth"));

		AxisAlignedBB boundingBox = new AxisAlignedBB(pos1, pos2.add(1, 1, 1));
		if(!world.checkNoEntityCollision(boundingBox))
			return;

		NBTTagList blockList = nbt.getTagList("blockList", 10);
		int i = 0, j = 0;
		for (int x = pos1.getX(); x <= pos2.getX(); x++)
		for (int y = pos1.getY(); y <= pos2.getY(); y++)
		for (int z = pos1.getZ(); z <= pos2.getZ(); z++)
		{
			BlockPos pos = new BlockPos(x, y, z);
			NBTTagCompound bnbt = (NBTTagCompound) blockList.get(i++);
			IBlockState block = NBTUtil.readBlockState(bnbt);
			IBlockState oldBlock = world.getBlockState(pos);

			if(block.getBlock() != Blocks.AIR)
			{
				if (oldBlock.getBlockHardness(world, pos) >= 0 && (block.getBlockHardness(world, pos) < 0 || oldBlock.getBlockHardness(world, pos) <= block.getBlockHardness(world, pos)))
				{
					world.playEvent(2001, pos, Block.getStateId(oldBlock));
					oldBlock.getBlock().dropBlockAsItem(world, pos, oldBlock, 0);
					world.setBlockState(pos, block);

					TileEntity te = world.getTileEntity(pos);
					if (te != null && bnbt.hasKey("mmTileEntity"))
					{
						NBTTagCompound tenbt = bnbt.getCompoundTag("mmTileEntity");
						tenbt.setInteger("x", x);
						tenbt.setInteger("y", y);
						tenbt.setInteger("z", z);
						te.readFromNBT(tenbt);
					}
					j++;
				}
				else
				{
					world.playEvent(2001, pos, Block.getStateId(block));
					block.getBlock().dropBlockAsItem(world, pos, block, 0);
				}
			}
		}

		if(j > 0)
			world.playSound(null, pos1, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);

		itemStack.shrink(1);
	}

	public static void storeStructure(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos1, BlockPos pos2)
	{
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = itemStack.getTagCompound();

		nbt.setInteger("width" , Math.abs(pos1.getX() - pos2.getX()));
		nbt.setInteger("height", Math.abs(pos1.getY() - pos2.getY()));
		nbt.setInteger("depth" , Math.abs(pos1.getZ() - pos2.getZ()));

		NBTTagList blockList = new NBTTagList();
		for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++)
		for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++)
		for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++)
		{
			BlockPos pos = new BlockPos(x, y, z);
			IBlockState block = world.getBlockState(pos);
			NBTTagCompound bnbt = new NBTTagCompound();
			if (block.getBlock() != Blocks.AIR && (block.getBlockHardness(world, pos) >= 0 || block.getBlock() instanceof IGodTierBlock))
			{
				NBTUtil.writeBlockState(bnbt, block);

				TileEntity te = world.getTileEntity(pos);
				if (te != null)
				{
					bnbt.setTag("mmTileEntity", te.writeToNBT(new NBTTagCompound()));
					te.invalidate();
				}
				world.setBlockToAir(pos);

				MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, 0x4bec13, 2, pos), player);
			}
			blockList.appendTag(bnbt);
		}
		nbt.setTag("blockList", blockList);
	}
}
