package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.gui.MSUGuiHandler;
import com.cibernet.minestuckuniverse.items.MSUItemBlock;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.recipes.MachineChasisRecipes;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockDecor;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockCeramicPorkhollow extends MSUBlockBase implements ITileEntityProvider, TileEntityMachineChasis.ICustomAssembly
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	private static final AxisAlignedBB AABB = new AxisAlignedBB(4 / 16d, 0, 2 / 16d, 12 / 16d, 10 / 16d, 14 / 16d);

	protected BlockCeramicPorkhollow()
	{
		super(Material.IRON, Material.IRON.getMaterialMapColor(), "ceramic_porkhollow");
		setUnlocalizedName("ceramicPorkhollow");
		setHarvestLevel("pickaxe", 0);
		setHardness(2.0F);
		setCreativeTab(TabMinestuckUniverse.main);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityItemStack te = new TileEntityItemStack();
		te.setStack(new ItemStack(MinestuckUniverseBlocks.ceramicPorkhollow));
		return te;
	}

	@Override
	protected BlockStateContainer createBlockState()
{
	return new BlockStateContainer(this, FACING);
}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if ((this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0))
		{
			if (te instanceof TileEntityItemStack)
			{
				int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
				List<ItemStack> items = new ArrayList<>();
				items.add(((TileEntityItemStack) te).getStack());
				net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());

				for (ItemStack item : items)
				{
					spawnAsEntity(worldIn, pos, item);
				}
			}
		}
		else
		{
			MinestuckPlayerData.PlayerData playerData = MinestuckPlayerData.getData(player);

			if (playerData.boondollars > 0)
			{
				int drops = (int) Math.min(playerData.boondollars, 10000);
				MinestuckPlayerData.addBoondollars(player, -drops);

				for (int i = 0; i < player.getRNG().nextInt(5) + 4; i++)
				{
					if (drops / 6 <= 0)
						break;

					int value = player.getRNG().nextInt(drops / 6);
					if (value > 0)
					{
						spawnAsEntity(worldIn, pos, ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), value));
						drops -= value;
					}
				}

				if (drops > 0)
					spawnAsEntity(worldIn, pos, ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), drops));
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			playerIn.openGui(MinestuckUniverse.instance, MSUUtils.CERAMIC_PORKHOLLOW_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public ItemBlock getItemBlock()
	{
		return new MSUItemBlock(this, "ceramic_porkhollow", "ceramicPorkhollow")
		{
			{
				setHasSubtypes(true);
			}

			@Override
			public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
			{
				if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
				{
					TileEntityItemStack te = (TileEntityItemStack) world.getTileEntity(pos);
					ItemStack newStack = stack.copy();
					newStack.setCount(1);
					te.setStack(newStack);
					return true;
				}
				else return false;
			}
		};
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

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}



	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BlockMSUDecor.rotateAABB(state.getValue(FACING), AABB);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return super.getSilkTouchDrop(state);
	}



	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityItemStack)
		{
			ItemStack stack = ((TileEntityItemStack) te).getStack();
			drops.add(stack);
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	public void onAssembly(World world, @Nullable EntityPlayer player, BlockPos pos, MachineChasisRecipes.Output output, ItemStack... input)
	{
		int color = 0;
		if(player != null && MinestuckPlayerData.getData(player) != null)
			color = MinestuckPlayerData.getData(player).color + 1;
		else
		{
			SburbConnection connection = SburbHandler.getConnectionForDimension(world.provider.getDimension());
			if(connection != null)
				color = MinestuckPlayerData.getData(connection.getClientIdentifier()).color;
		}

		TileEntityItemStack iste = new TileEntityItemStack();
		iste.setStack(new ItemStack(MinestuckUniverseItems.ceramicPorkhollow, 1, color));
		world.setTileEntity(pos, iste);
	}
}
