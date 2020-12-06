package com.cibernet.minestuckuniverse.client.models;
//Made with Blockbench
//Paste this code into your mod.

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAcheron extends ModelBase {
	private final ModelRenderer Body;
	private final ModelRenderer Top;
	private final ModelRenderer Head;
	private final ModelRenderer Jaw;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Bottom;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer RightLeg;
	float scale = 4;

	public ModelAcheron() {
		textureWidth = 128;
		textureHeight = 128;
		float YOff = -24 + 24/scale;

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 24.0F + YOff, 0.0F);

		Top = new ModelRenderer(this);
		Top.setRotationPoint(0.0F, -22.0F, 0.5F);
		Body.addChild(Top);
		Top.cubeList.add(new ModelBox(Top, 0, 0, -9.0F, -18.0F, -8.5F, 18, 18, 13, 0.0F, false));

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -9.0F, -10.5F);
		Top.addChild(Head);
		Head.cubeList.add(new ModelBox(Head, 58, 58, -3.9982F, -14.9983F, -6.4965F, 9, 10, 8, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 86, 119, 1.0F, -8.5F, -6.5F, 8, 9, 0, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 81, 111, 5.075F, -8.5F, -6.625F, 0, 9, 8, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 81, 111, -4.05F, -8.5F, -6.625F, 0, 9, 8, 0.0F, true));
		Head.cubeList.add(new ModelBox(Head, 86, 119, -8.0F, -8.5F, -6.55F, 8, 9, 0, 0.0F, true));

		Jaw = new ModelRenderer(this);
		Jaw.setRotationPoint(0.0F, -4.0F, -2.0F);
		Head.addChild(Jaw);
		Jaw.cubeList.add(new ModelBox(Jaw, 0, 82, -2.9843F, -5.9983F, -3.4825F, 7, 8, 5, 0.0F, false));

		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, -9.0F, -0.5F);
		Top.addChild(RightArm);
		RightArm.cubeList.add(new ModelBox(RightArm, 62, 0, -16.0F, -9.5F, -9.0F, 8, 9, 8, 0.0F, false));
		RightArm.cubeList.add(new ModelBox(RightArm, 30, 65, -15.0F, -8.5F, -8.0F, 6, 17, 6, 0.0F, false));
		RightArm.cubeList.add(new ModelBox(RightArm, 34, 34, -16.0F, 8.0F, -9.0F, 8, 23, 8, 0.0F, false));

		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, -9.0F, -0.5F);
		Top.addChild(LeftArm);
		LeftArm.cubeList.add(new ModelBox(LeftArm, 54, 76, 9.0F, -8.5F, -8.0F, 6, 17, 6, 0.0F, false));
		LeftArm.cubeList.add(new ModelBox(LeftArm, 0, 31, 8.0F, 8.0F, -9.0F, 9, 23, 8, 0.0F, false));
		LeftArm.cubeList.add(new ModelBox(LeftArm, 58, 23, 8.0F, -9.5F, -9.0F, 9, 9, 8, 0.0F, false));

		Bottom = new ModelRenderer(this);
		Bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.addChild(Bottom);
		Bottom.cubeList.add(new ModelBox(Bottom, 0, 61, -4.5F, -27.0F, -2.0F, 9, 15, 6, 0.5F, false));

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(0.0F, 0.0F, 3.0F);
		Bottom.addChild(LeftLeg);
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 66, 40, 1.4983F, -11.9983F, -4.0F, 6, 12, 5, 0.0F, false));

		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(0.0F, 0.0F, 2.0F);
		Bottom.addChild(RightLeg);
		RightLeg.cubeList.add(new ModelBox(RightLeg, 78, 78, -6.5F, -12.0F, -3.0F, 6, 12, 5, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		f5 *= scale;
		Body.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}