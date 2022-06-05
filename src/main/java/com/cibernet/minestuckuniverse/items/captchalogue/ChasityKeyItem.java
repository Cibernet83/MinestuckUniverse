package com.cibernet.minestuckuniverse.items.captchalogue;

import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChasityKeyItem extends CruxiteItem
{
	public ChasityKeyItem(String name)
	{
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE));
		return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		entityItem.setEntityInvulnerable(true);
		if(entityItem.ticksExisted > 600 && !entityItem.isGlowing())
			entityItem.setGlowing(true);
		
		if(entityItem.posY < 1)
		{
			
			BlockPos safePos = entityItem.world.getTopSolidOrLiquidBlock(entityItem.getPosition()).up();
			entityItem.setPosition(safePos.getX(), safePos.getY(), safePos.getZ());
			entityItem.motionY = entityItem.world.rand.nextDouble()*1.2;
			
			ItemStack key = entityItem.getItem();
			if(key.hasTagCompound() && key.getTagCompound().hasUniqueId("PlayerUUID")
					&& entityItem.world.getPlayerEntityByUUID(key.getTagCompound().getUniqueId("PlayerUUID")) != null)
			{
				EntityPlayer player = entityItem.world.getPlayerEntityByUUID(key.getTagCompound().getUniqueId("PlayerUUID"));
				
				entityItem.motionX = Math.signum(player.posX - entityItem.posX) * 0.05;
				entityItem.motionZ = Math.signum(player.posZ - entityItem.posZ) * 0.05;
				/*
				if(!entityItem.world.isRemote)
					player.sendStatusMessage(new TextComponentTranslation("status.chasityVoid"), true);
				entityItem.setDefaultPickupDelay();
				entityItem.setPosition(player.posX, player.posY+1, player.posZ);
				entityItem.motionX = entityItem.world.rand.nextDouble() - 0.5D;
				entityItem.motionZ = entityItem.world.rand.nextDouble() - 0.5D;
				*/
			}
			else
			{
			}
			
		}
		
		return super.onEntityItemUpdate(entityItem);
	}
}
