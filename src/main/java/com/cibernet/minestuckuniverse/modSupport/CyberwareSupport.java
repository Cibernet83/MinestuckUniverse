package com.cibernet.minestuckuniverse.modSupport;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.events.handlers.KarmaEventHandler;
import flaxbeard.cyberware.api.CyberwareAPI;
import flaxbeard.cyberware.api.ICyberwareUserData;
import flaxbeard.cyberware.api.item.ICyberware;
import flaxbeard.cyberware.common.CyberwareContent;
import flaxbeard.cyberware.common.item.ItemCyberwareBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class CyberwareSupport
{
	public static void returnVitals(EntityPlayer player)
	{
		if(!MinestuckUniverse.isCyberwareLoaded) return;
		ICyberwareUserData data = CyberwareAPI.getCapabilityOrNull(player);
		if(data == null) return;

		for(ICyberware.EnumSlot slot : ICyberware.EnumSlot.values())
		{
			if(slot.isSided() && !slot.hasEssential()) continue;

			if(!slot.isSided() && !data.hasEssential(slot))
			{
				addCyberware(player, ((ItemCyberwareBase) CyberwareContent.bodyPart).getCachedStack(slot.ordinal()));
				data.setHasEssential(slot, true, true);
			}
			else
			{
				boolean[] sides = new boolean[] {data.hasEssential(slot, ICyberware.ISidedLimb.EnumSide.LEFT), data.hasEssential(slot, ICyberware.ISidedLimb.EnumSide.RIGHT)};
				if(!data.hasEssential(slot, ICyberware.ISidedLimb.EnumSide.LEFT))
				{
					addCyberware(player, ((ItemCyberwareBase)CyberwareContent.bodyPart).getCachedStack(slot.ordinal()));
					sides[0] = true;
				}
				if(!data.hasEssential(slot, ICyberware.ISidedLimb.EnumSide.RIGHT))
				{
					addCyberware(player, ((ItemCyberwareBase)CyberwareContent.bodyPart).getCachedStack(slot.ordinal() + 1));
					sides[1] = true;
				}
				data.setHasEssential(slot, sides[0], sides[1]);
			}
		}

		//add power and then change parts if theres not enough power (most likely no battery)

		data.addPower(Math.min(MSUConfig.addedPowerPerDeath, data.getCapacity()), ItemStack.EMPTY);
		if(!player.world.isRemote)
			CyberwareAPI.updateData(player);
		data.resetBuffer();
		data.resetBuffer();

		//Right Cyberarm (left arm needs to be a Cyberarm [doesnt matter if it has power cause im too lazy])
		ItemStack essentialWare = data.getCyberware(CyberwareContent.cyberlimbs.getCachedStack(1));
		if (!essentialWare.isEmpty() && (MSUConfig.acceptablePower > data.getStoredPower() || !data.usePower(essentialWare, CyberwareContent.cyberlimbs.getPowerConsumption(essentialWare))) && !data.getCyberware(CyberwareContent.cyberlimbs.getCachedStack(0)).isEmpty())
		{
			removeCyberware(player, essentialWare);
			addCyberware(player, ((ItemCyberwareBase)CyberwareContent.bodyPart).getCachedStack(9));
			data.setHasEssential(ICyberware.EnumSlot.ARM, data.hasEssential(ICyberware.EnumSlot.ARM, ICyberware.ISidedLimb.EnumSide.LEFT), true);
		}

		//Heart Pump
		essentialWare = data.getCyberware(CyberwareContent.cyberheart.getCachedStack(0));
		if (!essentialWare.isEmpty() && (MSUConfig.acceptablePower > data.getStoredPower() || !data.usePower(essentialWare, CyberwareContent.cyberheart.getPowerConsumption(essentialWare))))
		{
			removeCyberware(player, essentialWare);
			addCyberware(player, ((ItemCyberwareBase)CyberwareContent.bodyPart).getCachedStack(2));
			data.setHasEssential(ICyberware.EnumSlot.HEART, true, true);
		}

		//Cybereye
		essentialWare = data.getCyberware(CyberwareContent.cybereyes.getCachedStack(0));
		if (!essentialWare.isEmpty() && (MSUConfig.acceptablePower > data.getStoredPower() || !data.usePower(essentialWare, CyberwareContent.cybereyes.getPowerConsumption(essentialWare))))
		{
			removeCyberware(player, essentialWare);
			addCyberware(player, ((ItemCyberwareBase)CyberwareContent.bodyPart).getCachedStack(0));
			data.setHasEssential(ICyberware.EnumSlot.EYES, true, true);
		}
	}

	public static boolean addCyberware(EntityPlayer player, ItemStack toInstall)
	{
		if (!CyberwareAPI.isCyberware(toInstall)) return false;
		ICyberwareUserData data = CyberwareAPI.getCapabilityOrNull((Entity)player);
		ICyberware.EnumSlot slot = CyberwareAPI.getCyberware(toInstall).getSlot(toInstall);
		NonNullList<ItemStack> installed = data.getInstalledCyberware(slot);
		boolean added = false;
		for(int installedItem = 0; installedItem < installed.size(); installedItem++)
		{
			if (CyberwareAPI.areCyberwareStacksEqual(toInstall, installed.get(installedItem))
					&& toInstall.getCount() == installed.get(installedItem).getCount())
				break;
			if(!installed.get(installedItem).isEmpty()) continue;

			added = true;
			installed.set(installedItem, toInstall);
			data.setInstalledCyberware(player, slot, installed);
			if(!player.world.isRemote)
				CyberwareAPI.updateData(player);
			break;
		}
		return added;
	}

	//only removes toRemove and anything that requires toRemove and anything that requires what requires toRemove and so on
	public static boolean removeCyberware(EntityPlayer player, ItemStack toRemove)
	{
		if (!CyberwareAPI.isCyberware(toRemove)) return false;
		ICyberwareUserData data = CyberwareAPI.getCapabilityOrNull((Entity)player);
		ICyberware.EnumSlot slot = CyberwareAPI.getCyberware(toRemove).getSlot(toRemove);
		NonNullList<ItemStack> installed = data.getInstalledCyberware(slot);
		boolean removed = false;
		for(int installedItem = 0; installedItem < installed.size(); installedItem++)
		{
			ItemStack installedItemStack = installed.get(installedItem);
			if(installedItemStack.isEmpty() || !CyberwareAPI.areCyberwareStacksEqual(toRemove, installedItemStack)) continue;

			for(ItemStack requiresToRemove : requiresItemInPlayer(player, toRemove.copy()))
				removeCyberware(player, requiresToRemove);
			removed = true;
			installed.set(installedItem, ItemStack.EMPTY);
			data.setInstalledCyberware(player, slot, installed);
			if(!player.world.isRemote)
				CyberwareAPI.updateData(player);

			//either puts items into player inv or drops item
			if(!player.addItemStackToInventory(toRemove) && player.isServerWorld())
				player.world.spawnEntity(new EntityItem(player.world, (player.posX + 0.5F), (player.posY - 2.0F), (player.posZ + 0.5F), toRemove));

			break;
		}
		return removed;
	}

	public static List<ItemStack> requiresItemInPlayer(EntityPlayer player, ItemStack item)
	{
		ICyberwareUserData data = CyberwareAPI.getCapabilityOrNull((Entity)player);
		List<ItemStack> installedItems = new ArrayList<>();
		for(ICyberware.EnumSlot slot : ICyberware.EnumSlot.values())
			installedItems.addAll(data.getInstalledCyberware(slot));
		installedItems.removeIf(installedItem -> (installedItem.isEmpty()));
		List<ItemStack> requiresItem = new ArrayList<>();
		for(ItemStack installed : installedItems)
			if(CyberwareAPI.isCyberware(installed))
				for(NonNullList<ItemStack> installedRequires : CyberwareAPI.getCyberware(installed).required(installed))
					for(ItemStack installedRequiresItem : installedRequires)
						if(CyberwareAPI.areCyberwareStacksEqual(installedRequiresItem, item))
							requiresItem.add(installed);
		return requiresItem;
	}
}
