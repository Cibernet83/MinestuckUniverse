package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.IEnchantableProperty;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.bowkind.IPropertyArrow;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import javax.annotation.Nullable;

public class MSUBowBase extends MSUWeaponBase
{
	protected ResourceLocation arrowTexture = new ResourceLocation("textures/entity/projectiles/arrow.png");
	protected IIsArrow arrowCheck = stack -> stack.getItem() instanceof ItemArrow;

	public int drawTime;
	public float arrowVelocity;
	public float inaccuracy;
	public float arrowDamage;
	public boolean firesCustom;

	public MSUBowBase(int maxUses, double damageVsEntity, double weaponSpeed, float arrowDamage, int drawTime, float arrowVel, float inaccuracy,  int enchantability, boolean usesArrowProperties, String name, String unlocName)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);

		this.arrowDamage = arrowDamage;
		this.drawTime = drawTime;
		this.arrowVelocity = arrowVel;
		this.inaccuracy = inaccuracy;
		this.firesCustom = usesArrowProperties;

		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{

				if (entityIn == null)
					return 0.0F;
				else return !(entityIn.getActiveItemStack().getItem() instanceof MSUBowBase) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / (float)getDrawTime(entityIn, stack);
			}
		});
		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
	}

	private int getDrawTime(EntityLivingBase entityIn, ItemStack stack)
	{
		int result = drawTime;
		for(WeaponProperty p : getProperties(stack))
			if(p instanceof IPropertyArrow)
				result = ((IPropertyArrow)p).getDrawTime(entityIn, stack, result);
		return result;
	}

	public MSUBowBase(int maxUses, float arrowDamage, int drawSpeed, float arrowVel, float inaccuracy,  int enchantability, boolean firesCustom, String name, String unlocName)
	{
		this(maxUses, 0, 0, arrowDamage, drawSpeed, arrowVel, inaccuracy, enchantability, firesCustom, name, unlocName);
	}


	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		int result = 72000;
		for(WeaponProperty p : getProperties(stack))
			result = p.getMaxItemUseDuration(stack, result);
		return result;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
		if(result.getType() != EnumActionResult.PASS)
			return result;

		ItemStack itemstack = playerIn.getHeldItem(handIn);
		boolean flag = !requiresAmmo() || !this.findAmmo(playerIn).isEmpty();

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null) return ret;

		if (!playerIn.capabilities.isCreativeMode && !flag)
			return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
		else
		{
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			boolean flag = !requiresAmmo() || entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemstack = requiresAmmo() ? this.findAmmo(entityplayer) : ItemStack.EMPTY;

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag)
			{
				float f = getArrowVelocity(entityLiving, stack, i);

				if ((double)f >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

					if (!worldIn.isRemote)
					{
						ItemArrow itemarrow = (ItemArrow)(itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW);

						ItemStack copiedStack = itemstack.copy();
						copiedStack.setCount(1);
						EntityArrow entityarrow = firesCustom ? new EntityMSUArrow(worldIn, entityplayer, copiedStack, stack) : itemarrow.createArrow(worldIn, itemstack, entityplayer);

						if (f == 1.0F)
							entityarrow.setIsCritical(true);

						entityarrow = this.customizeArrow(entityarrow, i/(float)getDrawTime(entityLiving, stack));
						if(entityarrow == null)
							return;

						entityarrow.setDamage(arrowDamage);
						entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * arrowVelocity, inaccuracy);

						int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0)
							entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);

						int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0)
							entityarrow.setKnockbackStrength(k);

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
							entityarrow.setFire(100);

						stack.damageItem(1, entityplayer);

						if ((flag1 && requiresAmmo()) || entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))
							entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

						worldIn.spawnEntity(entityarrow);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

					if (!itemstack.isEmpty() && !(!requiresAmmo() || flag1) && !entityplayer.capabilities.isCreativeMode)
					{
						itemstack.shrink(1);

						if (itemstack.isEmpty())
							entityplayer.inventory.deleteStack(itemstack);
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
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
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(enchantment.type.equals(EnumEnchantmentType.BREAKABLE))
			return !unbreakable;

		if(Enchantments.INFINITY.equals(enchantment) && !requiresAmmo())
			return false;

		for(WeaponProperty p : getProperties(stack))
			if(p instanceof IEnchantableProperty)
			{
				Boolean bool = ((IEnchantableProperty) p).canEnchantWith(stack, enchantment);
				if(bool != null)
					return bool;
			}

		if(enchantment.type.equals(EnumEnchantmentType.BOW))
			return true;

		if(getTool() == null)
			return enchantment.type.canEnchantItem(stack.getItem());

		return getTool().canEnchantWith(enchantment);
	}

	private EntityArrow customizeArrow(EntityArrow arrow, float chargeTime)
	{
		for(WeaponProperty p : getProperties())
			if(p instanceof IPropertyArrow)
				arrow = ((IPropertyArrow)p).customizeArrow(arrow, chargeTime);
		return arrow;
	}

	public float getArrowVelocity(EntityLivingBase player, ItemStack stack, int charge)
	{
		float f = (float)charge / (float) getDrawTime(player, stack);
		f = (f * f + f * 2.0F) / 3.0F;

		if (f > 1.0F)
			f = 1.0F;

		for(WeaponProperty p : getProperties(stack))
			if(p instanceof IPropertyArrow)
				f = ((IPropertyArrow)p).getArrowVelocity(stack, charge, f);

		return f;
	}

	protected ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isArrow(itemstack))
				{
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	public boolean isArrow(ItemStack stack)
	{
		return arrowCheck.check(stack);
	}

	public MSUBowBase setArrowCheck(IIsArrow check)
	{
		arrowCheck = check;
		return this;
	}

	public MSUBowBase requireNoAmmo()
	{
		arrowCheck = null;
		return this;
	}

	public boolean requiresAmmo()
	{
		return arrowCheck != null;
	}

	public ResourceLocation getArrowTexture()
	{
		return arrowTexture;
	}

	public MSUBowBase setArrowTexture(String fileName)
	{
		arrowTexture = new ResourceLocation(getRegistryName().getResourceDomain(), "textures/entity/projectiles/"+fileName+".png");
		return this;
	}

	public MSUBowBase setCustomArrowTexture()
	{
		return this.setArrowTexture(getRegistryName().getResourcePath());
	}

	public interface IIsArrow
	{
		boolean check(ItemStack stack);
	}
}
