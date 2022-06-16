package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelGTRogue extends ModelGTAbstract
{
	private final ModelRenderer mask;

	public ModelGTRogue()
	{
		super(128, 128, EnumClass.ROGUE);
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

		mask = new ModelRenderer(this);
		mask.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.addChild(mask);
		mask.cubeList.add(new ModelBox(mask, 0, 40, -4.0F, -32.0F, -4.0F, 8, 8, 8, 0.1F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 96, 0, -5.0F, -0.75F, -3.0F, 10, 2, 6, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 80, 112, -4.0F, -1.5F, -2.0F, 8, 12, 4, 0.2515F, false));

		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.cubeList.add(new ModelBox(cape, 110, 93, -4.0F, 1.0F, 2.0F, 8, 7, 1, 0.0F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));
		torso.cubeList.add(new ModelBox(torso, 104, 106, -4.0F, 12.0F, -2.0F, 8, 2, 4, 0.2507F, false));

		skirtFront.setRotationPoint(0.0F, 11.5F, 0.0F);
		skirtFront.cubeList.add(new ModelBox(skirtFront, 55, 122, -4.0F, 0.5F, -2.0F, 8, 2, 4, 0.251F, false));

		skirtBack.setRotationPoint(0.0F, 11.5F, 0.0F);
		skirtBack.cubeList.add(new ModelBox(skirtBack, 55, 116, -4.0F, 0.5F, -2.0F, 8, 2, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2505F, true));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2515F, true));
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		mask.isHidden = hideExtras;
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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