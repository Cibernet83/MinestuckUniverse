package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;

public class BlockWoolTransportalizer extends BlockTransportalizer
{
	public EnumDyeColor color;
	public BlockWoolTransportalizer(EnumDyeColor color)
	{
		super();
		this.setCreativeTab(TabMinestuckUniverse.instance);
		this.setUnlocalizedName("woolTransportalizer."+color.getUnlocalizedName());
		this.setRegistryName(color.getName()+"_wool_transportalizer");
		this.color = color;

		MinestuckUniverseBlocks.sleevedTransportalizers.put(color, this);
	}
}
