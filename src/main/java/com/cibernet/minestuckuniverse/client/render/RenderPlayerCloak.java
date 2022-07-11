package com.cibernet.minestuckuniverse.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderPlayerCloak extends RenderPlayer
{
	public ResourceLocation texture;

	public RenderPlayerCloak(RenderManager renderManager, boolean useSmallArms, ResourceLocation texture)
	{
		super(renderManager, useSmallArms);
		this.texture = texture;
	}

	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity)
	{
		return texture;
	}
}
