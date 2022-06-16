package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.entity.EntityCruxiteSlime;
import com.cibernet.minestuckuniverse.gui.captchalogue.ChasityGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChasityModus extends Modus
{
	protected NonNullList<ChasityEntry> list;
	protected int size;
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		this.list = NonNullList.create();
		if (prev != null)
		{
			Iterator var3 = prev.iterator();
			
			while(var3.hasNext())
			{
				ItemStack stack = (ItemStack) var3.next();
				if(!stack.isEmpty())
				{
					this.list.add(new ChasityEntry(stack));
				}
			}
		}
	}
	
	@Override
	public boolean increaseSize()
	{
		if (MinestuckConfig.modusMaxSize > 0 && this.size >= MinestuckConfig.modusMaxSize)
			return false;
		++this.size;
		return true;
	}
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(MinestuckItems.captchaCard);
			} else return ItemStack.EMPTY;
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ChasityEntry entry : list)
			{
				int i = list.indexOf(entry);
				if(canUse(i))
				{
					CaptchaDeckHandler.launchAnyItem(player, entry.stack);
					list.remove(i);
				}
			}
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			return ItemStack.EMPTY;
		}
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
			
		if(canUse(id))
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			
			ItemStack item = list.remove(id).stack;
			
			if(asCard)
			{
				size--;
				item = AlchemyRecipes.createCard(item, false);
			}
			return item;
		}
		else if(list.get(id).locked && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.SKELETON_KEY))
		{
			list.get(id).locked = false;
			if(side.isServer())
			{
				player.sendStatusMessage(new TextComponentTranslation("status.chasityUnlock"), true);
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			}
			player.world.playSound(null, player.getPosition(), MSUSoundHandler.chasityUnlock, SoundCategory.PLAYERS, 0.8f, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F + 1.0F) * 0.95F);

		}

		else player.world.playSound(null, player.getPosition(), MSUSoundHandler.chasityRattle, SoundCategory.PLAYERS, 0.8f, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F + 1.0F) * 0.95F);
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		
		if(stack.getItem().equals(MinestuckUniverseItems.chastityKey))
		{
			if(!stack.hasTagCompound() || !stack.getTagCompound().hasUniqueId("CardUUID") || !stack.getTagCompound().hasUniqueId("PlayerUUID"))
			{
				if(side.isServer())
					player.sendStatusMessage(new TextComponentTranslation("status.chasityFail"), true);
				return false;
			}
			
			NBTTagCompound nbt = stack.getTagCompound();
			
			ChasityEntry card = getCard(nbt.getUniqueId("CardUUID"));
			
			if(!player.getUniqueID().equals(nbt.getUniqueId("PlayerUUID")) || card == null || !card.locked)
			{
				if(side.isServer())
					player.sendStatusMessage(new TextComponentTranslation("status.chasityFail"), true);
				return false;
			}
			
			card.locked = false;
			if(side.isServer())
			{
				player.sendStatusMessage(new TextComponentTranslation("status.chasityUnlock"), true);
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			}
			player.world.playSound(null, player.getPosition(), MSUSoundHandler.chasityUnlock, SoundCategory.PLAYERS, 0.8f, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F + 1.0F) * 0.95F);
			
			return true;
		}
		
		if(size <= list.size())
			return false;
		
		list.add(new ChasityEntry(stack));
		createKey(list.size()-1);
		MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if (this.side.isServer())
		{
			NonNullList<ItemStack> items = NonNullList.create();
			this.fillList(items);
			return items;
		} else
		{
			if (this.changed)
				this.fillList(this.items);
			return this.items;
		}
	}
	
	protected void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		
		ArrayList list = new ArrayList();
		this.list.forEach(entry -> list.add(entry.stack));
		
		Iterator<ItemStack> iter = list.iterator();
		
		for(int i = 0; i < this.size; ++i) {
			if (iter.hasNext()) {
				items.add(iter.next());
			} else {
				items.add(ItemStack.EMPTY);
			}
		}
		
	}
	
	public ChasityEntry getCard(UUID id)
	{
		for(ChasityEntry chasityEntry : list)
		{
			if(chasityEntry.uuid.equals(id))
				return chasityEntry;
		}
		return null;
	}
	
	public void createKey(int id)
	{
		ItemStack key = new ItemStack(MinestuckUniverseItems.chastityKey, 1, MinestuckPlayerData.getData(player).color+1);
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setUniqueId("CardUUID", list.get(id).uuid);
		nbt.setUniqueId("PlayerUUID", player.getUniqueID());
		
		key.setTagCompound(nbt);
		
		
		if(side.isServer())
			player.sendStatusMessage(new TextComponentTranslation("status.chasityHideKey"), true);
		player.world.playSound(null, player.getPosition(), MSUSoundHandler.chasityLock, SoundCategory.PLAYERS, 0.8f, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F + 1.0F) * 0.95F);
		
		World world = player.world;
		
		List<TileEntity> invList = StreamSupport.stream(BlockPos.getAllInBox(player.getPosition().add(-20, -10, -20),player.getPosition().add(20, 10, 20)).spliterator(), false).map(world::getTileEntity)
		.filter(te -> te instanceof IInventory).filter(te -> getInsertSlot((IInventory) te, key) != -1).collect(Collectors.toList());
		
		if(!invList.isEmpty())
		{
			IInventory inv = (IInventory) invList.get(world.rand.nextInt(invList.size()));
			inv.setInventorySlotContents(getInsertSlot(inv, key), key);
			return;
		}
	
		List<EntityLivingBase> livingList = StreamSupport.stream(world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(20, 10, 20))
				.spliterator(), false).filter(entity -> !entity.equals(player)).collect(Collectors.toList());
		
		if(!livingList.isEmpty() && world.rand.nextInt(3) != 0)
		{
			EntityLivingBase entity = livingList.get(world.rand.nextInt(livingList.size()));
			
			if(entity instanceof EntityPlayer)
			{
				if(!((EntityPlayer) entity).addItemStackToInventory(key))
					launchAnyKey(player, key);
				return;
			}
			if(entity instanceof EntityCruxiteSlime && ((EntityCruxiteSlime) entity).getStoredItem().isEmpty()) {
				((EntityCruxiteSlime) entity).setStoredItem(key);
				return;
			}
			if(entity instanceof EntityMob || (entity instanceof EntityArmorStand && ((EntityArmorStand) entity).getShowArms()))
			{
				EnumHand hand = entity.getHeldItemMainhand().isEmpty() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				if((!(entity instanceof EntityArmorStand) || (entity instanceof EntityArmorStand && hand != EnumHand.OFF_HAND)) && entity.getHeldItem(hand).isEmpty())
				{
					entity.setHeldItem(hand, key);
					if(entity instanceof EntityMob)
					{
						((EntityLiving) entity).setDropChance(hand.equals(EnumHand.MAIN_HAND) ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, 1);
						((EntityMob) entity).enablePersistence();
					}
				}
				else launchAnyKey(entity, key);
				return;
			}
		}
		EntityItem item = new EntityItem(world, player.posX + world.rand.nextInt(40)-20, Math.max(1, player.posY + world.rand.nextInt(20)-10), player.posZ + world.rand.nextInt(40)-20, key);
		item.setNoDespawn();
		world.spawnEntity(item);
		
		
	}
	
	protected static void launchAnyKey(EntityLivingBase entity, ItemStack stack) {
		EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY + 1.0D, entity.posZ, stack);
		item.motionX = entity.world.rand.nextDouble() - 0.5D;
		item.motionZ = entity.world.rand.nextDouble() - 0.5D;
		item.setNoDespawn();
		entity.world.spawnEntity(entity);
	}
	
	protected int getInsertSlot(IInventory inventory, ItemStack stack)
	{
		for(int slot = 0; slot < inventory.getSizeInventory(); slot++)
		{
			if(inventory.isItemValidForSlot(slot, stack) && inventory.getStackInSlot(slot).isEmpty())
			{
				return slot;
			}
		}
		return -1;
	}
	
	public boolean canUse(int id)
	{
		return (id >= list.size() || id < 0 || !list.get(id).locked);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.size = nbt.getInteger("size");
		this.list = NonNullList.create();
		
		for(int i = 0; i < this.size && nbt.hasKey("card" + i); ++i) {
			this.list.add(new ChasityEntry(nbt.getCompoundTag("card" + i)));
		}
		
		if (this.side.isClient()) {
			this.items = NonNullList.create();
			this.changed = true;
		}
		
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", this.size);
		Iterator<ChasityEntry> iter = this.list.iterator();
		
		for(int i = 0; i < this.list.size(); ++i) {
			ChasityEntry card = iter.next();
			nbt.setTag("card" + i, card.writeToNBT(new NBTTagCompound()));
		}
		
		return nbt;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new ChasityGuiHandler(this);
		return gui;
	}
	
	class ChasityEntry
	{
		UUID uuid;
		ItemStack stack;
		boolean locked = true;
		
		public ChasityEntry(UUID uuid, ItemStack stack)
		{
			this.uuid = uuid;
			this.stack = stack;
		}
		
		public ChasityEntry(ItemStack stack)
		{
			this(UUID.randomUUID(), stack);
		}
		
		public ChasityEntry(NBTTagCompound nbt)
		{
			this(nbt.getUniqueId("ChasityID"), new ItemStack(nbt));
			locked = nbt.getBoolean("Locked");
		}
		
		boolean isLocked()
		{
			return locked && !stack.isEmpty();
		}
		
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			stack.writeToNBT(nbt);
			nbt.setBoolean("Locked", locked);
			nbt.setUniqueId("ChasityID", uuid);
			return nbt;
		}
	}
}
