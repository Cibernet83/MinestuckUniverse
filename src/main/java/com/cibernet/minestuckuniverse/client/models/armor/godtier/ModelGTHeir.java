package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelGTHeir extends ModelGTAbstract
{
	private final ModelRenderer hood;
	private final ModelRenderer bone;

	public ModelGTHeir()
	{
		super(128,128, EnumClass.HEIR);
		addColorIgnores(4, 6, 7, 8);
		
		torso.setRotationPoint(0.0F, 0, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 90, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.2511F, false));
		
		neck.setRotationPoint(0.0F, 24.0F-24, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, -24.0F+24, -3.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, -24.0F+24, 2.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(torso, 66, 112, -4.0F, -0.1F, -2.0F, 8, 12, 4, 0.2515F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 60, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 16, 60, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -25.0F+24.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -33.0F+24.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -33.0F+24.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -33.0F+24.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -33.0F+24.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 2, -5.0F, -34.0F+24.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -34.0F+24.2f, -5.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -34.0F+24.2f, 4.0F, 8, 1, 2, 0.0F, false));

		super.hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 0.0F, 0.0F);
		headsock.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 108, 9, -4.0F, -33.0F+24, 5.0F, 8, 9, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 108, 20, -3.0F, -31.0F+24, 4.0F, 6, 8, 4, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 106, 32, -3.0F, -29.0F+24, 4.0F, 6, 8, 5, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 45, -2.0F, -23.0F+24, 5.5F, 4, 4, 3, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 112, 52, -2.0F, -23.0F+24, 5.5F, 4, 8, 4, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 64, -1.0F, -15.0F+24, 7.5F, 2, 4, 2, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 70, -0.5F, -11.0F+24, 8.0F, 1, 5, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 76, -1.0F, -7.2929F+24, 7.2929F, 2, 7, 1, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0.0F, 8.0F);
		hood.addChild(bone);
		setRotationAngle(bone, -0.7854F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 124, 84, -0.5F, -0.8071F, -0.6071F, 1, 1, 1, 0.1F, false));

		leftFoot.setRotationPoint(-1.9F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2f, 0F, -2.0F, 4, 12, 4, 0.2505F, false));

		rightFoot.setRotationPoint(1.9f, -12, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 16, 112, -2f, 0F, -2.0F, 4, 12, 4, 0.2505F, false));
		
		leftLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2, 0, -2.0F, 4, 12, 4, 0.2505F, false));
		
		rightLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 16, 96, -2, 0, -2.0F, 4, 12, 4, 0.2505F, false));

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}