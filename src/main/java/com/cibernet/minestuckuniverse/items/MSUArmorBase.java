package com.cibernet.minestuckuniverse.items;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class MSUArmorBase extends ItemArmor
{
    private final ModelBiped model;

    public MSUArmorBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocName, String registryName, ModelBiped model)
    {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setUnlocalizedName(unlocName);
        setRegistryName(registryName);
        this.model = model;

    }

    public MSUArmorBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocName, String registryName)
    {
        this(materialIn,renderIndexIn,equipmentSlotIn,unlocName,registryName,null);
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot,
                                    ModelBiped _default)
    {
        if(model == null) return super.getArmorModel(entity, stack, slot, _default);

        if(!stack.isEmpty())
        {
            if(stack.getItem() instanceof MSUArmorBase)
            {
                ModelBiped model = this.model;

                model.bipedRightLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
                model.bipedLeftLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;

                model.bipedBody.showModel = slot == EntityEquipmentSlot.CHEST;
                model.bipedLeftArm.showModel = slot == EntityEquipmentSlot.CHEST;
                model.bipedRightArm.showModel = slot == EntityEquipmentSlot.CHEST;

                model.bipedHead.showModel = slot == EntityEquipmentSlot.HEAD;
                model.bipedHeadwear.showModel = slot == EntityEquipmentSlot.HEAD;


                model.isSneak = _default.isSneak;
                model.isRiding = _default.isRiding;
                model.isChild = _default.isChild;

                model.rightArmPose = _default.rightArmPose;
                model.leftArmPose = _default.leftArmPose;

                return model;
            }
        }

        return null;
    }
}
