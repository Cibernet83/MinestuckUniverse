package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderThrowable<T extends EntityMSUThrowable> extends RenderSnowball<T>
{

    public RenderThrowable(RenderManager renderManagerIn, Item defaultItem, RenderItem itemRendererIn)
    {
        super(renderManagerIn, defaultItem, itemRendererIn);
    }

    @Override
    public ItemStack getStackToRender(T entityIn) {
        return entityIn.getStack();
    }
}
