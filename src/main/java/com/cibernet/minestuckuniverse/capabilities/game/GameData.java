package com.cibernet.minestuckuniverse.capabilities.game;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class GameData implements IGameData
{
	private static final ArrayList<Item> jujuSpawns = new ArrayList<>();

	public static boolean hasJujuSpawned(Item juju)
	{
		return jujuSpawns.contains(juju);
	}

	public static void setJujuSpawned(Item juju, boolean spawned)
	{
		if(spawned)
		{
			if(!hasJujuSpawned(juju))
				jujuSpawns.add(juju);
		} else if(hasJujuSpawned(juju))
			jujuSpawns.remove(juju);

	}

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList jujuNbt = new NBTTagList();
		if(!jujuSpawns.isEmpty())
		{
			for(Item i : jujuSpawns)
			{
				NBTTagCompound itemNbt = new NBTTagCompound();
				itemNbt.setString("id", i.getRegistryName().toString());
				jujuNbt.appendTag(itemNbt);
			}
			nbt.setTag("JujuSpawns", jujuNbt);
		}


		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		jujuSpawns.clear();
		NBTTagList jujuNbt = nbt.getTagList("JujuSpawns", 10);

		for (int i = 0; i < jujuNbt.tagCount(); ++i)
		{
			NBTTagCompound itemNbt = jujuNbt.getCompoundTagAt(i);
			jujuSpawns.add(Item.REGISTRY.getObject(new ResourceLocation(itemNbt.getString("id"))));
		}
	}
}
