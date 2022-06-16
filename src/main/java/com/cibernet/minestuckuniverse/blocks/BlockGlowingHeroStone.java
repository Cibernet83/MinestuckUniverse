package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlowingHeroStone extends MSUBlockBase implements IGodTierBlock
{

	public BlockGlowingHeroStone()
	{
		super(Material.ROCK, MapColor.QUARTZ, "glowing_hero_stone", "heroStoneGlowing");

		setHarvestLevel("pickaxe", 3);
		setHardness(20.0F);
		setBlockUnbreakable();
		setCreativeTab(TabMinestuckUniverse.godTier);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F);
	}


	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public EnumAspect getAspect() {
		return null;
	}

	@Override
	public boolean canGodTier() {
		return false;
	}
}
