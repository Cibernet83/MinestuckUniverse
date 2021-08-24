package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.mraof.minestuck.block.BlockTransportalizer;
import net.minecraft.item.EnumDyeColor;

public class BlockWoolTransportalizer extends BlockTransportalizer implements IRegistryItem
{
	public EnumDyeColor color;
	public BlockWoolTransportalizer(EnumDyeColor color)
	{
		super();
		this.setCreativeTab(TabMinestuckUniverse.main);
		this.setUnlocalizedName("woolTransportalizer."+color.getUnlocalizedName());
		this.color = color;

		MinestuckUniverseBlocks.sleevedTransportalizers.put(color, this);
	}

	@Override
	public void setRegistryName() {
		this.setRegistryName(color.getName()+"_wool_transportalizer");
	}
}
