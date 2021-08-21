package com.cibernet.minestuckuniverse.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;


/**
 * This event is {@link Cancelable}.<br>
 * The effect will count as successful if the event is cancelled, consuming one piece of Space Salt.<br>
 **/
@Cancelable
public class SpaceSaltEffectEvent extends Event
{
	private final BlockPos blockPos;
	private final EnumFacing blockFace;
	private final World world;
	@Nullable
	private final EntityPlayer player;

	private final RayTraceResult rayTrace;

	public SpaceSaltEffectEvent(World world, EntityPlayer player, BlockPos pos, EnumFacing facing, RayTraceResult rayTrace)
	{
		this.blockFace = facing;
		this.blockPos = pos;
		this.world = world;
		this.player = player;
		this.rayTrace = rayTrace;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	@Nullable
	public EntityPlayer getPlayer() {
		return player;
	}

	public EnumFacing getBlockFace() {
		return blockFace;
	}

	public World getWorld() {
		return world;
	}

	public RayTraceResult getRayTrace() {
		return rayTrace;
	}
}
