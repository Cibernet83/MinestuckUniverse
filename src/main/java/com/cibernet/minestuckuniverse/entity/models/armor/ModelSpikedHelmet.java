package com.cibernet.minestuckuniverse.entity.models.armor;
//Made by Ishumire
//Paste this code into your mod.

import net.minecraft.client.model.ModelBiped;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSpikedHelmet extends ModelBiped {
	private final ModelRenderer Helmet;
	private final ModelRenderer Main;
	private final ModelRenderer Bottom;
	private final ModelRenderer Top;
	private final ModelRenderer Windows;
	private final ModelRenderer Spikes;
	private final ModelRenderer Spike_1;
	private final ModelRenderer Spike_2;
	private final ModelRenderer Spike_3;
	private final ModelRenderer Spike_4;
	private final ModelRenderer Spike_5;
	private final ModelRenderer Spike_6;
	private final ModelRenderer Spike_7;
	private final ModelRenderer Spike_8;
	private final ModelRenderer Spike_9;
	private final ModelRenderer Spike_10;
	private final ModelRenderer Spike_11;
	private final ModelRenderer Spike_12;
	private final ModelRenderer Spike_13;
	private final ModelRenderer Spike_14;
	private final ModelRenderer Spike_15;
	private final ModelRenderer Spike_16;
	private final ModelRenderer Spike_17;

	public ModelSpikedHelmet() {
		bipedHead.cubeList.remove(0);
		textureWidth = 128;
		textureHeight = 128;

		Helmet = new ModelRenderer(this);
		Helmet.setRotationPoint(0.0F, 0.0F, 0.0F);

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

		Spikes = new ModelRenderer(this);
		Spikes.setRotationPoint(0.0F, -5.0F, 0.0F);
		Helmet.addChild(Spikes);

		Spike_1 = new ModelRenderer(this);
		Spike_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_1, 0.0F, 0.0F, 0.7854F);
		Spikes.addChild(Spike_1);
		Spike_1.cubeList.add(new ModelBox(Spike_1, 0, 0, -0.5F, -9.2218F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_1.cubeList.add(new ModelBox(Spike_1, 0, 0, -0.75F, -8.2218F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_1.cubeList.add(new ModelBox(Spike_1, 0, 0, -1.0F, -7.2218F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_2 = new ModelRenderer(this);
		Spike_2.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_2, 0.0F, 0.0F, -0.7854F);
		Spikes.addChild(Spike_2);
		Spike_2.cubeList.add(new ModelBox(Spike_2, 0, 0, -0.5F, -9.2218F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_2.cubeList.add(new ModelBox(Spike_2, 0, 0, -0.75F, -8.2218F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_2.cubeList.add(new ModelBox(Spike_2, 0, 0, -1.0F, -7.2218F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_3 = new ModelRenderer(this);
		Spike_3.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_3, 0.7854F, 0.0F, 0.0F);
		Spikes.addChild(Spike_3);
		Spike_3.cubeList.add(new ModelBox(Spike_3, 0, 0, -0.5F, -9.2218F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_3.cubeList.add(new ModelBox(Spike_3, 0, 0, -0.75F, -8.2218F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_3.cubeList.add(new ModelBox(Spike_3, 0, 0, -1.0F, -7.2218F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_4 = new ModelRenderer(this);
		Spike_4.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_4, -0.7854F, 0.0F, 0.0F);
		Spikes.addChild(Spike_4);
		Spike_4.cubeList.add(new ModelBox(Spike_4, 0, 0, -0.5F, -9.2218F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_4.cubeList.add(new ModelBox(Spike_4, 0, 0, -0.75F, -8.2218F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_4.cubeList.add(new ModelBox(Spike_4, 0, 0, -1.0F, -7.2218F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_5 = new ModelRenderer(this);
		Spike_5.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_5, -1.5708F, 0.0F, 0.0F);
		Spikes.addChild(Spike_5);
		Spike_5.cubeList.add(new ModelBox(Spike_5, 0, 0, -0.5F, -8.7218F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_5.cubeList.add(new ModelBox(Spike_5, 0, 0, -0.75F, -7.7218F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_5.cubeList.add(new ModelBox(Spike_5, 0, 0, -1.0F, -6.7218F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_6 = new ModelRenderer(this);
		Spike_6.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_6, 1.0472F, -0.7854F, 0.0F);
		Spikes.addChild(Spike_6);
		Spike_6.cubeList.add(new ModelBox(Spike_6, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_6.cubeList.add(new ModelBox(Spike_6, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_6.cubeList.add(new ModelBox(Spike_6, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_7 = new ModelRenderer(this);
		Spike_7.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_7, 1.0472F, 0.7854F, 0.0F);
		Spikes.addChild(Spike_7);
		Spike_7.cubeList.add(new ModelBox(Spike_7, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_7.cubeList.add(new ModelBox(Spike_7, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_7.cubeList.add(new ModelBox(Spike_7, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_8 = new ModelRenderer(this);
		Spike_8.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_8, 1.0472F, 2.3562F, 0.0F);
		Spikes.addChild(Spike_8);
		Spike_8.cubeList.add(new ModelBox(Spike_8, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_8.cubeList.add(new ModelBox(Spike_8, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_8.cubeList.add(new ModelBox(Spike_8, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_9 = new ModelRenderer(this);
		Spike_9.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_9, 1.0472F, -2.3562F, 0.0F);
		Spikes.addChild(Spike_9);
		Spike_9.cubeList.add(new ModelBox(Spike_9, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_9.cubeList.add(new ModelBox(Spike_9, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_9.cubeList.add(new ModelBox(Spike_9, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_10 = new ModelRenderer(this);
		Spike_10.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_10, 2.0944F, -2.3562F, 0.0F);
		Spikes.addChild(Spike_10);
		Spike_10.cubeList.add(new ModelBox(Spike_10, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_10.cubeList.add(new ModelBox(Spike_10, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_10.cubeList.add(new ModelBox(Spike_10, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_11 = new ModelRenderer(this);
		Spike_11.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_11, 2.0944F, -0.7854F, 0.0F);
		Spikes.addChild(Spike_11);
		Spike_11.cubeList.add(new ModelBox(Spike_11, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_11.cubeList.add(new ModelBox(Spike_11, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_11.cubeList.add(new ModelBox(Spike_11, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_12 = new ModelRenderer(this);
		Spike_12.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_12, 2.0944F, 0.7854F, 0.0F);
		Spikes.addChild(Spike_12);
		Spike_12.cubeList.add(new ModelBox(Spike_12, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_12.cubeList.add(new ModelBox(Spike_12, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_12.cubeList.add(new ModelBox(Spike_12, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_13 = new ModelRenderer(this);
		Spike_13.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_13, 2.0944F, 2.3562F, 0.0F);
		Spikes.addChild(Spike_13);
		Spike_13.cubeList.add(new ModelBox(Spike_13, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_13.cubeList.add(new ModelBox(Spike_13, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_13.cubeList.add(new ModelBox(Spike_13, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_14 = new ModelRenderer(this);
		Spike_14.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_14, 1.5708F, 2.3562F, 0.0F);
		Spikes.addChild(Spike_14);
		Spike_14.cubeList.add(new ModelBox(Spike_14, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_14.cubeList.add(new ModelBox(Spike_14, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_14.cubeList.add(new ModelBox(Spike_14, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_15 = new ModelRenderer(this);
		Spike_15.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_15, 1.5708F, 0.7854F, 0.0F);
		Spikes.addChild(Spike_15);
		Spike_15.cubeList.add(new ModelBox(Spike_15, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_15.cubeList.add(new ModelBox(Spike_15, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_15.cubeList.add(new ModelBox(Spike_15, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_16 = new ModelRenderer(this);
		Spike_16.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_16, 1.5708F, -0.7854F, 0.0F);
		Spikes.addChild(Spike_16);
		Spike_16.cubeList.add(new ModelBox(Spike_16, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_16.cubeList.add(new ModelBox(Spike_16, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_16.cubeList.add(new ModelBox(Spike_16, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		Spike_17 = new ModelRenderer(this);
		Spike_17.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(Spike_17, 1.5708F, -2.3562F, 0.0F);
		Spikes.addChild(Spike_17);
		Spike_17.cubeList.add(new ModelBox(Spike_17, 0, 0, -0.5F, -9.9718F, -0.435F, 1, 1, 1, 0.0F, false));
		Spike_17.cubeList.add(new ModelBox(Spike_17, 0, 0, -0.75F, -8.9718F, -0.685F, 1, 1, 1, 0.0F, false));
		Spike_17.cubeList.add(new ModelBox(Spike_17, 0, 0, -1.0F, -7.9718F, -0.935F, 2, 1, 2, 0.0F, false));

		bipedHead.addChild(Helmet);
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