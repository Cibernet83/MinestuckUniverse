package com.cibernet.minestuckuniverse.client.models.armor;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelSpikedHelmet extends ModelBiped {
	private final ModelRenderer Head;
	private final ModelRenderer SpikedHelmet;
	private final ModelRenderer Helmet;
	private final ModelRenderer Main;
	private final ModelRenderer Bottom;
	private final ModelRenderer Top;
	private final ModelRenderer Windows;
	private final ModelRenderer Spike1;
	private final ModelRenderer Spike2;
	private final ModelRenderer Spike3;
	private final ModelRenderer Spike4;
	private final ModelRenderer Spike5;
	private final ModelRenderer Spike6;
	private final ModelRenderer Spike7;
	private final ModelRenderer Spike8;
	private final ModelRenderer Spike9;
	private final ModelRenderer Spike10;
	private final ModelRenderer Spike11;
	private final ModelRenderer Spike12;
	private final ModelRenderer Spike13;
	private final ModelRenderer Spike14;
	private final ModelRenderer Spike15;
	private final ModelRenderer Spike16;
	private final ModelRenderer Spike17;

	public ModelSpikedHelmet() {
		textureWidth = 128;
		textureHeight = 128;

		bipedHead.cubeList.remove(0);
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		

		SpikedHelmet = new ModelRenderer(this);
		SpikedHelmet.setRotationPoint(-1.0F, 1.0F, 0.0F);
		Head.addChild(SpikedHelmet);
		

		Helmet = new ModelRenderer(this);
		Helmet.setRotationPoint(1.0F, 0.0F, 0.0F);
		SpikedHelmet.addChild(Helmet);
		

		Main = new ModelRenderer(this);
		Main.setRotationPoint(0.0F, 0.0F, 0.0F);
		Helmet.addChild(Main);
		Main.cubeList.add(new ModelBox(Main, 0, 43, -4.0F, -1.0F, -5.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 36, 45, -4.0F, -1.0F, 4.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 36, 25, -5.0F, -1.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 25, 4.0F, -1.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 34, 5.0F, -2.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 4, 57, 4.0F, -2.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 8, 57, -5.0F, -2.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 28, 57, -5.0F, -2.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 57, 4.0F, -2.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 72, 43, -4.0F, -2.0F, -6.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 45, -4.0F, -2.0F, 5.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 62, 16, -6.0F, -2.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 32, 0, -6.0F, -8.0F, -5.0F, 1, 6, 10, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 0, 5.0F, -8.0F, -5.0F, 1, 6, 10, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 16, -5.0F, -8.0F, -6.0F, 10, 6, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 22, 16, -5.0F, -8.0F, 5.0F, 10, 6, 1, 0.0F, false));

		Bottom = new ModelRenderer(this);
		Bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		Helmet.addChild(Bottom);
		Bottom.cubeList.add(new ModelBox(Bottom, 0, 25, 3.0F, 0.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 18, 25, -4.0F, 0.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 0, 49, -3.0F, 0.0F, 3.0F, 6, 1, 1, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 54, 45, -3.0F, 0.0F, -4.0F, 6, 1, 1, 0.0F, false));

		Top = new ModelRenderer(this);
		Top.setRotationPoint(0.0F, 0.0F, 0.0F);
		Helmet.addChild(Top);
		Top.cubeList.add(new ModelBox(Top, 12, 57, 4.0F, -9.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 16, 57, -5.0F, -9.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 20, 57, -5.0F, -9.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 24, 57, 4.0F, -9.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 44, 16, 5.0F, -9.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 0, 34, -6.0F, -9.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 54, 43, -4.0F, -9.0F, -6.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 36, 43, -4.0F, -9.0F, 5.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 43, -4.0F, -10.0F, 4.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 45, -4.0F, -10.0F, -5.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 34, 4.0F, -10.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 36, 34, -5.0F, -10.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 0, 0, -4.0F, -11.0F, -4.0F, 8, 1, 8, 0.0F, false));

		Windows = new ModelRenderer(this);
		Windows.setRotationPoint(0.0F, 0.0F, 0.0F);
		Helmet.addChild(Windows);
		Windows.cubeList.add(new ModelBox(Windows, 68, 45, -3.0F, -8.0F, -7.0F, 6, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 47, -3.0F, -3.0F, -7.0F, 6, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 20, 52, 2.0F, -7.0F, -7.0F, 1, 4, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 10, 52, -3.0F, -7.0F, -7.0F, 1, 4, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 34, 47, -7.0F, -7.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 14, 47, -7.0F, -4.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 30, 52, -7.0F, -6.0F, -2.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 38, 52, -7.0F, -6.0F, 1.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 34, 52, 6.0F, -6.0F, 1.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 42, 52, 6.0F, -6.0F, -2.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 44, 47, 6.0F, -7.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 24, 47, 6.0F, -4.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 52, -2.0F, -12.0F, 1.0F, 4, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 54, -2.0F, -12.0F, -2.0F, 4, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 24, 52, -2.0F, -12.0F, -1.0F, 1, 1, 2, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 14, 52, 1.0F, -12.0F, -1.0F, 1, 1, 2, 0.0F, false));

		Spike1 = new ModelRenderer(this);
		Spike1.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike1);
		setRotationAngle(Spike1, 0.0F, 0.0F, -0.7854F);
		Spike1.cubeList.add(new ModelBox(Spike1, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike1.cubeList.add(new ModelBox(Spike1, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike1.cubeList.add(new ModelBox(Spike1, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike1.cubeList.add(new ModelBox(Spike1, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike2 = new ModelRenderer(this);
		Spike2.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike2);
		setRotationAngle(Spike2, 0.0F, 0.0F, 0.7854F);
		Spike2.cubeList.add(new ModelBox(Spike2, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike2.cubeList.add(new ModelBox(Spike2, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike2.cubeList.add(new ModelBox(Spike2, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike2.cubeList.add(new ModelBox(Spike2, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike3 = new ModelRenderer(this);
		Spike3.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike3);
		setRotationAngle(Spike3, -0.7854F, 0.0F, 0.0F);
		Spike3.cubeList.add(new ModelBox(Spike3, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike3.cubeList.add(new ModelBox(Spike3, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike3.cubeList.add(new ModelBox(Spike3, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike3.cubeList.add(new ModelBox(Spike3, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike4 = new ModelRenderer(this);
		Spike4.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike4);
		setRotationAngle(Spike4, 0.7854F, 0.0F, 0.0F);
		Spike4.cubeList.add(new ModelBox(Spike4, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike4.cubeList.add(new ModelBox(Spike4, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike4.cubeList.add(new ModelBox(Spike4, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike4.cubeList.add(new ModelBox(Spike4, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike5 = new ModelRenderer(this);
		Spike5.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike5);
		setRotationAngle(Spike5, 2.3562F, 0.0F, 0.0F);
		Spike5.cubeList.add(new ModelBox(Spike5, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike5.cubeList.add(new ModelBox(Spike5, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike5.cubeList.add(new ModelBox(Spike5, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike5.cubeList.add(new ModelBox(Spike5, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike6 = new ModelRenderer(this);
		Spike6.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike6);
		setRotationAngle(Spike6, -2.3562F, 0.0F, 0.0F);
		Spike6.cubeList.add(new ModelBox(Spike6, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike6.cubeList.add(new ModelBox(Spike6, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike6.cubeList.add(new ModelBox(Spike6, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike6.cubeList.add(new ModelBox(Spike6, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike7 = new ModelRenderer(this);
		Spike7.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike7);
		setRotationAngle(Spike7, -1.5708F, 0.0F, 0.0F);
		Spike7.cubeList.add(new ModelBox(Spike7, 0, 0, -1.0F, -8.0F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike7.cubeList.add(new ModelBox(Spike7, 38, 16, 0.0F, -8.0F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike7.cubeList.add(new ModelBox(Spike7, 0, 0, -0.5F, -10.0F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike7.cubeList.add(new ModelBox(Spike7, 38, 16, 0.0F, -10.0F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike8 = new ModelRenderer(this);
		Spike8.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike8);
		setRotationAngle(Spike8, 0.0F, 0.0F, -2.3562F);
		Spike8.cubeList.add(new ModelBox(Spike8, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike8.cubeList.add(new ModelBox(Spike8, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike8.cubeList.add(new ModelBox(Spike8, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike8.cubeList.add(new ModelBox(Spike8, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike9 = new ModelRenderer(this);
		Spike9.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike9);
		setRotationAngle(Spike9, 0.0F, 0.0F, 2.3562F);
		Spike9.cubeList.add(new ModelBox(Spike9, 0, 0, -1.0F, -8.25F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike9.cubeList.add(new ModelBox(Spike9, 38, 16, 0.0F, -8.25F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike9.cubeList.add(new ModelBox(Spike9, 0, 0, -0.5F, -10.25F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike9.cubeList.add(new ModelBox(Spike9, 38, 16, 0.0F, -10.25F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike10 = new ModelRenderer(this);
		Spike10.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike10);
		setRotationAngle(Spike10, 1.1345F, -0.7854F, 0.0F);
		Spike10.cubeList.add(new ModelBox(Spike10, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike10.cubeList.add(new ModelBox(Spike10, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike10.cubeList.add(new ModelBox(Spike10, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike10.cubeList.add(new ModelBox(Spike10, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike11 = new ModelRenderer(this);
		Spike11.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike11);
		setRotationAngle(Spike11, 2.0071F, -0.7854F, 0.0F);
		Spike11.cubeList.add(new ModelBox(Spike11, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike11.cubeList.add(new ModelBox(Spike11, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike11.cubeList.add(new ModelBox(Spike11, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike11.cubeList.add(new ModelBox(Spike11, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike12 = new ModelRenderer(this);
		Spike12.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike12);
		setRotationAngle(Spike12, 2.0071F, 0.7854F, 0.0F);
		Spike12.cubeList.add(new ModelBox(Spike12, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike12.cubeList.add(new ModelBox(Spike12, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike12.cubeList.add(new ModelBox(Spike12, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike12.cubeList.add(new ModelBox(Spike12, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike13 = new ModelRenderer(this);
		Spike13.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike13);
		setRotationAngle(Spike13, 1.1345F, 0.7854F, 0.0F);
		Spike13.cubeList.add(new ModelBox(Spike13, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike13.cubeList.add(new ModelBox(Spike13, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike13.cubeList.add(new ModelBox(Spike13, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike13.cubeList.add(new ModelBox(Spike13, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike14 = new ModelRenderer(this);
		Spike14.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike14);
		setRotationAngle(Spike14, 1.1345F, 2.3562F, 0.0F);
		Spike14.cubeList.add(new ModelBox(Spike14, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike14.cubeList.add(new ModelBox(Spike14, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike14.cubeList.add(new ModelBox(Spike14, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike14.cubeList.add(new ModelBox(Spike14, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike15 = new ModelRenderer(this);
		Spike15.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike15);
		setRotationAngle(Spike15, 2.0071F, 2.3562F, 0.0F);
		Spike15.cubeList.add(new ModelBox(Spike15, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike15.cubeList.add(new ModelBox(Spike15, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike15.cubeList.add(new ModelBox(Spike15, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike15.cubeList.add(new ModelBox(Spike15, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike16 = new ModelRenderer(this);
		Spike16.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike16);
		setRotationAngle(Spike16, 2.0071F, -2.3562F, 0.0F);
		Spike16.cubeList.add(new ModelBox(Spike16, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike16.cubeList.add(new ModelBox(Spike16, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike16.cubeList.add(new ModelBox(Spike16, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike16.cubeList.add(new ModelBox(Spike16, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		Spike17 = new ModelRenderer(this);
		Spike17.setRotationPoint(1.0F, -5.0F, 0.0F);
		SpikedHelmet.addChild(Spike17);
		setRotationAngle(Spike17, 1.1345F, -2.3562F, 0.0F);
		Spike17.cubeList.add(new ModelBox(Spike17, 0, 0, -1.0F, -8.75F, 0.0F, 2, 2, 0, 0.0F, false));
		Spike17.cubeList.add(new ModelBox(Spike17, 38, 16, 0.0F, -8.75F, -1.0F, 0, 2, 2, 0.0F, false));
		Spike17.cubeList.add(new ModelBox(Spike17, 0, 0, -0.5F, -10.75F, 0.0F, 1, 2, 0, 0.0F, false));
		Spike17.cubeList.add(new ModelBox(Spike17, 38, 16, 0.0F, -10.75F, -0.5F, 0, 2, 1, 0.0F, false));

		bipedHead.addChild(Head);
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