package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.client.models.ModelBubble;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBubble extends Render<EntityBubble>
{
	private static final ModelBubble MODEL = new ModelBubble();
	private static final ResourceLocation TEXTURE = new ResourceLocation(MinestuckUniverse.MODID,"textures/entity/bubble.png");

	public RenderBubble(RenderManager renderManager)
	{
		super(renderManager);
	}



	@Override
	public void doRender(EntityBubble entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		float size = entity.getBubbleSize()*2f;
		int color = entity.getColor();

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.depthMask(true);

		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-size, -size, size);
		this.bindEntityTexture(entity);
		GlStateManager.color((float) (Math.floor(color / (256*256))/255f), (float) ((Math.floor(color / 256) % 256)/255f), (color % 256)/255f, Math.min(1, entity.ticksExisted/20f) * entity.getLifespan() < 0 ? 1 : Math.min(1, entity.getLifespan()/10f));
		MODEL.render(entity, 0.0F, 0.0F, 0.0F, 0, 0, 0.03125F);

		GlStateManager.disableNormalize();
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

	}



	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity)
	{
		return TEXTURE;
	}
}
