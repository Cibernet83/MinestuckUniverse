package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.properties.PropertyDualWield;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
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

	public static boolean addWeapon(EntityLivingBase entity, ItemStack stack, ItemStack offStack)
	{
		return addWeapon(entity, stack, offStack, true);
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
			StrifeSpecibus.PairedStack pair = new StrifeSpecibus.PairedStack(stack, ItemStack.EMPTY);

			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("StrifeAssigned", true);
			selSpecibus.getContents().add(pair);

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
					specibus.getContents().add(new StrifeSpecibus.PairedStack(stack, ItemStack.EMPTY));

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

	public static boolean addWeapon(EntityLivingBase entity, ItemStack stack, ItemStack offStack, boolean sendStatusMessage)
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
			else if(selSpecibus.putItemStack(stack, offStack))
			{
				if(entity instanceof EntityPlayer)
				{
					if(sendStatusMessage)
						((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assignWeaponPair", stack.getTextComponent(), offStack.getTextComponent(), selSpecibus.getDisplayName()), true);
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSpecibusIndex(selSpecibus)), (EntityPlayer) entity);
				}
				return true;
			}
			else if(selSpecibus.putItemStack(stack, ItemStack.EMPTY))
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
				else if(specibus.putItemStack(stack, offStack))
				{
					if(entity instanceof EntityPlayer)
					{
						if(sendStatusMessage)
							((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assignWeaponPair", stack.getTextComponent(), offStack.getTextComponent(), specibus.getDisplayName()), true);
						MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSpecibusIndex(specibus)), (EntityPlayer) entity);
					}
					return true;
				}
				else if(specibus.putItemStack(stack, ItemStack.EMPTY))
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
	public static boolean addWeapontoSlot(EntityLivingBase entity, ItemStack stack, ItemStack offStack, int slot)
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
			if(specibus != null && specibus.putItemStack(stack, offStack, slot))
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
			ItemStack offStack = ItemStack.EMPTY;
			if(stack.getItem() instanceof IPropertyWeapon && ((IPropertyWeapon)stack.getItem()).hasProperty(PropertyDualWield.class, stack))
				offStack = player.getHeldItem(hand == EnumHand.OFF_HAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			if(addWeapon(player, stack, offStack))
			{
				player.setHeldItem(hand, ItemStack.EMPTY);
				if(!offStack.isEmpty())
					player.setHeldItem(hand == EnumHand.OFF_HAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND, ItemStack.EMPTY);
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
		ItemStack card = ItemStrifeCard.injectStrifeSpecibus(cap.removeSpecibus(index), new ItemStack(MinestuckUniverseItems.strifeCard));

		if(!player.world.isRemote && (!(player instanceof EntityPlayer) || !((EntityPlayer) player).addItemStackToInventory(card)))
			player.entityDropItem(card, player.getEyeHeight());
	}

	public static void retrieveWeapon(EntityLivingBase player, int index, EnumHand hand)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		StrifeSpecibus.PairedStack pair = cap.getPortfolio()[cap.getSelectedSpecibusIndex()].retrieveStack(cap.getSelectedWeaponIndex());

		if(player.getHeldItem(hand).isEmpty() || StrifeEventHandler.isStackAssigned(player.getHeldItem(hand)))
		{
			if(StrifeEventHandler.isStackAssigned(player.getHeldItem(hand)) &&
					cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0 && cap.getSelectedWeaponIndex() >= 0
					&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && !cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty()
					&& ItemStack.areItemStacksEqual(cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()).mainStack, player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, ItemStack.EMPTY);
				cap.setArmed(false);
			}
			else if(!pair.mainStack.isEmpty())
			{
				player.setHeldItem(hand, pair.mainStack);
				cap.setArmed(true);
				if(!pair.offStack.isEmpty())
				{
					if(player.getHeldItem(hand == EnumHand.OFF_HAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND).isEmpty())
						player.setHeldItem(hand == EnumHand.OFF_HAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND, pair.offStack);
					else if(player instanceof EntityPlayer) CaptchaDeckHandler.launchAnyItem((EntityPlayer) player, pair.offStack);
				}
			}
			if(player instanceof  EntityPlayer)
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) player);
		}
	}

	public static void unassignSelected(EntityLivingBase player)
	{
		if(player.world.isRemote || !player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		StrifeSpecibus selSpecibus = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];
		selSpecibus.unassign(cap.getSelectedWeaponIndex());

		if(cap.getSelectedWeaponIndex() >= selSpecibus.getContents().size())
			cap.setSelectedWeaponIndex(0);
		cap.setArmed(false);
		if(player instanceof  EntityPlayer)
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), (EntityPlayer) player);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSelectedSpecibusIndex()), (EntityPlayer) player);
		}
	}
}
