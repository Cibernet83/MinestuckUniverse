package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.UUID;

public class UpdateStrifeDataPacket extends MSUPacket
{
	private NBTTagCompound nbtData;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		EntityLivingBase entity = (EntityLivingBase) args[0];
		NBTTagCompound nbt;

		IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(args.length <= 1)
			nbt = cap.writeConfig(cap.writeToNBT());
		else switch ((UpdateType)args[1])
		{
			case PORTFOLIO:
				int[] specibi = new int[args.length-2];
				for(int i = 0; i < specibi.length; i++)
					specibi[i] = (int)args[i+2];

				nbt = cap.writePortfolio(new NBTTagCompound(), specibi);
				break;
			case INDEXES: nbt = cap.writeSelectedIndexes(new NBTTagCompound()); break;
			case DROPPED_CARDS: nbt = cap.writeDroppedCards(new NBTTagCompound()); break;
			case CONFIG: nbt = cap.writeConfig(new NBTTagCompound()); break;
			default: nbt = cap.writeConfig(cap.writeToNBT()); break;
		}
		nbt.setUniqueId("TargetUUID", entity.getUniqueID());
		ByteBufUtils.writeTag(data, nbt);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		nbtData = ByteBufUtils.readTag(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		UUID uuid = nbtData.getUniqueId("TargetUUID");

		for (EntityLivingBase entity : player.world.getEntities(EntityLivingBase.class, (entity) -> entity.getUniqueID().equals(uuid)))
		{
			if(entity.getUniqueID().equals(player.getUniqueID()))
				entity = player;


			IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);
			cap.readFromNBT(nbtData);

			if(!player.world.isRemote && cap.isArmed())
			{
				EnumHand hand = StrifeEventHandler.isStackAssigned(entity.getHeldItemOffhand()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
				if(StrifeEventHandler.isStackAssigned(entity.getHeldItem(hand)) &&
						cap.getPortfolio().length <= 0 || cap.getSelectedSpecibusIndex() < 0 || cap.getSelectedWeaponIndex() < 0
						|| cap.getPortfolio()[cap.getSelectedSpecibusIndex()] == null || cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().isEmpty()
						|| cap.getSelectedWeaponIndex() >= cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().size()
						|| !ItemStack.areItemStacksEqual(cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().get(cap.getSelectedWeaponIndex()), entity.getHeldItem(hand)))
				{
					entity.setHeldItem(hand, ItemStack.EMPTY);
					cap.setArmed(false);
				}
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}

	public enum UpdateType
	{
		ALL,
		PORTFOLIO,
		INDEXES,
		DROPPED_CARDS,
		CONFIG
	}
}
