package com.cibernet.minestuckuniverse.entity.models.armor;//Made with Blockbench
//Paste this code into your mod.

import net.minecraft.client.model.ModelBiped;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPrismarineArmor extends ModelBiped {
	private final ModelRenderer Head;
	private final ModelRenderer Torso;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public ModelPrismarineArmor() {
		textureWidth = 128;
		textureHeight = 128;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.cubeList.add(new ModelBox(Head, 0, 0, -4.125F, -7.925F, -4.1F, 8, 8, 8, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 52, 14, -3.0F, -11.525F, -0.425F, 8, 4, 0, 0.0F, false));
		bipedHead.addChild(Head);
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		Torso.cubeList.add(new ModelBox(Torso, 24, 24, -2.175F, -0.025F, -4.2F, 4, 12, 8, 0.0F, false));
		bipedBody.addChild(Torso);
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		LeftArm.cubeList.add(new ModelBox(LeftArm, 0, 52, -2.025F, 0.1F, -8.025F, 4, 10, 4, 0.0F, false));
		LeftArm.cubeList.add(new ModelBox(LeftArm, 24, 16, -2.175F, -0.275F, -8.15F, 4, 2, 4, 0.0F, false));
		LeftArm.cubeList.add(new ModelBox(LeftArm, 56, 48, -2.175F, 10.1F, -8.15F, 4, 2, 4, 0.0F, false));
		bipedLeftArm.addChild(LeftArm);
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(RightArm, 0.0F, 3.1416F, 0.0F);
		RightArm.cubeList.add(new ModelBox(RightArm, 0, 52, -2.025F, 0.1F, -8.1F, 4, 10, 4, 0.0F, true));
		RightArm.cubeList.add(new ModelBox(RightArm, 24, 16, -2.25F, -0.275F, -8.225F, 4, 2, 4, 0.0F, true));
		RightArm.cubeList.add(new ModelBox(RightArm, 56, 48, -2.25F, 10.1F, -8.225F, 4, 2, 4, 0.0F, true));
		bipedRightArm.addChild(RightArm);
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(RightLeg, 0.0F, -3.1416F, 0.0F);
		RightLeg.cubeList.add(new ModelBox(RightLeg, 32, 60, -2.2275F, 22.04F, -4.0525F, 4, 2, 4, 0.0F, true));
		RightLeg.cubeList.add(new ModelBox(RightLeg, 52, 56, -2.0025F, 12.04F, -3.9275F, 4, 10, 4, 0.0F, true));
		RightLeg.cubeList.add(new ModelBox(RightLeg, 24, 0, -2.2525F, 12.39F, -4.3025F, 4, 2, 2, 0.0F, true));
		RightLeg.cubeList.add(new ModelBox(RightLeg, 64, 25, -2.1525F, 12.215F, -4.1775F, 4, 3, 2, 0.0F, true));
		RightLeg.cubeList.add(new ModelBox(RightLeg, 64, 0, -2.1275F, 11.84F, -4.0275F, 4, 4, 2, 0.0F, true));
		bipedRightLeg.addChild(RightLeg);
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 32, 60, -2.175F, 22.05F, -4.2F, 4, 2, 4, 0.0F, false));
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 52, 56, -2.025F, 12.05F, -4.075F, 4, 10, 4, 0.0F, false));
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 24, 0, -2.225F, 12.4F, -4.45F, 4, 2, 2, 0.0F, false));
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 64, 25, -2.125F, 12.225F, -4.325F, 4, 3, 2, 0.0F, false));
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 64, 0, -2.1F, 11.85F, -4.175F, 4, 4, 2, 0.0F, false));
		bipedLeftLeg.addChild(LeftLeg);
	}
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}