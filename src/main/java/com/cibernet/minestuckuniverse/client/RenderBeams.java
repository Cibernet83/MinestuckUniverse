package com.cibernet.minestuckuniverse.client;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderBeams
{
	@SubscribeEvent
	public static void onWorldRender(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if(mc.player == null)
			return;

		for(Beam beam : mc.player.world.getCapability(MSUCapabilities.BEAM_DATA, null).getBeams())
		{
			mc.getTextureManager().bindTexture(beam.getTexture());

			GlStateManager.glTexParameteri(3553, 10242, 10497);
			GlStateManager.glTexParameteri(3553, 10243, 10497);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			//GlStateManager.disableBlend();
			//GlStateManager.depthMask(true);
			//GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.depthMask(false);

			Vec3d start = beam.getStartPoint(event.getPartialTicks());
			Vec3d end = beam.getEndPoint(event.getPartialTicks());

			float beamRadius = beam.getRadius();

			float f2 = (float)end.x;
			float f3 = (float)end.y;
			float f4 = (float)end.z;
			double x = (double)f2 - start.x + (start.x-Beam.lerp(mc.player.prevPosX, mc.player.posX, event.getPartialTicks()));
			double y = (double)f3 - start.y + (start.y-Beam.lerp(mc.player.prevPosY, mc.player.posY, event.getPartialTicks()));
			double z = (double)f4 - start.z + (start.z-Beam.lerp(mc.player.prevPosZ, mc.player.posZ, event.getPartialTicks()));

			renderCrystalBeams(x, y-1, z, event.getPartialTicks(), (double)f2, (double)f3, (double)f4, start.x, start.y, start.z, beamRadius, beam.color, beam.getAlpha());

			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
		}
	}


	public static void renderCrystalBeams(double drawPosX, double drawPosY, double drawPosZ, float partialTicks, double endX, double endY, double endZ, double startX, double startY, double startZ, float radius, int color, float alpha)
	{
		float f = (float)(startX - endX);
		float f1 = (float)(startY - 1.0D - endY);
		float f2 = (float)(startZ - endZ);
		float f3 = MathHelper.sqrt(f * f + f2 * f2);
		float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
		GlStateManager.pushMatrix();

		float vOff = Minecraft.getMinecraft().world.getWorldTime()*-0.05f + partialTicks;

		GlStateManager.enableBlend();

		GlStateManager.translate((float)drawPosX, (float)drawPosY + 2.0F, (float)drawPosZ);
		GlStateManager.rotate((float)(-Math.atan2((double)f2, (double)f)) * (180F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float)(-Math.atan2((double)f3, (double)f1)) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		float f5 = 0.0F - vOff * radius;
		float f6 = f4 / 32.0F - vOff * radius;
		bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		int i = 8;


		float r = (float)((color & 16711680) >> 16) / 255.0F;
		float g = (float)((color & '\uff00') >> 8) / 255.0F;
		float b = (float)((color & 255) >> 0) / 255.0F;

		for (int j = 0; j <= i; ++j)
		{
			float f7 = MathHelper.sin((float)(j % i) * ((float)Math.PI * 2F) / 8.0F) * 0.1F;
			float f8 = MathHelper.cos((float)(j % i) * ((float)Math.PI * 2F) / 8.0F) * 0.1F;
			float f9 = (float)(j % i) / (float)i;

			bufferbuilder.pos((double)(f7 * 0.2F), (double)(f8 * 0.2F), 0.0D).tex((double)f9, (double)f5).color(r, g, b, alpha).endVertex();
			bufferbuilder.pos((double)f7, (double)f8, (double)f4).tex((double)f9, (double)f6).color(r, g, b, alpha).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();

		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}
}
