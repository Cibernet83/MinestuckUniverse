package com.cibernet.minestuckuniverse.items.properties;

import com.mraof.minestuck.util.Teleport;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PropertyTeleBlock extends WeaponProperty
{
	Block targetBlock;
	int radius;

	public PropertyTeleBlock(Block targetBlock, int radius)
	{
		this.targetBlock = targetBlock;
		this.radius = radius;
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		for(float x = 0; x < radius; x = (x*-1)- (((x == 0) ? 1f : Math.signum(x))*0.5f))
		for(float y = 0; y < radius; y = (y*-1)- (((y == 0) ? 1f : Math.signum(y))*0.5f))
			for(float z = 0; z < radius; z = (z*-1)- (((z == 0) ? 1f : Math.signum(z))*0.5f))
			{
				BlockPos pos = new BlockPos(x + player.posX, y + player.posY, z + player.posZ);
				if(player.world.getBlockState(pos).getBlock() == targetBlock)
				{
					target.motionX = 0;
					target.motionY = 0;
					target.motionZ = 0;
					Teleport.localTeleport(target, null, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
					return super.onCrit(stack, player, target, damageModifier);
				}
			}
		return super.onCrit(stack, player, target, damageModifier);
	}
}
