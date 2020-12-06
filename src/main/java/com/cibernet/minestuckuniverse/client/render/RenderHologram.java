package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderHologram extends TileEntitySpecialRenderer<TileEntityHolopad>
{
	@Override
	public void render(TileEntityHolopad te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		ItemStack item = AlchemyRecipes.getDecodedItem(te.getCard());
		float f = (float)te.innerRotation + partialTicks;
		float scale = 0.8F;
		GlStateManager.color(0.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.pushMatrix();
		//GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate((float)x + 0.5F, (float)y + 0.6F, (float)z + 0.5F);
		GlStateManager.rotate(f / 20.0F * 57.295776F, 0.0F, 1.0F, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.popMatrix();
	}
}
