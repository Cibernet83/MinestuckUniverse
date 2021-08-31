package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.gui.GuiStrifePortfolio;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.client.gui.playerStats.GuiStrifeSpecibus;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StrifeEventHandler
{

	//TODO config

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiStrifeSpecibus)
			event.setGui(new GuiStrifePortfolio());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderTooltip(ItemTooltipEvent event)
	{
		if(isStackAssigned(event.getItemStack()))
			event.getToolTip().add(1, I18n.format("strife.item.allocated"));
	}

	@SubscribeEvent
	public static void onPlayerAttack(LivingAttackEvent event)
	{
		if(!MSUConfig.preventUnallocatedWeaponsUse ||  !(event.getSource().getImmediateSource() instanceof EntityPlayer))
			return;

		EntityLivingBase source = (EntityLivingBase) event.getSource().getImmediateSource();
		ItemStack stack = source.getHeldItemMainhand();

		if(stack.isEmpty())
		{
			IStrifeData cap = source.getCapability(MSUCapabilities.STRIFE_DATA, null);
			if(cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0)
			{
				StrifeSpecibus selStrife = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];

				if(selStrife != null && selStrife.isAssigned() && selStrife.getKindAbstratus().isFist())
					return;
			}
			event.setCanceled(true);
		}

		if(!isStackAssigned(stack))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onEntityItemTick(TickEvent.WorldTickEvent event)
	{
		for(EntityItem item : event.world.getEntities(EntityItem.class, i -> isStackAssigned(i.getItem())))
			item.getItem().getTagCompound().removeTag("StrifeAssigned");
	}

	@SideOnly(Side.CLIENT)
	private static ItemStack updateSpecibus = ItemStack.EMPTY;

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
			return;

		IStrifeData cap = event.player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		ItemStack selectedWeapon = cap.isArmed() && cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0 && cap.getSelectedWeaponIndex() >= 0
				&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && !cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty() ?
				cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()) : ItemStack.EMPTY;

		boolean hasWeaponCheck = true;
		if(!selectedWeapon.isEmpty())
		{
			for(EnumHand hand : EnumHand.values())
				if(isStackAssigned(event.player.getHeldItem(hand)) && event.player.getHeldItem(hand).isItemEqualIgnoreDurability(selectedWeapon))
				{
					ItemStack stack = event.player.getHeldItem(hand);
					if(!ItemStack.areItemStacksEqual(stack, selectedWeapon))
					{
						cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().set(cap.getSelectedWeaponIndex(), stack);
						selectedWeapon = stack;
					}
					hasWeaponCheck = false;
					break;
				}
		}

		if(cap.isArmed() && (cap.getSelectedSpecibusIndex() < 0 || cap.getPortfolio()[cap.getSelectedSpecibusIndex()] == null))
		{
			for(EnumHand hand : EnumHand.values())
				if(isStackAssigned(event.player.getHeldItem(hand)))
				{
					if(hand == EnumHand.MAIN_HAND)
						event.player.inventory.setInventorySlotContents(event.player.inventory.currentItem, ItemStack.EMPTY);
					else event.player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
					event.player.inventory.markDirty();
					cap.setArmed(false);
					hasWeaponCheck = false;
					break;
				}
		}

		if(event.player.world.isRemote)
		{
			if(event.player.isUser() && !updateSpecibus.isEmpty())
			{
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.ASSIGN_STRIFE, updateSpecibus.copy(), cap.getSelectedWeaponIndex()));
				updateSpecibus = ItemStack.EMPTY;
			}

			ItemStack cursorStack = event.player.inventory.getItemStack();
			if(isStackAssigned(cursorStack))
			{
				event.player.inventory.setItemStack(ItemStack.EMPTY);
				cap.setArmed(false);
				event.player.inventory.markDirty();
				updateSpecibus = cursorStack;
			}

			return;
		}

		NonNullList<ItemStack> mainInv = event.player.inventory.mainInventory;
		for(int i = 0; i < mainInv.size(); i++)
		{
			if(i == event.player.inventory.currentItem)
				continue;

			ItemStack stack = mainInv.get(i);
			if(isStackAssigned(stack))
			{
				if(ItemStack.areItemStacksEqual(stack, selectedWeapon))
				{
					event.player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					cap.setArmed(false);
					hasWeaponCheck = false;
				}
				else
				{
					stack.getTagCompound().removeTag("StrifeAssigned");
					event.player.inventory.setInventorySlotContents(i, stack);
				}
				event.player.inventory.markDirty();
			}
		}

		if(!selectedWeapon.isEmpty() && hasWeaponCheck)
		{
			StrifePortfolioHandler.unassignSelected(event.player);
			cap.setArmed(false);
		}
	}

	public static boolean isStackAssigned(ItemStack stack)
	{
		return !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned");
	}
}
