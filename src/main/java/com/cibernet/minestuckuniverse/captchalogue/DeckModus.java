package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.DeckGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

public class DeckModus extends BaseModus
{
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
			gui = new DeckGuiHandler(this);
		return gui;
	}
	
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(!super.putItemStack(stack))
			return false;
		
		NonNullList<ItemStack> listA = new NonNullList<ItemStack>(){{addAll(list);}};
		NonNullList<ItemStack> listEmpties = new NonNullList<ItemStack>(){{addAll(list);}};
		
		listA.removeIf(s -> s.isEmpty());
		listEmpties.removeIf(s -> !s.isEmpty());
		Collections.shuffle(listA);
		listA.addAll(listEmpties);
		
		list = listA;
		return true;
	}
	
}
