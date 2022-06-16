package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench
// Paste this code into your mod.
// Make sure to generate all required imports

import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelGTKnight extends ModelGTAbstract
{
	public ModelGTKnight() {
		super(128,128, EnumClass.KNIGHT);
		addColorIgnores(6, 7, 8);
		
		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 44, 35, -4.0F, -0.0F, -3.0F, 8, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 44, 38, -4.0F, -0.0F, 2.2F, 8, 2, 1, 0.0F, false));
		
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 34, 34, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 42, 0, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 50, 9, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 32, 3, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 16, 52, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 34, 50, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 18, 20, -4.0F, -0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(cape, 0, 20, -4.0F, 1.0F, 2.0F, 8, 22, 1, 0.0F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 48, 48, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 44, 19, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 43, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 18, 36, -2F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(-1.9F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 59, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightFoot.setRotationPoint(1.9F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 16, 59, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}