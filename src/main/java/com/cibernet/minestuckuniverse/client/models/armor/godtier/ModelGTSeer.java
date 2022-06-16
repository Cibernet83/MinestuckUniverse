package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelGTSeer extends ModelGTAbstract
{

	public ModelGTSeer()
	{
		super(128, 128, EnumClass.SEER);
		addColorIgnores(6, 7, 8);

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.2515F, false));

		skirtBack.setRotationPoint(0.0F, 10.0F, 2.252F);
		skirtBack.cubeList.add(new ModelBox(skirtBack, 59, 115, -4.0F, 0.0F, 0.1F, 8, 12, 0, 0.0F, false));

		skirtFront.setRotationPoint(0.0F, 10.0F, -2.252F);
		skirtFront.cubeList.add(new ModelBox(skirtFront, 77, 115, -4.0F, 0.0F, -0F, 8, 12, 0, 0.0F, false));
		
		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 16, 56, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));
		
		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 56, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 80, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F+0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F+0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F+0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F+0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F+0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F+0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F+0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, -3.0F, 10, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, 2.0F, 10, 2, 1, 0.0F, false));

		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(cape, 110, 102, -4.0F, 1.0F, 2.0F, 8, 7, 1, 0.0F, false));
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