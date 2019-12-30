package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityParadoxTransportalizer extends TileEntity implements ITickable
{
	protected int textCooldown = 0;
	
	
	public void teleport(World world, BlockPos pos, Entity entity)
	{
			WorldServer spawnWorld = entity.getServer().getWorld(0);
			BlockPos spawnPos = spawnWorld.getTopSolidOrLiquidBlock(spawnWorld.getSpawnPoint());
			if(spawnPos.equals(pos.up()))
				return;
			
			if(world.isBlockPowered(pos))
			{
				if(entity instanceof EntityPlayerMP && textCooldown <= 0)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.transportalizerDisabled", new Object[0]));
				textCooldown = 2;
				return;
			}
			
			if(world.getBlockState(pos.up()).getMaterial().blocksMovement() && world.getBlockState(pos.up()).getMaterial().blocksMovement())
			{
				if(entity instanceof EntityPlayerMP && textCooldown <= 0)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.blocked", new Object[0]));
				textCooldown = 2;
				return;
			}
			
			int[] bannedDims = MinestuckConfig.forbiddenDimensionsTpz;
			for(int dim : bannedDims)
				if(dim == world.provider.getDimension())
				{
					if(entity instanceof EntityPlayerMP && textCooldown <= 0)
						entity.sendMessage(new TextComponentTranslation("message.transportalizer.forbidden", new Object[0]));
					textCooldown = 2;
					return;
				}
		
		
			entity.timeUntilPortal = entity.getPortalCooldown();
			Teleport.teleportEntity(entity, 0, null, spawnPos);
		
	}
	
	@Override
	public void update()
	{
		if(textCooldown > 0)
			textCooldown--;
	}
}
