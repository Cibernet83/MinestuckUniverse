package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.captchalogue.*;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.captchalogue.OperandiArmorItem;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CaptchalogueEventHandler
{
	@SubscribeEvent
	public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(!event.player.world.isRemote && MinestuckPlayerData.getData(IdentifierHandler.encode(event.player)) != null)
		{
			MinestuckPlayerData.PlayerData data = MinestuckPlayerData.getData(IdentifierHandler.encode(event.player));
			if(data.modus instanceof JujuModus)
				((JujuModus) data.modus).sendUpdateToClients();
			else if(data.modus instanceof CommunistModus)
				((CommunistModus)data.modus).sendUpdate(event.player);
		}
	}
	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START || MinestuckPlayerData.getData(IdentifierHandler.encode(event.player)) == null)
			return;

		Modus modus = MinestuckPlayerData.getData(IdentifierHandler.encode(event.player)).modus;

		if(modus instanceof CycloneModus)
			((CycloneModus) modus).cycle();

		if(!event.player.world.isRemote)
		{
			if(modus instanceof JujuModus)
				((JujuModus) modus).checkUnlink(event.player);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(TickEvent.WorldTickEvent event)
	{
		World world = event.world;
		List<Entity> mobsWithKeysList = StreamSupport.stream(world.loadedEntityList
				.spliterator(), false)
				.filter(entity -> entity instanceof EntityMob && (((EntityMob) entity).getHeldItemMainhand().getItem().equals(MinestuckUniverseItems.chastityKey) || ((EntityMob) entity).getHeldItemOffhand().getItem().equals(MinestuckUniverseItems.chastityKey)))
				.collect(Collectors.toList());
		for(Entity entity : mobsWithKeysList)
		{

			ItemStack stack = ((EntityMob) entity).getHeldItemMainhand();
			if(!stack.getItem().equals(MinestuckUniverseItems.chastityKey))
				stack = ((EntityMob)entity).getHeldItemOffhand();

			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt == null)
			{
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}

			int glowTimer = nbt.getInteger("GlowTimer");

			if(glowTimer > 1200)
				entity.setGlowing(true);
			else nbt.setInteger("GlowTimer", glowTimer+1);
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		ItemStack operandiArmor = ItemStack.EMPTY;

		if(event.getAmount() < 1)
			return;

		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			if(slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR) && event.getEntityLiving().getItemStackFromSlot(slot).getItem() instanceof OperandiArmorItem)
			{
				operandiArmor = event.getEntityLiving().getItemStackFromSlot(slot);
				break;
			}
		}

		if(!operandiArmor.isEmpty())
		{
			ItemStack storedStack = MSUItemBase.getStoredItem(operandiArmor);
			operandiArmor.damageItem(operandiArmor.getMaxDamage()+1, event.getEntityLiving());

			if(event.getAmount() < event.getEntityLiving().getHealth())
			{
				event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), MSUSoundHandler.operandiTaskComplete, SoundCategory.PLAYERS, 1, 1);

				if((event.getEntityLiving() instanceof EntityPlayer) && !((EntityPlayer) event.getEntityLiving()).addItemStackToInventory(storedStack))
					((EntityPlayer) event.getEntityLiving()).dropItem(storedStack, true);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Client
	{
		private static int shake = 0;
		private static int shakeCooldown = 0;
		private static int prevSelectedSlot = 0;

		private static int eightBallMessage = -1;
		private static int maxEightBallMsgs = 20;

		private static final UUID WEIGHT_MODUS_SPEED_UUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());
		private boolean captchaKeyPressed = false;

		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event)
		{

			EntityPlayer player = Minecraft.getMinecraft().player;

			if(player == null || event.phase != TickEvent.Phase.START)
				return;

			Modus modus = CaptchaDeckHandler.clientSideModus;

			if(modus != null)
			{
				double floatstoneValue = WeightModus.getFloatStones(modus)*1.5;
				double speedMod = (modus.getNonEmptyCards()-floatstoneValue) / -WeightModus.getItemCap(player);

				AttributeModifier WEIGHT_MODUS_SPEED = (new AttributeModifier(WEIGHT_MODUS_SPEED_UUID, "Backpack Modus speed penalty", Math.min(0, speedMod), 2)).setSaved(false);

				IAttributeInstance attributeInstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
				if(attributeInstance.hasModifier(WEIGHT_MODUS_SPEED))
					attributeInstance.removeModifier(WEIGHT_MODUS_SPEED);

				if(!Minecraft.getMinecraft().isGamePaused())
				{
					if(modus instanceof WeightModus)
					{
						attributeInstance.applyModifier(WEIGHT_MODUS_SPEED);
						if(!player.capabilities.isFlying)
							player.motionY += Math.min(0, speedMod+0.3)*(player.isInWater() || player.isElytraFlying() ? 0.07 : 0.1);
					}
					else if(!player.capabilities.isFlying && player.motionY <= 2)
					{
						player.motionY += (floatstoneValue/WeightModus.getItemCap(player))*(player.isInWater() || player.isElytraFlying() ? 0.07 : 0.1);
					}
				}

			}
			if(modus instanceof CycloneModus)
				((CycloneModus) modus).cycle();

			if(prevSelectedSlot != player.inventory.currentItem)
				shakeCooldown = 1;

			float currentCameraAvg = (player.rotationPitch + player.rotationYawHead)/2f;
			float prevCameraAvg = (player.prevRotationPitch + player.prevRotationYawHead)/2f;

			if(Math.abs(currentCameraAvg - prevCameraAvg) > 10)
			{
				shake++;
				shakeCooldown = 16;
			}
			else if(shakeCooldown == 1)
			{
				shake = 0;
				shakeCooldown--;
				eightBallMessage = -1;
			}
			else if(shakeCooldown > 0) shakeCooldown--;

			if(player.getHeldItemMainhand().getItem().equals(MinestuckUniverseItems.eightBall))
			{
				if(shake > 30)
				{
					if(eightBallMessage == -1)
						eightBallMessage = player.world.rand.nextInt(maxEightBallMsgs);
					ITextComponent msg = new TextComponentTranslation("status.eightBallMessage."+eightBallMessage).setStyle(new Style().setColor(TextFormatting.BLUE));
					ItemStack storedStack = MSUItemBase.getStoredItem(player.getHeldItemMainhand());
					player.sendStatusMessage(storedStack.isEmpty() ? msg : storedStack.getTextComponent().setStyle(new Style().setColor(TextFormatting.BLUE)), true);
				}
			}
			else if(player.getHeldItemOffhand().getItem().equals(MinestuckUniverseItems.eightBall))
			{
				if(shake > 30)
				{
					if(eightBallMessage == -1)
						eightBallMessage = player.world.rand.nextInt(maxEightBallMsgs);
					ITextComponent msg = new TextComponentTranslation("status.eightBallMessage."+eightBallMessage).setStyle(new Style().setColor(TextFormatting.BLUE));
					ItemStack storedStack = MSUItemBase.getStoredItem(player.getHeldItemOffhand());
					player.sendStatusMessage(storedStack.isEmpty() ? msg : storedStack.getTextComponent().setStyle(new Style().setColor(TextFormatting.BLUE)), true);
				}
			}
			else if( shakeCooldown > 0) shakeCooldown = 0;

			prevSelectedSlot = player.inventory.currentItem;
		}

		@SubscribeEvent
		public static void onClientSendChat(ClientChatEvent event)
		{
			EntityPlayer player = Minecraft.getMinecraft().player;
			if(CaptchaDeckHandler.clientSideModus instanceof ChatModus)
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.CHAT_MODUS_EJECT, event.getMessage(), true));
		}

		@SubscribeEvent
		public static void onReceiveChat(ClientChatReceivedEvent event)
		{

			EntityPlayer player = Minecraft.getMinecraft().player;
			if(CaptchaDeckHandler.clientSideModus instanceof ChatModus)
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.CHAT_MODUS_EJECT, event.getMessage().getFormattedText(), false));
		}

		@SubscribeEvent
		public static void onKeyInput(InputEvent.KeyInputEvent event)	//This is only called during the game, when no gui is active
		{

			if(Keyboard.isKeyDown(MinestuckKeyHandler.instance.captchaKey.getKeyCode()) && Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty())
			{

				if(CaptchaDeckHandler.clientSideModus instanceof OuijaModus)
					MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.GET, 0, false));
				if(CaptchaDeckHandler.clientSideModus instanceof WalletModus || CaptchaDeckHandler.clientSideModus instanceof CrystalBallModus)
					MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.WALLET_CAPTCHA, Minecraft.getMinecraft().objectMouseOver));
			}

		}


		@SubscribeEvent
		public void onTick(TickEvent.ClientTickEvent event)
		{
			try
			{
				this.captchaKeyPressed = Keyboard.isKeyDown(MinestuckKeyHandler.instance.captchaKey.getKeyCode());
			} catch(IndexOutOfBoundsException ignored)
			{}
		}

	}
}
