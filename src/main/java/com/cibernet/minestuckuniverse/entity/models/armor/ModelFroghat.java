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

public class ModelFroghat extends ModelBiped {
	private final ModelRenderer froghat;

	public ModelFroghat() {
		bipedHeadwear.cubeList.remove(0);
		bipedHead.cubeList.remove(0);
		textureWidth = 32;
		textureHeight = 32;

		froghat = new ModelRenderer(this);
		froghat.setRotationPoint(0.0F, 0.0F, 0.0F);
		froghat.cubeList.add(new ModelBox(froghat, 16, 9, -4.0F, -11.0F, -4.5F, 3, 3, 2, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 18, 1.0F, -11.0F, -4.5F, 3, 3, 2, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 20, 4, -1.0F, -10.0F, -4.0F, 2, 2, 2, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 6, -4.5F, -8.0F, -4.0F, 0, 7, 5, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 10, 19, -4.5F, -8.0F, 1.0F, 0, 5, 3, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 2, -4.5F, -0.5F, -4.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 2, 4, -4.5F, -0.5F, -2.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 2, 2, -4.5F, -0.5F, 0.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 10, 10, 4.0F, -8.0F, -4.0F, 0, 7, 5, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 16, 19, 4.0F, -8.0F, 1.0F, 0, 5, 3, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 3, 4.0F, -0.5F, -4.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 2, 3, 4.0F, -0.5F, -2.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 4, 4.0F, -0.5F, 0.0F, 0, 1, 1, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 6, -4.5F, -8.0F, 4.0F, 9, 5, 0, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 0, -1.5F, -3.0F, 4.0F, 3, 1, 0, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 0, -4.0F, -8.0F, -2.0F, 8, 0, 6, 0.0F, false));
		froghat.cubeList.add(new ModelBox(froghat, 0, 1, -1.0F, -8.0F, -4.5F, 2, 2, 0, 0.0F, false));

		bipedHead.addChild(froghat);
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