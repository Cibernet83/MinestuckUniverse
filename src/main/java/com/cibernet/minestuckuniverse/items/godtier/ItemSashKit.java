package com.cibernet.minestuckuniverse.items.godtier;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSashKit extends MSUItemBase
{
	public ItemSashKit(String unlocName, String name)
	{
		super(name, unlocName);
		setMaxStackSize(1);
		setCreativeTab(TabMinestuckUniverse.godTier);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(playerIn.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier())
		{
			BlockPos pos = playerIn.getPosition();
			if(worldIn.isRemote)
				playerIn.openGui(MinestuckUniverse.instance, MSUUtils.GOD_TIER_SASH_UI, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
