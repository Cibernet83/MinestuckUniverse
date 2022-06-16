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

public class ModelGTMuse extends ModelGTAbstract {
	private final ModelRenderer hood;
	private final ModelRenderer neckLeft;
	private final ModelRenderer neckRight;

	public ModelGTMuse()
	{
		super(128, 128, EnumClass.MUSE);
		addColorIgnores(6, 7, 8);
		textureWidth = 128;
		textureHeight = 128;

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -5.0F, -10.0F+0.2f, 4.0F, 10, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 110, 10, -4.0F, -9.5F, 5.0F, 8, 8, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 108, 19, -4.0F, -10.0F, 5.0F, 8, 8, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 30, -3.0F, -9.5F, 7.5F, 6, 7, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 112, 38, -3.0F, -9.0F, 8.5F, 6, 7, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 47, -2.0F, -8.0F, 10.0F, 4, 5, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 53, -2.0F, -7.5F, 10.5F, 4, 5, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 60, -1.0F, -6.0F, 12.0F, 2, 2, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 63, -1.0F, -5.5F, 12.5F, 2, 2, 2, 0.0F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 102, 0, -5.0F, 0.0F, 0.0F, 10, 4, 3, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 104, 96, -4.0F, 0.25F, -2.5F, 8, 12, 4, 0.251F, false));
		neck.cubeList.add(new ModelBox(torso, 104, 80, -4.0F, -0.1F, -2.0F, 8, 12, 4, 0.2515F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 56, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 56, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 80, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 80, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		neckLeft = new ModelRenderer(this);
		neckLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
		neckLeft.cubeList.add(new ModelBox(neckLeft, 82, 0, -1.0F, -2.0F, -2.0F, 4, 3, 4, 0.75F, true));

		neckRight = new ModelRenderer(this);
		neckRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
		neckRight.cubeList.add(new ModelBox(neckRight, 82, 0, -3.0F, -2.0F, -2.0F, 4, 3, 4, 0.75F, false));

		skirtBack.setRotationPoint(0.0F, 10.0F, 2.252F);
		skirtBack.cubeList.add(new ModelBox(skirtBack, 59, 115, -4.0F, 0.0F, 0.1F, 8, 12, 0, 0.0F, false));

		skirtFront.setRotationPoint(0.0F, 10.0F, -2.252F);
		skirtFront.cubeList.add(new ModelBox(skirtFront, 77, 115, -4.0F, 0.0F, -0F, 8, 12, 0, 0.0F, false));
	}

	@Override
	public EntityEquipmentSlot getSkirtSlot() {
		return EntityEquipmentSlot.CHEST;
	}

	@Override
	public void addExtraInfo(EntityLivingBase entityLiving, ItemStack stack, EntityEquipmentSlot armorSlot)
	{
		neckLeft.showModel = armorSlot == EntityEquipmentSlot.HEAD;
		neckRight.showModel = armorSlot == EntityEquipmentSlot.HEAD;
	}

	@Override
	protected void renderExtras(float scale)
	{
		neckRight.render(scale);
		neckLeft.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		copyModelAngles(leftArm, neckLeft);
		copyModelAngles(rightArm, neckRight);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}