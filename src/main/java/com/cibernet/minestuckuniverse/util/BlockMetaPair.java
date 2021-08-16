package com.cibernet.minestuckuniverse.util;

import net.minecraft.block.Block;

public class BlockMetaPair implements Comparable<BlockMetaPair>
{

	public final Block block;
	public final int meta;

	public BlockMetaPair(Block block, int meta)
	{
		this.block = block;
		this.meta = meta;
	}

	@Override
	public int compareTo(BlockMetaPair o)
	{
		if(meta == -1 || o.meta == -1)
			return Block.REGISTRY.getIDForObject(block) - Block.REGISTRY.getIDForObject(o.block);
		return Block.REGISTRY.getIDForObject(block)*(meta+2) - Block.REGISTRY.getIDForObject(o.block)*(o.meta+2);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof BlockMetaPair ? compareTo((BlockMetaPair) obj) == 0 : false;
	}

	@Override
	public String toString() {
		return block.getRegistryName() + ":" + meta;
	}
}
