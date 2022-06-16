package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.client.layers.LayerHopeGolemFlower;
import com.cibernet.minestuckuniverse.client.layers.LayerHopeGolemSkin;
import com.cibernet.minestuckuniverse.entity.EntityHopeGolem;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderHopeGolem extends RenderLiving<EntityHopeGolem>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(MinestuckUniverse.MODID, "textures/entity/hope_golem.png");
	private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(MinestuckUniverse.MODID, "textures/entity/hope_golem_enraged.png");

	//

	public RenderHopeGolem(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelIronGolem(), 0.5f);
		this.addLayer(new LayerHopeGolemFlower(this));
		this.addLayer(new LayerHopeGolemSkin(this));
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityHopeGolem entity) {
		return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
	}

	@Override
	protected void applyRotations(EntityHopeGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);

		if ((double)entityLiving.limbSwingAmount >= 0.01D)
		{
			float f = 13.0F;
			float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
			float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
			GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	public boolean isVisible(EntityHopeGolem golem) {
		return super.isVisible(golem);
	}

	@Override
	protected void renderModel(EntityHopeGolem golem, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
	}
}
