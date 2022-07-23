package com.cibernet.minestuckuniverse.client.render;

import com.cibernet.minestuckuniverse.client.layers.LayerConsortCosmetics;
import com.cibernet.minestuckuniverse.client.models.consort.ModelConsort;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.entity.consort.EntityConsort;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;

public class RenderConsort extends RenderEntityMinestuck<EntityConsort>
{
	public RenderConsort(RenderManager manager, ModelConsort model, float shadowSize)
	{
		super(manager, model, shadowSize);
		//addLayer(new LayerCustomHead(model.head));
		addLayer(new LayerConsortCosmetics(this, model.hood, model.head, model.head));
		addLayer(new LayerCustomHead(model.head));
	}
}
