package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.items.ItemStrifeCard;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class AssignStrifePacket extends MSUPacket
{
	EnumHand hand;
	StrifeSpecibus specibus;

	ItemStack stack;
	int slot;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeBoolean(args[0] instanceof ItemStack);

		if(args[0] instanceof ItemStack)
		{
			ByteBufUtils.writeItemStack(data, (ItemStack) args[0]);
			data.writeInt((Integer) args[1]);
			return this;
		}

		data.writeInt(((EnumHand)args[0]).ordinal());
		data.writeBoolean(args.length > 1);
		if(args.length > 1)
			ByteBufUtils.writeTag(data, ((StrifeSpecibus)args[1]).writeToNBT(new NBTTagCompound()));

		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		if(data.readBoolean())
		{
			stack = ByteBufUtils.readItemStack(data);
			slot = data.readInt();
			return this;
		}

		hand = EnumHand.values()[data.readInt()];
		if(data.readBoolean())
			specibus = new StrifeSpecibus(ByteBufUtils.readTag(data));

		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(!MSUConfig.combatOverhaul)
			return;

		if(stack != null)
		{
			StrifePortfolioHandler.addWeapontoSlot(player, stack, slot);
			return;
		}

		if(specibus != null)
			ItemStrifeCard.injectStrifeSpecibus(specibus, player.getHeldItem(hand));
		StrifePortfolioHandler.assignStrife(player, hand);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
