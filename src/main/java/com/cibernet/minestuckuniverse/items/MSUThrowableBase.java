package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.control.Tab;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MSUThrowableBase extends MSUItemBase implements IPropertyWeapon<MSUThrowableBase>
{

	protected int useDuration;
	protected int cooldownTime;
	protected double attackDamage;
	protected double attackSpeed;

	protected final ArrayList<WeaponProperty> properties = new ArrayList<>();

	public MSUThrowableBase(int useDuration, int cooldownTime, int stackSize, double meleeDamage, double meleeSpeed, String name, String unlocName)
	{
		super(name, unlocName);

		this.useDuration = Math.max(1, useDuration);
		this.cooldownTime = cooldownTime;
		this.attackDamage = meleeDamage;
		this.attackSpeed = meleeSpeed;

		setMaxStackSize(stackSize);
		setCreativeTab(TabMinestuckUniverse.weapons);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorProjectileDispense()
		{
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
			{
				ItemStack thrownStack = stackIn.copy();
				thrownStack.setCount(1);
				return new EntityMSUThrowable(worldIn, position.getX(), position.getY(), position.getZ(), thrownStack);
			}
		});
	}

	public MSUThrowableBase(int useDuration, int cooldownTime, int stackSize, String name, String unlocName)
	{
		this(useDuration, cooldownTime, stackSize, 0, 0, name, unlocName);
	}

	//Throwable Material
	public MSUThrowableBase(String name, String unlocName)
	{
		this(1, 0, 64, 0, 0, name, unlocName);
		setCreativeTab(TabMinestuckUniverse.main);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		int result = useDuration;
		for(WeaponProperty p : getProperties(stack))
			result = p.getMaxItemUseDuration(stack, result);
		return result;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{

		for (WeaponProperty p : getProperties(playerIn.getHeldItem(handIn)))
		{
			EnumActionResult actionResult = p.onItemRightClick(worldIn, playerIn, handIn);
			if(actionResult != EnumActionResult.PASS)
				return ActionResult.newResult(actionResult, playerIn.getHeldItem(handIn));
		}

		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		for (WeaponProperty p : getProperties(stack))
			stack = p.onItemUseFinish(stack, worldIn, entityLiving);

		if (!worldIn.isRemote)
		{
			ItemStack thrownStack = stack.copy();
			thrownStack.setCount(1);

			EntityMSUThrowable proj = new EntityMSUThrowable(worldIn, entityLiving, thrownStack);
			proj.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, 1.5F, 1.0F);
			worldIn.spawnEntity(proj);
		}

		if (!(entityLiving instanceof EntityPlayer) || !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
			stack.shrink(1);
		worldIn.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(entityLiving instanceof EntityPlayer)
		{
			if(cooldownTime > 0)
				((EntityPlayer)entityLiving).getCooldownTracker().setCooldown(this, cooldownTime);
			((EntityPlayer)entityLiving).addStat(StatList.getObjectUseStats(this));
		}

		return stack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String name = super.getItemStackDisplayName(stack);
		for(WeaponProperty p : getProperties(stack))
			name = p.getItemStackDisplayName(stack, name);
		return name;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		for (WeaponProperty p : getProperties(player.getHeldItem(hand)))
		{
			EnumActionResult actionResult = p.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			if(actionResult != EnumActionResult.PASS)
				return actionResult;
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		for (WeaponProperty p : getProperties(player.getHeldItem(hand)))
		{
			EnumActionResult actionResult = p.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
			if(actionResult != EnumActionResult.PASS)
				return actionResult;
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		getProperties(entityItem.getItem()).forEach(p -> p.onEntityItemUpdate(entityItem));
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		getProperties(stack).forEach(p -> p.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected));
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		for(WeaponProperty p : getProperties(stack))
			p.onEntityHit(stack, target, player);


		return true;
	}


	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		for (WeaponProperty p : getProperties(oldStack))
			if(!p.shouldCauseBlockBreakReset(oldStack, newStack))
				return false;

		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		for (WeaponProperty p : getProperties(oldStack))
			if(!p.shouldCauseReequipAnimation(oldStack, newStack, slotChanged))
				return false;

		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		int dmg = 1;
		if(!canHarvestBlock(state))
			dmg = 2;
		if((double)state.getBlockHardness(worldIn, pos) == 0.0D)
			dmg = 0;

		stack.damageItem(dmg, entityLiving);

		getProperties(stack).forEach(p -> p.onBlockDestroyed(stack, worldIn, state, pos, entityLiving));

		return true;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
	{
		return oldStack.isItemEqualIgnoreDurability(newStack);
	}


	public double getAttackDamage(ItemStack stack)
	{
		double dmg = attackDamage;

		for(WeaponProperty p : getProperties(stack))
			dmg = p.getAttackDamage(stack,dmg);

		return dmg;
	}

	public double getAttackSpeed(ItemStack stack)
	{
		double spd = attackSpeed;

		for(WeaponProperty p : getProperties(stack))
			spd = p.getAttackSpeed(stack, spd);

		return spd;
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if (slot == EntityEquipmentSlot.MAINHAND) {
			double dmg = this.getAttackDamage(stack);
			double spd = this.getAttackSpeed(stack);

			if(dmg != 0)
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", dmg, 0));
			if(spd != 0)
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", spd, 0));
		}

		return multimap;
	}

	@Override
	public List<WeaponProperty> getProperties() {
		return properties;
	}
}
