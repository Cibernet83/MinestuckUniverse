package com.cibernet.minestuckuniverse.potions;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MSUPotionBase extends Potion
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/potions.png");
    private final int index;

    private static int currentIndex = 0;

    protected MSUPotionBase(boolean isBadEffectIn, int liquidColorIn, String name)
    {
        super(isBadEffectIn, liquidColorIn);
        this.index = currentIndex++;
        setPotionName("effect.minestuckuniverse."+name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        this.renderEffect(x + 6, y + 7, 1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
    {
        this.renderEffect(x + 3, y + 3, alpha);
    }


    @SideOnly(Side.CLIENT)
    protected void renderEffect(int x, int y, float alpha)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        int textureX = this.index % 14 * 18;
        int textureY = this.index / 14 * 18;
        buf.pos((double)x, (double)(y + 18), 0.0D).tex((double)textureX * 0.00390625D, (double)(textureY + 18) * 0.00390625D).endVertex();
        buf.pos((double)(x + 18), (double)(y + 18), 0.0D).tex((double)(textureX + 18) * 0.00390625D, (double)(textureY + 18) * 0.00390625D).endVertex();
        buf.pos((double)(x + 18), (double)y, 0.0D).tex((double)(textureX + 18) * 0.00390625D, (double)textureY * 0.00390625D).endVertex();
        buf.pos((double)x, (double)y, 0.0D).tex((double)textureX * 0.00390625D, (double)textureY * 0.00390625D).endVertex();
        tessellator.draw();
    }
}
