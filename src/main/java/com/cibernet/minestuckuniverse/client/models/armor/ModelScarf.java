package com.cibernet.minestuckuniverse.client.models.armor;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelScarf extends ModelBiped {
	private final ModelRenderer Scarf;
	private final ModelRenderer Tail;
	private final ModelRenderer TailSub;

	public ModelScarf() {
		textureWidth = 64;
		textureHeight = 64;

		Scarf = new ModelRenderer(this);
		Scarf.setRotationPoint(0.0F, 0.0F, 0.0F);
		Scarf.cubeList.add(new ModelBox(Scarf, 0, 10, 3.5F, -2.0F, -4.0F, 1, 2, 8, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 0, 0, -4.5F, -2.0F, -4.0F, 1, 2, 8, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 10, 14, -4.0F, -2.0F, -4.5F, 8, 2, 1, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 10, 4, -4.0F, -2.0F, 3.5F, 8, 2, 1, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 0, 3, 2.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 10, 0, -4.0F, -0.5F, 1.0F, 8, 1, 3, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 10, 10, -4.0F, -0.5F, -4.0F, 8, 1, 3, 0.0F, false));
		Scarf.cubeList.add(new ModelBox(Scarf, 0, 0, -4.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F, false));

		Tail = new ModelRenderer(this);
		TailSub = new ModelRenderer(this);
		Tail.setRotationPoint(0.0F, 0.0F, 2.0F);
		TailSub.setRotationPoint(0.0F, 0.0F, 0.0F);
		Scarf.addChild(Tail);
		Tail.addChild(TailSub);
		TailSub.cubeList.add(new ModelBox(Tail, 18, 18, -3.0F, 0.0F, -0.5F, 3, 14, 1, 0.0F, false));

		bipedHead.cubeList.remove(0);
		bipedHeadwear.cubeList.remove(0);
		bipedHead.addChild(Scarf);
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


		Tail.rotateAngleX = Math.max(isSneak ? 0.5f : 0, -bipedHead.rotateAngleX);
		TailSub.rotationPointZ = (float) Math.max(0, -bipedHead.rotateAngleX*Math.PI/2f);
		Tail.rotateAngleX += Math.max(0,
				((float) Math.sqrt(Math.pow((entityIn.posX - entityIn.prevPosX), 2) + Math.pow(Math.max(0, entityIn.posY - entityIn.prevPosY), 2) + Math.pow((entityIn.posZ - entityIn.prevPosZ), 2)) * limbSwingAmount)-
				Math.max(0, bipedHead.rotateAngleX));

		if(isSneak)
			TailSub.rotationPointZ += Math.PI/2f;

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