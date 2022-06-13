package com.cibernet.minestuckuniverse.network;

import com.mraof.minestuck.item.MinestuckItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class StoneTabletRequestPacket extends MSUPacket
{
	private String text;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		if (args.length > 0 && args[0] instanceof String)
			ByteBufUtils.writeUTF8String(data, (String) args[0]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		if (data.readableBytes() > 0)
			text = ByteBufUtils.readUTF8String(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		ItemStack tablet = new ItemStack(MinestuckItems.stoneSlab);

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = stack.getTagCompound();

		if (!stack.isItemEqual(tablet))
			if (!(stack = player.getHeldItemOffhand()).isItemEqual(tablet))
				return;

		if (!text.trim().isEmpty())
			nbt.setString("text", text);
		else if (nbt.hasKey("text"))
			nbt.removeTag("text");
		stack.setTagCompound(nbt.hasNoTags() ? null : nbt);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER );
	}

}
