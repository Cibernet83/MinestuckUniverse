package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemSkaianScroll extends MSUItemBase
{
	public ItemSkaianScroll()
	{
		super("skaian_scroll", "skaianScroll");
		setMaxStackSize(1);
	}

	public static int getColor(ItemStack stack)
	{
		return hasSkill(stack) ? getSkill(stack).getColor() : -1;
	}

	public static ItemStack storeRandomSkill(ItemStack stack, Random rand)
	{
		return storeSkill(stack, MSUSkills.REGISTRY.getValues().get((int) ((MSUSkills.REGISTRY.getValues().size()-1)*rand.nextFloat())));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		Skill skill = getSkill(stack);
		if(skill != null)
		{
			tooltip.add(I18n.format(getUnlocalizedName()+".tooltip.skill", skill.getDisplayName()));
			IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			if(data.hasSkill(skill))
				tooltip.add(I18n.format(getUnlocalizedName()+".tooltip.hasSkill"));
			else if(MSUConfig.skaiaScrollLimit >= 0 && !isSuperScroll(stack))
				tooltip.add(I18n.format(getUnlocalizedName()+".tooltip." + (data.getScrollsUsed() < MSUConfig.skaiaScrollLimit ? "skillsLeft" : "noSkills"), MSUConfig.skaiaScrollLimit-data.getScrollsUsed()));
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return isSuperScroll(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		Skill skill = getSkill(stack);

		if(skill != null)
		{
			IGodTierData data = playerIn.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			if(!data.hasSkill(skill) && (isSuperScroll(stack) || MSUConfig.skaiaScrollLimit < 0 || data.getScrollsUsed() < MSUConfig.skaiaScrollLimit))
			{
				data.addSkill(skill, false);
				if(!isSuperScroll(stack))
					data.addScrollsUsed();
				data.update();

				stack.shrink(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}

		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public static ItemStack setSuperScroll(ItemStack stack, boolean v)
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setBoolean("Super", v);
		return stack;
	}

	public static boolean isSuperScroll(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Super");
	}

	public static ItemStack storeSkill(ItemStack stack, Skill skill)
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("Skill", skill.getRegistryName().toString());
		return stack;
	}

	public static boolean hasSkill(ItemStack stack)
	{
		return getSkill(stack) != null;
	}

	public static Skill getSkill(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("Skill") ? MSUSkills.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("Skill"))) : null;
	}
}