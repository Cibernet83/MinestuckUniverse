package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PacketSendPowerParticlesState extends MSUPacket
{
	private int entityId;
	private Class badge;
	private MSUParticles.PowerParticleState state;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt(((EntityLivingBase)args[0]).getEntityId());

		int stateIndex;
		if (args[1] instanceof Class)
		{
			data.writeBoolean(true);
			ByteBufUtils.writeUTF8String(data, ((Class) args[1]).getCanonicalName());
			stateIndex = 2;
		}
		else
		{
			data.writeBoolean(false);
			stateIndex = 1;
		}

		if (args.length > stateIndex)
		{
			data.writeBoolean(true);
			MSUParticles.PowerParticleState state = (MSUParticles.PowerParticleState) args[stateIndex];
			data.writeByte(state.type.ordinal());
			data.writeByte(state.count);
			data.writeByte(state.colors.length);
			for(int color : state.colors)
				data.writeInt(color);
		}
		else
			data.writeBoolean(false);

		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		entityId = data.readInt();

		if (data.readBoolean())
			try
			{
				badge = Class.forName(ByteBufUtils.readUTF8String(data));
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}

		if (data.readBoolean())
		{
			MSUParticles.ParticleType type = MSUParticles.ParticleType.values()[data.readByte()];
			int count = data.readByte();
			int[] colors = new int[data.readByte()];

			for(int i = 0; i < colors.length; i++)
				colors[i] = data.readInt();

			state = new MSUParticles.PowerParticleState(type, count, colors);

			/*
			if (classpect < EnumAspect.values().length)
				state = new MSUParticles.PowerParticleState(
						aura ? MSUParticles.ParticleType.AURA : MSUParticles.ParticleType.BURST,
						EnumAspect.values()[classpect],
						count
				);
			else
				state = new MSUParticles.PowerParticleState(
						aura ? MSUParticles.ParticleType.AURA : MSUParticles.ParticleType.BURST,
						EnumClass.values()[classpect - EnumAspect.values().length],
						count
					);
			*/
		}

		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if (badge == null)
			player.world.getEntityByID(entityId).getCapability(MSUCapabilities.BADGE_EFFECTS, null).oneshotPowerParticles(state);
		else if (state == null)
			player.world.getEntityByID(entityId).getCapability(MSUCapabilities.BADGE_EFFECTS, null).stopPowerParticles(badge);
		else
			player.world.getEntityByID(entityId).getCapability(MSUCapabilities.BADGE_EFFECTS, null).startPowerParticles(badge, state);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}
