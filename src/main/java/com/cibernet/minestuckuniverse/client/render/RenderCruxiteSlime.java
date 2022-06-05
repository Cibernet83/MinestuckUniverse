package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.client.layers.CruxiteSlimeGelLayer;
import com.cibernet.minestuckuniverse.client.models.ModelCruxiteSlime;
import com.cibernet.minestuckuniverse.entity.EntityCruxiteSlime;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCruxiteSlime extends RenderLiving<EntityCruxiteSlime>
{
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation(MinestuckUniverse.MODID,"textures/entity/cruxite_slime.png");
	
	public RenderCruxiteSlime(RenderManager manager)
	{
		super(manager, new ModelCruxiteSlime(16), 0.25F);
		this.addLayer(new CruxiteSlimeGelLayer(this));
	}
	
	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityCruxiteSlime entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.shadowSize = 0.25F * (float)entity.getSlimeSize();
		
		
		int color = entity.getSlimeColor();
		ItemStack stack = entity.getStoredItem();
		float r = (float) (Math.floor(color / (256*256))/255f);
		float g = (float) ((Math.floor(color / 256) % 256)/255f);
		float b = (color % 256)/255f;
		
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.color(1,1,1);
		GlStateManager.popMatrix();
	}
	
	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	protected void preRenderCallback(EntityCruxiteSlime entitylivingbaseIn, float partialTickTime)
	{
		float f = 0.999F;
		GlStateManager.scale(0.999F, 0.999F, 0.999F);
		float f1 = (float)entitylivingbaseIn.getSlimeSize();
		float f2 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityCruxiteSlime entity)
	{
		return SLIME_TEXTURES;
	}
	
	public static class Factory<T extends EntityCruxiteSlime> implements IRenderFactory<T>
	{
		protected ModelBase modelBase;
		protected float shadowSize;
		
		public Factory() {
		}
		
		public Render<? super T> createRenderFor(RenderManager manager) {
			return new RenderCruxiteSlime(manager);
		}
	}
}
