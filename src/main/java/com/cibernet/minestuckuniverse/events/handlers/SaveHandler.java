package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.captchalogue.CommunistModus;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldSave(WorldEvent.Save event)
	{
		if(event.getWorld().provider.getDimension() != 0)	//Only save one time each world-save instead of one per dimension each world-save.
			return;

		File dataFile = event.getWorld().getSaveHandler().getMapFileFromName("MinesruckUniverseData");
		if (dataFile != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setTag("GlobalModus", CommunistModus.writeToNBTGlobal(new NBTTagCompound()));

			try {
				CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(dataFile));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld().provider.getDimension() != 0 || event.getWorld().isRemote)
			return;
		ISaveHandler saveHandler = event.getWorld().getSaveHandler();
		File dataFile = saveHandler.getMapFileFromName("MinesruckUniverseData");

		if(dataFile != null && dataFile.exists())
		{
			NBTTagCompound nbt = null;
			try
			{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(dataFile));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			if(nbt != null)
			{
				CommunistModus.readFromNBTGlobal(nbt.getCompoundTag("GlobalModus"));
				return;
			}
		}
	}
}
