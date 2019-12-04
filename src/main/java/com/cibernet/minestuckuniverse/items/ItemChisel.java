package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChisel extends MSUItemBase
{
	public ItemChisel(String materialName, int durabillity)
	{
		super(materialName+"_chisel", materialName+"Chisel");
		this.setMaxDamage(durabillity);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.getBlockState(pos).getBlock().equals(MinestuckUniverseBlocks.zillyStone) && !worldIn.isRemote)
		{
			InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(MinestuckUniverseItems.zillystoneShard));
			player.getHeldItem(hand).damageItem(1, player);
			return EnumActionResult.SUCCESS;
		}
		else return EnumActionResult.PASS;
	}
}
