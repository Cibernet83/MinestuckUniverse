package com.cibernet.minestuckuniverse.client.layers;

import com.cibernet.minestuckuniverse.client.render.RenderCruxiteSlime;
import com.cibernet.minestuckuniverse.entity.EntityCruxiteSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;

public class CruxiteSlimeGelLayer implements LayerRenderer<EntityCruxiteSlime>
{
	private final RenderCruxiteSlime slimeRenderer;
	private final ModelBase slimeModel = new ModelSlime(0);
	
	public CruxiteSlimeGelLayer(RenderCruxiteSlime slimeRendererIn)
	{
		this.slimeRenderer = slimeRendererIn;
	}
	
	public void doRenderLayer(EntityCruxiteSlime entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!entity.isInvisible())
		{
			int color = entity.getSlimeColor();
			ItemStack stack = entity.getStoredItem();
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
			this.slimeModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			
			float f = 0.999F;
			GlStateManager.scale(0.999F, 0.999F, 0.999F);
			float f1 = (float)entity.getSlimeSize();
			float f2 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTicks) / (f1 * 0.5F + 1.0F);
			float f3 = 1.0F / (f2 + 1.0F);
			GlStateManager.scale(1/(f3*f1), 1/(1.0F / f3*f1), 1/(f3*f1));
			GlStateManager.translate(0,f1*0.94f,0);
			
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}
	
	public boolean shouldCombineTextures()
	{
		return true;
	}
}
