package com.cibernet.minestuckuniverse.entity.models;//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelAcheron extends ModelBase {
private final ModelRenderer body;
private final ModelRenderer bone6;
private final ModelRenderer head;
private final ModelRenderer bone5;
private final ModelRenderer arm1;
private final ModelRenderer bone4;
private final ModelRenderer arm0;
private final ModelRenderer bone3;
private final ModelRenderer leg0;
private final ModelRenderer leg1;

public ModelAcheron() {
		textureWidth = 128;
		textureHeight = 128;
		
		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -7.0F, 0.0F);
		
		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, 9.0F, 0.5F);
		body.addChild(bone6);
		bone6.cubeList.add(new ModelBox(bone6, 0, 0, -9.0F, -18.0F, -8.5F, 18, 18, 13, 0.0F, false));
		
		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -7.0F, -2.0F);
		
		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -4.0F, -2.0F);
		head.addChild(bone5);
		bone5.cubeList.add(new ModelBox(bone5, 0, 82, -2.9843F, -0.9983F, -3.4825F, 7, 8, 5, 0.0F, false));
		
		arm1 = new ModelRenderer(this);
		arm1.setRotationPoint(0.0F, -7.0F, 0.0F);
		
		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-12.0F, 9.0F, -5.0F);
		arm1.addChild(bone4);
		bone4.cubeList.add(new ModelBox(bone4, 34, 34, -4.0F, -0.5F, -4.0F, 8, 23, 8, 0.0F, false));
		
		arm0 = new ModelRenderer(this);
		arm0.setRotationPoint(0.0F, -7.0F, 0.0F);
		
		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(12.5F, 8.5F, -5.0F);
		arm0.addChild(bone3);
		bone3.cubeList.add(new ModelBox(bone3, 0, 31, -4.5F, 0.0F, -4.0F, 9, 22, 8, 0.0F, false));
		
		leg0 = new ModelRenderer(this);
		leg0.setRotationPoint(4.0F, 11.0F, 0.0F);
		
		leg1 = new ModelRenderer(this);
		leg1.setRotationPoint(-5.0F, 11.0F, 0.0F);
		}

@Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		body.render(f5);
		head.render(f5);
		arm1.render(f5);
		arm0.render(f5);
		leg0.render(f5);
		leg1.render(f5);
		}
public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
		}
		}