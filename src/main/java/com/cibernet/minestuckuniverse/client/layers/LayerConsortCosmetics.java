package com.cibernet.minestuckuniverse.client.layers;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.client.models.armor.ModelCrumplyHat;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.google.common.collect.Maps;
import com.mraof.minestuck.client.model.ModelIguana;
import com.mraof.minestuck.client.model.ModelTurtle;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.model.armor.ModelArmor;

import java.util.HashSet;
import java.util.Map;

public class LayerConsortCosmetics implements LayerRenderer<EntityLivingBase> {

	private static final ModelBiped _DEFAULT = new ModelBiped();
	private final RenderLivingBase<EntityLivingBase> renderer;

	public LayerConsortCosmetics(RenderLivingBase<EntityLivingBase> renderer)
	{
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if(entitylivingbaseIn.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null) == null)
			return;

		ModelBiped base = renderer.getMainModel() instanceof ModelBiped ? (ModelBiped) renderer.getMainModel() : _DEFAULT;
		ItemStack stack = entitylivingbaseIn.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).getHeadStack().copy();
		boolean hasColor = stack.getItem() instanceof ItemArmor;// && ((ItemArmor) stack.getItem()).hasColor(stack);

		if(stack.isEmpty())
			return;

		ModelBiped armorModel = stack.getItem().getArmorModel(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, base);
		ResourceLocation texture = getArmorResource(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, null);
		boolean hasNeck = !(renderer.getMainModel() instanceof ModelTurtle || renderer.getMainModel() instanceof ModelIguana); //i don't get it why don't they have model anims >_>

		if(armorModel == null && stack.getItem() instanceof ItemArmor)
		{
			armorModel = new ModelArmor(EntityEquipmentSlot.HEAD);
			armorModel.setVisible(false);
			armorModel.bipedHead.showModel = true;
			armorModel.bipedHeadwear.showModel = true;

			GlStateManager.scale(entitylivingbaseIn instanceof EntityFrog ? 1.25 : 1.1, 1, 1.1);
			GlStateManager.translate(0, 0, 0.025);
		}

		if(armorModel == null || texture == null)
		{
			double headScale = entitylivingbaseIn instanceof EntityFrog ? 0.6 : 0.8;
			GlStateManager.scale(-headScale, -headScale, (stack.getItem().equals(Item.getItemFromBlock(Blocks.PUMPKIN)) ? 1 : -1) * headScale);

			if(entitylivingbaseIn instanceof EntityNakagator)
				GlStateManager.translate(0, -0.45, 0.04);
			else if(entitylivingbaseIn instanceof EntityFrog)
				GlStateManager.translate(0, -1.35, 0);
			else GlStateManager.translate(0, -0.5, 0.04);

			if(hasNeck)
			{
				GlStateManager.rotate((stack.getItem().equals(Item.getItemFromBlock(Blocks.PUMPKIN)) ? -1 : 1) * netHeadYaw, 0, 1, 0);
				GlStateManager.rotate((stack.getItem().equals(Item.getItemFromBlock(Blocks.PUMPKIN)) ? -1 : 1) * headPitch, 1, 0, 0);
			}

			Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, stack, ItemCameraTransforms.TransformType.HEAD);
			return;
		}

		renderer.bindTexture(texture);

		if(hasColor)
		{
			int i = ((ItemArmor) stack.getItem()).getColor(stack);
			float r = (float)(i >> 16 & 255) / 255.0F;
			float g = (float)(i >> 8 & 255) / 255.0F;
			float b = (float)(i & 255) / 255.0F;
			GlStateManager.color(r, g, b);
		}
		else GlStateManager.color(1, 1, 1);
		armorModel.setModelAttributes(base);
		armorModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
		armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, hasNeck ? netHeadYaw : 0, hasNeck ? headPitch : 0, 1.0F, entitylivingbaseIn);

		if(entitylivingbaseIn instanceof EntityNakagator)
			GlStateManager.translate(0, 0.05, 0);
		else if(entitylivingbaseIn instanceof EntityFrog)
			GlStateManager.translate(0, 0.4, 0);
		else GlStateManager.translate(0, 0.1, 0);

		armorModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, hasNeck ? netHeadYaw : 0, hasNeck ? headPitch : 0, scale);

		texture = getArmorResource(entitylivingbaseIn, stack, EntityEquipmentSlot.HEAD, "overlay");
		if(texture != null)
		{
			renderer.bindTexture(texture);
			GlStateManager.color(1,1,1);
			armorModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, hasNeck ? netHeadYaw : 0, hasNeck ? headPitch : 0, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}

	public static void addLayers()
	{
		HashSet<RenderLivingBase> addedRenders = new HashSet<>();

		for(Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet())
			if(entry.getValue() instanceof RenderLivingBase && !addedRenders.contains(entry.getValue()) && (EntityConsort.class.isAssignableFrom(entry.getKey()) || EntityFrog.class.isAssignableFrom(entry.getKey())))
			{
				((RenderLivingBase)entry.getValue()).addLayer(new LayerConsortCosmetics((RenderLivingBase) entry.getValue()));
				addedRenders.add((RenderLivingBase) entry.getValue());
			}
	}

	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
	public static ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type)
	{
		String s1 = null;

		if(stack.getItem() instanceof ItemArmor)
		{
			ItemArmor item = (ItemArmor)stack.getItem();
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

		if(s1 == null)
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
