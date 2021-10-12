package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.gui.GuiStrifePortfolio;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.client.gui.playerStats.GuiStrifeSpecibus;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StrifeEventHandler
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiStrifeSpecibus && Minecraft.getMinecraft().player.getCapability(MSUCapabilities.STRIFE_DATA, null).canStrife())
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
		if(!MSUConfig.combatOverhaul ||  !MSUConfig.preventUnallocatedWeaponsUse ||  !(event.getSource().getImmediateSource() instanceof EntityPlayer))
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

	public static final List<Item> USABLE_ASSIGNED_ONLY = new ArrayList<Item>()
	{{
		add(MinestuckUniverseItems.needlewands);
		add(MinestuckUniverseItems.oglogothThorn);
		add(MinestuckUniverseItems.litGlitterBeamTransistor);
		add(MinestuckUniverseItems.archmageDaggers);
		add(MinestuckUniverseItems.gasterBlaster);
	}};
	public static final List<Item> FORCED_USABLE_UNASSIGNED = new ArrayList<Item>()
	{{
		add(MSUKindAbstrata.getItem("botania", "managun"));
		add(Items.EGG);
		add(Items.SNOWBALL);
		add(Items.ENDER_EYE);
		add(Items.ENDER_PEARL);
		add(Items.EXPERIENCE_BOTTLE);
		add(Items.POTIONITEM);
		add(MinestuckUniverseItems.yarnBall);
	}};

	@SubscribeEvent
	public static void onItemInteract(PlayerInteractEvent.RightClickItem event)
	{
		if(!MSUConfig.combatOverhaul ||  !MSUConfig.preventUnallocatedWeaponsUse)
			return;

		ItemStack stack = event.getItemStack();
		if(FORCED_USABLE_UNASSIGNED.contains(stack.getItem()) || (stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned")))
			return;

		if(USABLE_ASSIGNED_ONLY.contains(stack.getItem()))
		{
			event.setCancellationResult(EnumActionResult.PASS);
			event.setCanceled(true);
			return;
		}

		for(KindAbstratus abstratus : getAbstrataList(stack, false))
			if(abstratus.preventsRightClick())
			{
				event.setCancellationResult(EnumActionResult.PASS);
				event.setCanceled(true);
				return;
			}

	}

	@SubscribeEvent
	public static void onEntityItemTick(TickEvent.WorldTickEvent event)
	{
		for(EntityItem item : event.world.getEntities(EntityItem.class, i -> isStackAssigned(i.getItem())))
			item.getItem().getTagCompound().removeTag("StrifeAssigned");
	}

	@Deprecated
	private static ItemStack updateSpecibus = ItemStack.EMPTY;

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
			return;

		IStrifeData cap = event.player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		ItemStack selectedWeapon = cap.isArmed() && cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0 && cap.getSelectedWeaponIndex() >= 0
				&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && !cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty()
				&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().size() > cap.getSelectedWeaponIndex() ?
				cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()) : ItemStack.EMPTY;

		boolean hasWeaponCheck = true;
		if(!selectedWeapon.isEmpty() && !event.player.world.isRemote)
		{
			for(EnumHand hand : EnumHand.values())
				if(isStackAssigned(event.player.getHeldItem(hand)) && event.player.getHeldItem(hand).isItemEqualIgnoreDurability(selectedWeapon))
				{
					ItemStack stack = event.player.getHeldItem(hand);
					StrifeSpecibus specibus = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];

					if(!specibus.getKindAbstratus().isStackCompatible(stack))
					{
						if(StrifePortfolioHandler.moveSelectedWeapon(event.player, stack) == null)
						{
							if(cap.getSelectedWeaponIndex() >= 0 && !ItemStack.areItemStacksEqual(stack, selectedWeapon))
								specibus.getContents().set(cap.getSelectedWeaponIndex(), stack);

							if(specibus.getContents().size() <= 1)
							{
								List<KindAbstratus> abstratusList = StrifeEventHandler.getAbstrataList(stack, false);
								if(abstratusList.isEmpty())
								{
									StrifePortfolioHandler.unassignSelected(event.player);
									stack.getTagCompound().removeTag("StrifeAssigned");
									if(stack.getTagCompound().hasNoTags())
										stack.setTagCompound(null);
								}
								else if(!event.player.world.isRemote)
								{
									specibus.switchKindAbstratus(abstratusList.get(0), event.player);
									MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, event.player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSelectedSpecibusIndex()), event.player);
								}
							}
							else
							{
								StrifePortfolioHandler.unassignSelected(event.player);
								stack.getTagCompound().removeTag("StrifeAssigned");
								if(stack.getTagCompound().hasNoTags())
									stack.setTagCompound(null);
							}
						}
						return;
					}

					if(cap.getSelectedWeaponIndex() >= 0 && !ItemStack.areItemStacksEqual(stack, selectedWeapon))
					{
						specibus.getContents().set(cap.getSelectedWeaponIndex(), stack);
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

		if(event.player.world.isRemote && event.player.isUser() && !updateSpecibus.isEmpty())
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

		if(event.player.world.isRemote)
			return;

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
					if(stack.getTagCompound().hasNoTags())
						stack.setTagCompound(null);
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

		boolean unlockSwitcher = MinestuckPlayerData.getData(event.player).echeladder.getRung() >= MSUConfig.abstrataSwitcherRung;
		if(cap.abstrataSwitcherUnlocked() != unlockSwitcher)
		{
			cap.unlockAbstrataSwitcher(unlockSwitcher);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, event.player, UpdateStrifeDataPacket.UpdateType.CONFIG), event.player);

			if(unlockSwitcher)
				event.player.sendStatusMessage(new TextComponentTranslation("status.strife.unlockSwitcher"), false);
		}
	}

	public static boolean isStackAssigned(ItemStack stack)
	{
		return MSUConfig.combatOverhaul && !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMobDrops(LivingDropsEvent event)
	{
		if(!MSUConfig.strifeCardMobDrops || !(event.getEntityLiving() instanceof IMob) || event instanceof PlayerDropsEvent || !event.isRecentlyHit() || !(event.getSource().getTrueSource() instanceof EntityPlayer) || event.getSource().getTrueSource() instanceof FakePlayer)
			return;

		EntityPlayer source = (EntityPlayer) event.getSource().getTrueSource();
		IStrifeData cap = source.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(cap.canDropCards() && source.world.rand.nextFloat() < 0.02f*event.getLootingLevel())
		{
			boolean droppedCard = false;
			for(EntityItem item : event.getDrops())
			{
				LinkedList<KindAbstratus> abstrata = getAbstrataList(item.getItem(), false);
				if(!abstrata.isEmpty())
				{
					StrifeSpecibus specibus = new StrifeSpecibus(abstrata.get(source.world.rand.nextInt(abstrata.size())));
					specibus.putItemStack(item.getItem());
					item.setItem(ItemStrifeCard.injectStrifeSpecibus(specibus, new ItemStack(MinestuckUniverseItems.strifeCard)));
					cap.setDroppedCards(cap.getDroppedCards()+1);
					droppedCard = true;
					break;
				}
			}

			if(!droppedCard && event.getEntityLiving() instanceof EntityUnderling)
			{
				ArrayList<KindAbstratus> abstrata = new ArrayList<>(KindAbstratus.REGISTRY.getValuesCollection());

				abstrata.removeIf(k -> k.isEmpty());
				abstrata.add(null);

				EntityItem item = new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,
						ItemStrifeCard.injectStrifeSpecibus(new StrifeSpecibus(abstrata.get(source.world.rand.nextInt(abstrata.size()))), new ItemStack(MinestuckUniverseItems.strifeCard)));
				item.setDefaultPickupDelay();
				event.getDrops().add(item);
				cap.setDroppedCards(cap.getDroppedCards()+1);
			}
		}
	}

	public static LinkedList<KindAbstratus> getAbstrataList(ItemStack stack, boolean ignoreHidden)
	{
		LinkedList<KindAbstratus> result = new LinkedList<>();

		if(stack.isEmpty())
			result.add(MSUKindAbstrata.fistkind);
		else for(KindAbstratus abstratus : KindAbstratus.REGISTRY.getValuesCollection())
		{
			if(abstratus.isEmpty() || abstratus == MSUKindAbstrata.jokerkind)
				continue;

			if((!ignoreHidden || ignoreHidden != abstratus.isHidden()) && abstratus.isStackCompatible(stack))
				result.add(abstratus);
		}
		return result;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDrops(PlayerDropsEvent event)
	{
		if(!MSUConfig.combatOverhaul)
			return;

		IStrifeData cap = event.getEntityPlayer().getCapability(MSUCapabilities.STRIFE_DATA, null);

		ItemStack selectedWeapon = !MSUConfig.keepPortfolioOnDeath && cap.isArmed() && cap.getPortfolio().length > 0 && cap.getSelectedSpecibusIndex() >= 0 && cap.getSelectedWeaponIndex() >= 0
				&& cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && !cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty() ?
				cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()) : ItemStack.EMPTY;

		if(!selectedWeapon.isEmpty())
		{
			boolean selectedDropped = false;
			for(EntityItem item : event.getDrops())
				if(ItemStack.areItemStacksEqual(item.getItem(), selectedWeapon))
				{
					selectedDropped = true;
					break;
				}
			if(!selectedDropped)
				StrifePortfolioHandler.unassignSelected(event.getEntityPlayer());

		}

		event.getDrops().removeIf(item -> isStackAssigned(item.getItem()));
		cap.setArmed(false);

		if(!MSUConfig.keepPortfolioOnDeath)
		{
			for(StrifeSpecibus specibus : cap.getPortfolio())
				if(specibus != null)
					event.getDrops().add(event.getEntityPlayer().dropItem(ItemStrifeCard.injectStrifeSpecibus(specibus, new ItemStack(MinestuckUniverseItems.strifeCard)), true, false));
			cap.clearPortfolio();
			cap.setSelectedSpecibusIndex(-1);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event)
	{
		event.getEntity().getCapability(MSUCapabilities.STRIFE_DATA, null).readFromNBT(event.getOriginal().getCapability(MSUCapabilities.STRIFE_DATA, null).writeToNBT());
	}
}
