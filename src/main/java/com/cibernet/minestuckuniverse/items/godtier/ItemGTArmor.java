package com.cibernet.minestuckuniverse.items.godtier;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.client.models.armor.godtier.*;
import com.cibernet.minestuckuniverse.items.armor.MSUArmorBase;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class ItemGTArmor extends MSUArmorBase
{

	public static final ItemArmor.ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("GOD_TIER", MinestuckUniverse.MODID+":blank", -1, new int[] {0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);


	@SideOnly(Side.CLIENT)
	public static final HashMap<EnumClass, ModelGTAbstract> models = new HashMap<EnumClass, ModelGTAbstract>()
	{{
		put(EnumClass.KNIGHT, new ModelGTKnight());
		put(EnumClass.HEIR, new ModelGTHeir());
		put(EnumClass.WITCH, new ModelGTWitch());
		put(EnumClass.SEER, new ModelGTSeer());
		put(EnumClass.PAGE, new ModelGTPage());
		put(EnumClass.MAGE, new ModelGTMage());
		put(EnumClass.BARD, new ModelGTBard());
		put(EnumClass.THIEF, new ModelGTThief());
		put(EnumClass.PRINCE, new ModelGTPrince());
		put(EnumClass.MAID, new ModelGTMaid());
		put(EnumClass.ROGUE, new ModelGTRogue());
		put(EnumClass.SYLPH, new ModelGTSylph());

		put(EnumClass.LORD, new ModelGTLord());
		put(EnumClass.MUSE, new ModelGTMuse());
	}};


	public ItemGTArmor(EntityEquipmentSlot equipmentSlotIn, String unlocName, String name)
	{
		super(MATERIAL, 0, equipmentSlotIn, unlocName, name);
		setCreativeTab(TabMinestuckUniverse.godTier);

		addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "hide_features"), ((stack, worldIn, entityIn) -> getHideExtras(stack) ? 1 : 0));
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String heroClass = TextFormatting.OBFUSCATED + "Class" + TextFormatting.RESET;
		String heroAspect = TextFormatting.OBFUSCATED + "Thing" + TextFormatting.RESET;

		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt.hasKey("class"))
			{
				int c = nbt.getInteger("class");
				if(c >= 0 && c < EnumClass.values().length)
					heroClass = EnumClass.getClassFromInt(c).getDisplayName();
			}
			if(nbt.hasKey("aspect"))
			{
				int a = nbt.getInteger("aspect");
				if(a >= 0 && a < EnumAspect.values().length)
					heroAspect = EnumAspect.getAspectFromInt(a).getDisplayName();
			}
		}
		return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", I18n.translateToLocalFormatted("title.format", heroClass, heroAspect)).trim();
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(getHideExtras(stack))
		{
			if((getHeroClass(stack) == (EnumClass.ROGUE) && getType().equals("hood")))
				tooltip.add(I18n.translateToLocal("item.gtHood.hiddenExtras.rogue"));
			if((getHeroClass(stack) == (EnumClass.LORD) && getType().equals("shirt")))
				tooltip.add(I18n.translateToLocal("item.gtShirt.hiddenExtras.lord"));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) 
	{
		EnumClass heroClass = getHeroClass(stack);
		
		if(heroClass == null)
			return super.getArmorTexture(stack, entity, slot, type);
		
		return MinestuckUniverse.MODID + ":textures/models/armor/god_tier/gt_" + heroClass.toString() + ".png";
	}
	
	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		EnumClass heroClass = getHeroClass(stack);
		EnumAspect heroAspect = getHeroAspect(stack);
		
		if(heroClass == null)
			return super.getArmorModel(entityLiving, stack, armorSlot, _default);
		
		ModelGTAbstract model = models.get(heroClass);
		
		model.heroAspect = heroAspect;
		
		model.head.showModel = armorSlot == EntityEquipmentSlot.HEAD;
		model.hood.showModel = armorSlot == EntityEquipmentSlot.HEAD;
		model.neck.showModel = armorSlot == EntityEquipmentSlot.HEAD;
		
		model.symbol.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		model.torso.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		model.leftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		model.rightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		model.cape.showModel = armorSlot == model.getCapeSlot();
		
		model.skirtFront.showModel = armorSlot == model.getSkirtSlot();
		model.skirtMiddle.showModel = model.skirtFront.showModel;
		model.skirtBack.showModel = model.skirtFront.showModel;
		
		model.belt.showModel = armorSlot == EntityEquipmentSlot.LEGS;
		model.leftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
		model.rightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
		
		model.leftFoot.showModel = armorSlot == EntityEquipmentSlot.FEET;
		model.rightFoot.showModel = armorSlot == EntityEquipmentSlot.FEET;
		
		model.isSneak = _default.isSneak;
		model.isRiding = _default.isRiding;
		model.isChild = _default.isChild;
		
		model.rightArmPose = _default.rightArmPose;
		model.leftArmPose = _default.leftArmPose;
		
		model.hideExtras = getHideExtras(stack);
		
		model.addExtraInfo(entityLiving, stack, armorSlot);
		
		return model;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(playerIn.isSneaking() && ((getHeroClass(stack) == (EnumClass.ROGUE) && getType().equals("hood")) || (getHeroClass(stack) == (EnumClass.LORD) && getType().equals("shirt"))))
		{
			setHideExtras(stack, !getHideExtras(stack));
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		else return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public String getType()
	{
		switch (getEquipmentSlot())
		{
			case HEAD: return "hood";
			case CHEST: return "shirt";
			case LEGS: return "pants";
			default: return "shoes";
		}
	}
	
	public static EnumClass getHeroClass(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("class"))
			return null;
		
		int c = nbt.getInteger("class");
		if(c >= 0 && c < EnumClass.values().length)
			return EnumClass.getClassFromInt(c);
		return null;
	}
	
	public static boolean getHideExtras(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("hideExtras"))
			return false;
		
		return nbt.getBoolean("hideExtras");
	}
	public static void setHideExtras(ItemStack stack, boolean v)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = stack.getTagCompound();
		
		nbt.setBoolean("hideExtras", v);
	}
	
	public static EnumAspect getHeroAspect(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("aspect"))
			return null;
		
		int a = nbt.getInteger("aspect");
		if(a >= 0 && a < EnumAspect.values().length)
			return EnumAspect.getAspectFromInt(a);
		return null;
	}
}
