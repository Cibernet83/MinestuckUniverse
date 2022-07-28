package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.OnionGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OnionModus extends BaseModus
{
	//TODO
	
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
			gui = new OnionGuiHandler(this);
		return gui;
	}
}
