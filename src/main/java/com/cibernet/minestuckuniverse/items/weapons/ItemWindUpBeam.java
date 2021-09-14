package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import com.cibernet.minestuckuniverse.items.properties.PropertyDualWield;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemWindUpBeam extends MSUWeaponBase implements IBeamStats
{
	public float beamRadius;
	public float beamDamage;
	public float beamSpeed;
	public int beamHurtTime;
	public int chargeTime;
	
	protected SoundEvent chargeSound = null;
	protected SoundEvent releaseSound = null;
	
	protected ResourceLocation beamTexture = new ResourceLocation(MinestuckUniverse.MODID, "textures/entity/projectiles/beam.png");

	public ItemWindUpBeam(int maxUses, double damageVsEntity, double weaponSpeed, float beamRadius, float beamDamage, float beamSpeed, int chargeTime, int beamHurtTime, int enchantability, String name, String unlocName) {
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
		this.beamDamage = beamDamage;
		this.beamRadius = beamRadius;
		this.beamSpeed = beamSpeed;
		this.beamHurtTime = beamHurtTime;
		this.chargeTime = chargeTime;
	}

	public ItemWindUpBeam(int maxUses, double damageVsEntity, double weaponSpeed, float beamRadius, float beamDamage, float beamSpeed, int chargeTime, int enchantability, String name, String unlocName) {
		this(maxUses, damageVsEntity, weaponSpeed, beamRadius, beamDamage, beamSpeed, chargeTime,15, enchantability, name, unlocName);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean("InUse") ? EnumAction.BOW : EnumAction.NONE;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{

		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasUniqueId("Beam"))
			{
				Beam beam = worldIn.getCapability(MSUCapabilities.BEAM_DATA, null).getBeam(stack.getTagCompound().getUniqueId("Beam"));
				if(beam == null)
				{
					stack.getTagCompound().removeTag("BeamLeast");
					stack.getTagCompound().removeTag("BeamMost");
					stack.getTagCompound().setBoolean("InUse", false);
				}
			} else stack.getTagCompound().setBoolean("InUse", entityIn instanceof EntityLivingBase && ItemStack.areItemsEqualIgnoreDurability(stack, ((EntityLivingBase) entityIn).getActiveItemStack())
					&& getMaxItemUseDuration(stack) - ((EntityLivingBase) entityIn).getItemInUseCount() <= chargeTime);
		}

		if (entityIn instanceof EntityLivingBase && ItemStack.areItemsEqualIgnoreDurability(stack, ((EntityLivingBase) entityIn).getActiveItemStack())
				&& getMaxItemUseDuration(stack) - ((EntityLivingBase) entityIn).getItemInUseCount() == chargeTime)
		{
			if(!worldIn.isRemote)
			{
				Beam beam = new Beam((EntityLivingBase) entityIn, stack, beamSpeed);
				beam.setDuration(20);

				beam.damage = beamDamage;

				if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setUniqueId("Beam", beam.getUniqueID());

				beam.releaseBeam();
				Beam.fireBeam(beam);
			}

			if(releaseSound != null)
				entityIn.playSound(releaseSound, 0.7f, 1f);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);

		if(!worldIn.isRemote && entityLiving instanceof EntityPlayer)
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 20);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ActionResult<ItemStack> sup = super.onItemRightClick(worldIn, playerIn, handIn);

		if(sup.getType() != EnumActionResult.PASS)
			return sup;

		ItemStack stack = playerIn.getHeldItem(handIn);

		if(hasProperty(PropertyDualWield.class, stack) && (handIn != EnumHand.MAIN_HAND || !ItemStack.areItemsEqualIgnoreDurability(stack, playerIn.getHeldItemOffhand())))
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(chargeSound != null)
			playerIn.playSound(chargeSound, 0.7f, 1f);



		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	public ItemWindUpBeam setSounds(SoundEvent chargeSound, SoundEvent releaseSound)
	{
		this.chargeSound = chargeSound;
		this.releaseSound = releaseSound;
		return this;
	}
	
	@Override
	public float getBeamRadius(ItemStack stack) {
		return beamRadius;
	}

	@Override
	public int getBeamHurtTime(ItemStack stack) {
		return beamHurtTime;
	}


	@Override
	public ResourceLocation getBeamTexture()
	{
		return beamTexture;
	}

	@Override
	public void setBeamTexture(String fileName)
	{
		beamTexture = new ResourceLocation(getRegistryName().getResourceDomain(), "textures/entity/projectiles/"+fileName+".png");
	}

	@Override
	public void setCustomBeamTexture()
	{
		setBeamTexture(getRegistryName().getResourcePath());
	}
}
