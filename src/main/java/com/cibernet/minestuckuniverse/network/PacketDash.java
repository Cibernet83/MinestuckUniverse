package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.util.MSUUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketDash extends MSUPacket
{
	float velocity;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeFloat(args.length > 0 ? (float) args[0] : 1);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		velocity = data.readFloat();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		MovementInput input = ((EntityPlayerSP)player).movementInput;
		float magnitude = (float) Math.sqrt(Math.pow(input.moveForward, 2) + Math.pow(input.moveStrafe, 2));

		player.moveRelative(input.moveStrafe*magnitude, (float)Math.cos((player.rotationPitch+90)*Math.PI/180f)/(MSUUtils.isTrulyOnGround(player) ? 2.5F : 1.25F),
				input.moveForward == 0 && input.moveStrafe == 0 ? (float)Math.sin((player.rotationPitch+90)*Math.PI/180f) : (float)Math.sin((player.rotationPitch+90)*Math.PI/180f * input.moveForward * magnitude),
				velocity * (MSUUtils.isTrulyOnGround(player) ? 1 : 0.5f));

	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
