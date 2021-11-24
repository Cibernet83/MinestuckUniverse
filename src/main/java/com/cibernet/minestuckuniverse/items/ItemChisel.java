package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemChisel extends MSUItemBase
{
	public ItemChisel(String materialName, int durabillity)
	{
		super(materialName+"_chisel", materialName+"Chisel");
		this.setMaxDamage(durabillity);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(worldIn.getBlockState(pos).getBlock().equals(MinestuckUniverseBlocks.zillyStone) )
		{
			if (!worldIn.isRemote)
			{
				float luck = (player.getLuck() + MSUConfig.baseZillystoneLuck) * (float)MSUConfig.zillystoneYields;
				float rand = itemRand.nextFloat()*luck;
				float val = Math.round(1+(rand));

				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(MinestuckUniverseItems.zillystoneShard, (int) val));
				player.getHeldItem(hand).damageItem(1, player);
			}
			return EnumActionResult.SUCCESS;
		}
		else return EnumActionResult.PASS;
	}
}
