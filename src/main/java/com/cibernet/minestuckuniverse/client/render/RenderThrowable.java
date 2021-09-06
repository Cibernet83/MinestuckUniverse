package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderThrowable extends Render<EntityMSUThrowable>
{

    private final RenderItem itemRenderer;

    public RenderThrowable(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
    }

    public void doRender(EntityMSUThrowable entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(entity.getProjectileSize(), entity.getProjectileSize(), entity.getProjectileSize());
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        IRenderProperties properties = entity.getRenderProperties();

        if(properties != null)
            properties.pre(entity, partialTicks);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        if(properties != null)
            properties.post(entity, partialTicks);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack getStackToRender(EntityMSUThrowable entityIn) {
        return entityIn.getStack();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMSUThrowable entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public interface IRenderProperties
    {
        void pre(EntityMSUThrowable entity, float partialTicks);

        default void post(EntityMSUThrowable entity, float partialTicks)
        {

        }
    }
}
