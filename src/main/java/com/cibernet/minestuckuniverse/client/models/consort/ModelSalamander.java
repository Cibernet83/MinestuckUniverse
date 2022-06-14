package com.cibernet.minestuckuniverse.client.models.consort;// Made with Blockbench 4.2.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSalamander extends ModelConsort {
	private final ModelRenderer upperTail;
	private final ModelRenderer lowerTail;
	private final ModelRenderer upperJaw;
	private final ModelRenderer lowerJaw;

	public ModelSalamander() {
		textureWidth = 64;
		textureHeight = 32;

		upperTail = new ModelRenderer(this);
		upperTail.setRotationPoint(0.0F, 18.0F, 3.0F);
		setRotationAngle(upperTail, 0.2231F, 0.0F, 0.0F);
		upperTail.cubeList.add(new ModelBox(upperTail, 34, 18, -1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F, false));

		lowerTail = new ModelRenderer(this);
		lowerTail.setRotationPoint(0.0F, 22.0F, 6.0F);
		lowerTail.cubeList.add(new ModelBox(lowerTail, 34, 24, -1.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F, false));

		upperJaw = new ModelRenderer(this);
		upperJaw.setRotationPoint(0.0F, 11.0F, 0.0F);
		upperJaw.cubeList.add(new ModelBox(upperJaw, 0, 11, -2.0F, -2.0F, -6.0F, 4, 2, 3, 0.0F, false));

		lowerJaw = new ModelRenderer(this);
		lowerJaw.setRotationPoint(0.0F, 13.0F, 0.0F);
		lowerJaw.cubeList.add(new ModelBox(lowerJaw, 14, 11, -2.0F, -2.0F, -6.0F, 4, 1, 3, 0.0F, false));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 12.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 10, 18, -3.0F, 0.0F, -3.0F, 6, 8, 6, 0.0F, false));

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-1.0F, 20.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 18, -2.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F, false));

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(3.0F, 20.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 25, -2.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 12.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -3.0F, -4.0F, -3.5F, 6, 4, 7, 0.0F, false));

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 6.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 26, 0, -3.0F, 0.0F, -2.0F, 6, 6, 6, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		upperTail.render(f5);
		lowerTail.render(f5);
		upperJaw.render(f5);
		lowerJaw.render(f5);
		body.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		head.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		this.head.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.upperJaw.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.upperJaw.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.lowerJaw.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.lowerJaw.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
	}
}