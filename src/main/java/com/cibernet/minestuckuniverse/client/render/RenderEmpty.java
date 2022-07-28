package com.cibernet.minestuckuniverse.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderEmpty extends Render<Entity>
{
	public RenderEmpty(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}
