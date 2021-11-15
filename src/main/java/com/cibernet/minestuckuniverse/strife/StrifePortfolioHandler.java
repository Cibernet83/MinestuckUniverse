package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class StrifePortfolioHandler
{
	public static boolean isFull(EntityLivingBase entity)
	{
		return entity.getCapability(MSUCapabilities.STRIFE_DATA, null).isPortfolioFull();
	}

	public static boolean isEmpty(EntityLivingBase entity)
	{
		return entity.getCapability(MSUCapabilities.STRIFE_DATA, null).isPortfolioEmpty();
	}

	public static boolean addWeapon(EntityLivingBase entity, ItemStack stack)
	{
		return addWeapon(entity, stack, true);
	}

	public static StrifeSpecibus moveSelectedWeapon(EntityLivingBase entity, ItemStack stack)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return null;

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);
		StrifeSpecibus selSpecibus = cap.getSelectedSpecibusIndex() >= 0 ? cap.getPortfolio()[cap.getSelectedSpecibusIndex()] : null;

		if(selSpecibus == null)
			return null;

		StrifeSpecibus cantFitIn = null;

		if(MSUConfig.strifeDeckMaxSize >= 0 && (selSpecibus.getKindAbstratus() != null && selSpecibus.getKindAbstratus().isStackCompatible(stack)) && selSpecibus.getContents().size() >= MSUConfig.strifeDeckMaxSize)
		{
			if(cantFitIn == null)
				cantFitIn = selSpecibus;
		}
		else if(!stack.isEmpty() && selSpecibus.kindAbstratus != null && selSpecibus.kindAbstratus.isStackCompatible(stack))
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("StrifeAssigned", true);
			selSpecibus.getContents().add(stack);

			int prevSelectedSpecibus = cap.getSelectedSpecibusIndex();
			selSpecibus.unassign(cap.getSelectedWeaponIndex());
			cap.setSelectedWeaponIndex(selSpecibus.getContents().indexOf(stack));
			if(entity instanceof EntityPlayer)
			{
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, prevSelectedSpecibus, cap.getSpecibusIndex(selSpecibus)), (EntityPlayer) entity);
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) entity);
			}
			return selSpecibus;
		}
		for(StrifeSpecibus specibus : cap.getPortfolio())
		{
			if(specibus != null && specibus != selSpecibus)
			{
				if(MSUConfig.strifeDeckMaxSize >= 0 && specibus.getKindAbstratus() != null && specibus.getKindAbstratus().isStackCompatible(stack) && specibus.getContents().size() >= MSUConfig.strifeDeckMaxSize)
				{
					if(cantFitIn == null)
						cantFitIn = specibus;
				}
				else if(!stack.isEmpty() && specibus.kindAbstratus != null && specibus.kindAbstratus.isStackCompatible(stack))
				{
					if(!stack.hasTagCompound())
						stack.setTagCompound(new NBTTagCompound());
					stack.getTagCompound().setBoolean("StrifeAssigned", true);
					specibus.getContents().add(stack);

					int prevSelectedSpecibus = cap.getSelectedSpecibusIndex();
					selSpecibus.unassign(cap.getSelectedWeaponIndex());
					cap.setSelectedSpecibusIndex(cap.getSpecibusIndex(specibus));
					cap.setSelectedWeaponIndex(specibus.getContents().indexOf(stack));
					if(entity instanceof EntityPlayer)
					{
						MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, prevSelectedSpecibus, cap.getSpecibusIndex(specibus)), (EntityPlayer) entity);
						MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) entity);
					}
					return specibus;
				}
			}
		}
		return null;
	}

	public static boolean addWeapon(EntityLivingBase entity, ItemStack stack, boolean sendStatusMessage)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		StrifeSpecibus selSpecibus = cap.getSelectedSpecibusIndex() >= 0 ? cap.getPortfolio()[cap.getSelectedSpecibusIndex()] : null;

		StrifeSpecibus cantFitIn = null;

		if(selSpecibus != null)
		{
			if(MSUConfig.strifeDeckMaxSize >= 0 && (selSpecibus.getKindAbstratus() != null && selSpecibus.getKindAbstratus().isStackCompatible(stack)) && selSpecibus.getContents().size() >= MSUConfig.strifeDeckMaxSize)
			{
				if(cantFitIn == null)
					cantFitIn = selSpecibus;
			}
			else if(selSpecibus.putItemStack(stack))
			{
				if(entity instanceof EntityPlayer)
				{
					if(sendStatusMessage)
						((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assignWeapon", stack.getTextComponent(), selSpecibus.getDisplayName()), true);
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSpecibusIndex(selSpecibus)), (EntityPlayer) entity);
				}
				return true;
			}
		}

		for(StrifeSpecibus specibus : cap.getPortfolio())
		{
			if(specibus != null && specibus != selSpecibus)
			{
				if(MSUConfig.strifeDeckMaxSize >= 0 && specibus.getKindAbstratus() != null && specibus.getKindAbstratus().isStackCompatible(stack) && specibus.getContents().size() >= MSUConfig.strifeDeckMaxSize)
				{
					if(cantFitIn == null)
						cantFitIn = specibus;
				}
				else if(specibus.putItemStack(stack))
				{
					if(entity instanceof EntityPlayer)
					{
						if(sendStatusMessage)
							((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assignWeapon", stack.getTextComponent(), specibus.getDisplayName()), true);
						MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSpecibusIndex(specibus)), (EntityPlayer) entity);
					}
					return true;
				}
			}
		}

		if(entity instanceof EntityPlayer && sendStatusMessage)
		{
			if(cantFitIn != null)
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.strifeDeckFull", cantFitIn.getDisplayName()), true);
			else ((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.weaponMissmach", stack.getTextComponent()), true);
		}
		return false;
	}

	@Deprecated
	public static boolean addWeapontoSlot(EntityLivingBase entity, ItemStack stack, int slot)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		if(stack.hasTagCompound())
		{
			stack.getTagCompound().removeTag("StrifeAssigned");
			if(stack.getTagCompound().hasNoTags())
				stack.setTagCompound(null);
		}

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		for(StrifeSpecibus specibus : cap.getPortfolio())
		{
			if(specibus != null && specibus.putItemStack(stack, slot))
			{
				if(entity instanceof EntityPlayer)
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSpecibusIndex(specibus)), (EntityPlayer) entity);
				return true;
			}
		}

		return false;
	}

	public static boolean addSpecibus(EntityLivingBase entity, StrifeSpecibus specibus)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		if(specibus == null)
			specibus = StrifeSpecibus.empty();

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(cap.isPortfolioFull())
		{
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.portfolioFull"), true);
			return false;
		}
		if(specibus.isAssigned() && cap.portfolioHasAbstratus(specibus.kindAbstratus))
		{
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.portfolioDuplicate", specibus.kindAbstratus.getLocalizedName()), true);
			return false;
		}

		cap.addSpecibus(specibus);

		if(entity instanceof EntityPlayer)
		{
			if(specibus.isAssigned())
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assign", specibus.kindAbstratus.getLocalizedName()), true);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO), (EntityPlayer) entity);
		}

		return true;
	}

	public static void assignStrife(EntityPlayer player, EnumHand hand)
	{
		if(player.world.isRemote)
			return;

		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() instanceof ItemStrifeCard)
		{
			if(addSpecibus(player, ItemStrifeCard.getStrifeSpecibus(stack)))
			{
				stack.shrink(1);
				player.inventory.markDirty();
			}
		}
		else
		{
			if(addWeapon(player, stack))
			{
				player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
				player.inventory.markDirty();
			}
		}
	}

	public static StrifeSpecibus[] getPortfolio(EntityLivingBase entity)
	{
		if(entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return entity.getCapability(MSUCapabilities.STRIFE_DATA, null).getPortfolio();
		return new StrifeSpecibus[StrifeData.PORTFOLIO_SIZE];
	}

	public static void retrieveCard(EntityLivingBase player, int index)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(cap.isArmed() && cap.getSelectedSpecibusIndex() == index)
			for(EnumHand hand : EnumHand.values())
				if(StrifeEventHandler.isStackAssigned(player.getHeldItem(hand)))
					player.setHeldItem(hand, ItemStack.EMPTY);

		ItemStack card = ItemStrifeCard.injectStrifeSpecibus(cap.removeSpecibus(index), new ItemStack(MinestuckUniverseItems.strifeCard));

		if(!player.world.isRemote && (!(player instanceof EntityPlayer) || !((EntityPlayer) player).addItemStackToInventory(card)))
			player.entityDropItem(card, player.getEyeHeight());
	}

	public static void retrieveWeapon(EntityLivingBase player, int index, EnumHand hand)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);


		ItemStack stack = ItemStack.EMPTY;

		try
		{ stack = cap.getPortfolio()[cap.getSelectedSpecibusIndex()].retrieveStack(cap.getSelectedWeaponIndex()); } catch (Throwable t) {}

		if(player.getHeldItem(hand).isEmpty() || StrifeEventHandler.isStackAssigned(player.getHeldItem(hand)))
		{
			if(StrifeEventHandler.isStackAssigned(player.getHeldItem(hand)) &&
					cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0 && cap.getSelectedWeaponIndex() >= 0
					&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && !cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty()
					&& ItemStack.areItemStacksEqual(cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()), player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, ItemStack.EMPTY);
				cap.setArmed(false);
			}
			else if(!stack.isEmpty())
			{
				player.setHeldItem(hand, stack);
				cap.setArmed(true);
			}
			if(player instanceof  EntityPlayer)
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) player);
		}
	}

	public static void swapOffhandWeapon(EntityLivingBase player, int specibusIndex, int weaponIndex)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		ItemStack stack = ItemStack.EMPTY;

		try { stack = cap.getPortfolio()[specibusIndex].retrieveStack(weaponIndex); } catch (Throwable t) {}

		if(!stack.isEmpty())
		{
			if(cap.isArmed() && cap.getSelectedSpecibusIndex() == specibusIndex && cap.getSelectedWeaponIndex() == weaponIndex)
			{
				cap.setArmed(false);
				player.setHeldItem(StrifeEventHandler.isStackAssigned(player.getHeldItemOffhand()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ItemStack.EMPTY);
				if(player instanceof EntityPlayer)
					((EntityPlayer)player).inventory.markDirty();
			}
			StrifeSpecibus selSpecibus = cap.getPortfolio()[specibusIndex];
			selSpecibus.unassign(weaponIndex);
			if(weaponIndex >= selSpecibus.getContents().size())
				cap.setSelectedWeaponIndex(0);
		}

		ItemStack offhand = player.getHeldItemOffhand();
		if(offhand.isEmpty() || addWeapon(player, offhand))
		{
			player.setHeldItem(EnumHand.OFF_HAND, stack);
			if(player instanceof EntityPlayer)
				((EntityPlayer)player).inventory.markDirty();
		}
		else player.entityDropItem(stack, player.getEyeHeight());

		if(player instanceof  EntityPlayer)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) player);
	}

	public static void unassignSelected(EntityLivingBase player)
	{
		if(player.world.isRemote || !player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		int sel = cap.getSelectedSpecibusIndex();

		StrifeSpecibus selSpecibus = cap.getPortfolio()[sel];
		selSpecibus.unassign(cap.getSelectedWeaponIndex());

		if(cap.getSelectedWeaponIndex() >= selSpecibus.getContents().size())
			cap.setSelectedWeaponIndex(0);
		cap.setArmed(false);
		if(player instanceof  EntityPlayer)
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) player);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, sel), (EntityPlayer) player);
		}
	}
}
