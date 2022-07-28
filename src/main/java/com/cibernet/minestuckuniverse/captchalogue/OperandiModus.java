package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class OperandiModus extends BaseModus
{
	
	public static final ArrayList<Item> itemPool = new ArrayList<>();
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		ItemStack operandiItem = new ItemStack(itemPool.get(player.world.rand.nextInt(itemPool.size())));
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack item : list)
				CaptchaDeckHandler.launchAnyItem(player, MSUItemBase.storeItem(operandiItem, item));
			list.clear();
			return ItemStack.EMPTY;
		}
		ItemStack item = super.getItem(id, asCard);
		if(asCard || item.isEmpty() || itemPool.isEmpty())
			return item;
		return MSUItemBase.storeItem(operandiItem, item);
	}
	
	@Override
	protected boolean getSort()
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new BaseModusGuiHandler(this, 13) {};
		return gui;
	}
}
