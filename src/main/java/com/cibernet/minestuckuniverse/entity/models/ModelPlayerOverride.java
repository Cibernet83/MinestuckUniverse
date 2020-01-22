package com.cibernet.minestuckuniverse.entity.models;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ModelPlayerOverride extends ModelPlayer
{
	private ModelBase oldModel;
	private float thirdPersonPartialTicks;
	
	public ModelPlayerOverride(ModelBase oldModel, float modelSize, boolean smallArmsIn)
	{
		super(modelSize, smallArmsIn);
		this.oldModel = oldModel;
	}
	
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
	{
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
		oldModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
		thirdPersonPartialTicks = partialTickTime;
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		oldModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		EntityPlayer player = (EntityPlayer) entityIn;
		if(player == null) return;
		
		boolean isClient = player.getEntityId() == Minecraft.getMinecraft().player.getEntityId();
		//EnumHand hand = player.getActiveHand();
		
		if(player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(MinestuckUniverseItems.rocketFist))
		{
			//bipedRightArm.rotateAngleY;
			bipedRightArm.rotateAngleX = (float) Math.toRadians(-90);
			bipedRightArm.rotateAngleY = (float) Math.toRadians(0);
			bipedRightArm.rotateAngleZ = (float) Math.toRadians(90);
			
			boolean elytraCheck = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
			this.bipedRightArm.rotateAngleX += netHeadYaw * 0.017453292F;
			
			if(elytraCheck)
				this.bipedRightArm.rotateAngleY -= -((float) Math.PI / 4F) - netHeadYaw * 0.017453292F;
			 else
				this.bipedRightArm.rotateAngleY -= headPitch * 0.017453292F - netHeadYaw * 0.017453292F;
		}
		if(isClient)
		{
			if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
			{
				
				float heldPercent = 0.0F;
				
				GlStateManager.rotate(-50F * heldPercent, 1, 0, 0);
				GlStateManager.rotate(30F * heldPercent, 0, 1, 0);
				GlStateManager.rotate(-30F * heldPercent, 0, 0, 1);
				GlStateManager.translate(-0.3 * heldPercent, -0.2 * heldPercent, -0.5 * heldPercent);
				
			}
		}
	}
	
}
