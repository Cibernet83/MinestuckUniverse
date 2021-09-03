package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.capabilities.strife.StrifeData;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.UpdateStrifeDataPacket;
import com.cibernet.minestuckuniverse.strife.StrifeSpecibus;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.LinkedList;

@SideOnly(Side.CLIENT)
public class GuiStrifeSwitcher extends Gui
{

	private static final ResourceLocation WIDGETS = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/icons.png");
	private static final String iconsLoc = "textures/gui/strife_specibus/icons/";

	public static boolean showSwitcher = false;
	private static boolean strifeDown;

	@SubscribeEvent
	public static void renderEvent(TickEvent.RenderTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int screenWidth = scaledresolution.getScaledWidth();
		int screenHeight = scaledresolution.getScaledHeight();

		if(!showSwitcher || mc.player == null)
			return;

		IStrifeData cap = mc.player.getCapability(MSUCapabilities.STRIFE_DATA, null);

		if(strifeDown != MSUKeys.strifeKey.isKeyDown())
		{
			strifeDown = MSUKeys.strifeKey.isKeyDown();
			if(!strifeDown)
			{
				showSwitcher = false;
				EnumHand hand = StrifeEventHandler.isStackAssigned(mc.player.getHeldItemOffhand()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_STRIFE, mc.player, UpdateStrifeDataPacket.UpdateType.INDEXES));
				if(!(mc.player.isSneaking() && canUseAbstrataSwitcher()))
					MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.RETRIEVE_STRIFE, cap.getSelectedWeaponIndex(), false, hand));
			}
		}

		int selSpecibusIndex = Math.max(0, cap.getSelectedSpecibusIndex());
		int selWeaponIndex = cap.getSelectedWeaponIndex();
		StrifeSpecibus[] portfolio =  cap.getNonEmptyPortfolio();

		if(portfolio.length <= 0)
			return;

		if(canUseAbstrataSwitcher() && (cap.getPortfolio()[selSpecibusIndex] == null || (!cap.getPortfolio()[selSpecibusIndex].getKindAbstratus().isFist() && cap.getPortfolio()[selSpecibusIndex].getContents().isEmpty())))
		{
			cap.setSelectedSpecibusIndex(cap.getSpecibusIndex(portfolio[0]));
			selSpecibusIndex = cap.getSpecibusIndex(portfolio[0]);
			System.out.println("mmm");
		}

		if(mc.player.isSneaking() && canUseAbstrataSwitcher())
		{
			if(portfolio.length > 0)
			{
				int toDisplay = (int) Math.min(5, Math.ceil((portfolio.length-1)/2f)*2);
				for(int i = -(toDisplay/2); i <= (toDisplay/2); i++)
				{
					StrifeSpecibus specibus = portfolio[(i + portfolio.length + Arrays.asList(portfolio).indexOf(cap.getPortfolio()[selSpecibusIndex])) % portfolio.length];
					if(specibus == null || specibus.getKindAbstratus() == null)
						continue;

					if(i == 0)
					{
						mc.getTextureManager().bindTexture(WIDGETS);
						drawScaledCustomSizeModalRect(18*i + screenWidth/2 -11, screenHeight*3/4 -3, 112, 0, 22, 22, 22, 22, 256, 256);
						String str = specibus.getDisplayName();
						mc.fontRenderer.drawString(str, screenWidth/2 - mc.fontRenderer.getStringWidth(str)/2, screenHeight*3/4 - 14, 0x00AB54, true);
					}

					ResourceLocation loc = specibus.getKindAbstratus().getRegistryName();
					mc.getTextureManager().bindTexture(new ResourceLocation(loc.getResourceDomain(), iconsLoc+loc.getResourcePath()+".png"));
					drawScaledCustomSizeModalRect(20*i + screenWidth/2 -8, screenHeight*3/4, 0, 0, 16, 16, 16, 16, 16, 16);
				}
			}
		}
		else
		{
			StrifeSpecibus selSpecibus = cap.getPortfolio()[selSpecibusIndex];

			if(selSpecibus == null)
				selSpecibus = cap.getPortfolio()[0];
			if(selSpecibus == null)
				return;

			LinkedList<ItemStack> strifeDeck = selSpecibus.getContents();

			if(!strifeDeck.isEmpty())
			{
				int toDisplay = (int) Math.min(5, Math.ceil((strifeDeck.size()-1)/2f)*2);
				for(int i = -(toDisplay/2); i <= (toDisplay/2); i++)
				{
					ItemStack stack = strifeDeck.get((i + strifeDeck.size() + selWeaponIndex) % strifeDeck.size());
					if(stack == null)
						continue;

					if(i == 0)
					{
						mc.getTextureManager().bindTexture(WIDGETS);
						drawScaledCustomSizeModalRect(18*i + screenWidth/2 -11, screenHeight*3/4 -3, 112, 0, 22, 22, 22, 22, 256, 256);
						String str = stack.getDisplayName();
						mc.fontRenderer.drawString(str, screenWidth/2 - mc.fontRenderer.getStringWidth(str)/2, screenHeight*3/4 - 14, 0x00AB54, true);
					}

					renderItem(mc, 20*i + screenWidth/2 -8, screenHeight*3/4, event.renderTickTime, mc.player, stack);
				}
			}
		}
	}

	@SubscribeEvent
	public static void scrollEvent(MouseEvent event)
	{
		if(MSUKeys.strifeKey.isKeyDown() && Minecraft.getMinecraft().player != null)
		{
			IStrifeData cap = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.STRIFE_DATA, null);
			StrifeSpecibus[] nonNullPortfolio = cap.getNonEmptyPortfolio();

			if(nonNullPortfolio.length > 0)
			{
				if(Minecraft.getMinecraft().player.isSneaking() && canUseAbstrataSwitcher())
				{
					if(cap.getSelectedSpecibusIndex() < 0 || cap.getPortfolio()[cap.getSelectedSpecibusIndex()] == null)
						cap.setSelectedSpecibusIndex(0);
					else
					{
						StrifeSpecibus selectedSpecibus = cap.getPortfolio()[cap.getSelectedSpecibusIndex()];
						int i = 0;
						for(int j = 0; j < nonNullPortfolio.length; j++)
							if(nonNullPortfolio[j] == selectedSpecibus)
							{
								i = j;
								break;
							}
						i = (int) ((i + Math.signum(event.getDwheel()) + nonNullPortfolio.length) % nonNullPortfolio.length);
						cap.setSelectedSpecibusIndex(cap.getSpecibusIndex(nonNullPortfolio[i]));
					}
				}
				else if(cap.getSelectedSpecibusIndex() >= 0 && cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null)
				{
					int deckSize = cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getContents().size();
					cap.setSelectedWeaponIndex((int) ((cap.getSelectedWeaponIndex()+Math.signum(event.getDwheel())+deckSize)%deckSize));
				}

			}



			event.setCanceled(true);
		}
	}

	protected static boolean canUseAbstrataSwitcher()
	{
		return Minecraft.getMinecraft().player.getCapability(MSUCapabilities.STRIFE_DATA, null).abstrataSwitcherUnlocked();
	}

	protected static void renderItem(Minecraft mc, int x, int y, float partialTicks, EntityPlayer player, ItemStack stack)
	{
		if (!stack.isEmpty())
		{
			float f = (float)stack.getAnimationsToGo() - partialTicks;

			if (f > 0.0F)
			{
				GlStateManager.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				GlStateManager.translate((float)(x + 8), (float)(y + 12), 0.0F);
				GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
			}

			mc.getRenderItem().renderItemAndEffectIntoGUI(player, stack, x, y);

			if (f > 0.0F)
			{
				GlStateManager.popMatrix();
			}
		}
	}

}
