package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.events.WeaponAssignedEvent;
import com.cibernet.minestuckuniverse.gui.GuiStrifePortfolio;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.weapons.MSUWeaponBase;
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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.*;
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

		if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
		{
			String str = "";
			for(KindAbstratus abstratus : getAbstrataList(event.getItemStack(), false))
				str += abstratus.getLocalizedName().toLowerCase() + ", ";
			if(!str.isEmpty())
				event.getToolTip().add(I18n.format("strife.item.abstrataList", str.trim().substring(0, str.lastIndexOf(','))));
		}
	}

	@SubscribeEvent
	public static void setUnderlingDamage(LivingDamageEvent event)
	{
		if(!(event.getSource().getImmediateSource() instanceof EntityLivingBase))
			return;
		EntityLivingBase source = (EntityLivingBase) event.getSource().getImmediateSource();

		ItemStack stack = source.getHeldItemMainhand();

		//bypass MSUConfig.weaponAttackMultiplier against underlings
		if(event.getEntityLiving() instanceof EntityUnderling && stack.getItem() instanceof MSUWeaponBase)
		{
			IAttributeInstance dmgAttr = source.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			AttributeModifier weaponMod = dmgAttr.getModifier(MSUWeaponBase.getAttackDamageUUID());
			double dmg = ((MSUWeaponBase) stack.getItem()).getUnmodifiedAttackDamage(stack);

			if(weaponMod != null && weaponMod.getAmount() != dmg)
			{
				dmgAttr.removeModifier(MSUWeaponBase.getAttackDamageUUID());
				dmgAttr.applyModifier(new AttributeModifier(MSUWeaponBase.getAttackDamageUUID(), "Weapon modifier", dmg, 0));

				event.getEntityLiving().attackEntityFrom(event.getSource(), (float) dmgAttr.getAttributeValue());
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerAttack(LivingAttackEvent event)
	{
		if(!MSUConfig.combatOverhaul ||  !MSUConfig.restrictedStrife ||  !(event.getSource().getImmediateSource() instanceof EntityPlayer) || event.getSource().getImmediateSource() instanceof FakePlayer)
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
			WeaponAssignedEvent checkEvent = new WeaponAssignedEvent(source, stack, false);
			MinecraftForge.EVENT_BUS.post(checkEvent);
			if(!checkEvent.getCheckResult())
				event.setCanceled(true);
			return;
		}

		boolean isAssigned = isStackAssigned(stack);
		WeaponAssignedEvent checkEvent = new WeaponAssignedEvent(source, stack, isAssigned);
		MinecraftForge.EVENT_BUS.post(checkEvent);
		if(!checkEvent.getCheckResult())
			event.setCanceled(true);

		if(event.isCanceled())
			return;
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
		add(MSUKindAbstrata.getItem("bibliocraft", "bibliodrill"));
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
		if(!MSUConfig.combatOverhaul ||  !MSUConfig.restrictedStrife || event.getEntityPlayer() instanceof FakePlayer)
			return;

		ItemStack stack = event.getItemStack();
		boolean canUse = true;

		if(FORCED_USABLE_UNASSIGNED.contains(stack.getItem()) || isStackAssigned(stack))
			canUse = true;
		else if(USABLE_ASSIGNED_ONLY.contains(stack.getItem()))
		{
			event.setCancellationResult(EnumActionResult.PASS);
			canUse = false;
		}
		else for(KindAbstratus abstratus : getAbstrataList(stack, false))
				if(abstratus.preventsRightClick())
				{
					event.setCancellationResult(EnumActionResult.PASS);
					canUse = false;
				}

		WeaponAssignedEvent checkEvent = new WeaponAssignedEvent(event.getEntityPlayer(), stack, canUse);
		MinecraftForge.EVENT_BUS.post(checkEvent);
		canUse = checkEvent.getCheckResult();
		if(!canUse)
			event.setCanceled(true);

	}

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();
		ItemStack mainStack = event.getEntityPlayer().getHeldItemMainhand();
		ItemStack offStack = event.getEntityPlayer().getHeldItemOffhand();

		if(isStackAssigned(mainStack) && stack.getItem() == mainStack.getItem() && mainStack.getCount() < mainStack.getMaxStackSize())
		{
			NBTTagCompound copyNbt = stack.copy().getTagCompound();
			if(copyNbt == null) copyNbt = new NBTTagCompound();
			copyNbt.setBoolean("StrifeAssigned", true);
			if(mainStack.getTagCompound().equals(copyNbt))
			{
				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("StrifeAssigned", true);
			}
		} else if(isStackAssigned(offStack) && stack.getItem() == offStack.getItem() && offStack.getCount() < offStack.getMaxStackSize())
		{
			NBTTagCompound copyNbt = stack.copy().getTagCompound();
			if(copyNbt == null) copyNbt = new NBTTagCompound();
			copyNbt.setBoolean("StrifeAssigned", true);
			if(offStack.getTagCompound().equals(copyNbt))
			{
				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("StrifeAssigned", true);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityItemTick(TickEvent.WorldTickEvent event)
	{
		for(EntityItem item : event.world.getEntities(EntityItem.class, i -> isStackAssigned(i.getItem())))
			item.getItem().getTagCompound().removeTag("StrifeAssigned");
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
			return;

		IStrifeData cap = event.player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		checkArmed(event.player);

		if(event.player.world.isRemote)
			return;

		//unlock abstrata switcher
		boolean unlockSwitcher = MinestuckPlayerData.getData(event.player).echeladder.getRung() >= MSUConfig.abstrataSwitcherRung;
		if(!event.player.world.isRemote && cap.abstrataSwitcherUnlocked() != unlockSwitcher)
		{
			cap.unlockAbstrataSwitcher(unlockSwitcher);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, event.player, UpdateStrifeDataPacket.UpdateType.CONFIG), event.player);

			if(unlockSwitcher)
				event.player.sendStatusMessage(new TextComponentTranslation("status.strife.unlockSwitcher"), false);
		}

		cap.setPrevSelSlot(event.player.inventory.currentItem);
	}

	private static void checkArmed(EntityPlayer player)
	{
		IStrifeData cap = player.getCapability(MSUCapabilities.STRIFE_DATA, null);
		if(cap == null)
			return;

		if(cap.isArmed())
		{
			ItemStack weapon;

			try
			{
				weapon = cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex());
			} catch (Throwable t)
			{
				if(!player.world.isRemote)
				{
					cap.setArmed(false);
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), player);
				}
				return;
			}

			boolean weaponHeld = false;
			boolean hasWeapon = false;
			//for(EnumHand hand : EnumHand.values())
			{
				if(ItemStack.areItemStacksEqual(player.getHeldItem(EnumHand.MAIN_HAND), weapon))
				{
					hasWeapon = true;
					weaponHeld = true;
					//break;
				}
			}

			ItemStack offhandStack = player.getHeldItemOffhand();
			if(isStackAssigned(offhandStack))
			{
				if(!hasWeapon && ItemStack.areItemStacksEqual(weapon, offhandStack))
				{
					player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
					if(!player.world.isRemote)
					{
						cap.setArmed(false);
						MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.INDEXES), player);
					}
					return;
				}
				else
				{
					offhandStack.getTagCompound().removeTag("StrifeAssigned");
					if(offhandStack.getTagCompound().hasNoTags())
						offhandStack.setTagCompound(null);
				}
			}

			if(!player.world.isRemote)
			{
				//innocuous double
				if(player.inventory.currentItem == cap.getPrevSelSlot() && !weaponHeld && player.openContainer instanceof ContainerPlayer)
				//for(EnumHand hand : EnumHand.values())
				{
					ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
					if(!stack.isEmpty())
					{
						StrifeSpecibus specibus = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];

						if(specibus.getKindAbstratus().isStackCompatible(stack))
						{
							specibus.getContents().set(cap.getSelectedWeaponIndex(), stack);
							if(!stack.hasTagCompound())
								stack.setTagCompound(new NBTTagCompound());
							stack.getTagCompound().setBoolean("StrifeAssigned", true);
							weapon = stack;
							weaponHeld = true;
							hasWeapon = true;
						}
						else if(StrifePortfolioHandler.moveSelectedWeapon(player, stack) == null)
						{
							List<KindAbstratus> abstratusList = StrifeEventHandler.getAbstrataList(stack, false);
							abstratusList.sort(KindAbstratus::compareTo);
							if(!abstratusList.isEmpty() && specibus.getContents().size() <= 1)
							{
								specibus.getContents().set(cap.getSelectedWeaponIndex(), stack);
								if(!stack.hasTagCompound())
									stack.setTagCompound(new NBTTagCompound());
								stack.getTagCompound().setBoolean("StrifeAssigned", true);
								weapon = stack;
								weaponHeld = true;
								hasWeapon = true;

								specibus.switchKindAbstratus(abstratusList.get(0), player);
								if(!player.world.isRemote)
									MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO, cap.getSelectedSpecibusIndex()), player);
							}
							else
							{
								//StrifePortfolioHandler.unassignSelected(player);
								if(stack.hasTagCompound())
								{
									stack.getTagCompound().removeTag("StrifeAssigned");
									if(stack.getTagCompound().hasNoTags())
										stack.setTagCompound(null);
								}
								weaponHeld = false;
								hasWeapon = false;
							}
						}
						else
						{
							weapon = stack;
							weaponHeld = true;
							hasWeapon = true;
						}
						//break;
					}

				}

				for(ItemStack stack : player.inventory.mainInventory)
				{
					int slot = getSlotFor(player.inventory, stack);
					if(slot == player.inventory.currentItem)
					{
						if(!cap.isArmed() && isStackAssigned(stack))
							player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);

						continue;
					}

					if(isStackAssigned(stack))
					{
						if(!hasWeapon && ItemStack.areItemStacksEqual(weapon, stack))
						{
							hasWeapon = true;
							player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
							player.inventory.markDirty();
						}
						else
						{
							stack.getTagCompound().removeTag("StrifeAssigned");
							if(stack.getTagCompound().hasNoTags())
								stack.setTagCompound(null);
						}
					}
				}
			}

			ItemStack cursorStack = player.inventory.getItemStack();
			if(isStackAssigned(cursorStack))
			{
				if(!hasWeapon && ItemStack.areItemStacksEqual(weapon, cursorStack))
				{
					hasWeapon = true;
					player.inventory.setItemStack(ItemStack.EMPTY);
				}
				else
				{
					cursorStack.getTagCompound().removeTag("StrifeAssigned");
					if(cursorStack.getTagCompound().hasNoTags())
						cursorStack.setTagCompound(null);
				}
			}

			if(!player.world.isRemote && !weaponHeld)
			{
				if(!hasWeapon)
					StrifePortfolioHandler.unassignSelected(player);
				cap.setArmed(false);
			}
		}
		else
		{
			NonNullList<ItemStack> inv = NonNullList.create();
			inv.addAll(player.inventory.mainInventory);
			inv.addAll(player.inventory.offHandInventory);
			for(ItemStack stack : inv)
			{
				if(isStackAssigned(stack))
				{
					stack.getTagCompound().removeTag("StrifeAssigned");
					if(stack.getTagCompound().hasNoTags())
						stack.setTagCompound(null);
				}
			}
		}
	}

	public static boolean isStackAssigned(ItemStack stack)
	{
		return MSUConfig.combatOverhaul && !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().getBoolean("StrifeAssigned");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMobDrops(LivingDropsEvent event)
	{
		if(!(event.getEntityLiving() instanceof IMob) || event instanceof PlayerDropsEvent || !event.isRecentlyHit() || !(event.getSource().getTrueSource() instanceof EntityPlayer) || event.getSource().getTrueSource() instanceof FakePlayer)
			return;

		EntityPlayer source = (EntityPlayer) event.getSource().getTrueSource();
		IStrifeData cap = source.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(cap.canDropCards() && source.world.rand.nextFloat() < (cap.getDroppedCards() <= 0  && event.getEntityLiving() instanceof EntityUnderling ? 0.05f : 0.01f)*(event.getLootingLevel()+1))
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

	public static int getSlotFor(InventoryPlayer inv, ItemStack stack)
	{
		for (int i = 0; i < inv.mainInventory.size(); ++i)
		{
			if (!inv.mainInventory.get(i).isEmpty() && stackEqualExact(stack, inv.mainInventory.get(i)))
			{
				return i;
			}
		}

		return -1;
	}

	private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
}
