package com.cibernet.minestuckuniverse.client.models.armor.godtier;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.cibernet.minestuckuniverse.items.godtier.ItemGTArmor;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelGTSylph extends ModelGTAbstract
{
	//private final ModelRenderer head;
	private final ModelRenderer hood;
	//private final ModelRenderer neck;
	//private final ModelRenderer torso;
	//private final ModelRenderer rightArm;
	//private final ModelRenderer leftArm;
	//private final ModelRenderer rightLeg;
	//private final ModelRenderer leftLeg;
	//private final ModelRenderer rightFoot;
	//private final ModelRenderer leftFoot;
	//private final ModelRenderer skirtFront;
	private final ModelRenderer skirtFrontCenter;
	private final ModelRenderer hoodThingsParent;
	private final ModelRenderer hoodThings;
	private final ModelRenderer skirtFrontRight;
	private final ModelRenderer skirtFrontLeft;
	//private final ModelRenderer skirtMiddle;
	private final ModelRenderer skirtMiddleRight;
	private final ModelRenderer skirtMiddleLeft;
	//private final ModelRenderer skirtBack;
	private final ModelRenderer skirtBackCenter;
	private final ModelRenderer skirtBackRight;
	private final ModelRenderer skirtBackLeft;
	private final ModelRenderer skirtFrontShirt;
	private final ModelRenderer skirtFrontCenter2;
	private final ModelRenderer skirtFrontRight2;
	private final ModelRenderer skirtFrontLeft2;
	private final ModelRenderer skirtMiddleShirt;
	private final ModelRenderer skirtMiddleRight2;
	private final ModelRenderer skirtMiddleLeft2;
	private final ModelRenderer skirtBackShirt;
	private final ModelRenderer skirtBackCenter2;
	private final ModelRenderer skirtBackRight2;
	private final ModelRenderer skirtBackLeft2;
	private final ModelRenderer hoodSkirtlessLeft;
	private final ModelRenderer hoodSkirtlessRight;

	public ModelGTSylph()
	{
		super(128, 128, EnumClass.SYLPH);
		addColorIgnores(6, 7, 8);

		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -1.0F +0.2f, -5.0F, 10, 1, 10, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 11, -5.0F, -9.0F +0.2f, 4.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 20, -5.0F, -9.0F +0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 11, -5.0F, -9.0F +0.2f, -5.0F, 10, 8, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 20, 4.0F, -9.0F +0.2f, -4.0F, 1, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 0, -5.0F, -10.0F +0.2f, -4.0F, 10, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 18, 36, -4.0F, -10.0F +0.2f, 4.0F, 8, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -10.0F +0.2f, -5.0F, 8, 1, 1, 0.0F, false));

		super.hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, -5.0F, 0.0F);
		headsock.addChild(hood);
		hood.cubeList.add(new ModelBox(hood, 110, 30, -4.0F, -4.0F, 5.0F, 8, 8, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 110, 39, -4.0F, -2.0F, 6.0F, 8, 6, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 46, 1.0F, -0.5F, 4.0F, 3, 6, 3, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 55, 1.0F, 0.0F, 5.0F, 4, 11, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 69, 3.0F, 8.0F, 5.5F, 2, 7, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 77, 3.5F, 15.0F, 5.0F, 2, 6, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 84, 3.5F, 20.0F, 5.0F, 1, 8, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 116, 46, -4.0F, -0.5F, 4.0F, 3, 6, 3, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 114, 55, -5.0F, 0.0F, 5.0F, 4, 11, 3, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 69, -5.0F, 8.0F, 5.5F, 2, 7, 1, 0.5F, false));
		hood.cubeList.add(new ModelBox(hood, 122, 77, -5.5F, 15.0F, 5.0F, 2, 6, 1, 0.0F, false));
		hood.cubeList.add(new ModelBox(hood, 124, 84, -4.5F, 20.0F, 5.0F, 1, 8, 1, 0.0F, false));

		neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		neck.cubeList.add(new ModelBox(neck, 84, 0, -5.0F, 0.0F, -3.0F, 10, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 106, 0, -5.0F, 0.0F, 2.0F, 10, 2, 1, 0.0F, false));
		neck.cubeList.add(new ModelBox(neck, 126, 4, -4.5F, 1.0F, -2.5F, 1, 11, 0, 0.25F, false));
		neck.cubeList.add(new ModelBox(neck, 126, 4, 3.5F, 1.0F, -2.5F, 1, 11, 0, 0.25F, false));
		neck.cubeList.add(new ModelBox(neck, 80, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.2515F, false));

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.cubeList.add(new ModelBox(torso, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.251F, false));

		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 0, 74, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 0, 74, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.251F, true));

		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2525F, false));

		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 96, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.2525F, true));

		rightFoot.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightFoot.cubeList.add(new ModelBox(rightFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, false));

		leftFoot.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftFoot.cubeList.add(new ModelBox(leftFoot, 0, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.251F, true));

		skirtFront.setRotationPoint(-2.0F, 12.0F, 0.0F);

		skirtFrontCenter = new ModelRenderer(this);
		skirtFrontCenter.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtFront.addChild(skirtFrontCenter);
		setRotationAngle(skirtFrontCenter, -0.1745F, 0.0F, 0.0F);
		skirtFrontCenter.cubeList.add(new ModelBox(skirtFrontCenter, 16, 117, -2.0F, 0.5F, -2.0F, 4, 9, 1, 0.5F, false));
		skirtFrontCenter.cubeList.add(new ModelBox(skirtFrontCenter, 16, 106, 2.0F, 0.5F, -2.0F, 4, 9, 1, 0.5F, false));
		
		hoodThingsParent = new ModelRenderer(this);
		hoodThingsParent.setRotationPoint(-2.0F, 12.0F, 0.0F);
		
		hoodThings = new ModelRenderer(this);
		hoodThings.setRotationPoint(0.0F, 0.2342F, -1.892F);
		setRotationAngle(hoodThings, -0.1745F, 0.0F, 0.0F);
		hoodThingsParent.addChild(hoodThings);
		hoodThings.cubeList.add(new ModelBox(hoodThings, 126, 15, 5.5F, 0/*0.6527F*/, -0.5304F, 1, 10, 0, 0.25F, false));
		hoodThings.cubeList.add(new ModelBox(hoodThings, 126, 15, -2.5F, 0/*0.6527F*/, -0.5304F, 1, 10, 0, 0.25F, false));

		skirtFrontRight = new ModelRenderer(this);
		skirtFrontRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtFront.addChild(skirtFrontRight);
		setRotationAngle(skirtFrontRight, 0.0F, 0.0F, 0.1745F);
		skirtFrontRight.cubeList.add(new ModelBox(skirtFrontRight, 32, 114, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtFrontLeft = new ModelRenderer(this);
		skirtFrontLeft.setRotationPoint(4.0F, 0.0F, 0.0F);
		skirtFront.addChild(skirtFrontLeft);
		setRotationAngle(skirtFrontLeft, 0.0F, 0.0F, -0.1745F);
		skirtFrontLeft.cubeList.add(new ModelBox(skirtFrontLeft, 48, 114, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtMiddle.setRotationPoint(0.0F, 24.0F, 0.0F);
		
		skirtMiddleRight = new ModelRenderer(this);
		skirtMiddleRight.setRotationPoint(-2.0F, -12.0F, 0.0F);
		skirtMiddle.addChild(skirtMiddleRight);
		setRotationAngle(skirtMiddleRight, 0.0F, 0.0F, 0.1745F);
		skirtMiddleRight.cubeList.add(new ModelBox(skirtMiddleRight, 32, 114, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtMiddleLeft = new ModelRenderer(this);
		skirtMiddleLeft.setRotationPoint(2.0F, -12.0F, 0.0F);
		skirtMiddle.addChild(skirtMiddleLeft);
		setRotationAngle(skirtMiddleLeft, 0.0F, 0.0F, -0.1745F);
		skirtMiddleLeft.cubeList.add(new ModelBox(skirtMiddleLeft, 48, 114, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtBack.setRotationPoint(-2.0F, 12.0F, 0.0F);
		
		skirtBackCenter = new ModelRenderer(this);
		skirtBackCenter.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtBack.addChild(skirtBackCenter);
		setRotationAngle(skirtBackCenter, 0.1745F, 0.0F, 0.0F);
		skirtBackCenter.cubeList.add(new ModelBox(skirtBackCenter, 16, 95, -2.0F, 0.5F, 1.0F, 4, 9, 1, 0.5F, false));
		skirtBackCenter.cubeList.add(new ModelBox(skirtBackCenter, 16, 84, 2.0F, 0.5F, 1.0F, 4, 9, 1, 0.5F, false));

		skirtBackRight = new ModelRenderer(this);
		skirtBackRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtBack.addChild(skirtBackRight);
		setRotationAngle(skirtBackRight, 0.0F, 0.0F, 0.1745F);
		skirtBackRight.cubeList.add(new ModelBox(skirtBackRight, 32, 114, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtBackLeft = new ModelRenderer(this);
		skirtBackLeft.setRotationPoint(4.0F, 0.0F, 0.0F);
		skirtBack.addChild(skirtBackLeft);
		setRotationAngle(skirtBackLeft, 0.0F, 0.0F, -0.1745F);
		skirtBackLeft.cubeList.add(new ModelBox(skirtBackLeft, 48, 114, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.5F, false));

		skirtFrontShirt = new ModelRenderer(this);
		skirtFrontShirt.setRotationPoint(-2.0F, 12.0F, 0.0F);
		

		skirtFrontCenter2 = new ModelRenderer(this);
		skirtFrontCenter2.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtFrontShirt.addChild(skirtFrontCenter2);
		setRotationAngle(skirtFrontCenter2, -0.1745F, 0.0F, 0.0F);
		skirtFrontCenter2.cubeList.add(new ModelBox(skirtFrontCenter2, 0, 41, -2.0F, 0.5F, -2.0F, 4, 9, 1, 0.51F, false));
		skirtFrontCenter2.cubeList.add(new ModelBox(skirtFrontCenter2, 10, 41, 2.0F, 0.5F, -2.0F, 4, 9, 1, 0.51F, false));

		skirtFrontRight2 = new ModelRenderer(this);
		skirtFrontRight2.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtFrontShirt.addChild(skirtFrontRight2);
		setRotationAngle(skirtFrontRight2, 0.0F, 0.0F, 0.1745F);
		skirtFrontRight2.cubeList.add(new ModelBox(skirtFrontRight2, 0, 53, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		skirtFrontLeft2 = new ModelRenderer(this);
		skirtFrontLeft2.setRotationPoint(4.0F, 0.0F, 0.0F);
		skirtFrontShirt.addChild(skirtFrontLeft2);
		setRotationAngle(skirtFrontLeft2, 0.0F, 0.0F, -0.1745F);
		skirtFrontLeft2.cubeList.add(new ModelBox(skirtFrontLeft2, 10, 53, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		skirtMiddleShirt = new ModelRenderer(this);
		skirtMiddleShirt.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		skirtMiddleRight2 = new ModelRenderer(this);
		skirtMiddleRight2.setRotationPoint(-2.0F, -12.0F, 0.0F);
		skirtMiddleShirt.addChild(skirtMiddleRight2);
		setRotationAngle(skirtMiddleRight2, 0.0F, 0.0F, 0.1745F);
		skirtMiddleRight2.cubeList.add(new ModelBox(skirtMiddleRight2, 0, 53, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		skirtMiddleLeft2 = new ModelRenderer(this);
		skirtMiddleLeft2.setRotationPoint(2.0F, -12.0F, 0.0F);
		skirtMiddleShirt.addChild(skirtMiddleLeft2);
		setRotationAngle(skirtMiddleLeft2, 0.0F, 0.0F, -0.1745F);
		skirtMiddleLeft2.cubeList.add(new ModelBox(skirtMiddleLeft2, 10, 53, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		skirtBackShirt = new ModelRenderer(this);
		skirtBackShirt.setRotationPoint(-2.0F, 12.0F, 0.0F);
		

		skirtBackCenter2 = new ModelRenderer(this);
		skirtBackCenter2.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtBackShirt.addChild(skirtBackCenter2);
		setRotationAngle(skirtBackCenter2, 0.1745F, 0.0F, 0.0F);
		skirtBackCenter2.cubeList.add(new ModelBox(skirtBackCenter2, 30, 41, -2.0F, 0.5F, 1.0F, 4, 9, 1, 0.51F, false));
		skirtBackCenter2.cubeList.add(new ModelBox(skirtBackCenter2, 20, 41, 2.0F, 0.5F, 1.0F, 4, 9, 1, 0.51F, false));

		skirtBackRight2 = new ModelRenderer(this);
		skirtBackRight2.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtBackShirt.addChild(skirtBackRight2);
		setRotationAngle(skirtBackRight2, 0.0F, 0.0F, 0.1745F);
		skirtBackRight2.cubeList.add(new ModelBox(skirtBackRight2, 0, 53, -2.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		skirtBackLeft2 = new ModelRenderer(this);
		skirtBackLeft2.setRotationPoint(4.0F, 0.0F, 0.0F);
		skirtBackShirt.addChild(skirtBackLeft2);
		setRotationAngle(skirtBackLeft2, 0.0F, 0.0F, -0.1745F);
		skirtBackLeft2.cubeList.add(new ModelBox(skirtBackLeft2, 10, 53, 1.0F, 0.5F, -2.0F, 1, 9, 4, 0.51F, false));

		hoodSkirtlessLeft = new ModelRenderer(this);
		hoodSkirtlessLeft.setRotationPoint(-2.0F, 12.2342F, -1.892F);
		hoodSkirtlessLeft.cubeList.add(new ModelBox(hoodSkirtlessLeft, 126, 16, 5.5F, 0.2658F, -0.608F, 1, 10, 0, 0.25F, false));

		hoodSkirtlessRight = new ModelRenderer(this);
		hoodSkirtlessRight.setRotationPoint(-2.0F, 12.2342F, -1.892F);
		hoodSkirtlessRight.cubeList.add(new ModelBox(hoodSkirtlessRight, 126, 16, -2.5F, 0.2658F, -0.608F, 1, 10, 0, 0.25F, false));
	}
	
	@Override
	public void addExtraInfo(EntityLivingBase entityLiving, ItemStack stack, EntityEquipmentSlot armorSlot)
	{
		boolean sylphDress = (ItemGTArmor.getHeroClass(entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS)) == EnumClass.SYLPH);
		boolean witchDress = ItemGTArmor.getHeroClass(entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS)) == EnumClass.WITCH;
		
		leftLeg.showModel = armorSlot == EntityEquipmentSlot.CHEST && !sylphDress;
		rightLeg.showModel = armorSlot == EntityEquipmentSlot.CHEST && !sylphDress;
		
		hoodThingsParent.showModel = armorSlot == EntityEquipmentSlot.HEAD && (sylphDress || witchDress);
		hoodSkirtlessLeft.showModel = armorSlot == EntityEquipmentSlot.HEAD && !(sylphDress || witchDress);
		hoodSkirtlessRight.showModel = armorSlot == EntityEquipmentSlot.HEAD && !(sylphDress || witchDress);
		
		skirtFrontShirt.showModel = armorSlot == EntityEquipmentSlot.CHEST && sylphDress;
		skirtMiddleShirt.showModel = armorSlot == EntityEquipmentSlot.CHEST && sylphDress;
		skirtBackShirt.showModel = armorSlot == EntityEquipmentSlot.CHEST  && sylphDress;
		
		setRotationAngle(hoodThings, sylphDress ? -0.1745F : -0.2618F, 0.0F, 0.0F);
	}
	
	@Override
	protected void renderExtras(float scale)
	{
		skirtFrontShirt.render(scale);
		skirtMiddleShirt.render(scale);
		skirtBackShirt.render(scale);
	}
	
	@Override
	protected void renderHeadExtras(float scale)
	{
		hoodThingsParent.render(scale);
		hoodSkirtlessLeft.render(scale);
		hoodSkirtlessRight.render(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		copyModelAngles(skirtFront, hoodThingsParent);
		copyModelAngles(skirtFront, skirtFrontShirt);
		copyModelAngles(skirtMiddle, skirtMiddleShirt);
		copyModelAngles(skirtBack, skirtBackShirt);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}