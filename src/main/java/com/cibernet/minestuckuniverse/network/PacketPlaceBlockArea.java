package com.cibernet.minestuckuniverse.network;

import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

import static com.mraof.minestuck.editmode.ServerEditHandler.isBlockItem;

public class PacketPlaceBlockArea extends MSUPacket
{
	BlockPos pos1;
	BlockPos pos2;
	Vec3d hitVec;
	EnumFacing face;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt(((BlockPos)args[0]).getX());
		data.writeInt(((BlockPos)args[0]).getY());
		data.writeInt(((BlockPos)args[0]).getZ());
		data.writeInt(((BlockPos)args[1]).getX());
		data.writeInt(((BlockPos)args[1]).getY());
		data.writeInt(((BlockPos)args[1]).getZ());
		data.writeDouble(((Vec3d)args[2]).x);
		data.writeDouble(((Vec3d)args[2]).y);
		data.writeDouble(((Vec3d)args[2]).z);
		data.writeInt(((EnumFacing)args[3]).ordinal());

		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		pos1 = new BlockPos(data.readInt(), data.readInt(), data.readInt());
		pos2 = new BlockPos(data.readInt(), data.readInt(), data.readInt());
		hitVec = new Vec3d(data.readDouble(), data.readDouble(), data.readDouble());
		face = EnumFacing.values()[data.readInt()];
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		EnumHand hand = player.getHeldItemMainhand().getItem() instanceof ItemBlock ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
		ItemStack stack = player.getHeldItem(hand);

		if(!(stack.getItem() instanceof ItemBlock))
			return;

		float f = (float)(hitVec.x - (double)pos1.getX());
		float f1 = (float)(hitVec.y - (double)pos1.getY());
		float f2 = (float)(hitVec.z - (double)pos1.getZ());



		boolean swingArm = false;
		for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++)
			for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++)
				for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++)
				{
					if(stack.isEmpty())
						return;



					int c = stack.getCount();
					BlockPos pos = new BlockPos(x, y, z);
					if(player.world.getBlockState(pos).getBlock().isReplaceable(player.world, pos) && editModePlaceCheck(player.world, player, hand) && stack.onItemUse(player, player.world, pos, hand, face, f, f1, f2) == EnumActionResult.SUCCESS)
					{
						if(player.isCreative())
							stack.setCount(c);
						swingArm = true;
					}
				}

		if(swingArm)
			player.swingArm(hand);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}
	
	private static boolean editModePlaceCheck(World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = ObfuscationReflectionHelper.getPrivateValue(EditData.class, data, "connection");

			ItemStack stack = player.getHeldItemMainhand();

			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || hand.equals(EnumHand.OFF_HAND))
				return false;

			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, connection);
			if(entry != null)
			{
				GristSet cost = connection.givenItems()[DeployList.getOrdinal(entry.getName())]
						? entry.getSecondaryGristCost(connection) : entry.getPrimaryGristCost(connection);
				if(!GristHelper.canAfford(MinestuckPlayerData.getGristSet(connection.getClientIdentifier()), cost))
				{
					StringBuilder str = new StringBuilder();
					if(cost != null)
					{
						for(GristAmount grist : cost.getArray())
						{
							if(cost.getArray().indexOf(grist) != 0)
								str.append(", ");
							str.append(grist.getAmount()+" "+grist.getType().getDisplayName());
						}
						player.sendStatusMessage(new TextComponentTranslation("grist.missing",str.toString()), true);
					}
					return false;
				}
			}
			else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(connection.getClientIdentifier(), stack, false))
				return false;
		}
		return true;
	}
}
