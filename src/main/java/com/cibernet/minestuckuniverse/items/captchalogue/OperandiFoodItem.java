package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.OperandiModus;
import com.cibernet.minestuckuniverse.items.MSUFoodBase;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

public class OperandiFoodItem extends MSUFoodBase
{
	private final EnumAction action;
	
	public OperandiFoodItem(String name, int amount, float saturation)
	{
		this(name, amount, saturation, EnumAction.EAT);
	}
	public OperandiFoodItem(String name, int amount, float saturation, EnumAction action)
	{
		super(name, amount, saturation, false, getConsumer());
		OperandiModus.itemPool.add(this);
		this.action = action;
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}
	
	
	public EnumAction getItemUseAction(ItemStack stack) {
		return action;
	}
	
	public static MSUFoodBase.FoodItemConsumer getConsumer()
	{
		return ((stack, worldIn, player) ->
		{
			ItemStack storedStack = MSUItemBase.getStoredItem(stack);
			if(storedStack.isEmpty())
				return null;
			
			worldIn.playSound(null, player.getPosition(), MSUSoundHandler.operandiTaskComplete, SoundCategory.PLAYERS, 1, 1);
			
			if(stack.getCount() <= 1)
				return storedStack;
			else if(!player.addItemStackToInventory(storedStack))
				player.dropItem(storedStack, true);
			return null;
		});
	}
}
