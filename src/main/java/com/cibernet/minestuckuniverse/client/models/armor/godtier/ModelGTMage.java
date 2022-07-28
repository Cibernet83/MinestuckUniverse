package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports

import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelGTMage extends ModelGTAbstract
{
	private final ModelRenderer hood;

	public ModelGTMage()
	{
		super(128, 128, EnumClass.MAGE);
		addColorIgnores(6, 7, 8);

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 110, 8, -4.0F, -32.0F, 4.5F, 8, 7, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 108, 16, -4.0F, -32.0F, 5.0F, 8, 7, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 25, -2.0F, -30.0F, 6.0F, 4, 4, 1, 1.0F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 30, -2.0F, -30.0F, 7.0F, 4, 4, 2, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 36, -1.0F, -28.0F, 8.5F, 2, 2, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 39, -0.5F, -27.5F, 9.0F, 1, 1, 1, 0.0F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, -3.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, 2.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 94, 96, -4.0F, -0.1F, -2.0F, 8, 12, 4, 0.252F, false));

		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(cape, 110, 78, -4.0F, 1.0F, 2.0F, 8, 8, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 106, 87, -5.0F, 9.0F, 2.0F, 10, 7, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 118, 95, -5.0F, 16.0F, 2.0F, 4, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 118, 95, 1.0F, 16.0F, 2.0F, 4, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 120, 98, -5.0F, 18.0F, 2.0F, 3, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 120, 98, 2.0F, 18.0F, 2.0F, 3, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 122, 101, -5.0F, 20.0F, 2.0F, 2, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 122, 101, 3.0F, 20.0F, 2.0F, 2, 2, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 124, 104, -5.0F, 22.0F, 2.0F, 1, 1, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 124, 104, 4.0F, 22.0F, 2.0F, 1, 1, 1, 0.0F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, true));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, false));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		skirtFront.setRotationPoint(0.0F, 10.0F, -2.252F);
		skirtFront.cubeList.add(new ModelBox(skirtFront, 77, 115, -4.0F, 0.0F, -0F, 8, 7, 0, 0.0F, false));
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
	public EntityEquipmentSlot getSkirtSlot()
	{
		return EntityEquipmentSlot.CHEST;
	}
}