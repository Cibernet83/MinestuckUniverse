package com.cibernet.minestuckuniverse.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBubble extends ModelBase
{
	/** The slime's bodies, both the inside box and the outside box */
	ModelRenderer main;

	public ModelBubble()
	{
		this.main = new ModelRenderer(this, 0, 0);
		this.main.addBox(-8.0F, -16f, -8.0F, 16, 16, 16);
	}
	
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		//GlStateManager.translate(0.0F, 0.001F, 0.0F);
		this.main.render(scale);
	}
}