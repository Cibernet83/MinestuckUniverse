package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderArrow extends net.minecraft.client.renderer.entity.RenderArrow<EntityMSUArrow>
{
	public RenderArrow(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityMSUArrow entity) {
		return entity.getArrowTexture();
	}
}
