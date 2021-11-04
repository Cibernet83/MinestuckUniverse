package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.entity.EntityUnrealAir;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemUnrealAir extends MSUItemBase
{
	public ItemUnrealAir(String name, String unlocName) {
		super(name, unlocName);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
	{
		ItemStack stack = player.getHeldItem(handIn);

		if(!worldIn.isRemote)
		{
			EntityUnrealAir board = new EntityUnrealAir(worldIn);
			board.setPositionAndRotation(player.posX, player.posY, player.posZ, worldIn.rand.nextFloat()*360, worldIn.rand.nextFloat()*-45f -45f);
			worldIn.spawnEntity(board);
		}

		if(!player.isCreative())
			stack.shrink(1);


		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			EntityUnrealAir board = new EntityUnrealAir(worldIn);
			pos = pos.offset(facing);
			board.setPositionAndRotation(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, worldIn.rand.nextFloat()*360, worldIn.rand.nextFloat()*-45f -45f);
			worldIn.spawnEntity(board);
		}

		if(!player.isCreative())
			player.getHeldItem(hand).shrink(1);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		Vec3d motionVec = new Vec3d(entityItem.motionX, entityItem.motionY, entityItem.motionZ).normalize().scale(0.2f);

		entityItem.setNoGravity(true);
		entityItem.motionX = motionVec.x;
		entityItem.motionY = motionVec.y;
		entityItem.motionZ = motionVec.z;

		return super.onEntityItemUpdate(entityItem);
	}
}
