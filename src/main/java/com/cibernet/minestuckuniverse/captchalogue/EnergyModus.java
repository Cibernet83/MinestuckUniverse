package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.EnergyGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EnergyModus extends BaseModus
{
	public int charge = 20;
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		
		if(stack.getItem().equals(MinestuckUniverseItems.battery))
		{
			if(charge < getMaxCharge())
			{
				ItemStack spare = new ItemStack(MinestuckUniverseItems.battery, Math.max(0, stack.getCount() - (getMaxCharge()-charge)));
				
				if(charge+stack.getCount() >= getMaxCharge()-1 && side.isServer())
					player.sendStatusMessage(new TextComponentTranslation("status.energyModusFull"), true);
				charge = Math.min(getMaxCharge(), charge + stack.getCount());
				CaptchaDeckHandler.launchAnyItem(player, spare);
					
				if(side.isServer())
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
				return true;
			}
			return false;
		}
		
		
		if(charge <= 1 && side.isServer())
			player.sendStatusMessage(new TextComponentTranslation("status.energyModusEmpty"), true);
		if(charge > 0)
		{
			boolean result = super.putItemStack(stack);
			if(result)
			{
				charge--;
				if(side.isServer())
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			}
			return result;
		}
		return false;
	}
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			if(charge >= list.size())
			{
				charge -= list.size();
				super.getItem(id, asCard);
				if(side.isServer())
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			}
			else if(side.isServer())
				player.sendStatusMessage(new TextComponentTranslation("status.energyModusNotEnough"), false);
			
			return ItemStack.EMPTY;
		}
		
		if(id == CaptchaDeckHandler.EMPTY_CARD)
			return super.getItem(id, asCard);
		
		if(charge <= 1 && side.isServer())
			player.sendStatusMessage(new TextComponentTranslation("status.energyModusEmpty"), true);
		if(charge > 0)
		{
			ItemStack result = super.getItem(id, asCard);
			
			if(!result.isEmpty())
			{
				charge--;
				if(side.isServer())
					MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
			}
			return result;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean increaseSize()
	{
		if(charge <= 0)
		{
			if(side.isServer())
				player.sendStatusMessage(new TextComponentTranslation("status.energyModusEmpty"), true);
			return false;
		}
		
		boolean result = super.increaseSize();
		
		if(result)
		{
			charge--;
			if(charge <= 0 && side.isServer())
				player.sendStatusMessage(new TextComponentTranslation("status.energyModusEmpty"), true);
		}
		return result;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		charge = nbt.getInteger("Charge");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Charge", charge);
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected boolean getSort()
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new EnergyGuiHandler(this);
		return gui;
	}
	
	public static void launchAnyItem(EntityPlayer player, ItemStack item) {
		EntityItem entity = new EntityItem(player.world, player.posX, player.posY + 1.0D, player.posZ, item);
		entity.motionX = CaptchaDeckHandler.rand.nextDouble() - 0.5D;
		entity.motionZ = CaptchaDeckHandler.rand.nextDouble() - 0.5D;
		entity.setNoPickupDelay();
		player.world.spawnEntity(entity);
	}
	
	public int getMaxCharge()
	{
		return Math.max(20, Math.min(Math.max(0, MinestuckPlayerData.getData(player).echeladder.getRung()-4)*10, 100));
	}
}
