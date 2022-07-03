package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStoneTablet extends MSUItemBase
{

	public ItemStoneTablet()
	{
		super("minestuck:stone_slab", "stoneTablet");
		setMaxStackSize(1);
		setCreativeTab(TabMinestuck.instance);
		addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "written"), ((stack, worldIn, entityIn) ->
		{
			NBTTagCompound nbt = stack.getTagCompound();
			return (nbt != null && nbt.hasKey("text") && !nbt.getString("text").isEmpty()) ? 1 : 0;
		}));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		BlockPos pos = playerIn.getPosition();
		playerIn.openGui(MinestuckUniverse.instance, MSUUtils.STONE_TABLET_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("text") && !nbt.getString("text").isEmpty())
			tooltip.add(TextFormatting.GRAY + I18n.format("item.stoneTablet.carved"));
	}
}
