package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		for(StrifeSpecibus specibus : cap.getPortfolio())
		{
			if(specibus != null && specibus.putItemStack(stack))
			{
				if(entity instanceof EntityPlayer)
				{
					((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assignWeapon", stack.getTextComponent(), specibus.getDisplayName()), true);
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity), (EntityPlayer) entity);
				}
				return true;
			}
		}

		if(entity instanceof EntityPlayer)
			((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.weaponMissmach", stack.getTextComponent()), true);
		return false;
	}

	@Deprecated
	public static boolean addWeapontoSlot(EntityLivingBase entity, ItemStack stack, int slot)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		if(stack.hasTagCompound())
			stack.getTagCompound().removeTag("StrifeAssigned");

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		for(StrifeSpecibus specibus : cap.getPortfolio())
		{
			if(specibus != null && specibus.putItemStack(stack, slot))
			{
				if(entity instanceof EntityPlayer)
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity), (EntityPlayer) entity);
				return true;
			}
		}

		return false;
	}

	public static boolean addSpecibus(EntityLivingBase entity, StrifeSpecibus specibus)
	{
		if(entity.world.isRemote || !entity.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return false;

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(cap.isPortfolioFull())
		{
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.portfolioFull"), true);
			return false;
		}
		if(cap.portfolioHasAbstratus(specibus.kindAbstratus))
		{
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.portfolioDuplicate", specibus.kindAbstratus.getLocalizedName()), true);
			return false;
		}

		cap.addSpecibus(specibus);

		if(entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("status.strife.assign", specibus.kindAbstratus.getLocalizedName()), true);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, entity), (EntityPlayer) entity);
		}

		return true;
	}

	public static void assignStrife(EntityPlayer player, EnumHand hand)
	{
		if(player.world.isRemote)
			return;

		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() instanceof ItemStrifeCard && ItemStrifeCard.hasSpecibus(stack))
		{
			if(addSpecibus(player, ItemStrifeCard.getStrifeSpecibus(stack)))
				stack.shrink(1);
		}
		else
		{
			if(addWeapon(player, stack))
				player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
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

		if(!(player instanceof EntityPlayer) || !((EntityPlayer) player).addItemStackToInventory(card))
			player.entityDropItem(card, player.getEyeHeight());
	}

	public static void retrieveWeapon(EntityLivingBase player, int index)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		ItemStack stack = cap.getPortfolio()[cap.getSelectedSpecibusIndex()].retrieveStack(cap.getSelectedWeaponIndex());

		if(player.getHeldItemMainhand().isEmpty())
		{
			player.setHeldItem(EnumHand.MAIN_HAND, stack);
			cap.setArmed(true);
			if(player instanceof  EntityPlayer)
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player), (EntityPlayer) player);
		}
	}

	public static void unassignSelected(EntityLivingBase player)
	{
		if(!player.hasCapability(MSUCapabilities.STRIFE_DATA, null))
			return;

		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		StrifeSpecibus selSpecibus = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];
		selSpecibus.unassign(cap.getSelectedWeaponIndex());

		if(cap.getSelectedWeaponIndex() >= selSpecibus.getContents().size())
			cap.setSelectedWeaponIndex(0);

		if(player instanceof  EntityPlayer)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player), (EntityPlayer) player);
	}
}
