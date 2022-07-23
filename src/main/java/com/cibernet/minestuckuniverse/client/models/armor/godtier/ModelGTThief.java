package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelGTThief extends ModelGTAbstract
{
	private final ModelRenderer hood;
	
	public ModelGTThief()
	{
		super(128, 128, EnumClass.THIEF);
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
		hood.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 110, 8, -4.0F, -9.0F, 4.5F, 8, 8, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 110, 8, -4.0F, -9.0F, 6.0F, 8, 8, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 17, -3.0F, -8.0F, 6.5F, 6, 6, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 17, -3.0F, -8.5F, 8.0F, 6, 6, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 24, -2.0F, -7.5F, 8.5F, 4, 4, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 118, 24, -2.0F, -8.0F, 10.0F, 4, 4, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 29, -1.0F, -9.0F, 11.0F, 2, 4, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 34, -0.5F, -10.0F, 12.0F, 1, 3, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 38, -0.5F, -10.5F, 11.5F, 1, 1, 1, 0.0F, false));
		
		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, 2.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, -3.0F, 10, 1, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 104, 96, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.2515F, false));
		
		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(cape, 106, 52, -5.0F, 1.0F, 2.0F, 10, 1, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 118, 54, 0.5F, 2.0F, 2.0F, 4, 8, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 120, 63, 1.0F, 10.0F, 2.0F, 3, 6, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 122, 70, 1.5F, 16.0F, 2.0F, 2, 5, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 124, 76, 2.0F, 21.0F, 2.0F, 1, 3, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 118, 54, -4.5F, 2.0F, 2.0F, 4, 8, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 120, 63, -4.0F, 10.0F, 2.0F, 3, 6, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 122, 70, -3.5F, 16.0F, 2.0F, 2, 5, 1, 0.0F, false));
		cape.cubeList.add(new ModelBox(cape, 124, 76, -3.0F, 21.0F, 2.0F, 1, 3, 1, 0.0F, false));
		
		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));
		
		skirtFront.setRotationPoint(0.0F, 12.0F, 0.0F);
		skirtFront.cubeList.add(new ModelBox(skirtFront, 80, 122, -4.0F, 0.0F, -2.0F, 8, 2, 4, 0.251F, false));
		
		skirtBack.setRotationPoint(0.0F, 12.0F, 0.0F);
		skirtBack.cubeList.add(new ModelBox(skirtBack, 80, 116, -4.0F, 0.0F, -2.0F, 8, 2, 4, 0.251F, false));
		
		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));
		
		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));
		
		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, false));
		
		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, true));
		
		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.255F, false));
		rightFoot.cubeList.add(new ModelBox(rightFoot, 16, 123, -2.0F, 6.0F, -2.0F, 4, 1, 4, 0.5F, false));
		
		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.255F, false));
		leftFoot.cubeList.add(new ModelBox(leftFoot, 16, 123, -2.0F, 6.0F, -2.0F, 4, 1, 4, 0.5F, true));
	}
	
	@Override
	public EntityEquipmentSlot getSkirtSlot()
	{
		return EntityEquipmentSlot.CHEST;
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}