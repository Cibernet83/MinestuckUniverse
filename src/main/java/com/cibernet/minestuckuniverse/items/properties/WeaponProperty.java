package com.cibernet.minestuckuniverse.items.properties;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WeaponProperty
{
	public String getItemStackDisplayName(ItemStack stack, String name)
	{
		return name;
	}

	public double getAttackDamage(ItemStack stack, double dmg)
	{
		return dmg;
	}

	public double getAttackSpeed(ItemStack stack, double speed)
	{
		return speed;
	}

	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{

	}

	public void onEntityItemUpdate(EntityItem entityItem)
	{

	}

	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{

	}

	public void onEmptyHit(ItemStack stack, EntityPlayer player)
	{

	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.PASS;
	}

	public int getMaxItemUseDuration(ItemStack stack, int duration) {
		return duration;
	}

	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.PASS;
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		return stack;
	}

	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return EnumActionResult.PASS;
	}

	public void onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{

	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return true;
	}

	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		return true;
	}

	public boolean compatibleWith(WeaponProperty other)
	{
		return true;
	}

	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier) {
		return damageModifier;
	}

	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		return amount;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return null;
	}

	public boolean isAbilityActive(ItemStack stack, World world, EntityLivingBase player)
	{
		return false;
	}

	public void getAttributeModifiers(EntityLivingBase player, ItemStack stack, Multimap<String, AttributeModifier> multimap) {  }

	public int onDurabilityChanged(ItemStack stack, int damage) {return damage;}

	public void onStopUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{

	}
}
