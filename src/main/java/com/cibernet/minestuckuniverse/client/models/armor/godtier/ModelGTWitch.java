package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelGTWitch extends ModelGTAbstract
{
	private final ModelRenderer hood;
	private final ModelRenderer skirtFrontCenter;
	private final ModelRenderer skirtFrontLeft;
	private final ModelRenderer skirtFrontRight;
	private final ModelRenderer skirtFrontLeftSide;
	private final ModelRenderer skirtFrontRightSide;
	private final ModelRenderer skirtBackCenter;
	private final ModelRenderer skirtBackLeft;
	private final ModelRenderer skirtBackRight;
	private final ModelRenderer skirtBackLeftSide;
	private final ModelRenderer skirtBackRightSide;
	private final ModelRenderer skirtMiddleLeftSide;
	private final ModelRenderer skirtMiddleRightSide;

	public ModelGTWitch()
	{
		super(128, 128, EnumClass.WITCH);
		addColorIgnores(4, 8);
		
		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, -3.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, 2.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 104, 96, -4.0F, -0.1F, -2.0F, 8, 12, 4, 0.2515F, false));
		
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		super.hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		headsock.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 108, 8, -4.0F, -33.0F, 5.0F, 8, 9, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 19, -4.0F, -31.0F, 5.0F, 3, 7, 2, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 19, 1.0F, -31.0F, 5.0F, 3, 7, 2, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 28, -5.0F, -30.0F, 5.0F, 4, 7, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 28, 1.0F, -30.0F, 5.0F, 4, 7, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 38, -4.75F, -24.0F, 4.75F, 3, 2, 3, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 38, 1.75F, -24.0F, 4.75F, 3, 2, 3, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 43, -5.0F, -24.0F, 5.0F, 3, 4, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 43, 2.0F, -24.0F, 5.0F, 3, 4, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 50, -4.75F, -20.0F, 5.25F, 2, 1, 2, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 50, 2.75F, -20.0F, 5.25F, 2, 1, 2, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 53, -5.0F, -21.0F, 5.0F, 2, 6, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 120, 53, 3.0F, -21.0F, 5.0F, 2, 6, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 61, -4.25F, -15.0F, 5.75F, 1, 2, 1, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 61, 3.25F, -15.0F, 5.75F, 1, 2, 1, 0.25F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 64, -4.0F, -16.0F, 6.0F, 1, 9, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 64, 3.0F, -16.0F, 6.0F, 1, 9, 1, 0.0F, false));
		
		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.cubeList.add(new ModelBox(belt, 80, 96, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.252F, false));
		
		
		skirtMiddle.setRotationPoint(0.0F, 12.0F, -0.0F);
		skirtMiddleLeftSide = new ModelRenderer(this);
		skirtMiddleLeftSide.setRotationPoint(-3.0F, 12.0F, 1.0F);
		skirtMiddle.addChild(skirtMiddleLeftSide);
		setRotationAngle(skirtMiddleLeftSide, 0.0F, 0.0F, 0.2618F);
		skirtMiddleLeftSide.cubeList.add(new ModelBox(skirtMiddleLeftSide, 84, 117, -5.0F, -11.5F, -2.0F, 2, 7, 4, -0.002F, true));
		
		skirtMiddleRightSide = new ModelRenderer(this);
		skirtMiddleRightSide.setRotationPoint(3.0F, 12.0F, 1.0F);
		skirtMiddle.addChild(skirtMiddleRightSide);
		setRotationAngle(skirtMiddleRightSide, 0.0F, 0.0F, -0.2618F);
		skirtMiddleRightSide.cubeList.add(new ModelBox(skirtMiddleRightSide, 84, 117, 3.0F, -11.5F, -2.0F, 2, 7, 4, -0.002F, false));
		
		skirtFront.setRotationPoint(0.0F, 12.0F, -1.0F);
		
		skirtFrontCenter = new ModelRenderer(this);
		skirtFrontCenter.setRotationPoint(0.0F, 13.0F, -3.0F);
		skirtFront.addChild(skirtFrontCenter);
		setRotationAngle(skirtFrontCenter, -0.2618F, 0.0F, 0.0F);
		skirtFrontCenter.cubeList.add(new ModelBox(skirtFrontCenter, 27, 106, -2.5F, -14.0F, -3.0F, 5, 8, 2, 0.0F, true));
		
		skirtFrontLeft = new ModelRenderer(this);
		skirtFrontLeft.setRotationPoint(-1.0F, 12.0F, -4.0F);
		skirtFront.addChild(skirtFrontLeft);
		setRotationAngle(skirtFrontLeft, -0.2618F, 0.6109F, 0.0F);
		skirtFrontLeft.cubeList.add(new ModelBox(skirtFrontLeft, 44, 116, -5.0F, -13.0F, -3.0F, 3, 8, 2, 0.0F, false));
		
		skirtFrontRight = new ModelRenderer(this);
		skirtFrontRight.setRotationPoint(-1.0F, 12.0F, 0.0F);
		skirtFront.addChild(skirtFrontRight);
		setRotationAngle(skirtFrontRight, -0.2618F, -0.6109F, 0.0F);
		skirtFrontRight.cubeList.add(new ModelBox(skirtFrontRight, 44, 116, 1.344F, -11.855F, -7.273F, 3, 8, 2, 0.0F, false));
		
		skirtFrontLeftSide = new ModelRenderer(this);
		skirtFrontLeftSide.setRotationPoint(-3.0F, 12.0F, 1.0F);
		skirtFront.addChild(skirtFrontLeftSide);
		setRotationAngle(skirtFrontLeftSide, 0.0F, 0.0F, 0.2618F);
		skirtFrontLeftSide.cubeList.add(new ModelBox(skirtFrontLeftSide, 84, 117, -5.0F, -11.5F, -2.0F, 2, 7, 4, 0.0F, true));
		
		skirtFrontRightSide = new ModelRenderer(this);
		skirtFrontRightSide.setRotationPoint(3.0F, 12.0F, 1.0F);
		skirtFront.addChild(skirtFrontRightSide);
		setRotationAngle(skirtFrontRightSide, 0.0F, 0.0F, -0.2618F);
		skirtFrontRightSide.cubeList.add(new ModelBox(skirtFrontRightSide, 84, 117, 3.0F, -11.5F, -2.0F, 2, 7, 4, 0.0F, false));
		
		skirtBack.setRotationPoint(0.0F, 12.0F, 1.0F);
		
		
		skirtBackCenter = new ModelRenderer(this);
		skirtBackCenter.setRotationPoint(0.0F, 12.0F, 3.0F);
		skirtBack.addChild(skirtBackCenter);
		setRotationAngle(skirtBackCenter, 0.2618F, 0.0F, 0.0F);
		skirtBackCenter.cubeList.add(new ModelBox(skirtBackCenter, 26, 116, -2.5F, -13.0F, 1.0F, 5, 8, 2, 0.0F, false));
		
		skirtBackLeft = new ModelRenderer(this);
		skirtBackLeft.setRotationPoint(-1.0F, 12.0F, 4.0F);
		skirtBack.addChild(skirtBackLeft);
		setRotationAngle(skirtBackLeft, 0.2618F, -0.6109F, 0.0F);
		skirtBackLeft.cubeList.add(new ModelBox(skirtBackLeft, 58, 116, -5.0F, -13.0F, 1.0F, 3, 8, 2, 0.0F, false));
		
		skirtBackRight = new ModelRenderer(this);
		skirtBackRight.setRotationPoint(1.0F, 12.0F, 4.0F);
		skirtBack.addChild(skirtBackRight);
		setRotationAngle(skirtBackRight, 0.2618F, 0.6109F, 0.0F);
		skirtBackRight.cubeList.add(new ModelBox(skirtBackRight, 58, 116, 2.0F, -13.0F, 1.0F, 3, 8, 2, 0.0F, false));
		
		skirtBackLeftSide = new ModelRenderer(this);
		skirtBackLeftSide.setRotationPoint(-3.0F, 12.0F, -1.0F);
		skirtBack.addChild(skirtBackLeftSide);
		setRotationAngle(skirtBackLeftSide, 0.0F, 0.0F, 0.2618F);
		skirtBackLeftSide.cubeList.add(new ModelBox(skirtBackLeftSide, 84, 117, -5.0F, -11.5F, -2.0F, 2, 7, 4, -0.001F, true));
		
		skirtBackRightSide = new ModelRenderer(this);
		skirtBackRightSide.setRotationPoint(3.0F, 12.0F, -1.0F);
		skirtBack.addChild(skirtBackRightSide);
		setRotationAngle(skirtBackRightSide, 0.0F, 0.0F, -0.2618F);
		skirtBackRightSide.cubeList.add(new ModelBox(skirtBackRightSide, 84, 117, 3.0F, -11.5F, -2.0F, 2, 7, 4, -0.001F, false));
		
		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, -0.0F, -2.0F, 8, 12, 4, 0.251F, false));
		
		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 61, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));
		
		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 16, 61, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));
		
		leftFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, false));
		
		rightFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 96, -2.0F, -0.0F, -2.0F, 4, 12, 4, 0.2505F, false));
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}