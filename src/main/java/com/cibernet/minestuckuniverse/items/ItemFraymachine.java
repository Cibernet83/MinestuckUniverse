package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.WorldServer;

public class ItemFraymachine extends MSUItemBlock
{
	public ItemFraymachine() {
		super(MinestuckUniverseBlocks.fraymachine, "abilitechnosynth");
	}

	public static boolean isAvailable(SburbConnection sburbConnection)
	{
		if(sburbConnection.getClientIdentifier() == null || sburbConnection.getServerIdentifier() == null || sburbConnection.getServerIdentifier().getPlayer() == null)
			return false;
		EntityPlayer player = MSUUtils.getOfflinePlayer((WorldServer) sburbConnection.getServerIdentifier().getPlayer().world, sburbConnection.getClientIdentifier());
		return player != null && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).getAllAbilitechs().size() > 0;
	}
}
