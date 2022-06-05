package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MSUFoodBase extends ItemFood
{
	protected final FoodItemConsumer consumer;
	
	public MSUFoodBase(String name, int amount, float saturation, boolean isWolfFood, FoodItemConsumer consumer)
	{
		super(amount, saturation, isWolfFood);
		this.consumer = consumer;

		setCreativeTab(TabMinestuckUniverse.main);
		
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	public MSUFoodBase(String name, int amount, float saturation, boolean isWolfFood)
	{
		this(name, amount, saturation, isWolfFood, null);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		ItemStack oldStack = stack.copy();
		ItemStack result = super.onItemUseFinish(stack, worldIn, entityLiving);
		if(consumer != null && entityLiving instanceof EntityPlayer)
		{
			ItemStack consumerResult = consumer.accept(oldStack, worldIn, (EntityPlayer) entityLiving);
			if(consumerResult != null)
				return consumerResult;
		}
		return result;
	}
	
	public static FoodItemConsumer getPopBallConsumer()
	{
		return (stack, worldIn, player) ->
		{
			if(!worldIn.isRemote)
			{
				int eightBallMessage = worldIn.rand.nextInt(20);
				ITextComponent msg = new TextComponentTranslation("status.eightBallMessage." + eightBallMessage).setStyle(new Style().setColor(TextFormatting.BLUE).setItalic(true));
				player.sendStatusMessage(msg, false);
			}
			return null;
		};
	}
	
	public interface FoodItemConsumer
	{
		ItemStack accept(ItemStack stack, World worldIn, EntityPlayer player);
	}
}
