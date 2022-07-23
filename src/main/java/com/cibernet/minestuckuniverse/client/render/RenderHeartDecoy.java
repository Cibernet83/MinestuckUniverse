package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.entity.EntityHeartDecoy;
import com.mraof.minestuck.client.renderer.entity.RenderDecoy;
import com.mraof.minestuck.entity.EntityDecoy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderHeartDecoy extends RenderLivingBase<EntityHeartDecoy>
{
	private static final ModelPlayer MODEL = new ModelPlayer(0F, false);
	private static final ModelPlayer MODEL_SLIM = new ModelPlayer(0F, true);

	public RenderHeartDecoy(RenderManager manager) {
		super(manager, MODEL, 0F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
	}

	@Override
	public void doRender(EntityHeartDecoy entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		mainModel = isSlim(entity) == 1 ? MODEL_SLIM : MODEL;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityHeartDecoy entity)
	{
		return entity.getLocationSkin();
	}
	
	protected int isSlim(EntityHeartDecoy entity)
	{
		return entity.isSlim();
	}
}
