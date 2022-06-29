package com.cibernet.minestuckuniverse.client.models.armor.godtier;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.util.AspectColorHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ModelGTAbstract extends ModelBiped
{
	@SideOnly(Side.CLIENT)
	public static final HashMap<EnumClass, ModelGTAbstract> MODELS = new HashMap<EnumClass, ModelGTAbstract>()
	{{
		put(EnumClass.KNIGHT, new ModelGTKnight());
		put(EnumClass.HEIR, new ModelGTHeir());
		put(EnumClass.WITCH, new ModelGTWitch());
		put(EnumClass.SEER, new ModelGTSeer());
		put(EnumClass.PAGE, new ModelGTPage());
		put(EnumClass.MAGE, new ModelGTMage());
		put(EnumClass.BARD, new ModelGTBard());
		put(EnumClass.THIEF, new ModelGTThief());
		put(EnumClass.PRINCE, new ModelGTPrince());
		put(EnumClass.MAID, new ModelGTMaid());
		put(EnumClass.ROGUE, new ModelGTRogue());
		put(EnumClass.SYLPH, new ModelGTSylph());

		put(EnumClass.LORD, new ModelGTLord());
		put(EnumClass.MUSE, new ModelGTMuse());
	}};

	public final EnumClass heroClass;
	public EnumAspect heroAspect;
	
	public final ModelRenderer head;
	public final ModelRenderer hood;
	public final ModelRenderer headsock;
	public final ModelRenderer neck;
	public final ModelRenderer torso;
	public final ModelRenderer cape;
	public final ModelRenderer leftArm;
	public final ModelRenderer rightArm;
	public final ModelRenderer skirtFront;
	public final ModelRenderer skirtMiddle;
	public final ModelRenderer skirtBack;
	public final ModelRenderer belt;
	public final ModelRenderer leftLeg;
	public final ModelRenderer rightLeg;
	public final ModelRenderer leftFoot;
	public final ModelRenderer rightFoot;
	
	public final ModelRenderer symbol;
	
	public boolean hideExtras = true;
	
	private Float hoodY = null;
	private Float skirtFrontY = null;
	private Float skirtFrontZ = null;
	private Float skirtBackY = null;
	private Float skirtBackZ = null;
	private Float skirtMiddleY = null;
	private Float skirtMiddleZ = null;
	private Float capeZ = null;
	
	public final ArrayList<Integer> IGNORE_COLORS = new ArrayList<>();
	
	public ModelGTAbstract(int textWidth, int textHeight, EnumClass heroClass)
	{
		this.heroClass = heroClass;
		textureWidth = 12;
		textureHeight = 6;
		
		symbol = new ModelRenderer(this);
		symbol.setRotationPoint(0.0F, 0.0F, 0.0F);
		symbol.cubeList.add(new ModelBox(symbol, 0, 0, -3.0F, 2.0F, -2.0F, 6, 6, 0, 0.2512F, false));
		
		textureWidth = textWidth;
		textureHeight = textHeight;
		
		head = new ModelRenderer(this);
		hood = new ModelRenderer(this);
		headsock = new ModelRenderer(this);
		headsock.setRotationPoint(0,0,0);
		hood.addChild(headsock);

		neck = new ModelRenderer(this);
		torso = new ModelRenderer(this);
		cape = new ModelRenderer(this);
		
		leftArm = new ModelRenderer(this);
		rightArm = new ModelRenderer(this);
		
		skirtFront = new ModelRenderer(this);
		skirtMiddle = new ModelRenderer(this);
		skirtBack = new ModelRenderer(this);
		belt = new ModelRenderer(this);
		leftLeg = new ModelRenderer(this);
		rightLeg = new ModelRenderer(this);
		
		leftFoot = new ModelRenderer(this);
		rightFoot = new ModelRenderer(this);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		
		
		if (this.isChild)
		{
			float f = 2.0F;
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			renderHead(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			renderBody(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
		else
		{
			if (entityIn.isSneaking())
			{
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}
			
			renderHead(scale);
			renderBody(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
		
		GlStateManager.popMatrix();
	}
	
	private void renderBody(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		AspectColorHandler.AspectColor[] colorSet = AspectColorHandler.getAspectColorSet(heroAspect);
		
		for(int i = 0; i < colorSet.length; i++)
		{
			if(IGNORE_COLORS.contains(i))
				continue;
			
			AspectColorHandler.AspectColor color = colorSet[i];
			ResourceLocation loc = new ResourceLocation(MinestuckUniverse.MODID, "textures/models/armor/god_tier/gt_"+heroClass.toString()+"_layer_"+(i+1)+".png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			
			GlStateManager.color(color.r, color.g, color.b);
			
			this.torso.render(scale);
			this.neck.render(scale);
			//renderCape(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			this.cape.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.skirtFront.render(scale);
			this.skirtMiddle.render(scale);
			this.skirtBack.render(scale);
			this.belt.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
			this.leftFoot.render(scale);
			this.rightFoot.render(scale);
			
			renderExtras(scale);
		}
		
		GlStateManager.color(1,1,1);
		String aspectName = heroAspect == null ? "default" : heroAspect.toString();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MinestuckUniverse.MODID, "textures/models/armor/symbol/"+aspectName+".png"));
		this.symbol.render(scale);
	}
	
	protected void renderExtras(float scale)
	{
	}
	
	private void renderHead(float scale)
	{
		AspectColorHandler.AspectColor[] colorSet = AspectColorHandler.getAspectColorSet(heroAspect);
		
		for(int i = 0; i < colorSet.length; i++)
		{
			if(IGNORE_COLORS.contains(i))
				continue;
				
			AspectColorHandler.AspectColor color = colorSet[i];
			ResourceLocation loc = new ResourceLocation(MinestuckUniverse.MODID, "textures/models/armor/god_tier/gt_"+heroClass.toString()+"_layer_"+(i+1)+".png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			GlStateManager.color(color.r, color.g, color.b);
			
			this.head.render(scale);
			this.hood.render(scale);
			renderHeadExtras(scale);
		}
	}
	
	protected void renderHeadExtras(float scale)
	{
	}
	
	private void renderCape(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		
		if(!(entityIn instanceof AbstractClientPlayer))
		{
			cape.render(scale);
			return;
		}
		
		AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer) entityIn;
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		
		if (itemstack.getItem() != Items.ELYTRA)
		{
			GlStateManager.pushMatrix();
			//GlStateManager.translate(0.0F, 0.0F, 0.125F);
			double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)ageInTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)ageInTicks);
			double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)ageInTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)ageInTicks);
			double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)ageInTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)ageInTicks);
			float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * ageInTicks;
			//f /= 4;
			double d3 = (double) MathHelper.sin(f * 0.017453292F);
			double d4 = (double)(-MathHelper.cos(f * 0.017453292F));
			float f1 = (float)d1;// * 10.0F;
			f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
			float f2 = Math.max(0, (float)(d0 * d3 + d2 * d4)/* 100.0F*/);
			float f3 = (float)(d0 * d4 - d2 * d3) /* 100.0F*/;
			
			float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * ageInTicks;
			f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * ageInTicks) * 6.0F) * 32.0F * f4;
			
			if (entitylivingbaseIn.isSneaking())
			{
				f1 += 25.0F;
			}
			
			//GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
			//GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
			//GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
			//GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			//cape.rotateAngleX = (6.0F + f2 / 2.0F + f1);//180f * (float)Math.PI;
			//cape.rotateAngleZ = (f3 / 2.0F);//180f * (float)Math.PI;
			//cape.rotateAngleY = (-f3 / 2.0F);//180f * (float)Math.PI;
			//cape.rotateAngleZ = (float) d2;//180f * (float)Math.PI;
			//cape.rotateAngleY = (float) d1;//180f * (float)Math.PI;
			
			
			
			cape.rotateAngleX = (float) Math.sqrt(Math.pow((entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX), 2) + Math.pow(Math.max(0, entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY), 2) + Math.pow((entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ), 2)) * limbSwingAmount;//180f * (float)Math.PI;

			cape.render(scale);
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		
		if(hoodY == null)
			hoodY = headsock.rotationPointY;
		if(skirtFrontY == null)
			skirtFrontY = skirtFront.rotationPointY;
		if(skirtFrontZ == null)
			skirtFrontZ = skirtFront.rotationPointZ;
		if(skirtMiddleY == null)
			skirtMiddleY = skirtMiddle.rotationPointY;
		if(skirtMiddleZ == null)
			skirtMiddleZ = skirtMiddle.rotationPointZ;
		if(skirtBackY == null)
			skirtBackY = skirtBack.rotationPointY;
		if(skirtBackZ == null)
			skirtBackZ = skirtBack.rotationPointZ;
	
		this.skirtFront.rotationPointZ = skirtFrontZ;
		this.skirtFront.rotationPointY = skirtFrontY;
		this.skirtBack.rotationPointZ = skirtBackZ;
		this.skirtBack.rotationPointY = skirtBackY;
		this.skirtMiddle.rotationPointZ = skirtMiddleZ;
		this.skirtMiddle.rotationPointY = skirtMiddleY;
		
		if (entityIn instanceof EntityArmorStand)
		{
			EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
			head.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
			head.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
			head.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
			head.setRotationPoint(0.0F, 1.0F, 0.0F);
			torso.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			torso.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			torso.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			leftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
			leftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
			leftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
			rightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
			rightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
			rightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
			leftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
			leftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
			leftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
			leftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
			rightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
			rightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
			rightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
			rightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
			copyModelAngles(torso, neck);
			copyModelAngles(torso, symbol);
			copyModelAngles(torso, cape);
			copyModelAngles(torso, belt);
			copyModelAngles(leftLeg, leftFoot);
			copyModelAngles(rightLeg, rightFoot);
		} else
		{
			if(entityIn instanceof EntityPlayer)
			{
				RenderPlayer render = (RenderPlayer) Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(entityIn);
				ModelPlayer modelPlayer = render.getMainModel();
				
				copyModelAngles(modelPlayer.bipedHead, head);
				copyModelAngles(modelPlayer.bipedHead, hood);
				copyModelAngles(modelPlayer.bipedBody, neck);
				copyModelAngles(modelPlayer.bipedBody, torso);
				copyModelAngles(modelPlayer.bipedBody, symbol);
				copyModelAngles(modelPlayer.bipedBody, cape);
				copyModelAngles(modelPlayer.bipedBody, belt);
				copyModelAngles(modelPlayer.bipedLeftArm, leftArm);
				copyModelAngles(modelPlayer.bipedRightArm, rightArm);
				copyModelAngles(modelPlayer.bipedLeftLeg, leftLeg);
				copyModelAngles(modelPlayer.bipedRightLeg, rightLeg);
				copyModelAngles(modelPlayer.bipedLeftLeg, leftFoot);
				copyModelAngles(modelPlayer.bipedRightLeg, rightFoot);
				
			} else
			{
				copyModelAngles(bipedHead, head);
				copyModelAngles(bipedHead, hood);
				copyModelAngles(bipedBody, neck);
				copyModelAngles(bipedBody, torso);
				copyModelAngles(bipedBody, cape);
				copyModelAngles(bipedBody, belt);
				copyModelAngles(bipedLeftArm, leftArm);
				copyModelAngles(bipedRightArm, rightArm);
				copyModelAngles(bipedLeftLeg, leftLeg);
				copyModelAngles(bipedRightLeg, rightLeg);
				copyModelAngles(bipedLeftLeg, leftFoot);
				copyModelAngles(bipedRightLeg, rightFoot);
				
			}
			
			cape.rotateAngleX += ((float) Math.sqrt(Math.pow((entityIn.posX - entityIn.prevPosX), 2) + Math.pow(Math.max(0, entityIn.posY - entityIn.prevPosY), 2) + Math.pow((entityIn.posZ - entityIn.prevPosZ), 2)) * limbSwingAmount);
			if (this.isSneak)
			{
				this.skirtFront.rotationPointZ += 4;
				this.skirtFront.rotationPointY -= 2;
				this.skirtBack.rotationPointZ += 4;
				this.skirtBack.rotationPointY -= 2;
				this.skirtMiddle.rotationPointZ += 4;
				this.skirtMiddle.rotationPointY -= 2;
			}
		}
		skirtBack.rotateAngleX = Math.max(0, Math.max(leftLeg.rotateAngleX, rightLeg.rotateAngleX));
		skirtFront.rotateAngleX = Math.min(0, Math.min(leftLeg.rotateAngleX, rightLeg.rotateAngleX));
		hood.rotateAngleX = Math.max(0, head.rotateAngleX);
		hood.rotationPointY = (float) (-Math.min(0, Math.sin(head.rotateAngleX)))*8f;
		headsock.rotationPointZ = (float) (-Math.min(0, head.rotateAngleX));
		//headsock.rotationPointX = (float) (Math.min(0, Math.sin(head.rotateAngleX))*4f);

	}
	
	protected void addColorIgnores(Integer... index)
	{
		for(Integer i : index)
		IGNORE_COLORS.add(i-1);
	}
	
	protected void addColorIgnores(AspectColorHandler.EnumColor... index)
	{
		for(AspectColorHandler.EnumColor i : index)
		IGNORE_COLORS.add(i.ordinal());
	}
	
	public EntityEquipmentSlot getSkirtSlot()
	{
		return EntityEquipmentSlot.LEGS;
	}
	public EntityEquipmentSlot getCapeSlot()
	{
		return EntityEquipmentSlot.HEAD;
	}
	
	public void addExtraInfo(EntityLivingBase entityLiving, ItemStack stack, EntityEquipmentSlot armorSlot)
	{
	
	}
}
