package com.cibernet.minestuckuniverse.client.layers;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.client.models.armor.godtier.ModelGTAbstract;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.UUID;

public class LayerConsortCosmetics implements LayerRenderer<EntityLivingBase>
{

	private static final ModelBiped _DEFAULT = new ModelBiped();
	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
	private final RenderLivingBase<? extends EntityLivingBase> renderer;
	private final ModelRenderer itemHead;
	private final ModelRenderer hatHead;
	private final ModelRenderer parentHead;

	public LayerConsortCosmetics(RenderLivingBase<? extends EntityLivingBase> renderer, ModelRenderer hatHead, ModelRenderer itemHead, ModelRenderer parentHead)
	{
		this.renderer = renderer;
		this.itemHead = itemHead;
		this.hatHead = hatHead;
		this.parentHead = parentHead;
	}

	public static void copyValues(ModelRenderer from, ModelRenderer to)
	{
		to.rotateAngleX = from.rotateAngleX;
		to.rotateAngleY = from.rotateAngleY;
		to.rotateAngleZ = from.rotateAngleZ;
		to.rotationPointX = from.rotationPointX;
		to.rotationPointY = from.rotationPointY;
		to.rotationPointZ = from.rotationPointZ;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!entitylivingbaseIn.hasCapability(MSUCapabilities.CONSORT_HATS_DATA, null))
			return;


		ModelBiped base = renderer.getMainModel() instanceof ModelBiped ? (ModelBiped) renderer.getMainModel() : _DEFAULT;
		ItemStack stack = entitylivingbaseIn.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).getHeadStack().copy();
		boolean hasColor = stack.getItem() instanceof ItemArmor;// && ((ItemArmor) stack.getItem()).hasColor(stack);

		if (stack.isEmpty())
			return;

		GlStateManager.pushMatrix();

		if(stack.getItem() instanceof ItemArmor)
		{
			ModelBiped armorModel = stack.getItem().getArmorModel(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, base);
			ResourceLocation texture = getArmorResource(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, null);

			if (armorModel == null)
			{
				armorModel = new ModelBiped();
				armorModel.setVisible(false);
				armorModel.bipedHead.showModel = true;
				armorModel.bipedHeadwear.showModel = true;
			}

			renderer.bindTexture(texture);

			if (hasColor)
			{
				int i = ((ItemArmor) stack.getItem()).getColor(stack);
				float r = (float) (i >> 16 & 255) / 255.0F;
				float g = (float) (i >> 8 & 255) / 255.0F;
				float b = (float) (i & 255) / 255.0F;
				GlStateManager.color(r, g, b);
			}
			else GlStateManager.color(1, 1, 1);
			float size = 0;

			for(ModelBox box : hatHead.cubeList)
				size += (Math.abs(box.posX1-box.posX2) + Math.abs(box.posY1-box.posY2) + Math.abs(box.posZ1-box.posZ2))/3f;
			size = size/hatHead.cubeList.size() / 8f;

			GlStateManager.scale(size, size, size);

			if(parentHead != null && parentHead != hatHead)
				parentHead.postRender(0.0625F);
			hatHead.postRender(0.0625F);

			//armorModel.setModelAttributes(base);
			//armorModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			//armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1.0F, entitylivingbaseIn);

			armorModel.setRotationAngles(0, 0, 0, 0, 0, scale, entitylivingbaseIn);

			if(armorModel instanceof ModelGTAbstract)
				((ModelGTAbstract)armorModel).renderHead(scale);
			else armorModel.bipedHead.render(scale);

			//armorModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

			texture = getArmorResource(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, "overlay");
			if (texture != null && (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).hasOverlay(stack)))
			{
				renderer.bindTexture(texture);
				GlStateManager.color(1, 1, 1);
				armorModel.bipedHead.render(scale);
				//armorModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			}
		}
		GlStateManager.popMatrix();
		Item item = stack.getItem();
		Minecraft minecraft = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();

		if (entitylivingbaseIn.isSneaking())
		{
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		boolean flag = entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityZombieVillager;

		float size = 0;
		for(ModelBox box : hatHead.cubeList)
			size += (Math.abs(box.posX1-box.posX2) + Math.abs(box.posY1-box.posY2) + Math.abs(box.posZ1-box.posZ2))/3f;
		size = size/hatHead.cubeList.size() / 8f ;

		GlStateManager.translate(0.0F, 0.125F*0.7f / size * scale, 0.0F);
		GlStateManager.scale(size, size, size);
		GlStateManager.translate(0.0F, 4.0F*0.7f / size * scale, 0.0F);

		if (entitylivingbaseIn.isChild() && !(entitylivingbaseIn instanceof EntityVillager))
		{
			float f = 2.0F;
			float f1 = 1.4F;
			GlStateManager.translate(0.0F, 0.5F * scale, 0.0F);
			GlStateManager.scale(0.7F, 0.7F, 0.7F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
		}

		if(parentHead != null && parentHead != itemHead)
			parentHead.postRender(0.0625F);
		this.itemHead.postRender(0.0625F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (item == Items.SKULL)
		{
			float f2 = 1.1875F;
			GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);

			if (flag)
			{
				GlStateManager.translate(0.0F, 0.0625F, 0.0F);
			}

			GameProfile gameprofile = null;

			if (stack.hasTagCompound())
			{
				NBTTagCompound nbttagcompound = stack.getTagCompound();

				if (nbttagcompound.hasKey("SkullOwner", 10))
				{
					gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
				}
				else if (nbttagcompound.hasKey("SkullOwner", 8))
				{
					String s = nbttagcompound.getString("SkullOwner");

					if (!StringUtils.isBlank(s))
					{
						gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
						nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
					}
				}
			}

			TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, stack.getMetadata(), gameprofile, -1, limbSwing);
		}
		else if (!(item instanceof ItemArmor) || ((ItemArmor)item).getEquipmentSlot() != EntityEquipmentSlot.HEAD)
		{
			float f3 = 0.625F;
			GlStateManager.translate(0.0F, -0.25F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(0.625F, -0.625F, -0.625F);

			if (flag)
			{
				GlStateManager.translate(0.0F, 0.1875F, 0.0F);
			}

			minecraft.getItemRenderer().renderItem(entitylivingbaseIn, stack, ItemCameraTransforms.TransformType.HEAD);
		}

		GlStateManager.popMatrix();


	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}

	public static ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type)
	{
		String s1 = null;

		if (stack.getItem() instanceof ItemArmor)
		{
			ItemArmor item = (ItemArmor) stack.getItem();
			String texture = item.getArmorMaterial().getName();
			String domain = "minecraft";
			int idx = texture.indexOf(':');
			if (idx != -1)
			{
				domain = texture.substring(0, idx);
				texture = texture.substring(idx + 1);
			}
			s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (1), type == null ? "" : String.format("_%s", type));
		}

		s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);

		if (s1 == null)
			return null;

		ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

		if (resourcelocation == null)
		{
			resourcelocation = new ResourceLocation(s1);
			ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
}
