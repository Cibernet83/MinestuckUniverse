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
		if(args.length <= 1)
			nbt = entity.getCapability(MSUCapabilities.STRIFE_DATA, null).writeToNBT();
		else switch ((UpdateType)args[1])
		{
			case PORTFOLIO: nbt = entity.getCapability(MSUCapabilities.STRIFE_DATA, null).writeToNBT(); break;
			case INDEXES: nbt = entity.getCapability(MSUCapabilities.STRIFE_DATA, null).writeToNBT(); break;
			default: nbt = entity.getCapability(MSUCapabilities.STRIFE_DATA, null).writeToNBT(); break;
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

	public static enum UpdateType
	{
		ALL,
		PORTFOLIO,
		INDEXES
	}
}
