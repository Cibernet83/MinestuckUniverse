package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.JujuGuiHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.captchalogue.JujuModusPacket;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class JujuModus extends BaseModus
{
	
	public int partnerID = -1;
	public int cardTexIndex = 0;
	@SideOnly(Side.CLIENT)
	public NonNullList<ItemStack> displayStacks;
	
	
	
	public static boolean link(IdentifierHandler.PlayerIdentifier target, IdentifierHandler.PlayerIdentifier partner)
	{
		if(target == null || partner == null)
		{
			if(target.getPlayer() != null)
				target.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusLinkFail"), false);
			return false;
		}
		
		if(!(MinestuckPlayerData.getData(target).modus instanceof JujuModus && MinestuckPlayerData.getData(partner).modus instanceof JujuModus))
		{
			if(target.getPlayer() != null)
				target.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusLinkFail"), false);
			return false;
		}
		
		JujuModus targetModus = (JujuModus) MinestuckPlayerData.getData(target).modus;
		JujuModus partnerModus = (JujuModus) MinestuckPlayerData.getData(partner).modus;
		
		targetModus.partnerID = partner.getId();
		targetModus.cardTexIndex = 1;
		partnerModus.partnerID = target.getId();
		partnerModus.cardTexIndex = 2;
		
		if(target.getPlayer() != null)
			target.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusLink", partner.getUsername()), false);
		if(partner.getPlayer() != null)
			partner.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusLink", target.getUsername()), false);
		
		sendUpdateToClients(target, partner);
		
		return true;
	}
	
	public static boolean unlink(IdentifierHandler.PlayerIdentifier target)
	{
		if(target == null)
			return false;
		
		if(!(MinestuckPlayerData.getData(target).modus instanceof JujuModus))
			return false;
		
		JujuModus targetModus = (JujuModus) MinestuckPlayerData.getData(target).modus;
		IdentifierHandler.PlayerIdentifier partner = IdentifierHandler.getById(targetModus.partnerID);
		
		if(MinestuckPlayerData.getData(partner).modus instanceof JujuModus)
		{
			((JujuModus) MinestuckPlayerData.getData(partner).modus).partnerID = -1;
			((JujuModus) MinestuckPlayerData.getData(partner).modus).cardTexIndex = 0;
		}
		targetModus.partnerID = -1;
		targetModus.cardTexIndex = 0;
		
		if(target.getPlayer() != null)
			target.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusUnlink", partner.getUsername()), false);
		if(partner.getPlayer() != null)
			partner.getPlayer().sendStatusMessage(new TextComponentTranslation("status.jujuModusUnlink", target.getUsername()), false);
		
		JujuModus.sendUpdateToClients(target, partner);
		
		return true;
	}
	
	public boolean checkUnlink(EntityPlayer player)
	{
		if(partnerID == -1)
			return false;
		
		if(IdentifierHandler.getById(partnerID) == null || MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)) == null)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.jujuModusUnlinkUnknown"), false);
			cardTexIndex = 0;
			partnerID = -1;
			sendUpdateToClients();
			
			return true;
		}
		else if(!(MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)).modus instanceof JujuModus) ||
				((JujuModus) MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)).modus).partnerID != IdentifierHandler.encode(player).getId())
		{
			player.sendStatusMessage(new TextComponentTranslation("status.jujuModusUnlink", IdentifierHandler.getById(partnerID).getUsername()), false);
			cardTexIndex = 0;
			partnerID = -1;
			sendUpdateToClients();
			
			return true;
		}
		return false;
	}
	
	public static EntityPlayer getLinkPlayer(EntityPlayer player)
	{
		List<EntityPlayer> players = player.world.getPlayers(EntityPlayer.class, (p) ->
		{
			Modus pModus = MinestuckPlayerData.getData(p).modus;
			return !p.equals(player) && pModus instanceof JujuModus && ((JujuModus) pModus).partnerID == -1;
		} );
		
		if(players.isEmpty())
			return null;
		
		return players.get(player.world.rand.nextInt(players.size()));
	}
	
	public void sendUpdateToClients()
	{
		if(player != null)
			sendUpdateToClients(IdentifierHandler.encode(player), IdentifierHandler.getById(partnerID));
	}
	
	public static void sendUpdateToClients(IdentifierHandler.PlayerIdentifier target, IdentifierHandler.PlayerIdentifier partner)
	{
		
		NBTTagCompound targetNBT = new NBTTagCompound();
		NBTTagCompound partnerNBT = new NBTTagCompound();
		
		if(target != null && MinestuckPlayerData.getData(target).modus instanceof JujuModus)
			targetNBT = CaptchaDeckHandler.writeToNBT(MinestuckPlayerData.getData(target).modus);
		if(partner != null &&  MinestuckPlayerData.getData(partner).modus instanceof JujuModus)
			partnerNBT = CaptchaDeckHandler.writeToNBT(MinestuckPlayerData.getData(partner).modus);
		
		if(!partnerNBT.hasNoTags() && target != null && target.getPlayer() != null && MinestuckPlayerData.getData(target).modus instanceof JujuModus)
		{
			NBTTagCompound nbt = targetNBT.copy();
			nbt.setTag("PartnerModus", partnerNBT);
			
			MSUPacket packet = MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, nbt);
			MSUChannelHandler.sendToPlayer(packet, target.getPlayer());
		}
		
		if(!targetNBT.hasNoTags() && partner != null && partner.getPlayer() != null && MinestuckPlayerData.getData(partner).modus instanceof JujuModus)
		{
			NBTTagCompound nbt = partnerNBT.copy();
			nbt.setTag("PartnerModus", targetNBT);
			
			MSUPacket packet = MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, nbt);
			MSUChannelHandler.sendToPlayer(packet, partner.getPlayer());
		}
	}
	
	public NonNullList<ItemStack> getPartnerItems()
	{
		if(IdentifierHandler.getById(partnerID) == null)
			return super.getItems();
		
		if(side == Side.CLIENT)
			return displayStacks;
		Modus partnerModus = MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)).modus;
		
		if(partnerModus == null)
			return NonNullList.create();
		return partnerModus.getItems();
	}
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(IdentifierHandler.getById(partnerID) == null)
			return ItemStack.EMPTY;
		
		
		Modus partnerModus = MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)).modus;
		
		if(partnerModus == null)
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX && partnerModus instanceof JujuModus)
		{
			ejectPartnerSylladex((JujuModus) partnerModus);
			sendUpdateToClients();
			return ItemStack.EMPTY;
		}
		
		ItemStack result = partnerModus instanceof JujuModus ? ((JujuModus)partnerModus).getItemSuper(id, asCard) : partnerModus.getItem(id, asCard);
		sendUpdateToClients();
		return result;
	}
	
	protected void ejectPartnerSylladex(JujuModus partnerModus)
	{
		for(ItemStack item : partnerModus.list)
			CaptchaDeckHandler.launchAnyItem(player, item);
		partnerModus.list.clear();
	}
	
	public ItemStack getItemSuper(int id, boolean asCard)
	{
		return super.getItem(id, asCard);
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		boolean result = super.putItemStack(stack);
		sendUpdateToClients();
		return result;
	}
	
	@Override
	public boolean increaseSize()
	{
		boolean result = super.increaseSize();
		if(result) sendUpdateToClients();
		return result;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		partnerID = nbt.getInteger("PartnerID");
		cardTexIndex = nbt.getInteger("CardTexture");
		
		if(side == Side.CLIENT && nbt.hasKey("PartnerModus"))
		{
			NBTTagCompound partnerNbt = nbt.getCompoundTag("PartnerModus");
			Modus partnerModus = CaptchaDeckHandler.readFromNBT(nbt.getCompoundTag("PartnerModus"), false);
			if(partnerModus != null)
			displayStacks = partnerModus.getItems();
			/*
			displayStacks = NonNullList.withSize(partnerNbt.getInteger("size"), ItemStack.EMPTY);
			for(int i = 0; i < displayStacks.size() && nbt.hasKey("item" + i); ++i)
				displayStacks.set(i, new ItemStack(partnerNbt.getCompoundTag("item" + i)));
			*/
			getGuiHandler().updateContent();
		}
		
		if(player != null && side == Side.SERVER && IdentifierHandler.getById(partnerID) != null && MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)) != null)
		{
			if(!(MinestuckPlayerData.getData(IdentifierHandler.getById(partnerID)).modus instanceof JujuModus))
				unlink(IdentifierHandler.encode(player));
			else sendUpdateToClients();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("PartnerID", partnerID);
		nbt.setInteger("CardTexture", cardTexIndex);
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
			gui = new JujuGuiHandler(this);
		
		if(partnerID != -1 && displayStacks == null)
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.JUJU_UPDATE, JujuModusPacket.Type.REQUEST_CLIENT));
		
		return gui;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		boolean result = super.canSwitchFrom(modus);
		
		if(!result)
		{
			if(side == Side.CLIENT)
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.JUJU_UPDATE, JujuModusPacket.Type.UNLINK));
			else unlink(IdentifierHandler.encode(player));
		}
		
		return result;
	}
}
