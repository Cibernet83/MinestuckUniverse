package com.cibernet.minestuckuniverse.client.models;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRock extends ModelBase {
	private final ModelRenderer rock;

	public ModelRock()
	{
		textureWidth = 256;
		textureHeight = 256;

		rock = new ModelRenderer(this);
		rock.setRotationPoint(0.0F, 24.0F, 0.0F);
		rock.cubeList.add(new ModelBox(rock, 0, 0, -16.0F, -24.0F, -16.0F, 32, 24, 32, 0.0F, false));
		rock.cubeList.add(new ModelBox(rock, 88, 84, -12.0F, -32.0F, -12.0F, 24, 4, 24, 0.0F, false));
		rock.cubeList.add(new ModelBox(rock, 0, 56, -12.0F, -28.0F, -16.0F, 24, 4, 32, 0.0F, false));
		rock.cubeList.add(new ModelBox(rock, 80, 56, -16.0F, -28.0F, -12.0F, 32, 4, 24, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		rock.render(f5);
	}
}