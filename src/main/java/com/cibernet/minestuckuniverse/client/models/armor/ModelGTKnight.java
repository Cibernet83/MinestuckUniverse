package com.cibernet.minestuckuniverse.client.models.armor;//Made with Blockbench
//Paste this code into your mod.

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelGTKnight extends ModelBiped {
	//private final ModelRenderer leftLeg;
	//private final ModelRenderer rightLeg;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer cape;

	public ModelGTKnight() {
		textureWidth = 128;
		textureHeight = 128;

		this.bipedLeftLeg = new ModelRenderer(this, 0, 43);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 18, 36);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 48, 48, 3.95F, -4.1F, -2F, 4, 12, 4, 0.0F, false));

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 44, 19, 2.05F, -4.1F, -2F, 4, 12, 4, 0.0F, false));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 18, 20, -4F, -0.05F, -2F, 8, 12, 4, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 44, 35, -4F, -0.05F, -3F, 8, 2, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 44, 38, -4F, -0.05F, 2F, 8, 2, 1, 0.0F, false));

		cape = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(body, 0, 20, -4F, 1.05F, 3F, 8, 22, 1, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 34, 34, 4.0F, -8.0F, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 50, 9, -5.0F, -8.0F, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 34, 50, -4.0F, -9.0F, -5.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 32, 3, -5.0F, -8.0F, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 42, 0, -5.0F, -8.0F, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 16, 52, -4.0F, -9.0F, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, 0.0F, -5.0F, 10, 1, 10, 0.0F, false));

		this.bipedHead.cubeList.remove(0);

		this.bipedHead.addChild(head);
		this.bipedBody.addChild(body);
		this.bipedBody.addChild(cape);
		this.bipedLeftArm.addChild(leftArm);
		this.bipedRightArm.addChild(rightArm);
		//this.bipedLeftLeg.addChild(leftLeg);
		//this.bipedRightLeg.addChild(rightLeg);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		GlStateManager.scale(1.01F,1.01F,1.01F);
		super.render(entity, f, f1, f2, f3, f4, f5);


	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}