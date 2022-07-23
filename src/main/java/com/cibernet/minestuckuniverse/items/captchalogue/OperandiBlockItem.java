package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.OperandiModus;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.items.MSUItemBlock;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OperandiBlockItem extends MSUItemBlock
{
	final String registryName;

	public OperandiBlockItem(String name, Block block)
	{
		super(block, name);
		registryName = name;
		
		setUnlocalizedName(name);
		
		setCreativeTab(TabMinestuck.instance);
		setMaxStackSize(1);

		OperandiModus.itemPool.add(this);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
		{
			TileEntityItemStack te = (TileEntityItemStack)world.getTileEntity(pos);
			ItemStack newStack = MSUItemBase.getStoredItem(stack);
			te.setStack(newStack);
			return true;
		} else return false;
	}
}
