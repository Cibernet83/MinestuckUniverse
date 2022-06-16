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

import java.util.EnumSet;

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
			data.writeBoolean(state.type == MSUParticles.ParticleType.AURA);
			if (state.aspect != null)
				data.writeByte(state.aspect.ordinal());
			else
				data.writeByte(EnumAspect.values().length + state.clazz.ordinal());
			data.writeByte(state.count);
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
			boolean aura = data.readBoolean();
			int classpect = data.readByte();
			int count = data.readByte();

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
