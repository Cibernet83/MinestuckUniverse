package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelGTBard extends ModelGTAbstract
{
	private final ModelRenderer hood;
	private final ModelRenderer cape1;
	private final ModelRenderer cape2;
	private final ModelRenderer cape3;
	private final ModelRenderer cape4;
	private final ModelRenderer codPieceTop;
	private final ModelRenderer codPieceRight;
	private final ModelRenderer codPieceLeft;
	private final ModelRenderer boneRight;
	private final ModelRenderer boneLeft;

	public ModelGTBard()
	{
		super(128, 128, EnumClass.BARD);
		addColorIgnores(8);
		
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9f+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, -10.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 96, 9, -4.0F, -1.0F+0.2f, -4.0F, 8, 2, 8, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 96, 19, -4.0F, -3.0F+0.2f, -4.0F, 8, 3, 8, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 104, 30, -3.0F, -4.0F+0.2f, -3.0F, 6, 1, 6, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 104, 37, -3.0F, -6.0F+0.2f, -3.0F, 6, 3, 6, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 112, 46, -2.0F, -7.0F+0.2f, -2.0F, 4, 1, 4, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 112, 51, -2.0F, -9.0F+0.2f, -2.0F, 4, 3, 4, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 58, -1.0F, -9.0F+0.2f, -1.0F, 2, 1, 2, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 61, -1.0F, -10.0F+0.2f, -1.0F, 2, 1, 2, 0.0F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, -2.75F, 10, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, 1.75F, 10, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 79, 112, -4.0F, -0.1F, -2.0F, 8, 12, 4, 0.252F, false));

		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		cape1 = new ModelRenderer(this);
		cape1.setRotationPoint(0.0F, -19.0F, 0.0F);
		cape.addChild(cape1);
		cape1.cubeList.add(new ModelBox(cape1, 122, 78, -5.0F, 20.0F, 2.25F, 2, 22, 1, 0.0F, true));

		cape2 = new ModelRenderer(this);
		cape2.setRotationPoint(0.0F, -19.0F, 0.0F);
		cape.addChild(cape2);
		cape2.cubeList.add(new ModelBox(cape2, 116, 78, -2.35F, 20.0F, 2.25F, 2, 22, 1, 0.0F, true));

		cape3 = new ModelRenderer(this);
		cape3.setRotationPoint(0.0F, -19.0F, 0.0F);
		cape.addChild(cape3);
		cape3.cubeList.add(new ModelBox(cape3, 116, 78, 0.35F, 20.0F, 2.25F, 2, 22, 1, 0.0F, false));

		cape4 = new ModelRenderer(this);
		cape4.setRotationPoint(0.0F, -19.0F, 0.0F);
		cape.addChild(cape4);
		cape4.cubeList.add(new ModelBox(cape4, 122, 78, 3.0F, 20.0F, 2.25F, 2, 22, 1, 0.0F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.cubeList.add(new ModelBox(belt, 55, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.255F, false));

		codPieceTop = new ModelRenderer(this);
		codPieceTop.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.addChild(codPieceTop);
		codPieceTop.cubeList.add(new ModelBox(codPieceTop, 86, 68, -5.0F, 9.0F, -3.0F, 10, 1, 6, 0.0F, false));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		codPieceRight = new ModelRenderer(this);
		codPieceRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		rightLeg.addChild(codPieceRight);
		codPieceRight.cubeList.add(new ModelBox(codPieceRight, 92, 75, -3.0F, 1.0F, -3.0F, 6, 1, 6, -0.001F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		codPieceLeft = new ModelRenderer(this);
		codPieceLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
		leftLeg.addChild(codPieceLeft);
		codPieceLeft.cubeList.add(new ModelBox(codPieceLeft, 92, 75, -3.0F, 1.0F, -3.0F, 6, 1, 6, -0.001F, false));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));
		rightFoot.cubeList.add(new ModelBox(rightFoot, 18, 125, -1.5F, 11.0F, -4.0F, 3, 1, 2, 0.251F, true));
		rightFoot.cubeList.add(new ModelBox(rightFoot, 18, 123, -0.5F, 10.5F, -4.5F, 1, 1, 1, 0.5F, true));

		boneRight = new ModelRenderer(this);
		boneRight.setRotationPoint(0.0F, 10.0F, -5.0F);
		rightFoot.addChild(boneRight);
		setRotationAngle(boneRight, -0.7854F, 0.0F, 0.0F);
		boneRight.cubeList.add(new ModelBox(boneRight, 18, 120, -0.5F, -0.5F, -0.5F, 1, 1, 2, 0.0F, true));
		boneRight.cubeList.add(new ModelBox(boneRight, 18, 118, -0.5F, -1.0F, -1.0F, 1, 1, 1, 0.0F, true));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));
		leftFoot.cubeList.add(new ModelBox(leftFoot, 18, 125, -1.5F, 11.0F, -4.0F, 3, 1, 2, 0.251F, false));
		leftFoot.cubeList.add(new ModelBox(leftFoot, 18, 123, -0.5F, 10.5F, -4.5F, 1, 1, 1, 0.5F, false));

		boneLeft = new ModelRenderer(this);
		boneLeft.setRotationPoint(0.0F, 10.0F, -5.0F);
		leftFoot.addChild(boneLeft);
		setRotationAngle(boneLeft, -0.7854F, 0.0F, 0.0F);
		boneLeft.cubeList.add(new ModelBox(boneLeft, 18, 120, -0.5F, -0.5F, -0.5F, 1, 1, 2, 0.0F, false));
		boneLeft.cubeList.add(new ModelBox(boneLeft, 18, 118, -0.5F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}