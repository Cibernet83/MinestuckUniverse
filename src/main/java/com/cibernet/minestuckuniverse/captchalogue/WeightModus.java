package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WeightModus extends BaseModus
{
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
			gui = new BaseModusGuiHandler(this, 1) {};
		return gui;
	}
	
	public static int getFloatStones(Modus modus)
	{
		int result = 0;
		for(ItemStack stack : modus.getItems())
			if(stack.getItem().equals(MinestuckUniverseItems.floatStone))
				result ++;
		return result;
	}
	
	public static double getItemCap(EntityPlayer player)
	{
		return (Math.min(Math.max(0, (MinestuckPlayerData.getData(player).echeladder.getRung()-4)*4), 20))+20;
	}
}
