package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.entity.EntityRock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBigRock extends MSUItemBase
{
	public ItemBigRock(String name, String unlocName)
	{
		super(name, unlocName);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(facing == EnumFacing.UP)
		{
			if(!worldIn.isRemote)
			{
				EntityRock rock = new EntityRock(worldIn);
				pos = pos.offset(EnumFacing.UP);
				rock.setPosition(pos.getX(), pos.getY(), pos.getZ());

				if(!rock.isEntityInsideOpaqueBlock())
					worldIn.spawnEntity(rock);
			}

			if(!player.isCreative())
				player.getHeldItem(hand).shrink(1);

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;

	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if(!entityItem.isDead && !entityItem.world.isRemote)
		{
			EntityRock rock = new EntityRock(entityItem.world);
			rock.setPosition(entityItem.posX, entityItem.posY, entityItem.posZ);

			if(entityItem.getThrower() != null)
			{
				rock.motionX = entityItem.motionX;
				rock.motionY = entityItem.motionY;
				rock.motionZ = entityItem.motionZ;
			}

			entityItem.world.spawnEntity(rock);
			entityItem.setDead();
		}


		return super.onEntityItemUpdate(entityItem);
	}
}
