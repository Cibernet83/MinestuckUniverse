package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSound extends MSUItemBase
{
	protected SoundEvent sound;
	public ItemSound(String name, String unloc, SoundEvent sound)
	{
		super(name, unloc);
		this.sound = sound;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
	{
		player.world.playSound(player, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.5F, 1.0F);
		player.swingArm(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
	}
}
