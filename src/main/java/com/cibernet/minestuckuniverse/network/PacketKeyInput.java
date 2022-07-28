package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketKeyInput extends MSUPacket
{
	private SkillKeyStates.Key key;
	private boolean pressed;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeByte((int) args[0]);
		data.writeBoolean((boolean) args[1]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		key = SkillKeyStates.Key.values()[data.readByte()];
		pressed = data.readBoolean();
		return null;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		player.getCapability(MSUCapabilities.SKILL_KEY_STATES, null).updateKeyState(key, pressed);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return null;
	}
}
