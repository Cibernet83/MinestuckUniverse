package com.cibernet.minestuckuniverse.blocks;

import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class BlockGrist extends MSUBlockBase
{
	public GristType type;
	public int value;
	
	public BlockGrist(GristType type)
	{
		super(Material.GOURD, MapColor.LIGHT_BLUE_STAINED_HARDENED_CLAY);
		this.type = type;
		this.value = (type.getValue() >= 5.0F || !type.equals(GristType.Build)) ? 1 : 10;
		
		
		setHardness(0.4f);
		
		setUnlocalizedName("gristBlock." + type.getName().toLowerCase());
		setRegistryName("grist_block_" + type.getName().toLowerCase());
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(I18n.translateToLocal("tile.gristBlock.tooltip"));
		super.addInformation(stack, player, tooltip, advanced);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if(!worldIn.isRemote && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) <= 0 && !player.isCreative())
			worldIn.spawnEntity(new EntityGrist(worldIn, pos.getX(),pos.getY() + 0.5F,pos.getZ(), new GristAmount(this.type,this.value)));
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}
	
	
}
