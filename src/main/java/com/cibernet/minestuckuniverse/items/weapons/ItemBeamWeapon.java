package com.cibernet.minestuckuniverse.items.weapons;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import com.cibernet.minestuckuniverse.items.properties.PropertyDualWield;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBeamWeapon extends MSUWeaponBase
{
	public ItemBeamWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
	}

	public ItemBeamWeapon(double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
		super(damageVsEntity, weaponSpeed, enchantability, name, unlocName);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
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

		if(!worldIn.isRemote)
		{
			Beam beam = new Beam(playerIn, stack);

			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setUniqueId("Beam", beam.getUniqueID());

			Beam.fireBeam(beam);

			playerIn.setActiveHand(handIn);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
}
