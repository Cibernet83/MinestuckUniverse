package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.shieldkind.IPropertyShield;
import com.cibernet.minestuckuniverse.items.weapons.MSUShieldBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

public class PropertyEdible extends WeaponProperty implements IPropertyShield
{
	int amount;
	float saturation;
	int damageTaken;
	ArrayList<IConsumableEffect> effects = new ArrayList<>();

	public PropertyEdible(int amount, float saturation, int damageTaken, IConsumableEffect... effects)
	{
		this.amount = amount;
		this.saturation = saturation;
		this.damageTaken = damageTaken;
		this.effects.addAll(Arrays.asList(effects));
	}
	public PropertyEdible(int amount, float saturation, int damageTaken)
	{
		this(amount, saturation, damageTaken, ((stack, player) -> {}));
	}

	public PropertyEdible setPotionEffect(float chance, PotionEffect... effects)
	{

		this.effects.add ((stack, player) ->
		{
			if(!player.world.isRemote && player.world.rand.nextFloat() <= chance)
			{
				PotionEffect potion = effects[player.world.rand.nextInt(effects.length)];
				player.addPotionEffect(new PotionEffect(potion.getPotion(), potion.getDuration(), potion.getAmplifier(), potion.getIsAmbient(), potion.doesShowParticles()));
			}
		});
		return this;
	}

	@Override
	public boolean isShielding(ItemStack stack, EntityLivingBase player)
	{
		return !stack.hasTagCompound() || !stack.getTagCompound().getBoolean("Eat");
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if(stack.getItem() instanceof MSUShieldBase && stack.hasTagCompound() && !stack.getTagCompound().getBoolean("Eat"))
			return super.getItemUseAction(stack);
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack, int duration)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("Eat"))
			return 32;
		return Math.max(duration, 32);
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		if(stack.getItem() instanceof MSUShieldBase || stack.getItem().isShield(stack, playerIn))
		{
			boolean shouldEat = playerIn.isSneaking() && playerIn.getFoodStats().needFood();

			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("Eat", shouldEat);

			if(shouldEat)
			{
				playerIn.setActiveHand(handIn);
				return EnumActionResult.SUCCESS;
			}

			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		playerIn.setActiveHand(handIn);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(stack.getItem().isShield(stack, entityLiving) && !stack.getTagCompound().getBoolean("Eat"))
			return stack;

		stack.damageItem(this.damageTaken, entityLiving);
		for(IConsumableEffect effect : effects)
			effect.consume(stack, entityLiving);
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			entityplayer.getFoodStats().addStats(this.amount, this.saturation);
			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		}

		return stack;
	}

	interface IConsumableEffect
	{
		void consume(ItemStack stack, EntityLivingBase player);
	}
}
