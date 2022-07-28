package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.CycloneGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CycloneModus extends BaseModus
{
	public float cyclePosition = -20;
	public static final float cycleSpeed = 0.01f;
	
	public void cycle()
	{
		cyclePosition += cycleSpeed;
		if(cyclePosition >= size || cyclePosition < 0)
			cyclePosition = 0;
	}
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("CyclePos"))
			cyclePosition = nbt.getFloat("CyclePos");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(cyclePosition != -20)
			nbt.setFloat("CylcePos", cyclePosition);
		return super.writeToNBT(nbt);
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
		{
			gui = new CycloneGuiHandler(this);
			cyclePosition = 0;
		}
		return gui;
	}
}
