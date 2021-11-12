package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.IPropertyShield;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MSUShieldBase extends MSUWeaponBase
{
	protected int parryTime;
	protected float parryDeflect;

	public MSUShieldBase(int maxUses, double damageVsEntity, double weaponSpeed, int parryTimeFrame, float parryDamageDeflect, int enchantability, String name, String unlocName)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
		this.parryDeflect = parryDamageDeflect;
		this.parryTime = parryTimeFrame;

		this.addPropertyOverride(new ResourceLocation("blocking"), (stack, worldIn, entityIn) -> entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack && isShield(stack, entityIn) ? 1.0F : 0.0F);

	}

	public MSUShieldBase(int maxUses, int parryTimeFrame, float parryDamageDeflect, int enchantability, String name, String unlocName)
	{
		this(maxUses, 0, 0, parryTimeFrame, parryDamageDeflect, enchantability, name, unlocName);
	}

	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
	{
		for(WeaponProperty p : getProperties(stack))
		{
			if(p instanceof IPropertyShield && !((IPropertyShield) p).isShielding(stack, entity))
				return false;
		}

		return true;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		int result = 72000;
		for(WeaponProperty p : getProperties(stack))
			result = p.getMaxItemUseDuration(stack, result);
		return result;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		for(WeaponProperty p : getProperties(stack))
		{
			EnumAction result = p.getItemUseAction(stack);
			if(result != null)
				return result;
		}

		return EnumAction.BLOCK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);

		if(result.getType() == EnumActionResult.PASS)
		{
			ItemStack itemstack = playerIn.getHeldItem(handIn);
			playerIn.setActiveHand(handIn);

			if(isParrying(itemstack))
				setParryTime(itemstack, 0);

			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}

		return result;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		entityLiving.resetActiveHand();

		if(isShield(stack, entityLiving) && (!(entityLiving instanceof EntityPlayer) || !((EntityPlayer) entityLiving).getCooldownTracker().hasCooldown(stack.getItem())))
			startParrying(stack);
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if (slot == EntityEquipmentSlot.MAINHAND)
		{
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
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		ItemStack stack = entityItem.getItem();
		if(isParrying(stack))
			setParryTime(stack, 0);

		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(isParrying(stack))
		{
			setParryTime(stack, getParryTime(stack)-1);
			if(getParryTime(stack) <= 0)
				stack.getTagCompound().setBoolean("Parried", false);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public void onHitWhileShielding(ItemStack stack, EntityLivingBase target, DamageSource source, float damage, boolean block)
	{
		for(WeaponProperty p : getProperties(stack))
			if(p instanceof IPropertyShield)
				((IPropertyShield) p).onHitWhileShielding(stack, target, source, damage, block);
	}

	public boolean onShieldParry(ItemStack stack, EntityLivingBase target, DamageSource source, float damage)
	{
		for(WeaponProperty p : getProperties(stack))
			if(p instanceof IPropertyShield && !((IPropertyShield) p).onShieldParry(stack, target, source, damage))
				return false;

		if(source.getImmediateSource() != null)
			source.getImmediateSource().attackEntityFrom(new ParryDamageSource(stack, target), damage*parryDeflect);
		stack.getTagCompound().setBoolean("Parried", true);
		return true;
	}

	public boolean isParrying(ItemStack stack)
	{
		return getParryTime(stack) > 0;
	}

	public int getParryTime(ItemStack stack)
	{
		return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getInteger("ParryTime");
	}

	public void setParryTime(ItemStack stack, int time)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("ParryTime", time);

	}

	public void startParrying(ItemStack stack)
	{
		setParryTime(stack, parryTime);
	}

	public static boolean canBlockDamageSource(EntityLivingBase entity, DamageSource damageSourceIn)
	{
		if (!damageSourceIn.isUnblockable() && entity.isActiveItemStackBlocking())
		{
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null)
			{
				Vec3d vec3d1 = entity.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(entity.posX, entity.posY, entity.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dotProduct(vec3d1) < 0.0D)
					return true;
			}
		}

		return false;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(stackA.hasTagCompound())
			stackA.getTagCompound().removeTag("ParryTime");
		if(stackB.hasTagCompound())
			stackB.getTagCompound().removeTag("ParryTime");

		return !ItemStack.areItemStacksEqual(stackA, stackB);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(!stackA.hasTagCompound())
			stackA.setTagCompound(new NBTTagCompound());
		if(!stackB.hasTagCompound())
			stackB.setTagCompound(new NBTTagCompound());

		stackB.getTagCompound().setInteger("ParryTime", 0);
		stackA.getTagCompound().setInteger("ParryTime", 0);


		return super.shouldCauseBlockBreakReset(stackA, stackB);
	}

	public static class ParryDamageSource extends EntityDamageSource
	{
		final ItemStack shield;

		public ParryDamageSource(ItemStack shield, @Nullable Entity damageSourceEntityIn)
		{
			super(MinestuckUniverse.MODID+".parry", damageSourceEntityIn);
			this.shield = shield;
		}

		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
		{
			String s = "death.attack." + this.damageType;
			String s1 = s + ".item";
			return !shield.isEmpty() && shield.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), shield.getTextComponent()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()});
		}

	}
}
