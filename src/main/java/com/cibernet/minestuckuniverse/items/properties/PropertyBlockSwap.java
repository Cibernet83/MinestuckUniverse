package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.util.BlockMetaPair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class PropertyBlockSwap extends WeaponProperty
{
	final HashMap<BlockMetaPair, BlockMetaPair> TRANSFORM_MAP;
	final int durabilityDmg;

	public PropertyBlockSwap(HashMap<BlockMetaPair, BlockMetaPair> transforms, int durabilityDmg)
	{
		TRANSFORM_MAP = transforms;
		this.durabilityDmg = durabilityDmg;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		BlockMetaPair pair = new BlockMetaPair(state.getBlock(), state.getBlock().getMetaFromState(state));
		BlockMetaPair transformTo = TRANSFORM_MAP.get(pair);

		for(Map.Entry<BlockMetaPair, BlockMetaPair> entry : TRANSFORM_MAP.entrySet())
		{
			if(entry.getKey().equals(pair))
			{
				transformTo = entry.getValue();
				break;
			}
		}

		if(transformTo != null)
		{
			worldIn.setBlockState(pos, transformTo.block.getStateFromMeta(transformTo.meta == -1 ? state.getBlock().getMetaFromState(state) : transformTo.meta));
			worldIn.playEvent(2005, pos, 0);
			player.getHeldItem(hand).damageItem(durabilityDmg, player);
			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
