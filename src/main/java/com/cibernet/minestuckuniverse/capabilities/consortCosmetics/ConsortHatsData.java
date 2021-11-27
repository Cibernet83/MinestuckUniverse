package com.cibernet.minestuckuniverse.capabilities.consortCosmetics;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.item.ItemNet;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class ConsortHatsData implements IConsortHatsData
{
	ItemStack hat = ItemStack.EMPTY;
	int pickupDelay = 0;

	public static final ArrayList<ItemStack> HAT_SPAWN_POOL = new ArrayList<ItemStack>()
			{{
				add(new ItemStack(MinestuckUniverseItems.crumplyHat));
				add(new ItemStack(MinestuckUniverseItems.wizardHat));
				add(new ItemStack(MinestuckUniverseItems.frogHat));
				add(new ItemStack(Items.LEATHER_HELMET));
				add(new ItemStack(Items.CHAINMAIL_HELMET));
			}};

	@Override
	public void setOwner(EntityLivingBase owner)
	{
		if(owner instanceof EntityConsort && owner.getRNG().nextFloat() < 0.05f)
			setHeadStack(HAT_SPAWN_POOL.get(owner.getRNG().nextInt(HAT_SPAWN_POOL.size())).copy());
	}

	@Override
	public void setHeadStack(ItemStack stack)
	{
		hat = stack;
	}

	@Override
	public ItemStack getHeadStack() {
		return hat;
	}

	@Override
	public void setPickupDelay(int i) {
		pickupDelay = i;
	}

	@Override
	public int getPickupDelay() {
		return pickupDelay;
	}

	@Override
	public int shrinkPickupDelay()
	{
		pickupDelay = Math.max(0, pickupDelay-1);
		return pickupDelay;
	}

	@Override
	public NBTTagCompound writeToNBT()
	{

		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stackNbt = new NBTTagCompound();
		getHeadStack().writeToNBT(stackNbt);
		nbt.setTag("Hat", stackNbt);
		nbt.setInteger("PickupDelay", getPickupDelay());

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		setHeadStack(new ItemStack(nbt.getCompoundTag("Hat")));
		setPickupDelay(nbt.getInteger("PickupDelay"));
	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
	{
		if(!event.getEntityLiving().world.isRemote && !event.getEntityLiving().isDead && event.getEntityLiving().getCapability(MSUCapabilities.CONSORT_HATS_DATA, null) != null)
		{
			IConsortHatsData cap = event.getEntityLiving().getCapability(MSUCapabilities.CONSORT_HATS_DATA, null);
			EntityLivingBase entity = event.getEntityLiving();

			if(cap.getPickupDelay() <= 0)
			for (EntityItem entityitem : entity.world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)))
			{
				if (!entityitem.isDead && !entityitem.getItem().isEmpty() && !entityitem.cannotPickup())
				{
					ItemStack stack = entityitem.getItem();
					if(EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && !ItemStack.areItemStacksEqual(stack, cap.getHeadStack()))
					{
						if(!cap.getHeadStack().isEmpty())
							entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY+entity.height, entity.posZ, cap.getHeadStack()));

						ItemStack pickedUp = stack.copy();
						pickedUp.setCount(1);
						cap.setHeadStack(pickedUp);
						stack.shrink(1);
						entity.onItemPickup(entityitem, 1);
						entityitem.setDead();

						if(!stack.isEmpty())
							entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY+entity.height, entity.posZ, stack));

						cap.setPickupDelay(200);
						MSUChannelHandler.sendToTracking(MSUPacket.makePacket(MSUPacket.Type.UPDATE_HATS, entity), entity);

						break;
					}
				}
			}

			cap.shrinkPickupDelay();
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if(!event.getEntityLiving().world.isRemote && event.getEntity().getCapability(MSUCapabilities.CONSORT_HATS_DATA, null) != null)
		{
			EntityLivingBase entity = event.getEntityLiving();
			IConsortHatsData cap = entity.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null);

			if(!cap.getHeadStack().isEmpty())
			{
				entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY+entity.height, entity.posZ, cap.getHeadStack()));
				cap.setHeadStack(ItemStack.EMPTY);
				MSUChannelHandler.sendToTracking(MSUPacket.makePacket(MSUPacket.Type.UPDATE_HATS, event.getEntityLiving()), event.getEntityLiving());
			}
		}
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if(!event.getEntityPlayer().world.isRemote && event.getTarget() instanceof EntityFrog && event.getEntityPlayer().getHeldItem(event.getHand()).getItem() instanceof ItemNet)
		{
			EntityFrog entity = (EntityFrog) event.getTarget();
			IConsortHatsData cap = entity.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null);

			if(!cap.getHeadStack().isEmpty())
			{
				entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY+entity.height, entity.posZ, cap.getHeadStack()));
				cap.setHeadStack(ItemStack.EMPTY);
				MSUChannelHandler.sendToTracking(MSUPacket.makePacket(MSUPacket.Type.UPDATE_HATS, event.getTarget()), event.getTarget());
			}
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event)
	{
		if(!event.getTarget().world.isRemote && event.getTarget().getCapability(MSUCapabilities.CONSORT_HATS_DATA, null) != null)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_HATS, event.getTarget()), event.getEntityPlayer());
	}
}
