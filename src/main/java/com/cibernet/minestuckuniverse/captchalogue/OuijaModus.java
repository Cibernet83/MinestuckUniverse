package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.OuijaGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Random;

public class OuijaModus extends BaseModus
{
	final Random rand = new Random();
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(asCard || id < 0)
			return super.getItem(id, asCard);
		
		return list.size() >= 0 ? super.getItem(rand.nextInt(list.size()), asCard) : ItemStack.EMPTY;
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(!stack.isEmpty())
			return super.putItemStack(stack);
		
		CaptchaDeckHandler.getItem((EntityPlayerMP)player, 0, false);
		return true;
	}
	
	@Override
	protected boolean getSort()
	{
		return true;
	}
	
	@Override
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new OuijaGuiHandler(this);
		return gui;
	}
}
