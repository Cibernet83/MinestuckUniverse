package com.cibernet.minestuckuniverse.modSupport;

import melonslise.locks.common.capability.ILockableStorage;
import melonslise.locks.common.init.LocksCapabilities;
import melonslise.locks.common.init.LocksSoundEvents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class LocksSupport {
	public static void useKey(World world, BlockPos pos, EntityPlayer player)
	{
		ILockableStorage lockables = world.getCapability(LocksCapabilities.LOCKABLES, null);
		List<Lockable> matching = lockables.get().values().stream().filter(lockable1 -> lockable1.box.intersects(pos)).collect(Collectors.toList());
		if(matching.isEmpty())
		{
			return;
		}
		world.playSound(player, pos, LocksSoundEvents.LOCK_OPEN, SoundCategory.BLOCKS, 1F, 1F);
		if(!world.isRemote)
			for(Lockable lockable : matching)
				if(lockable.lock.isLocked() || player.isSneaking())
					lockable.lock.setLocked(!lockable.lock.isLocked());
	}
}
