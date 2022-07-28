package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.entity.ai.EntityAIMindflayerTarget;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PacketMindflayerMovementInput extends MSUPacket
{
	private float moveForward, moveStrafe;
	private boolean jump, sneak;
	private int currentItem;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeFloat((float) args[0]);
		data.writeFloat((float) args[1]);
		data.writeBoolean((boolean) args[2]);
		data.writeBoolean((boolean) args[3]);
		data.writeInt((int) args[4]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		moveStrafe = data.readFloat();
		moveForward = data.readFloat();
		jump = data.readBoolean();
		sneak = data.readBoolean();
		currentItem = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		EntityLivingBase target = player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getMindflayerEntity();

		if(target == null)
			return;

		if (target instanceof EntityCreature)
		{
			for (EntityAITasks.EntityAITaskEntry entry : ((EntityCreature) target).tasks.taskEntries)
				if (entry.action instanceof EntityAIMindflayerTarget)
					((EntityAIMindflayerTarget) entry.action).setMove(moveStrafe, moveForward);
		}
		else
		{
			IBadgeEffects badgeEffects = target.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
			badgeEffects.setMovement(moveStrafe, moveForward, jump, sneak);

			if(target instanceof EntityPlayer && ((EntityPlayer) target).inventory.currentItem != currentItem)
			{
				((EntityPlayer) target).inventory.currentItem = currentItem;
				MSUChannelHandler.sendToPlayer(makePacket(Type.SET_CURRENT_ITEM, currentItem), (EntityPlayer) target);
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
