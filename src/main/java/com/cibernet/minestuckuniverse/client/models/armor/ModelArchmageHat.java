package com.cibernet.minestuckuniverse.client.models.armor;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArchmageHat extends ModelBiped {
	private final ModelRenderer wizardHat;
	private final ModelRenderer whRim;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer whLayer1;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer whLayer2;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer whLayer3;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer whLayer4;
	private final ModelRenderer cube_r25;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer cube_r29;
	private final ModelRenderer cube_r30;
	private final ModelRenderer whLayer5;
	private final ModelRenderer cube_r31;
	private final ModelRenderer cube_r32;
	private final ModelRenderer cube_r33;
	private final ModelRenderer cube_r34;
	private final ModelRenderer cube_r35;
	private final ModelRenderer cube_r36;
	private final ModelRenderer whLayer6;
	private final ModelRenderer cube_r37;
	private final ModelRenderer cube_r38;
	private final ModelRenderer cube_r39;
	private final ModelRenderer cube_r40;
	private final ModelRenderer cube_r41;
	private final ModelRenderer cube_r42;
	private final ModelRenderer whLayer7;
	private final ModelRenderer cube_r43;
	private final ModelRenderer cube_r44;
	private final ModelRenderer cube_r45;
	private final ModelRenderer cube_r46;
	private final ModelRenderer cube_r47;
	private final ModelRenderer cube_r48;
	private final ModelRenderer whLayer8;
	private final ModelRenderer cube_r49;
	private final ModelRenderer cube_r50;
	private final ModelRenderer cube_r51;
	private final ModelRenderer wizardStar;
	private final ModelRenderer bottom;

	public ModelArchmageHat() {
		textureWidth = 64;
		textureHeight = 64;

		wizardHat = new ModelRenderer(this);
		wizardHat.setRotationPoint(0.0F, -8.0F, 0.0F);

		bipedHead.cubeList.remove(0);
		bipedHeadwear.cubeList.remove(0);
		bipedHead.addChild(wizardHat);

		whRim = new ModelRenderer(this);
		whRim.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whRim);
		whRim.cubeList.add(new ModelBox(whRim, 0, 30, -3.0F, -1.0F, -7.2426F, 6, 1, 3, 0.0F, false));
		whRim.cubeList.add(new ModelBox(whRim, 24, 14, -3.0F, -1.0F, 4.2426F, 6, 1, 3, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(6.5607F, 0.0F, 5.0459F);
		whRim.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, -0.7854F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 20, 10, -11.2071F, -1.0F, -6.1716F, 6, 1, 3, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(4.0282F, 0.0F, 6.0711F);
		whRim.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, -1.5708F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 15, 27, -9.0711F, -1.0F, -3.2145F, 6, 1, 3, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(7.9246F, 0.0F, 0.318F);
		whRim.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.7854F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 15, 23, -8.3787F, -1.0F, -1.5858F, 6, 1, 3, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-5.8033F, 0.0F, 2.4393F);
		whRim.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, -0.7854F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 21, 19, -0.6213F, -1.0F, -1.5858F, 6, 1, 3, 0.0F, false));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(-5.9571F, 0.0F, -3.0F);
		whRim.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, -1.5708F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 26, 0.0F, -1.0F, -1.7145F, 6, 1, 3, 0.0F, false));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-4.4393F, 0.0F, -1.0754F);
		whRim.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.7854F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 22, -0.6213F, -1.0F, -3.3431F, 6, 1, 3, 0.0F, false));

		whLayer1 = new ModelRenderer(this);
		whLayer1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whLayer1);
		whLayer1.cubeList.add(new ModelBox(whLayer1, 28, 31, -2.0F, -3.0F, -5.0F, 4, 2, 1, 0.0F, false));
		whLayer1.cubeList.add(new ModelBox(whLayer1, 18, 31, -2.0F, -3.0F, 3.6569F, 4, 2, 1, 0.0F, false));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(0.5858F, -1.0F, -0.7574F);
		whLayer1.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, -0.7854F, 0.0F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 30, 23, -2.0F, -2.0F, -4.0F, 4, 2, 1, 0.0F, false));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(2.8284F, -1.0F, -3.0784F);
		whLayer1.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.0F, -1.5708F, 0.0F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 34, 0.9069F, -2.0F, -2.0F, 4, 2, 1, 0.0F, false));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(-1.4905F, -1.0F, -6.6517F);
		whLayer1.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.0F, 0.7854F, 0.0F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 30, 7, -5.5282F, -2.0F, 9.4645F, 4, 2, 1, 0.0F, false));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(-0.5858F, -1.0F, 0.4142F);
		whLayer1.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.0F, -0.7854F, 0.0F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 30, 27, -2.0F, -2.0F, 3.0F, 4, 2, 1, 0.0F, false));

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(-5.8284F, -1.0F, 5.5784F);
		whLayer1.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, -1.5708F, 0.0F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 10, 34, -7.75F, -2.0F, -2.0F, 4, 2, 1, 0.0F, false));

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(2.9497F, -1.0F, -1.4645F);
		whLayer1.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 0.7854F, 0.0F);
		cube_r12.cubeList.add(new ModelBox(cube_r12, 30, 4, -5.0F, -2.0F, -6.0F, 4, 2, 1, 0.0F, false));

		whLayer2 = new ModelRenderer(this);
		whLayer2.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whLayer2);
		setRotationAngle(whLayer2, -0.0873F, 0.0F, 0.0F);
		whLayer2.cubeList.add(new ModelBox(whLayer2, 7, 40, -1.5F, -4.7462F, -4.3372F, 3, 2, 1, 0.25F, false));
		whLayer2.cubeList.add(new ModelBox(whLayer2, 40, 6, -1.5F, -4.7462F, 2.6126F, 3, 2, 1, 0.25F, false));

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(-0.2301F, 0.0F, -5.6161F);
		whLayer2.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.0F, -0.7854F, 0.0F);
		cube_r13.cubeList.add(new ModelBox(cube_r13, 40, 3, 2.3777F, -4.7462F, -0.4225F, 3, 2, 1, 0.25F, false));

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(2.1997F, 0.0F, 4.1997F);
		whLayer2.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0F, -1.5708F, 0.0F);
		cube_r14.cubeList.add(new ModelBox(cube_r14, 31, 40, -6.062F, -4.7462F, -1.7751F, 3, 2, 1, 0.25F, false));

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(10.023F, 0.0F, -0.0806F);
		whLayer2.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, 0.7854F, 0.0F);
		cube_r15.cubeList.add(new ModelBox(cube_r15, 39, 29, -8.3881F, -4.7462F, -4.3116F, 3, 2, 1, 0.25F, false));

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(-3.0282F, 0.0F, 5.0282F);
		whLayer2.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.0F, -0.7854F, 0.0F);
		cube_r16.cubeList.add(new ModelBox(cube_r16, 40, 0, -3.1704F, -4.7462F, -2.978F, 3, 2, 1, 0.25F, false));

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(-4.75F, 0.0F, -2.75F);
		whLayer2.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0F, -1.5708F, 0.0F);
		cube_r17.cubeList.add(new ModelBox(cube_r17, 40, 22, 0.8877F, -4.7462F, -1.7751F, 3, 2, 1, 0.25F, false));

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(2.6694F, 0.0F, -2.5555F);
		whLayer2.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.0F, 0.7854F, 0.0F);
		cube_r18.cubeList.add(new ModelBox(cube_r18, 39, 25, -4.9384F, -4.7462F, -4.3116F, 3, 2, 1, 0.25F, false));

		whLayer3 = new ModelRenderer(this);
		whLayer3.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whLayer3);
		setRotationAngle(whLayer3, -0.1745F, 0.0F, 0.0F);
		whLayer3.cubeList.add(new ModelBox(whLayer3, 30, 0, -1.5F, -6.2348F, -4.1736F, 3, 2, 2, 0.0F, false));
		whLayer3.cubeList.add(new ModelBox(whLayer3, 24, 38, -1.5F, -6.2348F, 2.069F, 3, 2, 1, 0.0F, false));

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(3.6213F, 0.0F, 0.2426F);
		whLayer3.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.0F, -0.7854F, 0.0F);
		cube_r19.cubeList.add(new ModelBox(cube_r19, 0, 4, -4.6228F, -6.2348F, -1.6228F, 3, 2, 2, 0.0F, false));

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(2.1213F, 0.0F, -2.3787F);
		whLayer3.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, -1.5708F, 0.0F);
		cube_r20.cubeList.add(new ModelBox(cube_r20, 39, 13, 0.3264F, -6.2348F, -1.5F, 3, 2, 1, 0.0F, false));

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(5.7426F, 0.0F, -0.2929F);
		whLayer3.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, 0.7854F, 0.0F);
		cube_r21.cubeList.add(new ModelBox(cube_r21, 8, 37, -5.3772F, -6.2348F, -1.6228F, 3, 2, 1, 0.0F, false));

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(-4.3284F, 0.0F, 1.1213F);
		whLayer3.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, -0.7854F, 0.0F);
		cube_r22.cubeList.add(new ModelBox(cube_r22, 35, 10, 0.3772F, -6.2348F, -1.6228F, 3, 2, 1, 0.0F, false));

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(-4.1213F, 0.0F, 3.6213F);
		whLayer3.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0F, -1.5708F, 0.0F);
		cube_r23.cubeList.add(new ModelBox(cube_r23, 16, 38, -5.6736F, -6.2348F, -1.5F, 3, 2, 1, 0.0F, false));

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(-2.9142F, 0.0F, -0.4645F);
		whLayer3.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, 0.7854F, 0.0F);
		cube_r24.cubeList.add(new ModelBox(cube_r24, 0, 0, 0.6228F, -6.2348F, -1.6228F, 3, 2, 2, 0.0F, false));

		whLayer4 = new ModelRenderer(this);
		whLayer4.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whLayer4);
		setRotationAngle(whLayer4, -0.3491F, 0.0F, 0.0F);
		whLayer4.cubeList.add(new ModelBox(whLayer4, 0, 37, -1.0F, -6.9397F, -3.592F, 2, 2, 2, 0.25F, false));
		whLayer4.cubeList.add(new ModelBox(whLayer4, 0, 8, -1.0F, -6.9397F, 0.9435F, 2, 1, 1, 0.25F, false));

		cube_r25 = new ModelRenderer(this);
		cube_r25.setRotationPoint(-0.341F, 0.0F, -0.1412F);
		whLayer4.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, -0.7854F, 0.0F);
		cube_r25.cubeList.add(new ModelBox(cube_r25, 34, 36, -1.2418F, -6.9397F, -3.4918F, 2, 2, 2, 0.25F, false));

		cube_r26 = new ModelRenderer(this);
		cube_r26.setRotationPoint(1.5178F, 0.0F, -3.4822F);
		whLayer4.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, -1.5708F, 0.0F);
		cube_r26.cubeList.add(new ModelBox(cube_r26, 42, 32, 1.658F, -6.9397F, -1.25F, 2, 1, 1, 0.25F, false));

		cube_r27 = new ModelRenderer(this);
		cube_r27.setRotationPoint(3.5481F, 0.0F, 3.0659F);
		whLayer4.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 0.7854F, 0.0F);
		cube_r27.cubeList.add(new ModelBox(cube_r27, 40, 36, -0.7582F, -6.9397F, -3.4918F, 2, 1, 1, 0.25F, false));

		cube_r28 = new ModelRenderer(this);
		cube_r28.setRotationPoint(-3.5481F, 0.0F, 3.0659F);
		whLayer4.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0F, -0.7854F, 0.0F);
		cube_r28.cubeList.add(new ModelBox(cube_r28, 42, 9, -1.2418F, -6.9397F, -3.4918F, 2, 1, 1, 0.25F, false));

		cube_r29 = new ModelRenderer(this);
		cube_r29.setRotationPoint(-3.0178F, 0.0F, 2.5178F);
		whLayer4.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, -1.5708F, 0.0F);
		cube_r29.cubeList.add(new ModelBox(cube_r29, 42, 38, -4.342F, -6.9397F, -1.25F, 2, 1, 1, 0.25F, false));

		cube_r30 = new ModelRenderer(this);
		cube_r30.setRotationPoint(0.341F, 0.0F, -0.1412F);
		whLayer4.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.0F, 0.7854F, 0.0F);
		cube_r30.cubeList.add(new ModelBox(cube_r30, 36, 32, -0.7582F, -6.9397F, -3.4918F, 2, 2, 2, 0.25F, false));

		whLayer5 = new ModelRenderer(this);
		whLayer5.setRotationPoint(0.0F, 0.0F, 0.0F);
		wizardHat.addChild(whLayer5);
		setRotationAngle(whLayer5, -0.5236F, 0.0F, 0.0F);
		whLayer5.cubeList.add(new ModelBox(whLayer5, 36, 18, -1.0F, -8.366F, -4.0F, 2, 2, 2, 0.0F, false));
		whLayer5.cubeList.add(new ModelBox(whLayer5, 39, 40, -1.0F, -8.366F, -0.1716F, 2, 2, 1, 0.0F, false));

		cube_r31 = new ModelRenderer(this);
		cube_r31.setRotationPoint(-0.4142F, 0.0F, -0.6716F);
		whLayer5.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.0F, -0.7854F, 0.0F);
		cube_r31.cubeList.add(new ModelBox(cube_r31, 28, 34, -1.3536F, -8.366F, -3.3536F, 2, 2, 2, 0.0F, false));

		cube_r32 = new ModelRenderer(this);
		cube_r32.setRotationPoint(1.4142F, 0.0F, -1.0858F);
		whLayer5.addChild(cube_r32);
		setRotationAngle(cube_r32, 0.0F, -1.5708F, 0.0F);
		cube_r32.cubeList.add(new ModelBox(cube_r32, 42, 16, -1.5F, -8.366F, -1.0F, 2, 2, 1, 0.0F, false));

		cube_r33 = new ModelRenderer(this);
		cube_r33.setRotationPoint(3.1213F, 0.0F, 2.0355F);
		whLayer5.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.0F, 0.7854F, 0.0F);
		cube_r33.cubeList.add(new ModelBox(cube_r33, 0, 41, -0.6464F, -8.366F, -3.3536F, 2, 2, 1, 0.0F, false));

		cube_r34 = new ModelRenderer(this);
		cube_r34.setRotationPoint(-3.1213F, 0.0F, 2.0355F);
		whLayer5.addChild(cube_r34);
		setRotationAngle(cube_r34, 0.0F, -0.7854F, 0.0F);
		cube_r34.cubeList.add(new ModelBox(cube_r34, 15, 41, -1.3536F, -8.366F, -3.3536F, 2, 2, 1, 0.0F, false));

		cube_r35 = new ModelRenderer(this);
		cube_r35.setRotationPoint(-2.4142F, 0.0F, -0.0858F);
		whLayer5.addChild(cube_r35);
		setRotationAngle(cube_r35, 0.0F, -1.5708F, 0.0F);
		cube_r35.cubeList.add(new ModelBox(cube_r35, 21, 41, -2.5F, -8.366F, -1.0F, 2, 2, 1, 0.0F, false));

		cube_r36 = new ModelRenderer(this);
		cube_r36.setRotationPoint(0.4142F, 0.0F, -0.6716F);
		whLayer5.addChild(cube_r36);
		setRotationAngle(cube_r36, 0.0F, 0.7854F, 0.0F);
		cube_r36.cubeList.add(new ModelBox(cube_r36, 20, 34, -0.6464F, -8.366F, -3.3536F, 2, 2, 2, 0.0F, false));

		whLayer6 = new ModelRenderer(this);
		whLayer6.setRotationPoint(0.0F, 0.0F, -1.0F);
		wizardHat.addChild(whLayer6);
		setRotationAngle(whLayer6, -0.6981F, 0.0F, 0.0F);
		whLayer6.cubeList.add(new ModelBox(whLayer6, 21, 44, -0.5F, -9.116F, -3.3928F, 1, 1, 1, 0.25F, false));
		whLayer6.cubeList.add(new ModelBox(whLayer6, 44, 19, -0.5F, -9.116F, -1.2715F, 1, 1, 1, 0.25F, false));

		cube_r37 = new ModelRenderer(this);
		cube_r37.setRotationPoint(0.5732F, -7.0F, -2.1161F);
		whLayer6.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.0F, 0.7854F, 0.0F);
		cube_r37.cubeList.add(new ModelBox(cube_r37, 26, 34, -0.0455F, -2.116F, -0.7045F, 1, 1, 1, 0.25F, false));

		cube_r38 = new ModelRenderer(this);
		cube_r38.setRotationPoint(1.3107F, -7.0F, -1.1893F);
		whLayer6.addChild(cube_r38);
		setRotationAngle(cube_r38, 0.0F, -1.5708F, 0.0F);
		cube_r38.cubeList.add(new ModelBox(cube_r38, 13, 44, -1.1428F, -2.116F, -0.25F, 1, 1, 1, 0.25F, false));

		cube_r39 = new ModelRenderer(this);
		cube_r39.setRotationPoint(0.5732F, -7.0F, -0.6161F);
		whLayer6.addChild(cube_r39);
		setRotationAngle(cube_r39, 0.0F, 0.7854F, 0.0F);
		cube_r39.cubeList.add(new ModelBox(cube_r39, 6, 8, -0.0455F, -2.116F, -0.7045F, 1, 1, 1, 0.25F, false));

		cube_r40 = new ModelRenderer(this);
		cube_r40.setRotationPoint(-0.5732F, -7.0F, -0.6161F);
		whLayer6.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.0F, -0.7854F, 0.0F);
		cube_r40.cubeList.add(new ModelBox(cube_r40, 43, 11, -0.9545F, -2.116F, -0.7045F, 1, 1, 1, 0.25F, false));

		cube_r41 = new ModelRenderer(this);
		cube_r41.setRotationPoint(-0.8107F, -7.0F, -1.1893F);
		whLayer6.addChild(cube_r41);
		setRotationAngle(cube_r41, 0.0F, -1.5708F, 0.0F);
		cube_r41.cubeList.add(new ModelBox(cube_r41, 17, 44, -1.1428F, -2.116F, -0.25F, 1, 1, 1, 0.25F, false));

		cube_r42 = new ModelRenderer(this);
		cube_r42.setRotationPoint(-0.5732F, -7.0F, -2.1161F);
		whLayer6.addChild(cube_r42);
		setRotationAngle(cube_r42, 0.0F, -0.7854F, 0.0F);
		cube_r42.cubeList.add(new ModelBox(cube_r42, 0, 44, -0.9545F, -2.116F, -0.7045F, 1, 1, 1, 0.25F, false));

		whLayer7 = new ModelRenderer(this);
		whLayer7.setRotationPoint(0.0F, -1.0F, -2.0F);
		wizardHat.addChild(whLayer7);
		setRotationAngle(whLayer7, -0.8727F, 0.0F, 0.0F);
		whLayer7.cubeList.add(new ModelBox(whLayer7, 30, 43, -0.5F, -10.3928F, -2.766F, 1, 2, 1, 0.0F, false));
		whLayer7.cubeList.add(new ModelBox(whLayer7, 42, 43, -0.5F, -10.3928F, -1.3518F, 1, 2, 1, 0.0F, false));

		cube_r43 = new ModelRenderer(this);
		cube_r43.setRotationPoint(-4.8033F, -8.0F, 0.4749F);
		whLayer7.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0F, 0.7854F, 0.0F);
		cube_r43.cubeList.add(new ModelBox(cube_r43, 27, 41, 5.0417F, -2.3928F, 1.4583F, 1, 2, 1, 0.0F, false));

		cube_r44 = new ModelRenderer(this);
		cube_r44.setRotationPoint(1.2071F, -8.0F, 0.2071F);
		whLayer7.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, -1.5708F, 0.0F);
		cube_r44.cubeList.add(new ModelBox(cube_r44, 34, 43, -2.266F, -2.3928F, 0.0F, 1, 2, 1, 0.0F, false));

		cube_r45 = new ModelRenderer(this);
		cube_r45.setRotationPoint(1.5607F, -8.0F, 0.0607F);
		whLayer7.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, -0.7854F, 0.0F);
		cube_r45.cubeList.add(new ModelBox(cube_r45, 5, 43, -2.0417F, -2.3928F, -0.5417F, 1, 2, 1, 0.0F, false));

		cube_r46 = new ModelRenderer(this);
		cube_r46.setRotationPoint(-0.1464F, -8.0F, -1.3536F);
		whLayer7.addChild(cube_r46);
		setRotationAngle(cube_r46, 0.0F, 0.7854F, 0.0F);
		cube_r46.cubeList.add(new ModelBox(cube_r46, 0, 10, -0.9583F, -2.3928F, -0.5417F, 1, 2, 1, 0.0F, false));

		cube_r47 = new ModelRenderer(this);
		cube_r47.setRotationPoint(-0.2071F, -8.0F, 0.2071F);
		whLayer7.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.0F, -1.5708F, 0.0F);
		cube_r47.cubeList.add(new ModelBox(cube_r47, 38, 43, -2.266F, -2.3928F, 0.0F, 1, 2, 1, 0.0F, false));

		cube_r48 = new ModelRenderer(this);
		cube_r48.setRotationPoint(0.5607F, -8.0F, -0.9393F);
		whLayer7.addChild(cube_r48);
		setRotationAngle(cube_r48, 0.0F, -0.7854F, 0.0F);
		cube_r48.cubeList.add(new ModelBox(cube_r48, 9, 43, -2.0417F, -2.3928F, -0.5417F, 1, 2, 1, 0.0F, false));

		whLayer8 = new ModelRenderer(this);
		whLayer8.setRotationPoint(0.0F, -2.0F, -2.0F);
		wizardHat.addChild(whLayer8);
		setRotationAngle(whLayer8, -1.0472F, 0.0F, 0.0F);
		whLayer8.cubeList.add(new ModelBox(whLayer8, 21, 3, 0.0F, -10.1808F, -2.6896F, 0, 1, 1, 0.35F, false));

		cube_r49 = new ModelRenderer(this);
		cube_r49.setRotationPoint(-0.1803F, -10.0F, -0.2211F);
		whLayer8.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.0F, 0.7854F, 0.0F);
		cube_r49.cubeList.add(new ModelBox(cube_r49, 21, 0, 1.018F, -0.1808F, -1.268F, 1, 1, 0, 0.35F, false));

		cube_r50 = new ModelRenderer(this);
		cube_r50.setRotationPoint(-0.3286F, -10.0F, -1.3286F);
		whLayer8.addChild(cube_r50);
		setRotationAngle(cube_r50, 0.0F, -1.5708F, 0.0F);
		cube_r50.cubeList.add(new ModelBox(cube_r50, 20, 0, -0.866F, -0.1808F, -0.8236F, 0, 1, 1, 0.35F, false));

		cube_r51 = new ModelRenderer(this);
		cube_r51.setRotationPoint(-0.5268F, -10.0F, -0.9282F);
		whLayer8.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, -0.7854F, 0.0F);
		cube_r51.cubeList.add(new ModelBox(cube_r51, 21, 5, -1.018F, -0.1808F, -1.268F, 1, 1, 0, 0.35F, false));

		wizardStar = new ModelRenderer(this);
		wizardStar.setRotationPoint(0.0F, -2.35F, -5.5F);
		wizardHat.addChild(wizardStar);
		setRotationAngle(wizardStar, -0.3491F, 0.0F, 0.0F);
		wizardStar.cubeList.add(new ModelBox(wizardStar, 0, 10, -4.0F, -5.75F, -1.775F, 8, 8, 4, -2.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 0, 46, -5.0F, -0.75F, -5.0F, 10, 0, 10, 0.0F, false));
		wizardHat.addChild(bottom);
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