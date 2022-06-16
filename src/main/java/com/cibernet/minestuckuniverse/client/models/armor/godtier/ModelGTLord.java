package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelGTLord extends ModelGTAbstract {
	private final ModelRenderer coatRight;
	private final ModelRenderer coatLeft;
	private final ModelRenderer leftSleeve;
	private final ModelRenderer rightSleeve;
	private final ModelRenderer coat;
	private final ModelRenderer hood;
	private final ModelRenderer suspenders;

	public ModelGTLord()
	{
		super(128, 128, EnumClass.LORD);
		addColorIgnores(4, 7, 8);

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 110, 10, -4.0F, -9.0F, 5.0F, 8, 8, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 108, 19, -4.0F, -8.5F, 5.5F, 8, 8, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 29, -3.0F, -6.5F, 7.0F, 6, 6, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 112, 36, -3.0F, -6.0F, 7.5F, 6, 6, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 44, -2.0F, -4.0F, 9.0F, 4, 4, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 49, -2.0F, -3.5F, 9.5F, 4, 4, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 55, -1.0F, -1.5F, 11.0F, 2, 2, 1, 0.5F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 32, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		coatRight = new ModelRenderer(this);
		coatRight.setRotationPoint(-2.0F, 12.0F, 0.0F);
		coatRight.cubeList.add(new ModelBox(coatRight, 96, 93, -2.0F, 0.0F, -2.0F, 4, 10, 4, 0.5F, true));

		coatLeft = new ModelRenderer(this);
		coatLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
		coatLeft.cubeList.add(new ModelBox(coatLeft, 112, 93, -2.0F, 0.0F, -2.0F, 4, 10, 4, 0.5F, true));

		rightSleeve = new ModelRenderer(this);
		rightSleeve.setRotationPoint(-5.5F, 2.0F, 0.0F);
		rightSleeve.cubeList.add(new ModelBox(rightSleeve, 84, 115, -3.5F, -2.0F, -2.0F, 4, 9, 4, 0.5F, true));
		rightSleeve.cubeList.add(new ModelBox(rightSleeve, 84, 110, -3.5F, 7.0F, -2.0F, 4, 1, 4, 0.75F, true));

		leftSleeve = new ModelRenderer(this);
		leftSleeve.setRotationPoint(5.5F, 2.0F, 0.0F);
		leftSleeve.cubeList.add(new ModelBox(leftSleeve, 84, 115, -0.5F, -2.0F, -2.0F, 4, 9, 4, 0.5F, false));
		leftSleeve.cubeList.add(new ModelBox(leftSleeve, 84, 110, -0.5F, 7.0F, -2.0F, 4, 1, 4, 0.75F, false));

		coat = new ModelRenderer(this);
		coat.setRotationPoint(0.0F, 0.0F, 0.0F);
		coat.cubeList.add(new ModelBox(coat, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.5F, false));

		suspenders = new ModelRenderer(this);
		suspenders.setRotationPoint(0.0F, 0.0F, 0.0F);
		suspenders.cubeList.add(new ModelBox(suspenders, 56, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.375F, false));
	}

	@Override
	public void addExtraInfo(EntityLivingBase entityLiving, ItemStack stack, EntityEquipmentSlot armorSlot)
	{
		coat.showModel = armorSlot == EntityEquipmentSlot.CHEST && !hideExtras;
		suspenders.showModel = armorSlot == EntityEquipmentSlot.LEGS && !hideExtras;
		coatLeft.showModel = armorSlot == EntityEquipmentSlot.CHEST && !hideExtras;
		coatRight.showModel = armorSlot == EntityEquipmentSlot.CHEST && !hideExtras;
		leftSleeve.showModel = armorSlot == EntityEquipmentSlot.CHEST && !hideExtras;
		rightSleeve.showModel = armorSlot == EntityEquipmentSlot.CHEST && !hideExtras;
	}

	@Override
	protected void renderExtras(float scale)
	{
		coatRight.render(scale);
		coatLeft.render(scale);
		coat.render(scale);
		leftSleeve.render(scale);
		rightSleeve.render(scale);
		suspenders.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		copyModelAngles(torso, suspenders);
		copyModelAngles(torso, coat);
		copyModelAngles(leftArm, leftSleeve);
		copyModelAngles(rightArm, rightSleeve);
		copyModelAngles(leftLeg, coatLeft);
		copyModelAngles(rightLeg, coatRight);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}