// Date: 1/8/2017 8:40:00 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX
package com.cibernet.minestuckuniverse.client.models.consort;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelTurtle extends ModelConsort
{
	ModelRenderer tail;
	ModelRenderer shell;
	ModelRenderer shellRim;
	ModelRenderer nose;

	public ModelTurtle()
	{
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2F, -4F, -4F, 4, 4, 5);
		head.setRotationPoint(-0.5F, 12F, 1F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		leftLeg = new ModelRenderer(this, 32, 0);
		leftLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
		leftLeg.setRotationPoint(1F, 20F, 1F);
		leftLeg.setTextureSize(64, 64);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
		rightLeg = new ModelRenderer(this, 32, 6);
		rightLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
		rightLeg.setRotationPoint(-2F, 20F, 1F);
		rightLeg.setTextureSize(64, 64);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		tail = new ModelRenderer(this, 48, 0);
		tail.addBox(-1F, -1F, 0F, 3, 4, 1);
		tail.setRotationPoint(-1F, 20F, 2F);
		tail.setTextureSize(64, 64);
		tail.mirror = true;
		setRotation(tail, 0.2974289F, 0F, 0F);
		body = new ModelRenderer(this, 0, 52);
		body.addBox(-3F, -1F, -2F, 5, 4, 8);
		body.setRotationPoint(0F, 18F, -1F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.570796F, 0F, 0F);
		shell = new ModelRenderer(this, 0, 18);
		shell.addBox(-2F, -1F, -4F, 5, 2, 7);
		shell.setRotationPoint(-1F, 15F, 4F);
		shell.setTextureSize(64, 64);
		shell.mirror = true;
		setRotation(shell, 1.570796F, 0F, 0F);
		shellRim = new ModelRenderer(this, 0, 34);
		shellRim.addBox(-3F, -1F, -3F, 6, 1, 8);
		shellRim.setRotationPoint(-0.5F, 16.5F, 3F);
		shellRim.setTextureSize(64, 64);
		shellRim.mirror = true;
		setRotation(shellRim, 1.570796F, 0F, 0F);
		nose = new ModelRenderer(this, 48, 5);
		nose.addBox(-1F, -1F, -1F, 3, 2, 1);
		nose.setRotationPoint(-0.6F, -1.5F, -4F);
		nose.setTextureSize(64, 64);
		nose.mirror = true;
		setRotation(nose, 0F, 0F, 0F);
		head.addChild(nose);

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 6.0F, -1.5F);
		//head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 136, 96, -3.0F, 0.0F, -2.0F, 6, 6, 6, 0.0F, false));
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
					   float headPitch, float scale)
	{
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		head.render(scale);
		leftLeg.render(scale);
		rightLeg.render(scale);
		tail.render(scale);
		body.render(scale);
		shell.render(scale);
		shellRim.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		float angleY = netHeadYaw / (180F / (float) Math.PI);
		float angleX = headPitch / (180F / (float) Math.PI);
		head.rotateAngleY = angleY;
		head.rotateAngleX = angleX;
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
	}
}