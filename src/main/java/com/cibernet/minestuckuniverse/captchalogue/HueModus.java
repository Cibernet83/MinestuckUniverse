package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.HueGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.TreeMap;

public class HueModus extends BaseModus
{
	public TreeMap<HueGuiHandler.CardColor, ArrayList<ItemStack>> itemMap = new TreeMap<HueGuiHandler.CardColor, ArrayList<ItemStack>>()
	{{
		
		for(HueGuiHandler.CardColor color : HueGuiHandler.CardColor.values())
			put(color, new ArrayList<>());
	}};
	
	public boolean canUse(int index)
	{
		if(index >= this.list.size())
			return true;
		
		for(ArrayList<ItemStack> list : itemMap.values())
			if(!list.isEmpty() && list.get(0).equals(this.list.get(index)))
				return true;
		
		return false;
	}
	
	public int getItemIndex(ItemStack stack)
	{
		return list.indexOf(stack);
	}
	
	@SideOnly(Side.CLIENT)
	public NonNullList<ItemStack> sortItems(NonNullList<ItemStack> list)
	{
		for(ArrayList<ItemStack> itemList : itemMap.values())
			itemList.clear();
		
		for(ItemStack stack : list)
		{
			HueGuiHandler.CardColor color = HueGuiHandler.getCardColor(stack);
			itemMap.get(color).add(stack);
		}
		
		list.clear();
		for(HueGuiHandler.CardColor color : HueGuiHandler.CardColor.values())
			list.addAll(itemMap.get(color));
		
		
		return list;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if (this.side.isServer())
		{
			NonNullList<ItemStack> items = NonNullList.create();
			this.fillList(items);
			return items;
		} else
		{
			if (this.changed)
				this.fillList(this.items);
			return sortItems(this.items);
		}
	}
	
	
	@Override
	protected boolean getSort()
	{
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new HueGuiHandler(this);
		return gui;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return modus instanceof HueModus;
	}
}
