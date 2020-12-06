package com.cibernet.minestuckuniverse.client.models.armor;//Made by Ishumire
//Paste this code into your mod.

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrogHat extends ModelBiped
{
	private final ModelRenderer froghat;

	public ModelFrogHat() {
		textureWidth = 64;
		textureHeight = 64;

		froghat = new ModelRenderer(this);
		froghat.setRotationPoint(0.0F, 0.0F, 0.0F);
		froghat.cubeList.add(new ModelBox(froghat, 7, 16, -4.0F, -11.0F, -5.5F, 3, 3, 2, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 24, 0, 1.0F, -11.0F, -5.5F, 3, 3, 2, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 0, -1.0F, -10.0F, -5.0F, 2, 2, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 16, -5.0F, -8.5F, -5.0F, 1, 8, 5, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 20, 9, -5.0F, -8.5F, 0.0F, 1, 6, 4, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 4, 4, -4.5F, -0.5F, -5.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 15, -4.5F, -0.5F, -3.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 4, 3, -4.5F, -0.5F, -1.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 12, 16, 4.0F, -8.5F, -5.0F, 1, 8, 5, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 24, 25, 4.0F, -8.5F, 0.0F, 1, 6, 4, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 5, 4.5F, -0.5F, -5.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 2, 5, 4.5F, -0.5F, -3.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 4, 5, 4.5F, -0.5F, -1.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 9, -4.5F, -8.5F, 4.0F, 9, 6, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 3, -1.5F, -2.5F, 5.0F, 3, 1, 0, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 0, -4.0F, -9.0F, -4.0F, 8, 1, 8, -0.01F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 4, -1.0F, -8.0F, -5.0F, 2, 2, 0, 0.0F, false));

		bipedHead.cubeList.remove(0);
		bipedHeadwear.cubeList.remove(0);
		bipedHead.addChild(froghat);
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

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}