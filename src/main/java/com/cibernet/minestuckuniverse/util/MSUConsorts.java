package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.consort.MessageType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MSUConsorts
{
	public static EnumConsort.MerchantType SHOP_SKILLS = EnumHelper.addEnum(EnumConsort.MerchantType.class, "SKILLS", new Class[0]);;

	public static void setupCustomConsortAttributes()
	{
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("helloWorld"))).type(SHOP_SKILLS);
	}

	public static class SkillShopGuiMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType initMessage;

		public SkillShopGuiMessage(MessageType message) {
			this(message.getString(), message);
		}

		public SkillShopGuiMessage(String name, MessageType message) {
			this.nbtName = name;
			this.initMessage = message;
		}

		public String getString() {
			return this.nbtName;
		}

		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.OPEN_GUI, MSUUtils.SKILL_SHOP_UI, (int) consort.posX,(int) consort.posY,(int) consort.posZ), player);
			return this.initMessage.getMessage(consort, player, chainIdentifier);
		}

		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier, String fromChain) {
			return null;
		}
	}

	@SubscribeEvent
	public static void onConsortSpawn(LivingSpawnEvent event)
	{
		if(event.getEntity() instanceof EntityConsort)
		{
			EntityConsort consort = (EntityConsort) event.getEntity();
			if(consort.merchantType == EnumConsort.MerchantType.GENERAL && event.getWorld().rand.nextFloat() < .05f)
			{
				consort.merchantType = MSUConsorts.SHOP_SKILLS;
				consort.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).setHeadStack(new ItemStack(MinestuckUniverseItems.archmageHat));
			}
		}
	}
}
