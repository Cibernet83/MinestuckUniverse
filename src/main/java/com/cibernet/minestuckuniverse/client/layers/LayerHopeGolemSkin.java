package com.cibernet.minestuckuniverse.client.layers;

import com.cibernet.minestuckuniverse.client.render.RenderHopeGolem;
import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerHopeGolemSkin implements LayerRenderer<EntityHopeGolem> {

	final RenderHopeGolem golemRenderer;
	final ModelIronGolem golemModel;

	public LayerHopeGolemSkin(RenderHopeGolem renderer)
	{
		golemRenderer = renderer;
		golemModel = (ModelIronGolem) renderer.getMainModel();
	}

	@Override
	public void doRenderLayer(EntityHopeGolem golem, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{

		boolean isVisible = golemRenderer.isVisible(golem);

		if (isVisible || (!isVisible && !golem.isInvisibleToPlayer(Minecraft.getMinecraft().player)))
		{
			this.golemRenderer.bindTexture(golemRenderer.getEntityTexture(golem));

			float a = Math.max(0.1f, (float)golem.getHopeTicks()/(float)(EntityHopeGolem.MAX_HOPE_TICKS/2) + 0.1f);

			GlStateManager.color(1, 1, 1, a);
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

			this.golemModel.setModelAttributes(this.golemRenderer.getMainModel());
			this.golemModel.setLivingAnimations(golem, limbSwing, limbSwingAmount, partialTicks);
			this.golemModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1.0F, golem);
			this.golemModel.render(golem, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.disableBlend();
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
