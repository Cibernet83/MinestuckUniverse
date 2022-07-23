package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFraymachine extends MSUBlockBase
{
	public BlockFraymachine()
	{
		super(Material.IRON, "mini_abilitechnosynth", "abilitechnosynth");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(!playerIn.isSneaking())
		{
			if(worldIn.isRemote)
				playerIn.openGui(MinestuckUniverse.instance, MSUUtils.FRAYMACHINE_UI, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;

	}
}
