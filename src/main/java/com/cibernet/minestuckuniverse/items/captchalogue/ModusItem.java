package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ModusItem extends MSUItemBase
{
	public static final List<Item> fetchModi = new ArrayList<Item>(){{
		add(MinestuckItems.modusCard);
	}};
	
	public ModusItem(String name)
	{
		super(name);
		setMaxStackSize(1);
		this.setCreativeTab(TabMinestuckUniverse.fetchModi);
		OreDictionary.registerOre("modus", this);
		
		fetchModi.add(this);
	}
}
