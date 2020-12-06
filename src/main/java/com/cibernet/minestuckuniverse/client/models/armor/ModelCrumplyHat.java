package com.cibernet.minestuckuniverse.client.models.armor;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelCrumplyHat extends ModelBiped {
	private final ModelRenderer Head;
	private final ModelRenderer msCrumpled;
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r5;
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r6;
	private final ModelRenderer bone5;
	private final ModelRenderer cube_r7;

	public ModelCrumplyHat() {
		textureWidth = 64;
		textureHeight = 64;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);

		msCrumpled = new ModelRenderer(this);
		msCrumpled.setRotationPoint(0.0F, -8.0F, 0.0F);
		Head.addChild(msCrumpled);
		msCrumpled.cubeList.add(new ModelBox(msCrumpled, 0, 55, -4.0F, 0.0F, -4.0F, 8, 0, 8, 0.25F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(-2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone);


		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(3.5F, 0.0F, 0.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.3491F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 15, -1.5F, -4.0F, -3.0F, 2, 3, 6, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3491F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 24, -1.0F, -2.0F, -3.0F, 1, 2, 6, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 0.0F, 3.0F);
		msCrumpled.addChild(bone2);


		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0873F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 24, 10, -2.0F, -5.0F, -0.5F, 4, 5, 1, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, -6.0F);
		bone2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0873F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 10, 15, -2.0F, -5.0F, 0.0F, 4, 5, 1, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone3);


		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, -0.3491F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 18, 2, 0.0F, -2.0F, -3.0F, 1, 2, 6, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone4);


		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-3.5F, -1.0F, 0.0F);
		bone4.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, -0.2182F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 16, 16, -0.5F, -4.0F, -3.0F, 1, 3, 6, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone5);


		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(-2.0F, -5.0F, 0.0F);
		bone5.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.0F, 0.1745F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 8, -2.5F, -0.5F, -3.25F, 6, 1, 6, 0.5F, false));

		bipedHead.cubeList.remove(0);
		bipedHeadwear.cubeList.remove(0);
		bipedHead.addChild(Head);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if (entityIn instanceof EntityArmorStand)
		{
			EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
			bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
			bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
			bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
			bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
			bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
			bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
			bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
			bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
			bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
			bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
			bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
			bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
			bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
			bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
			bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
			bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
			bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
			bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
			copyModelAngles(bipedHead, bipedHeadwear);
		}
	}
}