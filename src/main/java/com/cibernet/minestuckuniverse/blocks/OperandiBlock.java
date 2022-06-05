package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperandiBlock extends MSUBlockBase
{
	public static final Material LOG = new OperandiMaterial(true);
	
	public OperandiBlock(String name, float hardness, float resistance, Material blockMaterialIn, String toolClass)
	{
		super(blockMaterialIn, MapColor.NETHERRACK, name, name);

		setCreativeTab(TabMinestuckUniverse.main);
		
		setHardness(hardness);
		setResistance(resistance);
		if(!toolClass.isEmpty())
			setHarvestLevel(toolClass, 0);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return super.canHarvestBlock(world, pos, player);
	}
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		if (te instanceof TileEntityItemStack)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList();
			ItemStack storedStack = ((TileEntityItemStack)te).getStack();
			items.add(storedStack);
			ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0F, false, (EntityPlayer)this.harvesters.get());
			Iterator var9 = items.iterator();
			
			if(!storedStack.isEmpty())
				worldIn.playSound(null, player.getPosition(), MSUSoundHandler.operandiTaskComplete, SoundCategory.PLAYERS, 1, 1);
			while(var9.hasNext()) {
				ItemStack item = (ItemStack)var9.next();
				spawnAsEntity(worldIn, pos, item);
			}
		}
		
	}
	
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		/*
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityItemStack) {
			ItemStack stack = ((TileEntityItemStack)te).getStack();
			drops.add(stack);
		}
		*/
	}
	
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	public TileEntity createTileEntity(World world, IBlockState state) {
		TileEntityItemStack te = new TileEntityItemStack();
		return te;
	}
	
	public static class OperandiMaterial extends Material
	{
		
		public OperandiMaterial(boolean requiresTool)
		{
			super(MapColor.NETHERRACK);
			if(requiresTool)
				setRequiresTool();
		}
	}
}
