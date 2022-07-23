package com.cibernet.minestuckuniverse.potions;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class PotionDecay extends MSUPotionBase
{
	public static final DamageSource DECAY = new CritDamageSource("decay").setCrit().setDamageBypassesArmor();
	private static final ResourceLocation ICONS = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/overlay_icons.png");
	private static final Random rand = new Random();

	protected PotionDecay(boolean isBadEffectIn, int liquidColorIn, String name)
	{
		super(isBadEffectIn, liquidColorIn, name);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		int timeBetweenHits = 40 >> amplifier; // Each increased amplifier halves the amount of time between damages?

		if (timeBetweenHits > 0)
			return duration % timeBetweenHits == 0;
		else
			return true;
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		int decayTime = entityLivingBaseIn.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getDecayTime();
		entityLivingBaseIn.attackEntityFrom(DECAY, (decayTime++)/2);
		if (!entityLivingBaseIn.world.isRemote)
			entityLivingBaseIn.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setDecayTime(decayTime);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		entityLivingBaseIn.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setDecayTime(0);
	}

	@SideOnly(Side.CLIENT)
	private static long lastSystemTime, healthUpdateCounter;
	@SideOnly(Side.CLIENT)
	private static int lastPlayerHealth, playerHealth, updateCounter;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderHealth(RenderGameOverlayEvent.Pre event)
	{
		if (!(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH && Minecraft.getMinecraft().player.isPotionActive(MSUPotions.DECAY)))
			return;
		event.setCanceled(true);

		bind(ICONS);
		GlStateManager.enableBlend();

		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();

		EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
		int health = MathHelper.ceil(player.getHealth());
		boolean highlight = healthUpdateCounter > (long)updateCounter && (healthUpdateCounter - (long)updateCounter) / 3L %2L == 1L;

		if (health < playerHealth && player.hurtResistantTime > 0)
		{
			lastSystemTime = Minecraft.getSystemTime();
			healthUpdateCounter = (long)(updateCounter + 20);
		}
		else if (health > playerHealth && player.hurtResistantTime > 0)
		{
			lastSystemTime = Minecraft.getSystemTime();
			healthUpdateCounter = (long)(updateCounter + 10);
		}

		if (Minecraft.getSystemTime() - lastSystemTime > 1000L)
		{
			playerHealth = health;
			lastPlayerHealth = health;
			lastSystemTime = Minecraft.getSystemTime();
		}

		playerHealth = health;
		int healthLast = lastPlayerHealth;

		IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float healthMax = (float)attrMaxHealth.getAttributeValue();
		float absorb = MathHelper.ceil(player.getAbsorptionAmount());

		int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		rand.setSeed((long)(updateCounter * 312871));

		int left = width / 2 - 91;
		int top = height - GuiIngameForge.left_height;
		GuiIngameForge.left_height += (healthRows * rowHeight);
		if (rowHeight != 10) GuiIngameForge.left_height += 10 - rowHeight;

		int regen = -1;
		if (player.isPotionActive(MobEffects.REGENERATION))
		{
			regen = updateCounter % 25;
		}

		final int TOP =  Minecraft.getMinecraft().world.getWorldInfo().isHardcoreModeEnabled() ? 9 : 0;
		final int BACKGROUND_MARGIN = (highlight ? 9 : 0);
		int MARGIN = 18;
		float absorbRemaining = absorb;

		for (int i = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i)
		{
			//int b0 = (highlight ? 1 : 0);
			int row = MathHelper.ceil((float)(i + 1) / 10.0F) - 1;
			int x = left + i % 10 * 8;
			int y = top - row * rowHeight;

			if (health <= 4) y += rand.nextInt(2);
			if (i == regen) y -= 2;

			drawTexturedModalRect(x, y, BACKGROUND_MARGIN, TOP, 9, 9);

			if (highlight)
			{
				if (i * 2 + 1 < healthLast)
					drawTexturedModalRect(x, y, MARGIN + 18, TOP, 9, 9);
				else if (i * 2 + 1 == healthLast)
					drawTexturedModalRect(x, y, MARGIN + 27, TOP, 9, 9);
			}

			if (absorbRemaining > 0.0F)
			{
				if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
				{
					drawTexturedModalRect(x, y, MARGIN + 0, TOP, 9, 9);
					absorbRemaining -= 1.0F;
				}
				else
				{
					drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9);
					absorbRemaining -= 2.0F;
				}
			}
			else
			{
				if (i * 2 + 1 < health)
					drawTexturedModalRect(x, y, MARGIN + 0, TOP, 9, 9);
				else if (i * 2 + 1 == health)
					drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9);
			}
		}

		GlStateManager.disableBlend();

		bind(Gui.ICONS);
	}

	@SideOnly(Side.CLIENT)
	private static void bind(ResourceLocation res)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
	}

	@SideOnly(Side.CLIENT)
	private static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
	{
		double zLevel = -90;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(x + 0), (double)(y + height), zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)(y + height), zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)(y + 0), zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + 0), (double)(y + 0), zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		tessellator.draw();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase != TickEvent.Phase.END)
			return;
		updateCounter++;
	}
}
